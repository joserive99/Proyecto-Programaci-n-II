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

                    if (nombre == null) {
                        nombre = "";
                    }

                    request.setAttribute("torneos", dao.buscar(nombre.trim()));

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

            request.setAttribute("error", "No se pudo cargar la información: " + e.getMessage());

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

            int torneoId = dao.crearConLlaves(torneo, equipos);

            response.sendRedirect(request.getContextPath() + "/PartidoServlet?accion=ver&torneo_id=" + torneoId);

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute("error", e.getMessage());

            try {

                request.setAttribute("equipos", dao.listarEquipos());

            } catch (Exception ex) {

                ex.printStackTrace();
            }

            request.getRequestDispatcher("/formularioTorneo.jsp").forward(request, response);
        }
    }

    
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<String[]> equipos = dao.listarEquipos();

        request.setAttribute("equipos", equipos);

        if (equipos.size() < 16) {
            request.setAttribute("error", "Deben existir al menos 16 equipos registrados. Actualmente hay " + equipos.size() + ".");
        }

        request.getRequestDispatcher("/formularioTorneo.jsp").forward(request, response);
    }

    //leer informacio
    private Torneo leerTorneo(HttpServletRequest request) {

        String nombre = request.getParameter("nombre");
        String imagen = request.getParameter("imagen");
        String fechaInicioTexto = request.getParameter("fechaInicio");
        String fechaFinalTexto = request.getParameter("fechaFinal");
        String premioTexto = request.getParameter("premio");

        if (nombre == null || nombre.isBlank() || imagen == null || imagen.isBlank() || fechaInicioTexto == null || fechaInicioTexto.isBlank() || fechaFinalTexto == null || fechaFinalTexto.isBlank() || premioTexto == null || premioTexto.isBlank()) {

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

    //leemos los 16 equipos
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