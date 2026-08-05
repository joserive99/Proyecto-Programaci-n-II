<%-- 
    Document   : Tornedo
    Created on : 4 ago 2026, 2:30:41 p. m.
    Author     : ddani
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Torneos.com.Torneo" %>
<%@ page import="java.util.List" %>

<%
    List<Torneo> torneos = (List<Torneo>) request.getAttribute("torneos");
    boolean hayTorneoActivo = false;

    if (torneos != null) {
        for (Torneo torneo : torneos) {
            if ("ACTIVO".equalsIgnoreCase(torneo.getEstado())) {
                hayTorneoActivo = true;
                break;
            }
        }
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Torneos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background: #f5f6fa; }
        .encabezado { background: #212529; color: white; padding: 18px 0; margin-bottom: 25px; }
        .card { border: none; border-radius: 15px; }
        .imagen-torneo { width: 100%; height: 210px; object-fit: cover; }
    </style>
</head>
<body>

<header class="encabezado">
    <div class="container d-flex justify-content-between align-items-center">
        <div>
            <h2 class="mb-1">Torneos</h2>
            <p class="mb-0">Administracion de torneos deportivos</p>
        </div>
        <div class="d-flex gap-2">
        <a href="<%= request.getContextPath() %>/Administrador.jsp"
           class="btn btn-outline-light">← Volver
        </a>
        <% if (!hayTorneoActivo) { %>
            <a href="<%= request.getContextPath() %>/TorneoServlet?accion=nuevo" class="btn btn-light">Nuevo torneo</a>
        <% } else { %>
            <button class="btn btn-secondary" disabled>Ya existe un torneo activo</button>
        <% } %>
    </div>
</header>

<main class="container pb-5">
    <form action="<%= request.getContextPath() %>/TorneoServlet" method="get" class="row g-2 mb-4">
        <input type="hidden" name="accion" value="buscar">
        <div class="col-md-9"><input type="search" name="nombre" class="form-control" placeholder="Buscar torneo por nombre"></div>
        <div class="col-md-3 d-grid"><button type="submit" class="btn btn-dark">Buscar</button></div>
    </form>

    <% if (torneos == null || torneos.isEmpty()) { %>
        <div class="alert alert-info text-center">No hay torneos registrados.</div>
    <% } else { %>
        <div class="row g-4">
            <% for (Torneo torneo : torneos) { %>
                <div class="col-md-6 col-lg-4">
                    <div class="card shadow h-100 overflow-hidden">
                        <img src="<%= torneo.getImagen() %>" class="imagen-torneo" alt="Imagen de <%= torneo.getNombre() %>">

                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <h4 class="mb-0"><%= torneo.getNombre() %></h4>
                                <span class="badge <%= "ACTIVO".equalsIgnoreCase(torneo.getEstado()) ? "bg-success" : "bg-secondary" %>"><%= torneo.getEstado() %></span>
                            </div>

                            <p><strong>Inicio:</strong> <%= torneo.getFechaInicio() %></p>
                            <p><strong>Final:</strong> <%= torneo.getFechaFinal() %></p>
                            <p><strong>Premio:</strong> ₡<%= String.format("%,.2f", torneo.getPremio()) %></p>
                            <p><strong>Campeon:</strong> <%= torneo.getNombreCampeon() == null ? "Sin campeon" : torneo.getNombreCampeon() %></p>
                        </div>
                    </div>
                </div>
            <% } %>
        </div>
    <% } %>
</main>

</body>
</html>