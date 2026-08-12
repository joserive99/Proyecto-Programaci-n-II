<%-- 
    Document   : Administrador.jsp
    Created on : 18 jul 2026, 12:53:26 p. m.
    Author     : Jose
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect(request.getContextPath() + "/Login.jsp");
        return;
    }

    if (!"ADMIN".equals(session.getAttribute("Rol"))) {
        response.sendRedirect(request.getContextPath() + "/Principal.jsp");
        return;
    }

    String contexto = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="es">

<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Panel de Administración</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>

        body {
            background: #f5f6fa;
        }

        .navbar {
            background: #212529;
        }

        .navbar-brand {
            font-weight: bold;
        }

        .card {
            border: none;
            border-radius: 15px;
            transition: 0.3s;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.20);
        }

        .card-body {
            padding: 35px;
        }

        .icon {
            font-size: 50px;
        }

        .btn-dark {
            background: #212529;
            border-color: #212529;
        }

        .btn-dark:hover {
            background: #000000;
            border-color: #000000;
        }

    </style>

</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container">
        <a class="navbar-brand" href="<%= contexto %>/Administrador.jsp">Sistema de Torneos</a>
        <div class="ms-auto text-white">
            Bienvenido, <strong><%= session.getAttribute("Nombre") %></strong>
            <a href="<%= contexto %>/LogoutServlet" class="btn btn-danger btn-sm ms-2">Cerrar sesión</a>
        </div>
    </div>
</nav>
<main class="container py-5">
    <div class="row g-4">
        <div class="col-md-4">
            <div class="card shadow h-100">
                <div class="card-body text-center">
                    <div class="icon">👥</div>
                    <h4 class="mt-3">Usuarios</h4>
                    <p>Administrar usuarios registrados.</p>
                    <a href="<%= contexto %>/UsuarioServlet?accion=listar" class="btn btn-dark">Ingresar</a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card shadow h-100">
                <div class="card-body text-center">
                    <div class="icon">⚽</div>
                    <h4 class="mt-3">Equipos</h4>
                    <p>Registrar y editar equipos.</p>
                    <a href="<%= contexto %>/EquipoServlet?accion=listar" class="btn btn-dark">Ingresar</a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card shadow h-100">
                <div class="card-body text-center">
                    <div class="icon">🏆</div>
                    <h4 class="mt-3">Torneos</h4>
                    <p>Gestionar torneos.</p>
                    <a href="<%= contexto %>/TorneoServlet?accion=listar" class="btn btn-dark">Ingresar</a>
                </div>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>