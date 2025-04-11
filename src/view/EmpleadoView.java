package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;
import db.DatabaseConnection;
import java.text.SimpleDateFormat;

public class EmpleadoView extends JFrame {
    private int idEmpleado;
    private JButton btnScan;
    private JButton btnCerrarSesion;
    private JTable tablaHistorial;
    private DefaultTableModel tableModel;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm"); // Formato solo hora:minuto

    public EmpleadoView(int idEmpleado) {
        this.idEmpleado = idEmpleado;
        setTitle("CheckAsist - Empleado");
        setSize(700, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout());

        // Panel superior con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnScan = new JButton("📷 Escanear QR");
        btnCerrarSesion = new JButton("⏏ Cerrar sesión");
        panelBotones.add(btnScan);
        panelBotones.add(btnCerrarSesion);

        panelPrincipal.add(panelBotones, BorderLayout.NORTH);

        // Tabla historial
        String[] columnas = {"Fecha", "Entrada", "Salida", "Duración", "Justificado"};
        tableModel = new DefaultTableModel(columnas, 0);
        tablaHistorial = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tablaHistorial);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        add(panelPrincipal);
        cargarHistorial();
    }

    public void addScanListener(ActionListener listener) {
        btnScan.addActionListener(listener);
    }

    public void addCerrarSesionListener(ActionListener listener) {
        btnCerrarSesion.addActionListener(listener);
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
                Timestamp entradaTs = rs.getTimestamp("hora_entrada");
                Timestamp salidaTs = rs.getTimestamp("hora_salida");

                String entrada = entradaTs != null ? timeFormat.format(entradaTs) : "-";
                String salida = salidaTs != null ? timeFormat.format(salidaTs) : "-";
                String duracion = calcularDuracion(entradaTs, salidaTs);
                String justificado = rs.getBoolean("justificado") ? "Sí" : "No";

                tableModel.addRow(new Object[]{fecha, entrada, salida, duracion, justificado});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + ex.getMessage());
        }
    }

    private String calcularDuracion(Timestamp entrada, Timestamp salida) {
        if (entrada == null || salida == null) {
            return "-"; // Si falta entrada o salida, no hay duración
        }
        long diff = salida.getTime() - entrada.getTime();
        if (diff < 0) {
            return "Error: Salida antes de entrada"; // Evitar duraciones negativas
        }
        long horas = diff / (1000 * 60 * 60);
        long minutos = (diff % (1000 * 60 * 60)) / (1000 * 60);
        return String.format("%dh %dm", horas, minutos); // Formato consistente
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    // Método main para pruebas
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmpleadoView view = new EmpleadoView(1); // Prueba con idEmpleado = 1
            view.setVisible(true);
        });
    }
}