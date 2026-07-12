
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
                
        public boolean RegistrarUsuario (Usuario usuario){
            
            if (ExisteCorreo(usuario.getCorreo())) {
                return false;
            }
            try{
                Database db = new Database();
                
                PreparedStatement pstmt = db.getConnection().prepareStatement("INSERT INTO Usuario (Nombre, Correo, Contrasena) VALUES (?,?,?)");
                pstmt.setString(1, usuario.getNombre());
                pstmt.setString(2, usuario.getCorreo());
                pstmt.setString(3, usuario.getContrasena());
            
               int filas = pstmt.executeUpdate();
               return filas > 0;
            }
            
            catch (SQLException ex) {
            System.getLogger(LoginServlet.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
       return false;
            
     }
        public boolean ExisteCorreo(String correo) {
            
            
        try {
            Database db = new Database();
            PreparedStatement pstmt = db.getConnection().prepareStatement("SELECT Correo FROM Usuario WHERE Correo = ?");

            pstmt.setString(1, correo);

            ResultSet rs = pstmt.executeQuery();

            return rs.next(); 
            
        } catch (SQLException ex) {
            System.getLogger(LoginServlet.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
    }      
}
