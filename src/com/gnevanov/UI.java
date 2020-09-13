package com.gnevanov;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

public class UI extends JFrame {
    private JPanel catalogPanel = new JPanel();
    private JList<File> filesList = new JList<>();
    private JScrollPane jScrollPane = new JScrollPane(filesList);
    private JPanel buttonPanel = new JPanel();
    private JButton addButton = new JButton("Create");
    private JButton backButton = new JButton("Back");
    private JButton deleteButton = new JButton("Delete");
    private JButton renameButton = new JButton("Rename");
    private ArrayList<String> dirsCache = new ArrayList<>();

    public UI() {
        super("Explorer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        catalogPanel.setLayout(new BorderLayout(5,5));
        catalogPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        buttonPanel.setLayout(new GridLayout(1,4,5,5));
        JDialog newDirDialog = new JDialog(UI.this, "Creating folder", true);
        JPanel newDirPanel = new JPanel();
        newDirDialog.add(newDirPanel);
        File[] discs = File.listRoots();
        jScrollPane.setPreferredSize(new Dimension(400,500));
        filesList.setListData(discs);
        filesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        filesList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    DefaultListModel<File> model = new DefaultListModel<>();
                    String selectedObject = filesList.getSelectedValue().toString();
                    String fullPath = toFullPath(dirsCache);
                    File selectedFile;
                    if (dirsCache.size() > 1) {
                        selectedFile = new File(fullPath, selectedObject);
                    } else {
                        selectedFile = new File(fullPath + selectedObject);
                    }

                    if (selectedFile.isDirectory()) {
                        String[] rootStr = selectedFile.list();
                        assert rootStr != null;
                        for (String str: rootStr) {
                            File checkObject = new File(selectedFile.getPath(), str);
                            if (!checkObject.isHidden()) {
                                if (checkObject.isDirectory()) {
                                    model.addElement(new File(str));
                                } else {
                                    model.addElement(new File("file-" + str));
                                }
                            }
                        }

                        dirsCache.add(selectedObject);
                        filesList.setModel(model);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}


        });

        backButton.addActionListener(e -> {
            if (dirsCache.size() > 1) {
                dirsCache.remove(dirsCache.size() - 1);
                createAndFillTheModel(toFullPath(dirsCache));
            } else {
                dirsCache.clear();
                filesList.setListData(discs);
            }
        });

        addButton.addActionListener(e -> {
            if (!dirsCache.isEmpty()) {
                String currentPath;
                File newFolder;
                FolderDialog folderDialog = new FolderDialog(UI.this);

                if (folderDialog.isReady()) {
                    currentPath = toFullPath(dirsCache);
                    newFolder = new File(currentPath, folderDialog.getNewFolderName());
                    if (!newFolder.exists()) {
                        newFolder.mkdir();
                    }
                    createAndFillTheModel(currentPath);
                }
            }
        });

        deleteButton.addActionListener(e -> {
            String selectedObject = filesList.getSelectedValue().toString();
            String currentPath = toFullPath(dirsCache);
            if (!selectedObject.isEmpty()) {
                deleteDir(new File(currentPath, selectedObject));
                createAndFillTheModel(currentPath);
            }
        });

        renameButton.addActionListener(e -> {
            if (!dirsCache.isEmpty() & filesList.getSelectedValue() != null) {
                String currentPath = toFullPath(dirsCache);
                String selectedObject = filesList.getSelectedValue().toString();
                RenameJDialog renameJDialog = new RenameJDialog(UI.this);
                if (renameJDialog.isReady()) {
                    File renameFile = new File(currentPath, selectedObject);
                    renameFile.renameTo(new File(currentPath, renameJDialog.getNewName()));
                    createAndFillTheModel(currentPath);
                }
            }
        });

        buttonPanel.add(backButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(renameButton);
        catalogPanel.setLayout(new BorderLayout());
        catalogPanel.add(jScrollPane, BorderLayout.CENTER);
        catalogPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(catalogPanel);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public String toFullPath(ArrayList<String> file) {
        StringBuilder listPart = new StringBuilder();
        for (String str: file) listPart.append(str);
        return listPart.toString();
    }

    public void deleteDir(File file) {
        File[] objects = file.listFiles();
        if (objects != null) {
            for (File f: objects) {
                deleteDir(f);
            }
        }
        file.delete();
    }

    private void createAndFillTheModel(String currentPath) {
        File updateDir = new File(currentPath);
        String[] updateArray = updateDir.list();
        DefaultListModel<File> updateModel = new DefaultListModel<>();
        assert updateArray != null;
        for (String str: updateArray) {
            File check = new File(updateDir.getPath(), str);
            if (!check.isHidden()) {
                if (check.isDirectory()) {
                    updateModel.addElement(new File(str));
                } else {
                    updateModel.addElement(new File("file-" + str));
                }
            }
        }
        filesList.setModel(updateModel);
    }
}
