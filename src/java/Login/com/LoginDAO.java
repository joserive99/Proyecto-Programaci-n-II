
package Login.com;
import Database.com.Database;
import java.sql.*;


public class LoginDAO {
    
    public Usuario validateLogin(Usuario usuario) {
        try {
            Database db = new Database();
            Connection conn = db.getConnection();

            PreparedStatement pstat = conn.prepareStatement("SELECT * FROM usuario WHERE Correo = ? AND Contrasena =?");

            pstat.setString(1, usuario.getCorreo());
            pstat.setString(2, usuario.getContrasena());

            ResultSet rs = pstat.executeQuery();

            while (rs.next()) {   
                
                usuario.setNombre(rs.getString("Nombre"));
                return usuario;
            }

            return null;
        } catch (SQLException ex) {
            return null;
        }
    }
}
