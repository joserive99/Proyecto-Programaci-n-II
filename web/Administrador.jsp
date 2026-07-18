<%-- 
    Document   : Administrador.jsp
    Created on : 18 jul 2026, 12:53:26 p. m.
    Author     : Jose
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel de Administración</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>

        body{
            background:#f5f6fa;
        }

        .navbar-brand{
            font-weight:bold;
        }

        .card{
            transition:.3s;
            border:none;
            border-radius:15px;
        }

        .card:hover{
            transform:translateY(-5px);
            box-shadow:0 8px 20px rgba(0,0,0,.2);
        }

        .icon{
            font-size:50px;
        }

    </style>

</head>

<body>
    <%
if(session == null || session.getAttribute("usuario") == null){
    response.sendRedirect("Login.jsp");
    return;
}
%>

<%
    if(session.getAttribute("usuario")==null){
        response.sendRedirect("Login.jsp");
        return;
    }

    if(session.getAttribute("Rol")==null ||
       !"ADMIN".equals(session.getAttribute("Rol"))){

        response.sendRedirect("Equipos.jsp");
        return;
    }
%>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">

    <div class="container">

        <a class="navbar-brand" href="#">
            Sistema de Torneos
        </a>
        <div class="ms-auto text-white">
            Bienvenido,
            <strong><%=session.getAttribute("Nombre")%></strong>
            <a href="LogoutServlet" class="btn btn-danger">
            Cerrar Sesión
        </a>
        </div>
    </div>
</nav>
<div class="container mt-5">
    <div class="row g-4">
        <div class="col-md-4">
            <div class="card shadow">
                <div class="card-body text-center">
                    <div class="icon">👥</div>
                    <h4 class="mt-3">Usuarios</h4>
                    <p>Administrar usuarios registrados.</p>
                    <a href="UsuarioServlet?accion=listar"class="btn btn-primary">Ingresar
                    </a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card shadow">
                <div class="card-body text-center">
                    <div class="icon">⚽</div>
                    <h4 class="mt-3">Equipos</h4>
                    <p>Registrar y editar equipos.</p>
                    <a href="EquipoServlet?accion=listar" class="btn btn-success">Ingresar</a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card shadow">
                <div class="card-body text-center">
                    <div class="icon">🏆</div>
                    <h4 class="mt-3">Torneos</h4>
                    <p>Gestionar torneos.</p>
                    <a href="Torneos.jsp"
                       class="btn btn-warning text-white">
                        Ingresar
                    </a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card shadow">
                <div class="card-body text-center">
                    <div class="icon">📅</div>
                    <h4 class="mt-3">Partidos</h4>
                    <p>Crear y administrar partidos.</p>
                    <a href="Partidos.jsp"
                       class="btn btn-info text-white">
                        Ingresar
                    </a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card shadow">
                <div class="card-body text-center">
                    <div class="icon">🥇</div>
                    <h4 class="mt-3">Resultados</h4>
                    <p>Registrar resultados.</p>
                    <a href="Resultados.jsp"
                       class="btn btn-secondary">
                        Ingresar
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>