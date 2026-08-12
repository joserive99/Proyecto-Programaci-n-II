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

                case "editar":

                    int idEditar = Integer.parseInt(request.getParameter("id"));

                    Equipo equipo = dao.buscarEquipo(idEditar);

                    if (equipo == null) {
                        throw new IllegalArgumentException("El equipo solicitado no existe.");
                    }

                    request.setAttribute("equipo", equipo);

                    request.getRequestDispatcher("/EditarEquipo.jsp").forward(request, response);
                    break;

                case "eliminar":

                    int idEliminar = Integer.parseInt(request.getParameter("id"));

                    boolean eliminado = dao.eliminarEquipo(idEliminar);

                    if (!eliminado) {
                        throw new IllegalArgumentException("No se pudo eliminar el equipo. Puede estar participando en un torneo.");
                    }

                    response.sendRedirect(request.getContextPath() + "/EquipoServlet?accion=listar");
                    break;

                default:

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

            } else if ("actualizar".equals(accion)) {

                actualizarEquipo(request, response);

            } else {

                response.sendRedirect(request.getContextPath() + "/EquipoServlet?accion=listar");
            }

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute("error", e.getMessage());

            if ("actualizar".equals(accion)) {

                cargarEquipoParaEditar(request);

                request.getRequestDispatcher("/EditarEquipo.jsp").forward(request, response);

            } else {

                request.getRequestDispatcher("/RegistrarEquipo.jsp").forward(request, response);
            }
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

    private void cargarEquipoParaEditar(HttpServletRequest request) {

        String equipoIDTexto = request.getParameter("equipoID");

        if (equipoIDTexto != null && !equipoIDTexto.isBlank()) {
            Equipo equipo = dao.buscarEquipo(Integer.parseInt(equipoIDTexto));
            request.setAttribute("equipo", equipo);
        }
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