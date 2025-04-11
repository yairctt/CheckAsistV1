package controller;

import db.DatabaseConnection;
import util.QRScanner;
import view.EmpleadoView;

import java.sql.*;

public class EmpleadoController {
    private EmpleadoView view;

    public EmpleadoController(EmpleadoView view) {
        this.view = view;
        initController();
    }

    private void initController() {
        view.addScanListener(e -> escanearYRegistrar());
    }

    private void escanearYRegistrar() {
        String qrCode = QRScanner.scanQRCode();

        if (qrCode == null || qrCode.isEmpty()) {
            view.mostrarMensaje("No se pudo leer el QR. Intenta de nuevo.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Buscar al empleado por UUID del QR
            String empleadoQuery = "SELECT id_empleado FROM empleados WHERE qr_code = ?";
            PreparedStatement empleadoStmt = conn.prepareStatement(empleadoQuery);
            empleadoStmt.setString(1, qrCode);
            ResultSet empleadoRs = empleadoStmt.executeQuery();

            if (!empleadoRs.next()) {
                view.mostrarMensaje("Código QR no reconocido.");
                return;
            }

            int idEmpleado = empleadoRs.getInt("id_empleado");

            // Verificar si ya hay registro de entrada hoy
            String query = "SELECT id_registro, hora_entrada, hora_salida FROM registros " +
                    "WHERE id_empleado = ? AND fecha = CURDATE()";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idEmpleado);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Timestamp salida = rs.getTimestamp("hora_salida");

                if (salida == null) {
                    // Ya tiene entrada, registrar salida
                    int idRegistro = rs.getInt("id_registro");
                    String update = "UPDATE registros SET hora_salida = CURRENT_TIMESTAMP WHERE id_registro = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(update);
                    updateStmt.setInt(1, idRegistro);
                    updateStmt.executeUpdate();

                    view.mostrarMensaje("Salida registrada correctamente.");
                } else {
                    view.mostrarMensaje("Ya registraste entrada y salida hoy.");
                }
            } else {
                // No hay registro hoy, insertar entrada
                String insert = "INSERT INTO registros (id_empleado, fecha, hora_entrada) " +
                        "VALUES (?, CURDATE(), CURRENT_TIMESTAMP)";
                PreparedStatement insertStmt = conn.prepareStatement(insert);
                insertStmt.setInt(1, idEmpleado);
                insertStmt.executeUpdate();

                view.mostrarMensaje("Entrada registrada correctamente.");
            }

            view.cargarHistorial();

        } catch (SQLException ex) {
            view.mostrarMensaje("Error al registrar asistencia: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

