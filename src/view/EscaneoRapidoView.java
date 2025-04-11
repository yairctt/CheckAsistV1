package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class EscaneoRapidoView extends JFrame {
    private JButton btnEscanear;
    private JLabel lblMensaje;

    public EscaneoRapidoView() {
        setTitle("CheckAsist - Escaneo Rápido");
        setSize(400, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        btnEscanear = new JButton("📷 Escanear QR");
        lblMensaje = new JLabel("Presiona el botón para registrar tu asistencia", SwingConstants.CENTER);

        panel.add(btnEscanear);
        panel.add(lblMensaje);

        add(panel);
    }

    public void addEscanearListener(ActionListener listener) {
        btnEscanear.addActionListener(listener);
    }

    public void mostrarMensaje(String mensaje) {
        lblMensaje.setText(mensaje);
    }

}