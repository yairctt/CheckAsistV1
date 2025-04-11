package view;

import com.formdev.flatlaf.FlatDarkLaf;  // Importa FlatLaf para el tema oscuro
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnEscaneoRapido; // Nuevo botón para escaneo rápido

    public LoginView() {
        // Cambiar el look and feel a FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf()); // Usar el tema oscuro de FlatLaf
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setTitle("CheckAsist - Login");
        setSize(350, 300); // Ajusté el tamaño para más espacio
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel con fondo y bordes redondeados
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10)); // Cambié el layout para agregar el botón
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Etiquetas y campos de texto con estilo FlatLaf
        JLabel lblUsername = new JLabel("Usuario:");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(txtUsername);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(txtPassword);

        // Botón de login con estilo
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        panel.add(btnLogin);

        // Botón para escaneo rápido
        btnEscaneoRapido = new JButton("Escaneo Rápido");
        btnEscaneoRapido.setFont(new Font("Arial", Font.BOLD, 14));
        btnEscaneoRapido.setFocusPainted(false);
        panel.add(btnEscaneoRapido);

        add(panel);
    }

    // Métodos para obtener datos del login
    public String getUsername() {
        return txtUsername.getText();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    // Agregar listeners
    public void addLoginListener(ActionListener listener) {
        btnLogin.addActionListener(listener);
    }

    public void addEscaneoRapidoListener(ActionListener listener) {
        btnEscaneoRapido.addActionListener(listener);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
