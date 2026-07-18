/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Login.com;

import Login.com.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
/**
 *
 * @author Jose
 */
@WebServlet("/UsuarioServlet")
public class UsuarioServlet extends HttpServlet {
protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
    String accion = request.getParameter("accion");
    
    if ("listar".equals(accion)) {
        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> lista = dao.listar(); 
        
        request.setAttribute("usuarios", lista); 
        
        request.getRequestDispatcher("Usuarios.jsp").forward(request, response);
    }
}
}
