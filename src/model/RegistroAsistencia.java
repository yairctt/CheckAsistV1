package model;

import java.time.LocalDateTime;

public class RegistroAsistencia {
    private int id;
    private int idEmpleado;
    private LocalDateTime fecha;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;
    private boolean justificado;

    public RegistroAsistencia(int id, int idEmpleado, LocalDateTime fecha, LocalDateTime horaEntrada, LocalDateTime horaSalida, boolean justificado) {
        this.id = id;
        this.idEmpleado = idEmpleado;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.justificado = justificado;
    }

    // Getters y setters
}