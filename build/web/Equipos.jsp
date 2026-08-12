<%-- 
    Document   : Equipos
    Created on : 15 jul 2026, 3:04:11 p. m.
    Author     : USER
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Equipo.com.Equipo" %>

<%
    String contexto = request.getContextPath();

    if (session.getAttribute("usuario") == null) {
        response.sendRedirect(contexto + "/Login.jsp");
        return;
    }

    String rol = (String) session.getAttribute("Rol");
    String nombreUsuario = (String) session.getAttribute("Nombre");

    boolean esAdministrador = "ADMIN".equalsIgnoreCase(rol);

    String paginaVolver;

    if (esAdministrador) {
        paginaVolver = contexto + "/Administrador.jsp";
    } else {
        paginaVolver = contexto + "/Principal.jsp";
    }

    List<Equipo> listaEquipos = (List<Equipo>) request.getAttribute("listaEquipos");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>

<html lang="es">

<head>

    <meta charset="UTF-8">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Equipos registrados</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>

        body {
            background: #f5f6fa;
        }

        .navbar {
            background: #212529;
        }

        .contenedor-principal {
            border-radius: 15px;
        }

        .escudo {
            width: 55px;
            height: 55px;
            object-fit: contain;
        }

    </style>

</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container-fluid px-4">
        <a class="navbar-brand fw-bold" href="<%= paginaVolver %>">
            Sistema de Torneos
        </a>
        <div class="d-flex align-items-center text-white">
            <span class="me-3">
               Bienvenido,
                <strong>
                    <%= nombreUsuario == null ? "Usuario" : nombreUsuario %>
                </strong>
            </span>
            <a href="<%= contexto %>/LogoutServlet" class="btn btn-danger btn-sm">
                Cerrar sesión
            </a>
        </div>
    </div>
</nav>

<main class="container py-4">
    <div class="bg-white p-4 shadow-sm contenedor-principal">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="mb-1 fw-bold">
                    Equipos registrados
                </h3>

                <p class="text-muted mb-0">
                <%= esAdministrador ? "Administración de equipos": "Registro y consulta de equipos"  %>

                </p>
            </div>
            <a href="<%= contexto %>/EquipoServlet?accion=nuevo" class="btn btn-success px-4">+ Nuevo equipo</a>
         </div>
        <% if (error != null && !error.isBlank()) { %>
            <div class="alert alert-danger">
                <%= error %>
            </div>
        <% } %>
        <div class="table-responsive">
            <table class="table table-hover align-middle border">
                <thead class="table-light">
                    <tr>
                        <th class="text-center">Escudo</th>
                        <th>Nombre</th>
                        <th>Teléfono</th>
                        <% if (esAdministrador) { %>
                            <th class="text-center">Acciones</th>
                        <% } %>
                    </tr>
                </thead>
                <tbody>
                    <% if (listaEquipos != null && !listaEquipos.isEmpty()) { %>
                        <% for (Equipo equipo : listaEquipos) { %>
                            <tr>
                                <td class="text-center">
                                    <img src="<%= equipo.getEscudo() %>" class="escudo" alt="Escudo de <%= equipo.getNombre() %>">
                                </td>
                                <td class="fw-bold">
                                    <%= equipo.getNombre() %>
                                </td>
                                <td>
                                    <%= equipo.getTelefono() %>
                                </td>
                                <% if (esAdministrador) { %>
                                    <td class="text-center">
                                        <a href="<%= contexto %>/EquipoServlet?accion=eliminar&amp;id=<%= equipo.getEquipoID() %>" class="btn btn-danger btn-sm" onclick="return confirm('¿Está seguro de que desea eliminar este equipo?');">
                                            Eliminar
                                        </a>
                                    </td>
                                <% } %>
                            </tr>
                        <% } %>
                    <% } else { %>
                        <tr>
                            <td colspan="<%= esAdministrador ? 4 : 3 %>" class="text-center py-4 text-muted">No hay equipos registrados.
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

        <div class="mt-4">
            <a href="<%= paginaVolver %>" class="btn btn-secondary">← Volver al panel</a>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>