package Login.com;


public class Usuario {

    private int UsuarioID;
    private String Nombre;
    private String Correo;
    private String Contrasena;
    private String rol;

    public Usuario() {
    }

    public Usuario(int UsuarioID, String Nombre, String Correo, String Contrasena, String rol) {
        this.UsuarioID = UsuarioID;
        this.Nombre = Nombre;
        this.Correo = Correo;
        this.Contrasena = Contrasena;
        this.rol = rol;
    }

    public void setUsuarioID(int UsuarioID) {
        this.UsuarioID = UsuarioID;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public void setCorreo(String Correo) {
        this.Correo = Correo;
    }

    public void setContrasena(String Contrasena) {
        this.Contrasena = Contrasena;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public int getUsuarioID() {
        return UsuarioID;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getCorreo() {
        return Correo;
    }

    public String getContrasena() {
        return Contrasena;
    }

    public String getRol() {
        return rol;
    }
}