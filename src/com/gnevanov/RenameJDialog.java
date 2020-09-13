package com.gnevanov;

import javax.swing.*;
import java.awt.*;

public class RenameJDialog extends JDialog {

    private JTextField nameOfFolder = new JTextField(10);
    private JButton okButton = new JButton("Rename");
    private JButton cancelButton = new JButton("Cancel");
    private String newName;
    private JLabel nameFolderSign = new JLabel("New name:");
    private boolean ready = false;

    public RenameJDialog(JFrame jFrame) {
        super(jFrame, "Rename folder", true);
        setLayout(new GridLayout(2,2,5,5));
        setSize(600, 200);

        okButton.addActionListener(e -> {
            newName = nameOfFolder.getText();
            setVisible(false);
            ready = true;
        });

        cancelButton.addActionListener(e -> {
            setVisible(false);
            ready = false;
        });

        getContentPane().add(nameFolderSign);
        getContentPane().add(nameOfFolder);
        getContentPane().add(okButton);
        getContentPane().add(cancelButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public String getNewName() {
        return newName;
    }

    public boolean isReady() {
        return ready;
    }

}
