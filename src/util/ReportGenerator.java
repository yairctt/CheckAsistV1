package util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import db.DatabaseConnection;

import java.io.FileWriter;
import java.sql.*;

public class ReportGenerator {
    public static void generarReporteCSV(String fechaInicio, String fechaFin, String empleado, String archivo) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             FileWriter writer = new FileWriter(archivo)) {
            String query = "SELECT e.nombre, e.apellido, r.fecha, r.hora_entrada, r.hora_salida, r.justificado " +
                    "FROM registros r JOIN empleados e ON r.id_empleado = e.id_empleado " +
                    "WHERE r.fecha BETWEEN ? AND ?";
            if (!empleado.equals("Todos")) {
                query += " AND CONCAT(e.nombre, ' ', e.apellido) = ?";
            }

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, fechaInicio);
            stmt.setString(2, fechaFin);
            if (!empleado.equals("Todos")) {
                stmt.setString(3, empleado);
            }

            ResultSet rs = stmt.executeQuery();
            writer.append("Nombre,Apellido,Fecha,Entrada,Salida,Justificado\n");
            while (rs.next()) {
                writer.append(rs.getString("nombre")).append(",");
                writer.append(rs.getString("apellido")).append(",");
                writer.append(rs.getString("fecha")).append(",");
                writer.append(rs.getString("hora_entrada")).append(",");
                writer.append(rs.getString("hora_salida")).append(",");
                writer.append(rs.getBoolean("justificado") ? "Sí" : "No").append("\n");
            }
        }
    }

    public static void generarReportePDF(String fechaInicio, String fechaFin, String empleado, String archivo) throws Exception {
        PdfWriter writer = new PdfWriter(archivo);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Reporte de Asistencias").setFontSize(18));
        document.add(new Paragraph("Desde: " + fechaInicio + " Hasta: " + fechaFin));

        Table table = new Table(new float[]{100, 100, 100, 150, 150, 50});
        table.addHeaderCell(new Cell().add(new Paragraph("Nombre")));
        table.addHeaderCell(new Cell().add(new Paragraph("Apellido")));
        table.addHeaderCell(new Cell().add(new Paragraph("Fecha")));
        table.addHeaderCell(new Cell().add(new Paragraph("Entrada")));
        table.addHeaderCell(new Cell().add(new Paragraph("Salida")));
        table.addHeaderCell(new Cell().add(new Paragraph("Justificado")));

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT e.nombre, e.apellido, r.fecha, r.hora_entrada, r.hora_salida, r.justificado " +
                    "FROM registros r JOIN empleados e ON r.id_empleado = e.id_empleado " +
                    "WHERE r.fecha BETWEEN ? AND ?";
            if (!empleado.equals("Todos")) {
                query += " AND CONCAT(e.nombre, ' ', e.apellido) = ?";
            }

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, fechaInicio);
            stmt.setString(2, fechaFin);
            if (!empleado.equals("Todos")) {
                stmt.setString(3, empleado);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                table.addCell(new Cell().add(new Paragraph(rs.getString("nombre"))));
                table.addCell(new Cell().add(new Paragraph(rs.getString("apellido"))));
                table.addCell(new Cell().add(new Paragraph(rs.getString("fecha"))));
                table.addCell(new Cell().add(new Paragraph(rs.getString("hora_entrada"))));
                table.addCell(new Cell().add(new Paragraph(rs.getString("hora_salida"))));
                table.addCell(new Cell().add(new Paragraph(rs.getBoolean("justificado") ? "Sí" : "No")));
            }
        }

        document.add(table);
        document.close();
    }
}