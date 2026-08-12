package Partidos.com;

import Torneos.com.Torneo;
import Torneos.com.TorneoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/PartidoServlet")
public class PartidoServlet extends HttpServlet {

    private final PartidoDAO dao = new PartidoDAO();
    private final TorneoDAO torneoDAO = new TorneoDAO();

    // Mostrar las llaves
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

       String accion = request.getParameter("accion");

    if (accion == null || accion.isBlank()) {
        accion = "ver";
    }

    try {

        switch (accion) {

            case "ver":

                String torneoIdTexto = request.getParameter("torneo_id");

                if (torneoIdTexto == null || torneoIdTexto.isBlank()) {
                    throw new IllegalArgumentException("No se recibió el ID del torneo.");
                }

                int torneoId = Integer.parseInt(torneoIdTexto);

                mostrarLlaves(request, response, torneoId);

                break;

            default:

                response.sendRedirect(request.getContextPath() + "/TorneoServlet?accion=listar");

                break;
        }

    } catch (Exception e) {

        e.printStackTrace();

        request.setAttribute("error", "No se pudieron cargar las llaves: " + e.getMessage());

        request.getRequestDispatcher("/error.jsp").forward(request, response);
    }
}

    // Registrar el resultado de un partido
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!estaAutenticado(request)) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }

        String accion = request.getParameter("accion");

        if (!"resultado".equals(accion)) {
            response.sendRedirect(request.getContextPath() + "/TorneoServlet?accion=listar");
            return;
        }

        if (!esAdministrador(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo el administrador puede registrar resultados.");
            return;
        }

        int torneoId = 0;

        try {

            torneoId = Integer.parseInt(request.getParameter("torneo_id"));

            int partidoId = Integer.parseInt(request.getParameter("partido_id"));
            int marcadorLocal = Integer.parseInt(request.getParameter("marcador_local"));
            int marcadorVisita = Integer.parseInt(request.getParameter("marcador_visita"));

            if (marcadorLocal < 0 || marcadorVisita < 0) {
                throw new IllegalArgumentException("Los marcadores no pueden ser negativos.");
            }

            if (marcadorLocal == marcadorVisita) {
                throw new IllegalArgumentException("No se permiten empates.");
            }

            dao.registrarResultado(partidoId, marcadorLocal, marcadorVisita);

            response.sendRedirect(request.getContextPath() + "/PartidoServlet?accion=ver&torneo_id=" + torneoId + "&mensaje=resultadoGuardado");

        } catch (NumberFormatException e) {

            request.setAttribute("error", "Los marcadores deben ser números enteros.");

            volverALasLlaves(request, response, torneoId);

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute("error", e.getMessage());

            volverALasLlaves(request, response, torneoId);
        }
    }

    // Cargar todos los datos para Llaves.jsp
    private void mostrarLlaves(HttpServletRequest request, HttpServletResponse response, int torneoId) throws Exception {

        Torneo torneo = torneoDAO.buscarPorId(torneoId);

        if (torneo == null) {
            throw new IllegalArgumentException("El torneo solicitado no existe.");
        }

        List<Partido> partidos = dao.listarPorTorneo(torneoId);

        if (partidos == null || partidos.isEmpty()) {
            throw new IllegalArgumentException("El torneo no tiene partidos generados.");
        }

        List<String[]> participantes = torneoDAO.listarParticipantes(torneoId);
        List<String[]> clasificados = dao.listarClasificados(torneoId);

        List<Partido> preliminares = new ArrayList<>();
        List<Partido> cuartos = new ArrayList<>();
        List<Partido> semifinales = new ArrayList<>();
        List<Partido> finalTorneo = new ArrayList<>();

        for (Partido partido : partidos) {

            switch (partido.getRonda()) {

                case 1:
                    preliminares.add(partido);
                    break;

                case 2:
                    cuartos.add(partido);
                    break;

                case 3:
                    semifinales.add(partido);
                    break;

                case 4:
                    finalTorneo.add(partido);
                    break;

                default:
                    break;
            }
        }

        request.setAttribute("torneo", torneo);
        request.setAttribute("torneoId", torneoId);
        request.setAttribute("nombreTorneo", torneo.getNombre());
        request.setAttribute("participantes", participantes);
        request.setAttribute("clasificados", clasificados);
        request.setAttribute("preliminares", preliminares);
        request.setAttribute("cuartos", cuartos);
        request.setAttribute("semifinales", semifinales);
        request.setAttribute("finalTorneo", finalTorneo);
        request.setAttribute("esAdministrador", esAdministrador(request));

        request.getRequestDispatcher("/Llaves.jsp").forward(request, response);
    }

    // Regresar a las llaves cuando ocurre un error
    private void volverALasLlaves(HttpServletRequest request, HttpServletResponse response, int torneoId) throws ServletException, IOException {

        if (torneoId <= 0) {
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        try {

            mostrarLlaves(request, response, torneoId);

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute("error", "No se pudieron volver a cargar las llaves: " + e.getMessage());

            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    // Comprobar que inició sesión
    private boolean estaAutenticado(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        return session != null && session.getAttribute("usuario") != null;
    }

    // Comprobar que el usuario es administrador
    private boolean esAdministrador(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return false;
        }

        String rol = (String) session.getAttribute("Rol");

        return "ADMIN".equalsIgnoreCase(rol);
    }
}
