<%-- 
    Document   : Tornedo
    Created on : 4 ago 2026, 2:30:41 p. m.
    Author     : ddani
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Torneos.com.Torneo" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>

<%
    String contexto = request.getContextPath();

    if (session.getAttribute("usuario") == null) {
        response.sendRedirect(contexto + "/Login.jsp");
        return;
    }

    String rol = (String) session.getAttribute("Rol");
    boolean esAdministrador = "ADMIN".equalsIgnoreCase(rol);
    String paginaVolver = esAdministrador ? contexto + "/Administrador.jsp" : contexto + "/Principal.jsp";

    List<Torneo> torneos = (List<Torneo>) request.getAttribute("torneos");
    String error = (String) request.getAttribute("error");

    boolean hayTorneoActivo = false;
    Torneo torneoActivo = null;

    if (torneos != null) {
        for (Torneo torneo : torneos) {
            if ("ACTIVO".equalsIgnoreCase(torneo.getEstado())) {
                hayTorneoActivo = true;
                torneoActivo = torneo;
                break;
            }
        }
    }

    NumberFormat formatoColones = NumberFormat.getCurrencyInstance(new Locale("es", "CR"));
%>

<!DOCTYPE html>

<html lang="es">

<head>

    <meta charset="UTF-8">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Torneos</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>

        body {
            background: #f5f6fa;
        }

        .encabezado {
            background: #212529;
            color: white;
            padding: 18px 0;
            margin-bottom: 25px;
        }

        .card {
            border: none;
            border-radius: 15px;
            overflow: hidden;
            transition: 0.3s;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.20);
        }

        .imagen-torneo {
            width: 100%;
            height: 210px;
            object-fit: contain;
            background: #e9ecef;
            padding: 15px;
        }

        .btn-negro {
            background: #212529;
            border: 1px solid #212529;
            color: white;
        }

        .btn-negro:hover {
            background: #000000;
            border-color: #000000;
            color: white;
        }

    </style>

</head>

<body>

<header class="encabezado">

    <div class="container d-flex justify-content-between align-items-center">

        <div>

            <h2 class="mb-1">Torneos</h2>

            <p class="mb-0">

                <%= esAdministrador ? "Administración de torneos deportivos" : "Consulta de torneos deportivos" %>

            </p>

        </div>

        <div class="d-flex gap-2">

            <% if (torneoActivo != null) { %>

                <a href="<%= contexto %>/PartidoServlet?accion=ver&amp;torneo_id=<%= torneoActivo.getTorneo_id() %>" class="btn btn-light">Ver llaves</a>

            <% } %>

            <a href="<%= paginaVolver %>" class="btn btn-outline-light">← Volver</a>

            <% if (esAdministrador) { %>

                <% if (!hayTorneoActivo) { %>

                    <a href="<%= contexto %>/TorneoServlet?accion=nuevo" class="btn btn-light">Nuevo torneo</a>

                <% } else { %>

                    <button type="button" class="btn btn-secondary" disabled>Ya existe un torneo activo</button>

                <% } %>

            <% } %>

        </div>

    </div>

</header>

<main class="container pb-5">

    <% if (error != null && !error.isBlank()) { %>

        <div class="alert alert-danger">

            <%= error %>

        </div>

    <% } %>

    <form action="<%= contexto %>/TorneoServlet" method="get" class="card shadow-sm p-3 mb-4">

        <input type="hidden" name="accion" value="buscar">

        <div class="row g-2">

            <div class="col-md-9">

                <input type="text" name="nombre" class="form-control" placeholder="Buscar torneo por nombre">

            </div>

            <div class="col-md-3 d-grid">

                <button type="submit" class="btn btn-negro">Buscar</button>

            </div>

        </div>

    </form>

    <% if (torneos == null || torneos.isEmpty()) { %>

        <div class="alert alert-info text-center shadow-sm">

            <h4>No hay torneos registrados</h4>

            <p>Actualmente no existen torneos para mostrar.</p>

            <% if (esAdministrador && !hayTorneoActivo) { %>

                <a href="<%= contexto %>/TorneoServlet?accion=nuevo" class="btn btn-negro">Registrar primer torneo</a>

            <% } %>

        </div>

    <% } else { %>

        <div class="row g-4">

            <% for (Torneo torneo : torneos) { %>

                <%
                    String imagen = torneo.getImagen();

                    if (imagen == null || imagen.isBlank()) {
                        imagen = contexto + "/images/torneo_default.png";
                    } else if (!imagen.startsWith("http://") && !imagen.startsWith("https://")) {
                        imagen = contexto + "/images/" + imagen;
                    }

                    String colorEstado = "bg-secondary";

                    if ("ACTIVO".equalsIgnoreCase(torneo.getEstado())) {
                        colorEstado = "bg-success";
                    } else if ("FINALIZADO".equalsIgnoreCase(torneo.getEstado())) {
                        colorEstado = "bg-dark";
                    } else if ("PENDIENTE".equalsIgnoreCase(torneo.getEstado())) {
                        colorEstado = "bg-warning text-dark";
                    }
                %>

                <div class="col-md-6 col-lg-4">
                    <div class="card shadow h-100">
                        <img src="<%= imagen %>" class="imagen-torneo" alt="Imagen del torneo">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <h4 class="card-title mb-0">

                                    <%= torneo.getNombre() %>

                                </h4>

                                <span class="badge <%= colorEstado %>">

                                    <%= torneo.getEstado() %>

                                </span>

                            </div>

                            <p>

                                <strong>Inicio:</strong>

                                <%= torneo.getFechaInicio() %>

                            </p>

                            <p>

                                <strong>Final:</strong>

                                <%= torneo.getFechaFinal() %>

                            </p>

                            <p>

                                <strong>Premio:</strong>

                                <%= formatoColones.format(torneo.getPremio()) %>

                            </p>

                            <p>

                                <strong>Campeón:</strong>

                                <%= torneo.getNombreCampeon() == null ? "Sin campeón" : torneo.getNombreCampeon() %>

                            </p>
                        </div>
                        <div class="card-footer bg-white border-0 pb-3">
                            <a href="<%= contexto %>/PartidoServlet?accion=ver&amp;torneo_id=<%= torneo.getTorneo_id() %>" class="btn btn-negro w-100">Ver llaves</a>
                        </div>
                    </div>
                </div>
            <% } %>
        </div>
    <% } %>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>