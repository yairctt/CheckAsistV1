package controller;

import db.DatabaseConnection;
import model.Usuario;
import view.LoginView;
import view.EmpleadoView;
import view.AdminView;
import view.EscaneoRapidoView;
import util.QRScanner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class LoginController {
    private LoginView view;

    public LoginController(LoginView view) {
        this.view = view;
        initController();
    }

    private void initController() {
        view.addLoginListener(e -> login());
        view.addEscaneoRapidoListener(e -> abrirEscaneoRapido());
    }

    private void login() {
        String username = view.getUsername();
        String password = view.getPassword();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT u.id_usuario, u.id_empleado, u.rol FROM usuarios u WHERE u.username = ? AND u.password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password); // En producción, usar hash
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String rol = rs.getString("rol");
                int idEmpleado = rs.getInt("id_empleado");
                view.dispose();

                if (rol.equals("EMPLEADO")) {
                    EmpleadoView empleadoView = new EmpleadoView(idEmpleado);
                    new EmpleadoController(empleadoView);
                    empleadoView.setVisible(true);
                } else if (rol.equals("ADMIN")) {
                    AdminView adminView = new AdminView();
                    new AdminController(adminView);
                    adminView.setVisible(true);
                }
            } else {
                view.showError("Usuario o contraseña incorrectos");
            }
        } catch (SQLException ex) {
            view.showError("Error de conexión: " + ex.getMessage());
        }
    }

    private void abrirEscaneoRapido() {
        EscaneoRapidoView escaneoView = new EscaneoRapidoView();
        escaneoView.setVisible(true);
        escaneoView.addEscanearListener(e -> registrarAsistenciaRapida(escaneoView));
    }

    private void registrarAsistenciaRapida(EscaneoRapidoView view) {
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

        } catch (SQLException ex) {
            view.mostrarMensaje("Error al registrar asistencia: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
