package Torneos.com;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@WebServlet("/TorneoServlet")
public class TorneoServlet extends HttpServlet {

    private final TorneoDAO dao = new TorneoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null || accion.isBlank()) {
            accion = "listar";
        }

        try {
            switch (accion) {
                case "nuevo":
                    mostrarFormulario(request, response);
                    break;
                case "buscar":
                    String nombre = request.getParameter("nombre");
                    request.setAttribute("torneos", dao.buscar(nombre == null ? "" : nombre.trim()));
                    request.getRequestDispatcher("/Torneos.jsp").forward(request, response);
                    break;
                case "listar":
                    request.setAttribute("torneos", dao.listar());
                    request.getRequestDispatcher("/Torneos.jsp").forward(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/TorneoServlet?accion=listar");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "No se pudo cargar la informacion: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if (!"crear".equals(accion)) {
            response.sendRedirect(request.getContextPath() + "/TorneoServlet?accion=listar");
            return;
        }

        try {
            Torneo torneo = leerTorneo(request);
            List<Integer> equipos = leerEquipos(request);
            dao.crearConLlaves(torneo, equipos);
            response.sendRedirect(request.getContextPath() + "/TorneoServlet?accion=listar");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());

            try {
                request.setAttribute("equipos", dao.listarEquiposAprobados());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            request.getRequestDispatcher("/FormularioTorneo.jsp").forward(request, response);
        }
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String[]> equipos = dao.listarEquiposAprobados();
        request.setAttribute("equipos", equipos);

        if (equipos.size() != 16) {
            request.setAttribute("error", "Deben existir exactamente 16 equipos aprobados. Actualmente hay " + equipos.size() + ".");
        }

        request.getRequestDispatcher("/FormularioTorneo.jsp").forward(request, response);
    }

    private Torneo leerTorneo(HttpServletRequest request) {
        String nombre = request.getParameter("nombre");
        String imagen = request.getParameter("imagen");
        String fechaInicioTexto = request.getParameter("fechaInicio");
        String fechaFinalTexto = request.getParameter("fechaFinal");
        String premioTexto = request.getParameter("premio");

        if (nombre == null || nombre.isBlank() || imagen == null || imagen.isBlank() || fechaInicioTexto == null || fechaFinalTexto == null || premioTexto == null) {
            throw new IllegalArgumentException("Debe completar todos los datos del torneo.");
        }

        Torneo torneo = new Torneo();
        torneo.setNombre(nombre.trim());
        torneo.setImagen(imagen.trim());
        torneo.setFechaInicio(Date.valueOf(fechaInicioTexto));
        torneo.setFechaFinal(Date.valueOf(fechaFinalTexto));
        torneo.setPremio(Double.parseDouble(premioTexto));
        torneo.setEstado("ACTIVO");
        torneo.setCampeon_id(null);

        if (torneo.getFechaFinal().before(torneo.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha final no puede ser anterior a la fecha inicial.");
        }

        if (torneo.getPremio() < 0) {
            throw new IllegalArgumentException("El premio no puede ser negativo.");
        }

        return torneo;
    }

    private List<Integer> leerEquipos(HttpServletRequest request) {
        String[] seleccionados = request.getParameterValues("equipos");

        if (seleccionados == null || seleccionados.length != 16) {
            throw new IllegalArgumentException("Debe seleccionar exactamente 16 equipos.");
        }

        List<Integer> equipos = new ArrayList<>();
        for (String equipoId : seleccionados) {
            equipos.add(Integer.parseInt(equipoId));
        }

        if (new HashSet<>(equipos).size() != 16) {
            throw new IllegalArgumentException("No puede seleccionar equipos repetidos.");
        }

        return equipos;
    }
}