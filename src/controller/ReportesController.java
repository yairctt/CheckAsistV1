package controller;

import db.DatabaseConnection;
import util.ReportGenerator;
import view.ReportesView;

import java.sql.*;

public class ReportesController {
    private ReportesView view;

    public ReportesController(ReportesView view) {
        this.view = view;
        initController();
        cargarEmpleados();
    }

    private void initController() {
        view.addGenerarCSVListener(e -> generarCSV());
        view.addGenerarPDFListener(e -> generarPDF());
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

    private void generarCSV() {
        String fechaInicio = view.getFechaInicio();
        String fechaFin = view.getFechaFin();
        String empleado = view.getSelectedEmpleado();

        try {
            ReportGenerator.generarReporteCSV(fechaInicio, fechaFin, empleado, "reporte_asistencias.csv");
            view.mostrarMensaje("Reporte CSV generado en reporte_asistencias.csv");
        } catch (Exception ex) {
            view.mostrarMensaje("Error al generar CSV: " + ex.getMessage());
        }
    }

    private void generarPDF() {
        String fechaInicio = view.getFechaInicio();
        String fechaFin = view.getFechaFin();
        String empleado = view.getSelectedEmpleado();

        try {
            ReportGenerator.generarReportePDF(fechaInicio, fechaFin, empleado, "reporte_asistencias.pdf");
            view.mostrarMensaje("Reporte PDF generado en reporte_asistencias.pdf");
        } catch (Exception ex) {
            view.mostrarMensaje("Error al generar PDF: " + ex.getMessage());
        }
    }
}