package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class JustificarAsistenciaView extends JFrame {
    private JComboBox<String> comboEmpleados;
    private JTable tablaRegistros;
    private DefaultTableModel tableModel;
    private JButton btnJustificar, btnVolver;

    public JustificarAsistenciaView() {
        setTitle("CheckAsist - Justificar Asistencias");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Selección de empleado
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Seleccionar Empleado:"));
        comboEmpleados = new JComboBox<>();
        topPanel.add(comboEmpleados);
        panel.add(topPanel, BorderLayout.NORTH);

        // Tabla de registros
        String[] columnas = {"ID", "Fecha", "Entrada", "Salida", "Justificado"};
        tableModel = new DefaultTableModel(columnas, 0);
        tablaRegistros = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tablaRegistros);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botones
        btnJustificar = new JButton("Justificar/Cancelar Justificación");
        btnVolver = new JButton("Volver");
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnJustificar);
        btnPanel.add(btnVolver);
        panel.add(btnPanel, BorderLayout.SOUTH);

        add(panel);
    }

    public void addJustificarListener(ActionListener listener) {
        btnJustificar.addActionListener(listener);
    }

    public void addVolverListener(ActionListener listener) {
        btnVolver.addActionListener(listener);
    }

    public void addEmpleadoListener(ActionListener listener) {
        comboEmpleados.addActionListener(listener);
    }

    public void cargarEmpleados(String[] empleados) {
        comboEmpleados.removeAllItems();
        for (String emp : empleados) {
            comboEmpleados.addItem(emp);
        }
    }

    public void cargarRegistros(Object[][] data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    public int getSelectedRegistroId() {
        int row = tablaRegistros.getSelectedRow();
        return row >= 0 ? (int) tableModel.getValueAt(row, 0) : -1;
    }

    public String getSelectedEmpleado() {
        return (String) comboEmpleados.getSelectedItem();
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}