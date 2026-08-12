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

    private final EquipoDAO dao = new EquipoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }

        String accion = request.getParameter("accion");

        if (accion == null || accion.isBlank()) {
            accion = "listar";
        }

        try {

            switch (accion) {

                case "listar":

                    listarEquipos(request, response);
                    break;

                case "nuevo":

                    request.getRequestDispatcher("/RegistrarEquipo.jsp").forward(request, response);
                    break;

                
                case "eliminar":

                    HttpSession sessionEliminar = request.getSession(false);

                    if (sessionEliminar == null || !"ADMIN".equalsIgnoreCase((String) sessionEliminar.getAttribute("Rol"))) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo el administrador puede eliminar equipos.");
                        return;
                    }

                    int idEliminar = Integer.parseInt(request.getParameter("id"));

                    boolean eliminado = dao.eliminarEquipo(idEliminar);

                    if (!eliminado) {

                        request.setAttribute("error", "No se puede eliminar el equipo porque forma parte del historial de un torneo. Los participantes no pueden eliminarse después de generar las llaves.");

                        request.setAttribute("listaEquipos", dao.listarEquipos());

                        request.getRequestDispatcher("/Equipos.jsp").forward(request, response);

                        return;
                    }

                    response.sendRedirect(request.getContextPath() + "/EquipoServlet?accion=listar");

                    break;
                            }

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute("error", e.getMessage());

            listarEquipos(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/Login.jsp");
            return;
        }

        String accion = request.getParameter("accion");

        try {

            if ("guardar".equals(accion)) {

                guardarEquipo(request, response, session);

            } else {

                response.sendRedirect(request.getContextPath() + "/EquipoServlet?accion=listar");

            }

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute("error", e.getMessage());

            request.getRequestDispatcher("/RegistrarEquipo.jsp").forward(request, response);

        }
    }

    private void listarEquipos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Equipo> lista = dao.listarEquipos();

        request.setAttribute("listaEquipos", lista);

        request.getRequestDispatcher("/Equipos.jsp").forward(request, response);
    }

    private void guardarEquipo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {

        String nombre = request.getParameter("nombre");
        String escudo = request.getParameter("escudo");
        String telefono = request.getParameter("telefono");

        validarDatos(nombre, escudo, telefono);

        Object usuarioID = session.getAttribute("UsuarioID");

        if (usuarioID == null) {
            throw new IllegalArgumentException("No se encontró el usuario de la sesión.");
        }

        Equipo equipo = new Equipo();

        equipo.setNombre(nombre.trim());
        equipo.setEscudo(escudo.trim());
        equipo.setTelefono(telefono.trim());
        equipo.setUsuarioID((Integer) usuarioID);

        boolean guardado = dao.guardarEquipo(equipo);

        if (!guardado) {
            throw new IllegalArgumentException("No se pudo guardar el equipo. El nombre puede estar repetido.");
        }

        response.sendRedirect(request.getContextPath() + "/EquipoServlet?accion=listar");
    }

    private void actualizarEquipo(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String equipoIDTexto = request.getParameter("equipoID");
        String nombre = request.getParameter("nombre");
        String escudo = request.getParameter("escudo");
        String telefono = request.getParameter("telefono");

        validarDatos(nombre, escudo, telefono);

        if (equipoIDTexto == null || equipoIDTexto.isBlank()) {
            throw new IllegalArgumentException("No se encontró el identificador del equipo.");
        }

        Equipo equipo = new Equipo();

        equipo.setEquipoID(Integer.parseInt(equipoIDTexto));
        equipo.setNombre(nombre.trim());
        equipo.setEscudo(escudo.trim());
        equipo.setTelefono(telefono.trim());

        boolean actualizado = dao.actualizarEquipo(equipo);

        if (!actualizado) {
            throw new IllegalArgumentException("No se pudo actualizar el equipo.");
        }

        response.sendRedirect(request.getContextPath() + "/EquipoServlet?accion=listar");
    }

    private void validarDatos(String nombre, String escudo, String telefono) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Debe escribir el nombre del equipo.");
        }

        if (escudo == null || escudo.isBlank()) {
            throw new IllegalArgumentException("Debe escribir el nombre de la imagen del escudo.");
        }

        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("Debe escribir el teléfono.");
        }
    }
}