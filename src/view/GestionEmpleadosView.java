package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.UUID;

public class GestionEmpleadosView extends JFrame {
    private JTextField txtNombre, txtApellido, txtUsername, txtPassword;
    private JComboBox<String> comboRol;
    private JTable tablaEmpleados;
    private DefaultTableModel tableModel;
    private JButton btnAgregar, btnEditar, btnBaja, btnPausar, btnVolver;

    public GestionEmpleadosView() {
        setTitle("CheckAsist - Gestión de Empleados");
        setSize(1200, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Formulario
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        formPanel.add(txtNombre);

        formPanel.add(new JLabel("Apellido:"));
        txtApellido = new JTextField();
        formPanel.add(txtApellido);

        formPanel.add(new JLabel("Usuario:"));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);

        formPanel.add(new JLabel("Contraseña:"));
        txtPassword = new JTextField();
        formPanel.add(txtPassword);

        formPanel.add(new JLabel("Rol:"));
        comboRol = new JComboBox<>(new String[]{"EMPLEADO", "ADMIN"});
        formPanel.add(comboRol);

        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnBaja = new JButton("Dar de Baja");
        btnPausar = new JButton("Pausar/Activar");
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAgregar);
        btnPanel.add(btnEditar);
        btnPanel.add(btnBaja);
        btnPanel.add(btnPausar);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(btnPanel, BorderLayout.CENTER);

        // Tabla
        String[] columnas = {"ID", "Nombre", "Apellido", "Usuario", "Rol", "Activo"};
        tableModel = new DefaultTableModel(columnas, 0);
        tablaEmpleados = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tablaEmpleados);
        panel.add(scrollPane, BorderLayout.SOUTH);

        btnVolver = new JButton("Volver");
        panel.add(btnVolver, BorderLayout.WEST);

        add(panel);
    }

    public void addAgregarListener(ActionListener listener) {
        btnAgregar.addActionListener(listener);
    }

    public void addEditarListener(ActionListener listener) {
        btnEditar.addActionListener(listener);
    }

    public void addBajaListener(ActionListener listener) {
        btnBaja.addActionListener(listener);
    }

    public void addPausarListener(ActionListener listener) {
        btnPausar.addActionListener(listener);
    }

    public void addVolverListener(ActionListener listener) {
        btnVolver.addActionListener(listener);
    }

    public String getNombre() { return txtNombre.getText(); }
    public String getApellido() { return txtApellido.getText(); }
    public String getUsername() { return txtUsername.getText(); }
    public String getPassword() { return txtPassword.getText(); }
    public String getRol() { return (String) comboRol.getSelectedItem(); }

    public void cargarEmpleados(Object[][] data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    public int getSelectedEmpleadoId() {
        int row = tablaEmpleados.getSelectedRow();
        return row >= 0 ? (int) tableModel.getValueAt(row, 0) : -1;
    }

    public void setCampos(int id, String nombre, String apellido, String username, String password, String rol) {
        txtNombre.setText(nombre);
        txtApellido.setText(apellido);
        txtUsername.setText(username);
        txtPassword.setText(password);
        comboRol.setSelectedItem(rol);
    }

    public void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        comboRol.setSelectedIndex(0);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}