<%-- 
    Document   : Principal
    Created on : 18 jul 2026, 4:13:29 p. m.
    Author     : Jose
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Panel de Usuario / Torneos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <%
        if (session.getAttribute("usuario") == null) {
            response.sendRedirect("Login.jsp");
            return;
        }
    %>

    <nav class="navbar navbar-dark bg-primary mb-4">
        <div class="container">
            <span class="navbar-brand mb-0 h1">Sistema de Torneos</span>
            <a href="LogoutServlet" class="btn btn-danger btn-sm">Cerrar Sesión</a>
        </div>
    </nav>

    <div class="container">
        <div class="bg-white p-5 rounded shadow-sm">
            <h2 class="mb-4">Bienvenido, ${usuario.nombre}</h2>
            <p>Selecciona una opción del sistema:</p>           
            <hr>        
            <div class="d-grid gap-2 d-md-block">
                <a href="EquipoServlet?accion=listar" class="btn btn-success">Ver Equipos Registrados</a>
            </div>
        </div>
    </div>

</body>
</html>