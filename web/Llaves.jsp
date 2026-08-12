<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Torneos.com.Torneo" %>
<%@ page import="Partidos.com.Partido" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>

<%
    String contexto = request.getContextPath();

    if (session.getAttribute("usuario") == null) {
        response.sendRedirect(contexto + "/Login.jsp");
        return;
    }

    String rol = (String) session.getAttribute("Rol");
    boolean esAdministrador = "ADMIN".equalsIgnoreCase(rol);

    String paginaPrincipal;

    if (esAdministrador) {
        paginaPrincipal = contexto + "/Administrador.jsp";
    } else {
        paginaPrincipal = contexto + "/Principal.jsp";
    }

    Torneo torneo = (Torneo) request.getAttribute("torneo");

    Integer torneoId = (Integer) request.getAttribute("torneoId");
    String nombreTorneo = (String) request.getAttribute("nombreTorneo");

    String error = (String) request.getAttribute("error");
    String mensaje = request.getParameter("mensaje");

    List<Partido> preliminares = (List<Partido>) request.getAttribute("preliminares");
    List<Partido> cuartos = (List<Partido>) request.getAttribute("cuartos");
    List<Partido> semifinales = (List<Partido>) request.getAttribute("semifinales");
    List<Partido> finalTorneo = (List<Partido>) request.getAttribute("finalTorneo");

    if (preliminares == null) {
        preliminares = new ArrayList<>();
    }

    if (cuartos == null) {
        cuartos = new ArrayList<>();
    }

    if (semifinales == null) {
        semifinales = new ArrayList<>();
    }

    if (finalTorneo == null) {
        finalTorneo = new ArrayList<>();
    }

    if (torneoId == null && torneo != null) {
        torneoId = torneo.getTorneo_id();
    }

    if ((nombreTorneo == null || nombreTorneo.isBlank()) && torneo != null) {
        nombreTorneo = torneo.getNombre();
    }

    if (nombreTorneo == null || nombreTorneo.isBlank()) {
        nombreTorneo = "Torneo";
    }

    if ("resultadoGuardado".equals(mensaje)) {
        mensaje = "El resultado fue guardado correctamente.";
    }

    List<List<Partido>> rondas = Arrays.asList(preliminares, cuartos, semifinales, finalTorneo);

    String[] nombresRondas = {
        "Ronda preliminar",
        "Cuartos de final",
        "Semifinales",
        "Final"
    };
%>

<!DOCTYPE html>

<html lang="es">

<head>

    <meta charset="UTF-8">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Llaves del torneo</title>

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

        .contenedor-cuadro {
            overflow-x: auto;
            padding-bottom: 30px;
        }

        .cuadro-eliminatorio {
            display: grid;
            grid-template-columns: repeat(4, minmax(270px, 1fr));
            gap: 35px;
            min-width: 1250px;
        }

        .ronda {
            display: flex;
            flex-direction: column;
        }

        .titulo-ronda {
            background: #212529;
            color: white;
            border-radius: 8px;
            padding: 10px;
            text-align: center;
            margin-bottom: 20px;
        }

        .lista-partidos {
            flex: 1;
            display: flex;
            flex-direction: column;
            justify-content: space-around;
            gap: 18px;
        }

        .partido {
            background: white;
            border-left: 5px solid #212529;
            border-radius: 10px;
            padding: 14px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
        }

        .partido-final {
            border-left-color: #ffc107;
        }

        .equipo {
            display: flex;
            justify-content: space-between;
            align-items: center;
            background: #f8f9fa;
            border-radius: 6px;
            padding: 8px 10px;
            margin-bottom: 7px;
        }

        .marcador {
            background: #212529;
            color: white;
            border-radius: 5px;
            min-width: 30px;
            padding: 3px 7px;
            text-align: center;
            font-weight: bold;
        }

        .ganador {
            color: #198754;
            font-weight: bold;
        }

        .formulario-resultado {
            border-top: 1px solid #dee2e6;
            margin-top: 10px;
            padding-top: 10px;
        }

    </style>

</head>

<body>

