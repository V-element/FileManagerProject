package com.gnevanov;

import javax.swing.*;
import java.awt.*;

public class FolderDialog extends JDialog {
    private JTextField nameOfNewFolder = new JTextField(10);
    private JButton okButton = new JButton("Create");
    private JButton cancelButton = new JButton("Cancel");
    private String newFolderName;
    private JLabel nameFolderSign = new JLabel("New folder name:");
    private boolean ready = false;

    public FolderDialog(JFrame jFrame) {
        super(jFrame, "Create new folder", true);
        setLayout(new GridLayout(2,2,5,5));
        setSize(600, 200);

        okButton.addActionListener(e -> {
            newFolderName =nameOfNewFolder.getText();
            setVisible(false);
            ready = true;
        });

        cancelButton.addActionListener(e -> {
            setVisible(false);
            ready = false;
        });

        getContentPane().add(nameFolderSign);
        getContentPane().add(nameOfNewFolder);
        getContentPane().add(okButton);
        getContentPane().add(cancelButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public String getNewFolderName() {
        return newFolderName;
    }

    public boolean isReady() {
        return ready;
    }

}
