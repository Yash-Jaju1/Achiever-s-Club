// src/AchieverClubApp.java

import javax.swing.SwingUtilities;

public class AchieverClubApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI gui = new MainGUI();
            gui.setVisible(true);
        });
    }
}