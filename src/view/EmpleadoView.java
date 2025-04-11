package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;
import db.DatabaseConnection;

public class EmpleadoView extends JFrame {
    private int idEmpleado;
    private JButton btnScan;
    private JTable tablaHistorial;
    private DefaultTableModel tableModel;

    public EmpleadoView(int idEmpleado) {
        this.idEmpleado = idEmpleado;
        setTitle("CheckAsist - Empleado");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        btnScan = new JButton("Escanear QR");
        panel.add(btnScan, BorderLayout.NORTH);

        String[] columnas = {"Fecha", "Entrada", "Salida", "Duración", "Justificado"};
        tableModel = new DefaultTableModel(columnas, 0);
        tablaHistorial = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tablaHistorial);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);
        cargarHistorial();
    }

    public void addScanListener(ActionListener listener) {
        btnScan.addActionListener(listener);
    }

    public void cargarHistorial() {
        tableModel.setRowCount(0); // Limpiar tabla
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT fecha, hora_entrada, hora_salida, justificado " +
                    "FROM registros WHERE id_empleado = ? ORDER BY fecha DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idEmpleado);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String fecha = rs.getDate("fecha").toString();
                String entrada = rs.getTimestamp("hora_entrada") != null ? rs.getTimestamp("hora_entrada").toString() : "-";
                String salida = rs.getTimestamp("hora_salida") != null ? rs.getTimestamp("hora_salida").toString() : "-";
                String duracion = calcularDuracion(rs.getTimestamp("hora_entrada"), rs.getTimestamp("hora_salida"));
                String justificado = rs.getBoolean("justificado") ? "Sí" : "No";

                tableModel.addRow(new Object[]{fecha, entrada, salida, duracion, justificado});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + ex.getMessage());
        }
    }

    private String calcularDuracion(Timestamp entrada, Timestamp salida) {
        if (entrada == null || salida == null) return "-";
        long diff = salida.getTime() - entrada.getTime();
        long horas = diff / (1000 * 60 * 60);
        long minutos = (diff % (1000 * 60 * 60)) / (1000 * 60);
        return horas + "h " + minutos + "m";
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}