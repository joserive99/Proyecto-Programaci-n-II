
package Login.com;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;


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

            if (usuario != null) {
                 HttpSession sesionAnterior = request.getSession(false);

            if (sesionAnterior != null) {
                sesionAnterior.invalidate();
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("usuario", usuario);
            session.setAttribute("UsuarioID", usuario.getUsuarioID());
            session.setAttribute("Nombre", usuario.getNombre());
            session.setAttribute("Rol", usuario.getRol());

            if ("ADMIN".equalsIgnoreCase(usuario.getRol())) {

                    response.sendRedirect(request.getContextPath() + "/Administrador.jsp");

                } else {

                    response.sendRedirect(request.getContextPath() + "/Principal.jsp");
                }

                return;

            } else {

                response.sendRedirect(request.getContextPath() + "/error.jsp");

                return;
            }

      }
    }
 }            


