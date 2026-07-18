<%-- 
    Document   : Usuarios
    Created on : 18 jul 2026, 2:53:30 p. m.
    Author     : Jose
--%>
<%@page import="java.util.List"%>
<%@page import="Login.com.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Usuarios Registrados</title>
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
                <h3 class="mb-0 fw-bold" style="color: #4a4a4a;">Usuarios Registrados</h3>
                <a href="Registrarse.jsp" class="btn btn-primary px-4">+ Nuevo Usuario</a>
            </div>

            <div class="table-responsive">
                <table class="table table-hover align-middle border">
                    <thead class="table-light">
                        <tr>
                            <th scope="col" class="text-center">#</th>
                            <th scope="col">Nombre</th>
                            <th scope="col">Correo</th>
                            <th scope="col">Rol</th> <!-- Nueva columna de Rol -->
                            <th scope="col" class="text-center">Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            List<Usuario> listaUsuarios = (List<Usuario>) request.getAttribute("usuarios");
                            
                            if (listaUsuarios != null && !listaUsuarios.isEmpty()) {
                                int contador = 1;
                                for (Usuario usuario : listaUsuarios) { 
                        %>
                                    <tr>
                                        <td class="text-center fw-bold text-secondary"><%= contador %></td>
                                        <td><%= usuario.getNombre() %></td>
                                        <td><%= usuario.getCorreo() %></td>
                                        <td><span class="badge bg-info text-dark"><%= usuario.getRol() %></span></td>
                                        <td class="text-center">
                                            <a href="UsuarioServlet?accion=editar&id=<%= usuario.getUsuarioID() %>" 
                                               class="btn btn-warning btn-sm me-2 text-dark fw-semibold">Editar</a>
                                            <a href="UsuarioServlet?accion=eliminar&id=<%= usuario.getUsuarioID() %>" 
                                               class="btn btn-danger btn-sm fw-semibold" 
                                               onclick="return confirm('¿Eliminar este usuario?')">Eliminar</a>
                                        </td>
                                    </tr>
                        <% 
                                    contador++;
                                } 
                            } else {
                        %>
                            <tr>
                                <td colspan="5" class="text-center py-4 text-muted">No hay usuarios registrados.</td>
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
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>