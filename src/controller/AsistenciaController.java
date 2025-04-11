package controller;

import db.DatabaseConnection;
import view.EmpleadoView;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AsistenciaController {
    private EmpleadoView view;
    private int idEmpleado;

    public AsistenciaController(EmpleadoView view, int idEmpleado) {
        this.view = view;
        this.idEmpleado = idEmpleado;
        initController();
    }

    private void initController() {
        view.addScanListener(e -> registrarAsistencia());
    }

    private void registrarAsistencia() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Simula lectura de QR (en producción, usar QRScanner)
            String qrCode = JOptionPane.showInputDialog("Ingrese código QR");

            // Verificar empleado por QR
            String queryEmpleado = "SELECT id_empleado FROM empleados WHERE qr_code = ? AND activo = TRUE";
            try (PreparedStatement stmtEmpleado = conn.prepareStatement(queryEmpleado)) {
                stmtEmpleado.setString(1, qrCode);
                ResultSet rsEmpleado = stmtEmpleado.executeQuery();

                if (rsEmpleado.next()) {
                    int idEmp = rsEmpleado.getInt("id_empleado");

                    if (idEmp != idEmpleado) {
                        JOptionPane.showMessageDialog(null, "Código QR no corresponde al usuario");
                        return;
                    }

                    // Verificar si ya hay entrada hoy
                    String queryRegistro = "SELECT id_registro, hora_entrada, hora_salida FROM registros WHERE id_empleado = ? AND fecha = ?";
                    try (PreparedStatement stmtRegistro = conn.prepareStatement(queryRegistro)) {
                        stmtRegistro.setInt(1, idEmpleado);
                        stmtRegistro.setDate(2, Date.valueOf(LocalDate.now()));
                        ResultSet rsRegistro = stmtRegistro.executeQuery();

                        if (rsRegistro.next()) {
                            if (rsRegistro.getTimestamp("hora_salida") == null) {
                                // Registrar salida
                                String updateSalida = "UPDATE registros SET hora_salida = ? WHERE id_registro = ?";
                                try (PreparedStatement stmtSalida = conn.prepareStatement(updateSalida)) {
                                    stmtSalida.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                                    stmtSalida.setInt(2, rsRegistro.getInt("id_registro"));
                                    stmtSalida.executeUpdate();
                                    JOptionPane.showMessageDialog(null, "Salida registrada");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Ya registraste entrada y salida hoy");
                            }
                        } else {
                            // Registrar entrada
                            String insertEntrada = "INSERT INTO registros (id_empleado, fecha, hora_entrada) VALUES (?, ?, ?)";
                            try (PreparedStatement stmtEntrada = conn.prepareStatement(insertEntrada)) {
                                stmtEntrada.setInt(1, idEmpleado);
                                stmtEntrada.setDate(2, Date.valueOf(LocalDate.now()));
                                stmtEntrada.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                                stmtEntrada.executeUpdate();
                                JOptionPane.showMessageDialog(null, "Entrada registrada");
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Código QR no válido");
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }
}
