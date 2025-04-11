package controller;

import db.DatabaseConnection;
import util.CodigoUtil;
import view.GestionEmpleadosView;

import java.sql.*;
import java.util.UUID;

public class GestionEmpleadosController {
    private GestionEmpleadosView view;

    public GestionEmpleadosController(GestionEmpleadosView view) {
        this.view = view;
        initController();
        cargarEmpleados();
    }

    private void initController() {
        view.addAgregarListener(e -> agregarEmpleado());
        view.addEditarListener(e -> editarEmpleado());
        view.addBajaListener(e -> darBajaEmpleado());
        view.addPausarListener(e -> pausarActivarEmpleado());
        view.addVolverListener(e -> view.dispose());
    }

    private void cargarEmpleados() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT e.id_empleado, e.nombre, e.apellido, u.username, u.rol, e.activo " +
                    "FROM empleados e LEFT JOIN usuarios u ON e.id_empleado = u.id_empleado";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                view.cargarEmpleados(new Object[][]{{
                        rs.getInt("id_empleado"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("username"),
                        rs.getString("rol"),
                        rs.getBoolean("activo") ? "Sí" : "No"
                }});
            }
        } catch (SQLException ex) {
            view.mostrarMensaje("Error al cargar empleados: " + ex.getMessage());
        }
    }

    private void agregarEmpleado() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String qrCode = UUID.randomUUID().toString();
            String query = "INSERT INTO empleados (nombre, apellido, qr_code, activo) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, view.getNombre());
            stmt.setString(2, view.getApellido());
            stmt.setString(3, qrCode);
            stmt.setBoolean(4, true);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int idEmpleado = rs.next() ? rs.getInt(1) : -1;

            query = "INSERT INTO usuarios (id_empleado, username, password, rol) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idEmpleado);
            stmt.setString(2, view.getUsername());
            stmt.setString(3, view.getPassword()); // Hashear si es producción
            stmt.setString(4, view.getRol());
            stmt.executeUpdate();

            // 🔽 Generar códigos QR y de barras con username incluido
            String rutaCodigos = "C:/Users/yairc/Documents/CheckAsistV1/src/resources/codigos/";
            String username = view.getUsername(); // Obtener el username
            String nombreArchivoQR = rutaCodigos + "QR_" + idEmpleado + "_" + username + ".png";
            String nombreArchivoBarras = rutaCodigos + "BARRA_" + idEmpleado + "_" + username + ".png";

            try {
                CodigoUtil.generarCodigoQR(qrCode, nombreArchivoQR);
                CodigoUtil.generarCodigoBarras(qrCode, nombreArchivoBarras);
            } catch (Exception ex) {
                view.mostrarMensaje("Error al generar códigos: " + ex.getMessage());
            }

            view.limpiarCampos();
            cargarEmpleados();
            view.mostrarMensaje("Empleado agregado exitosamente");
        } catch (SQLException ex) {
            view.mostrarMensaje("Error al agregar empleado: " + ex.getMessage());
        }
    }

    private void editarEmpleado() {
        int id = view.getSelectedEmpleadoId();
        if (id == -1) {
            view.mostrarMensaje("Selecciona un empleado para editar.");
            return;
        }

        String nombre = view.getNombre();
        String apellido = view.getApellido();
        String username = view.getUsername();
        String password = view.getPassword();
        String rol = view.getRol();

        if (nombre.isEmpty() || apellido.isEmpty() || username.isEmpty() || password.isEmpty() || rol == null || rol.isEmpty()) {
            view.mostrarMensaje("Todos los campos son obligatorios.");
            return;
        }

        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;

        try {
            conn = DatabaseConnection.getConnection();

            String query1 = "UPDATE empleados SET nombre = ?, apellido = ? WHERE id_empleado = ?";
            stmt1 = conn.prepareStatement(query1);
            stmt1.setString(1, nombre);
            stmt1.setString(2, apellido);
            stmt1.setInt(3, id);
            stmt1.executeUpdate();

            String query2 = "UPDATE usuarios SET username = ?, password = ?, rol = ? WHERE id_empleado = ?";
            stmt2 = conn.prepareStatement(query2);
            stmt2.setString(1, username);
            stmt2.setString(2, password);
            stmt2.setString(3, rol);
            stmt2.setInt(4, id);
            stmt2.executeUpdate();

            view.mostrarMensaje("Empleado actualizado correctamente.");
            cargarEmpleados();
            view.limpiarCampos();
        } catch (SQLException e) {
            e.printStackTrace();
            view.mostrarMensaje("Error al actualizar empleado: " + e.getMessage());
        } finally {
            try {
                if (stmt1 != null) stmt1.close();
                if (stmt2 != null) stmt2.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void darBajaEmpleado() {
        int id = view.getSelectedEmpleadoId();
        if (id == -1) {
            view.mostrarMensaje("Selecciona un empleado");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM usuarios WHERE id_empleado = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.executeUpdate();

            query = "DELETE FROM empleados WHERE id_empleado = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.executeUpdate();

            cargarEmpleados();
            view.limpiarCampos();
            view.mostrarMensaje("Empleado eliminado");
        } catch (SQLException ex) {
            view.mostrarMensaje("Error al dar de baja: " + ex.getMessage());
        }
    }

    private void pausarActivarEmpleado() {
        int id = view.getSelectedEmpleadoId();
        if (id == -1) {
            view.mostrarMensaje("Selecciona un empleado");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE empleados SET activo = NOT activo WHERE id_empleado = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.executeUpdate();

            cargarEmpleados();
            view.limpiarCampos();
            view.mostrarMensaje("Estado del empleado cambiado");
        } catch (SQLException ex) {
            view.mostrarMensaje("Error al cambiar estado: " + ex.getMessage());
        }
    }
}

