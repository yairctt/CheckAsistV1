package controller;

import view.AdminView;
import view.GestionEmpleadosView;
import view.JustificarAsistenciaView;
import view.ReportesView;
import view.LoginView;

public class AdminController {
    private AdminView view;

    public AdminController(AdminView view) {
        this.view = view;
        initController();
    }

    private void initController() {
        view.addGestionListener(e -> abrirGestionEmpleados());
        view.addJustificarListener(e -> abrirJustificarAsistencias());
        view.addReportesListener(e -> abrirReportes());
        view.addCerrarSesionListener(e -> cerrarSesion());
    }

    private void abrirGestionEmpleados() {
        GestionEmpleadosView gestionView = new GestionEmpleadosView();
        new GestionEmpleadosController(gestionView);
        gestionView.setVisible(true);
    }

    private void abrirJustificarAsistencias() {
        JustificarAsistenciaView justificarView = new JustificarAsistenciaView();
        new JustificarAsistenciaController(justificarView);
        justificarView.setVisible(true);
    }

    private void abrirReportes() {
        ReportesView reportesView = new ReportesView();
        new ReportesController(reportesView);
        reportesView.setVisible(true);
    }

    private void cerrarSesion() {
        view.dispose();
        LoginView loginView = new LoginView();
        new LoginController(loginView);
        loginView.setVisible(true);
    }
}
