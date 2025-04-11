package controller;

import db.DatabaseConnection;
import model.Usuario;
import view.LoginView;
import view.EmpleadoView;
import view.AdminView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    private LoginView view;

    public LoginController(LoginView view) {
        this.view = view;
        initController();
    }

    private void initController() {
        view.addLoginListener(e -> login());
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
                    new EmpleadoController(empleadoView); // <-- Esto es lo que te falta
                    empleadoView.setVisible(true);
                } else if (rol.equals("ADMIN")) {
                    AdminView adminView = new AdminView();
                    new AdminController(adminView); // ← Aquí conectas la vista al controlador
                    adminView.setVisible(true);
                }
            } else {
                view.showError("Usuario o contraseña incorrectos");
            }
        } catch (SQLException ex) {
            view.showError("Error de conexión: " + ex.getMessage());
        }
    }
}
