package controller;

import db.DatabaseConnection;
import view.JustificarAsistenciaView;

import java.sql.*;

public class JustificarAsistenciaController {
    private JustificarAsistenciaView view;

    public JustificarAsistenciaController(JustificarAsistenciaView view) {
        this.view = view;
        initController();
        cargarEmpleados();
    }

    private void initController() {
        view.addEmpleadoListener(e -> cargarRegistros());
        view.addJustificarListener(e -> justificarRegistro());
        view.addVolverListener(e -> view.dispose());
    }

    private void cargarEmpleados() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT CONCAT(nombre, ' ', apellido) AS nombre_completo FROM empleados";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                view.cargarEmpleados(new String[]{rs.getString("nombre_completo")});
            }
        } catch (SQLException ex) {
            view.mostrarMensaje("Error al cargar empleados: " + ex.getMessage());
        }
    }

    private void cargarRegistros() {
        String empleado = view.getSelectedEmpleado();
        if (empleado == null) return;

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT r.id_registro, r.fecha, r.hora_entrada, r.hora_salida, r.justificado " +
                    "FROM registros r JOIN empleados e ON r.id_empleado = e.id_empleado " +
                    "WHERE CONCAT(e.nombre, ' ', e.apellido) = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, empleado);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                view.cargarRegistros(new Object[][]{{
                        rs.getInt("id_registro"),
                        rs.getDate("fecha"),
                        rs.getTimestamp("hora_entrada"),
                        rs.getTimestamp("hora_salida"),
                        rs.getBoolean("justificado") ? "Sí" : "No"
                }});
            }
        } catch (SQLException ex) {
            view.mostrarMensaje("Error al cargar registros: " + ex.getMessage());
        }
    }

    private void justificarRegistro() {
        int idRegistro = view.getSelectedRegistroId();
        if (idRegistro == -1) {
            view.mostrarMensaje("Selecciona un registro");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE registros SET justificado = NOT justificado WHERE id_registro = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idRegistro);
            stmt.executeUpdate();

            cargarRegistros();
            view.mostrarMensaje("Registro actualizado");
        } catch (SQLException ex) {
            view.mostrarMensaje("Error al justificar: " + ex.getMessage());
        }
    }
}
