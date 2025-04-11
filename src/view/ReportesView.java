package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ReportesView extends JFrame {
    private JComboBox<String> comboEmpleados;
    private JTextField txtFechaInicio, txtFechaFin;
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

        panel.add(new JLabel("Fecha Inicio (YYYY-MM-DD):"));
        txtFechaInicio = new JTextField();
        panel.add(txtFechaInicio);

        panel.add(new JLabel("Fecha Fin (YYYY-MM-DD):"));
        txtFechaFin = new JTextField();
        panel.add(txtFechaFin);

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

    public String getFechaInicio() { return txtFechaInicio.getText(); }
    public String getFechaFin() { return txtFechaFin.getText(); }
    public String getSelectedEmpleado() { return (String) comboEmpleados.getSelectedItem(); }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
