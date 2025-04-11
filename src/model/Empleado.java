package model;

public class Empleado {
    private int id;
    private String nombre;
    private String apellido;
    private String qrCode;
    private boolean activo;

    public Empleado(int id, String nombre, String apellido, String qrCode, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.qrCode = qrCode;
        this.activo = activo;
    }

    // Getters y setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getQrCode() { return qrCode; }
    public boolean isActivo() { return activo; }
}