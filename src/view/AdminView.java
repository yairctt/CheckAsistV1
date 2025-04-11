package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AdminView extends JFrame {
    private JButton btnGestionEmpleados;
    private JButton btnJustificarAsistencias;
    private JButton btnGenerarReportes;
    private JButton btnCerrarSesion;
    private Image backgroundImage;

    public AdminView() {
        setTitle("CheckAsist - Administrador");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Cargar imagen de fondo
        backgroundImage = new ImageIcon("C:/Users/yairc/Documents/CheckAsistV1/src/resources/img/fondo.jpg").getImage(); // Asegúrate que el fondo esté en resources/img

        // Panel personalizado para dibujar fondo
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        // Crear botones con estilo moderno
        btnGestionEmpleados = createStyledButton("Gestionar Empleados");
        btnJustificarAsistencias = createStyledButton("Justificar Asistencias");
        btnGenerarReportes = createStyledButton("Generar Reportes");
        btnCerrarSesion = createStyledButton("Cerrar Sesión");

        // Panel contenedor de botones con fondo semitransparente
        JPanel botonesPanel = new JPanel(new GridLayout(4, 1, 20, 20));
        botonesPanel.setOpaque(false); // Transparente
        botonesPanel.setPreferredSize(new Dimension(300, 300));
        botonesPanel.add(btnGestionEmpleados);
        botonesPanel.add(btnJustificarAsistencias);
        botonesPanel.add(btnGenerarReportes);
        botonesPanel.add(btnCerrarSesion);

        backgroundPanel.add(botonesPanel);

        setContentPane(backgroundPanel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(0, 0, 0, 100)); // negro con transparencia
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Listeners
    public void addGestionListener(ActionListener listener) {
        btnGestionEmpleados.addActionListener(listener);
    }

    public void addJustificarListener(ActionListener listener) {
        btnJustificarAsistencias.addActionListener(listener);
    }

    public void addReportesListener(ActionListener listener) {
        btnGenerarReportes.addActionListener(listener);
    }

    public void addCerrarSesionListener(ActionListener listener) {
        btnCerrarSesion.addActionListener(listener);
    }
}

