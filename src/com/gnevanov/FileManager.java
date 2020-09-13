package com.gnevanov;

import static javax.swing.SwingUtilities.invokeLater;

public class FileManager {
    public static void main(String[] args) {
        invokeLater(UI::new);
    }
}
