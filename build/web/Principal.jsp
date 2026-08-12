<%-- 
    Document   : Principal
    Created on : 18 jul 2026, 4:13:29 p. m.
    Author     : Jose
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect(request.getContextPath() + "/Login.jsp");
        return;
    }

    String contexto = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="es">

<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Panel de usuario</title>

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

        .icon {
            font-size: 50px;
        }

    </style>

</head>
<body>
<nav class="navbar navbar-dark">
    <div class="container">
        <span class="navbar-brand">Sistema de Torneos</span>
        <div class="text-white">
            Bienvenido, <strong><%= session.getAttribute("Nombre") %></strong>
            <a href="<%= contexto %>/LogoutServlet" class="btn btn-danger btn-sm ms-2">Cerrar sesión</a>
        </div>
    </div>
</nav>
<main class="container py-5">
    <div class="text-center mb-5">
        <h2>Panel de usuario</h2>
        <p class="text-muted">Seleccione la información que desea consultar</p>
    </div>
    <div class="row justify-content-center g-4">
        <div class="col-md-5">
            <div class="card shadow h-100">
                <div class="card-body text-center p-5">
                    <div class="icon">⚽</div>
                    <h4 class="mt-3">Equipos</h4>
                    <p>Consultar los equipos registrados.</p>
                    <a href="<%= contexto %>/EquipoServlet?accion=listar" class="btn btn-dark">Consultar equipos</a>
                </div>
            </div>
        </div>
        <div class="col-md-5">
            <div class="card shadow h-100">
                <div class="card-body text-center p-5">
                    <div class="icon">🏆</div>
                    <h4 class="mt-3">Torneos</h4>
                    <p>Consultar torneos, participantes, llaves y resultados.</p>
                    <a href="<%= contexto %>/TorneoServlet?accion=listar" class="btn btn-dark">Consultar torneos</a>
                </div>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>