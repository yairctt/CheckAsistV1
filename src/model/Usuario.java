package model;

public class Usuario {
    private int id;
    private int idEmpleado;
    private String username;
    private String password;
    private String rol;

    public Usuario(int id, int idEmpleado, String username, String password, String rol) {
        this.id = id;
        this.idEmpleado = idEmpleado;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    // Getters y setters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRol() { return rol; }
    public int getIdEmpleado() { return idEmpleado; }
}