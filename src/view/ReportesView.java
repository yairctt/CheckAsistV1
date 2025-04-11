package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import com.toedter.calendar.JDateChooser;  // Importar JCalendar

public class ReportesView extends JFrame {
    private JComboBox<String> comboEmpleados;
    private JDateChooser dateInicio, dateFin;  // JDateChooser para fechas
    private JButton btnGenerarCSV, btnGenerarPDF, btnVolver;

    public ReportesView() {
        setTitle("CheckAsist - Generar Reportes");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Seleccionar Empleado (opcional):"));
        comboEmpleados = new JComboBox<>();
        comboEmpleados.addItem("Todos");
        panel.add(comboEmpleados);

        panel.add(new JLabel("Fecha Inicio:"));
        dateInicio = new JDateChooser();
        dateInicio.setDateFormatString("yyyy-MM-dd");
        panel.add(dateInicio);

        panel.add(new JLabel("Fecha Fin:"));
        dateFin = new JDateChooser();
        dateFin.setDateFormatString("yyyy-MM-dd");
        panel.add(dateFin);

        btnGenerarCSV = new JButton("Generar CSV");
        btnGenerarPDF = new JButton("Generar PDF");
        btnVolver = new JButton("Volver");

        panel.add(btnGenerarCSV);
        panel.add(btnGenerarPDF);
        panel.add(btnVolver);

        add(panel);
    }

    public void addGenerarCSVListener(ActionListener listener) {
        btnGenerarCSV.addActionListener(listener);
    }

    public void addGenerarPDFListener(ActionListener listener) {
        btnGenerarPDF.addActionListener(listener);
    }

    public void addVolverListener(ActionListener listener) {
        btnVolver.addActionListener(listener);
    }

    public void cargarEmpleados(String[] empleados) {
        comboEmpleados.removeAllItems();
        comboEmpleados.addItem("Todos");
        for (String emp : empleados) {
            comboEmpleados.addItem(emp);
        }
    }

    public String getFechaInicio() {
        if (dateInicio.getDate() != null) {
            return new java.text.SimpleDateFormat("yyyy-MM-dd").format(dateInicio.getDate());
        }
        return null;
    }

    public String getFechaFin() {
        if (dateFin.getDate() != null) {
            return new java.text.SimpleDateFormat("yyyy-MM-dd").format(dateFin.getDate());
        }
        return null;
    }

    public String getSelectedEmpleado() {
        return (String) comboEmpleados.getSelectedItem();
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}