<header class="encabezado">

    <div class="container-fluid px-4 d-flex justify-content-between align-items-center">

        <div>

            <h2 class="mb-1"><%= nombreTorneo %></h2>

            <p class="mb-0">Cuadro eliminatorio</p>

        </div>

        <div class="d-flex gap-2">

            <a href="<%= contexto %>/TorneoServlet?accion=listar" class="btn btn-outline-light">← Torneos</a>

            <a href="<%= paginaPrincipal %>" class="btn btn-light">Panel principal</a>

        </div>

    </div>

</header>

<main class="container-fluid px-4 pb-5">

    <% if (mensaje != null && !mensaje.isBlank()) { %>

        <div class="alert alert-success alert-dismissible fade show">

            <%= mensaje %>

            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>

        </div>

    <% } %>

    <% if (error != null && !error.isBlank()) { %>

        <div class="alert alert-danger alert-dismissible fade show">

            <%= error %>

            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>

        </div>

    <% } %>

    <div class="contenedor-cuadro">

        <div class="cuadro-eliminatorio">

            <% for (int i = 0; i < rondas.size(); i++) { %>

                <div class="ronda">

                    <h4 class="titulo-ronda">

                        <%= nombresRondas[i] %>

                    </h4>

                    <div class="lista-partidos">

                        <% if (rondas.get(i).isEmpty()) { %>

                            <div class="partido text-center text-muted">

                                No hay partidos en esta ronda.

                            </div>

                        <% } %>

                        <% for (Partido partido : rondas.get(i)) { %>

                            <div class="partido <%= partido.getRonda() == 4 ? "partido-final" : "" %>">

                                <div class="d-flex justify-content-between align-items-center mb-2">

                                    <strong>

                                        Partido <%= partido.getPosicion_llave() %>

                                    </strong>

                                    <% if ("FINALIZADO".equalsIgnoreCase(partido.getEstado())) { %>

                                        <span class="badge bg-success">FINALIZADO</span>

                                    <% } else { %>

                                        <span class="badge bg-warning text-dark">PENDIENTE</span>

                                    <% } %>

                                </div>

                                <div class="equipo">

                                    <span>

                                        <%= partido.getNombreLocal() == null ? "Por definir" : partido.getNombreLocal() %>

                                    </span>

                                    <span class="marcador">

                                        <%= partido.getMarcador_local() == null ? "-" : partido.getMarcador_local() %>

                                    </span>

                                </div>

                                <div class="equipo">

                                    <span>

                                        <%= partido.getNombreVisita() == null ? "Por definir" : partido.getNombreVisita() %>

                                    </span>

                                    <span class="marcador">

                                        <%= partido.getMarcador_visita() == null ? "-" : partido.getMarcador_visita() %>

                                    </span>

                                </div>

                                <% if ("FINALIZADO".equalsIgnoreCase(partido.getEstado())) { %>

                                    <div class="mt-2 text-center">

                                        <span class="ganador">

                                            Ganador: <%= partido.getNombreGanador() %>

                                        </span>

                                    </div>

                                <% } %>

                                <% if ("PENDIENTE".equalsIgnoreCase(partido.getEstado()) && (partido.getEquipo_local_id() == null || partido.getEquipo_visita_id() == null)) { %>

                                    <div class="text-muted text-center mt-2">

                                        Esperando ganadores anteriores

                                    </div>

                                <% } %>

                                <% if (esAdministrador && "PENDIENTE".equalsIgnoreCase(partido.getEstado()) && partido.getEquipo_local_id() != null && partido.getEquipo_visita_id() != null) { %>
                                    <form action="<%= contexto %>/PartidoServlet" method="post" class="formulario-resultado">
                                        <input type="hidden" name="accion" value="resultado">
                                        <input type="hidden" name="partido_id" value="<%= partido.getId_partidos() %>">
                                        <input type="hidden" name="torneo_id" value="<%= torneoId %>">
                                        <div class="row g-2">
                                            <div class="col-6">
                                                <label class="form-label small">Marcador local</label>
                                                <input type="number" name="marcador_local" class="form-control form-control-sm" min="0" required>
                                            </div>
                                            <div class="col-6">
                                                <label class="form-label small">Marcador visita</label>
                                                <input type="number" name="marcador_visita" class="form-control form-control-sm" min="0" required>
                                            </div>
                                        </div>
                                        <button type="submit" class="btn btn-dark btn-sm w-100 mt-2">Guardar resultado</button>
                                    </form>
                                <% } %>
                            </div>
                        <% } %>
                    </div>
                </div>
            <% } %>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>