
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
        LoginDAO dao = new LoginDAO();
        Usuario usuario = new Usuario();
        
        usuario.setCorreo(request.getParameter("Correo"));
        usuario.setContrasena(request.getParameter("Contrasena"));
                
        usuario = dao.validateLogin(usuario);
        
        if (usuario!= null){
            response.sendRedirect("main.jsp");
        }
        else
        {
            response.sendRedirect("error.jsp");
        }
    }

}
