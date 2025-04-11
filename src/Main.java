// src/Main.java
import com.formdev.flatlaf.FlatLightLaf;
import controller.LoginController;
import view.LoginView;

import javax.swing.*;
// o FlatDarkLaf, FlatIntelliJLaf, FlatDarculaLaf

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf()); // 🎨 Look moderno
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            new LoginController(loginView);
            loginView.setVisible(true);
        });
    }
}
