
package Login.com;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    
    
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
     String accion = request.getParameter("accion");

     LoginDAO dao = new LoginDAO();

        if ("registrar".equals(accion)) {

            Usuario usuario = new Usuario();

            usuario.setNombre(request.getParameter("Nombre"));
            usuario.setCorreo(request.getParameter("Correo"));
            usuario.setContrasena(request.getParameter("Contrasena"));

            boolean resultado = dao.RegistrarUsuario(usuario);

            if (resultado) 
            {
                response.sendRedirect("Login.jsp");
            } 
            else 
            {
                response.sendRedirect("Registrarse.jsp?error=correoExiste");
            }

        }
        
        else if ("login".equals(accion)) {

            Usuario usuario = new Usuario();

            usuario.setCorreo(request.getParameter("Correo"));
            usuario.setContrasena(request.getParameter("Contrasena"));

            usuario = dao.validateLogin(usuario);

            if (usuario != null) 
            {
                 response.sendRedirect("Equipos.jsp");
            } else 
            {
                response.sendRedirect("error.jsp");
            }

        }

     }
}
