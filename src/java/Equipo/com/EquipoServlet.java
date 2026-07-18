/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Equipo.com;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/EquipoServlet")
public class EquipoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        EquipoDAO dao = new EquipoDAO();
        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {

            case "listar":

                List<Equipo> lista = dao.listarEquipos();
                System.out.println("Tamaño de la lista encontrada: " + lista.size());
                request.setAttribute("listaEquipos", lista);
                request.getRequestDispatcher("Equipos.jsp")
                        .forward(request, response);
                break;

            case "editar":

                int idEditar = Integer.parseInt(request.getParameter("id"));
                Equipo equipo = dao.buscarEquipo(idEditar);
                request.setAttribute("equipo", equipo);
                request.getRequestDispatcher("EditarEquipo.jsp")
                        .forward(request, response);

                break;

            case "eliminar":

                int idEliminar = Integer.parseInt(request.getParameter("id"));
                dao.eliminarEquipo(idEliminar);
                response.sendRedirect("EquipoServlet?accion=listar");
                break;

            default:
                response.sendRedirect("EquipoServlet?accion=listar");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        EquipoDAO dao = new EquipoDAO();
        if ("guardar".equals(accion)) {

            Equipo equipo = new Equipo();
            equipo.setNombre(request.getParameter("Nombre"));
            equipo.setEscudo(request.getParameter("Escudo"));
            equipo.setTelefono(request.getParameter("Telefono"));

            HttpSession session = request.getSession();
            equipo.setUsuarioID((Integer) session.getAttribute("UsuarioID"));
            dao.guardarEquipo(equipo);
            response.sendRedirect("EquipoServlet?accion=listar");

        }

        else if ("actualizar".equals(accion)) {

            Equipo equipo = new Equipo();
            equipo.setEquipoID(
                    Integer.parseInt(request.getParameter("EquipoID")));
            equipo.setNombre(request.getParameter("Nombre"));
            equipo.setEscudo(request.getParameter("Escudo"));
            equipo.setTelefono(request.getParameter("Telefono"));
            dao.actualizarEquipo(equipo);
            response.sendRedirect("EquipoServlet?accion=listar");

        }

    }

}