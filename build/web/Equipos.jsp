<%-- 
    Document   : Equipos
    Created on : 15 jul 2026, 3:04:11 p. m.
    Author     : USER
--%>
<%@page import="java.util.List"%>
<%@page import="Equipo.com.Equipo"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect("Login.jsp");
        return;
    }
%>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Equipos Registrados</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <nav class="navbar navbar-dark bg-primary mb-4 shadow-sm">
        <div class="container-fluid px-4">
            <a class="navbar-brand fw-bold" href="Administrador.jsp">Sistema de Torneos</a>
            <div class="d-flex align-items-center text-white">
                <span class="me-3">Bienvenido, <strong>Administrador de torneos</strong></span>
                <a href="LogoutServlet" class="btn btn-danger btn-sm">Cerrar Sesión</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="bg-white p-4 rounded shadow-sm">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h3 class="mb-0 fw-bold" style="color: #4a4a4a;">Equipos Registrados</h3>
                <a href="RegistrarEquipo.jsp" class="btn btn-success px-4">+ Nuevo Equipo</a>
            </div>

            <div class="table-responsive">
                <table class="table table-hover align-middle border">
                    <thead class="table-light">
                        <tr>
                            <th scope="col" class="text-center">Escudo</th>
                            <th scope="col">Nombre</th>
                            <th scope="col">Teléfono</th>
                            <th scope="col" class="text-center">Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            List<Equipo> listaEquipos = (List<Equipo>) request.getAttribute("listaEquipos");
                            
                            if (listaEquipos != null && !listaEquipos.isEmpty()) {
                                for (Equipo equipo : listaEquipos) { 
                        %>
                                    <tr>
                                        <td class="text-center">
                                            <img src="imagenes/<%= equipo.getEscudo() %>" alt="Escudo" style="width: 40px; height: 40px;">
                                        </td>
                                        <td class="fw-bold"><%= equipo.getNombre() %></td>
                                        <td><%= equipo.getTelefono() %></td>
                                        <td class="text-center">
                                            <a href="EquiposServlet?accion=editar&id=<%= equipo.getEquipoID() %>" 
                                               class="btn btn-warning btn-sm me-2">Editar</a>
                                            <a href="EquiposServlet?accion=eliminar&id=<%= equipo.getEquipoID() %>" 
                                               class="btn btn-danger btn-sm" 
                                               onclick="return confirm('¿Eliminar?')">Eliminar</a>
                                        </td>
                                    </tr>
                        <% 
                                } 
                            } else {
                        %>
                            <tr>
                                <td colspan="4" class="text-center py-4 text-muted">No hay equipos registrados.</td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
            
            <div class="mt-4">
                <a href="Administrador.jsp" class="btn btn-secondary btn-sm">Volver al Panel</a>
            </div>
        </div>
    </div>
</body>
</html>