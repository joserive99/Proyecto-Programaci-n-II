/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Torneos.com;

import java.io.IOException;
import java.sql.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class TorneoCRUDServlet extends HttpServlet {

    TorneoDAO dao = new TorneoDAO();

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
            
        String accion = request.getParameter("accion");

        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {

            case "listar":

                List<Torneo> lista = dao.listar();

                request.setAttribute("torneos", lista);

                request.getRequestDispatcher("torneos.jsp").forward(request, response);

                break;

            case "nuevo":

                request.setAttribute("equipos", dao.listarEquipos());

                request.getRequestDispatcher("formularioTorneo.jsp")
                        .forward(request, response);

                break;

            case "editar":

                int id = Integer.parseInt(request.getParameter("id"));

                Torneo torneo = dao.buscarPorId(id);

                request.setAttribute("torneo", torneo);

                request.setAttribute("equipos", dao.listarEquipos());

                request.getRequestDispatcher("formularioTorneo.jsp")
                        .forward(request, response);

                break;

            case "eliminar":

                int eliminar = Integer.parseInt(request.getParameter("id"));

                dao.eliminar(eliminar);

                response.sendRedirect("TorneoCRUDServlet?accion=listar");

                break;

            default:

                response.sendRedirect("TorneoCRUDServlet?accion=listar");

        }

    }

        @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        
            String accion = request.getParameter("accion");

        if ("guardar".equals(accion)) {

            Torneo t = new Torneo();

            t.setNombre(request.getParameter("nombre"));
            t.setDeporte(request.getParameter("deporte"));
            t.setCategoria(request.getParameter("categoria"));
            t.setImagen(request.getParameter("imagen"));

            t.setFechaInicio(Date.valueOf(request.getParameter("fechaInicio")));
            t.setFechaFinal(Date.valueOf(request.getParameter("fechaFinal")));

            t.setPremio(Double.parseDouble(request.getParameter("premio")));

            t.setEstado(request.getParameter("estado"));

            String campeon = request.getParameter("campeon_id");

            if (campeon == null || campeon.isEmpty()) {

                t.setCampeon_id(null);

            } else {

                t.setCampeon_id(Integer.parseInt(campeon));

            }

            dao.agregar(t);

            response.sendRedirect("TorneoCRUDServlet?accion=listar");

        } else if ("actualizar".equals(accion)) {

            Torneo t = new Torneo();

            t.setTorneo_id(Integer.parseInt(request.getParameter("torneo_id")));

            t.setNombre(request.getParameter("nombre"));
            t.setDeporte(request.getParameter("deporte"));
            t.setCategoria(request.getParameter("categoria"));
            t.setImagen(request.getParameter("imagen"));

            t.setFechaInicio(Date.valueOf(request.getParameter("fechaInicio")));
            t.setFechaFinal(Date.valueOf(request.getParameter("fechaFinal")));

            t.setPremio(Double.parseDouble(request.getParameter("premio")));

            t.setEstado(request.getParameter("estado"));

            String campeon = request.getParameter("campeon_id");

            if (campeon == null || campeon.isEmpty()) {

                t.setCampeon_id(null);

            } else {

                t.setCampeon_id(Integer.parseInt(campeon));

            }

            dao.actualizar(t);

            response.sendRedirect("TorneoCRUDServlet?accion=listar");

        }

    }

    }

    
    

