<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>

<%
    List<String[]> equipos = (List<String[]>) request.getAttribute("equipos");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear torneo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background: #f5f6fa; }
        .card { border: none; border-radius: 15px; }
        .equipo-item { border: 1px solid #dee2e6; border-radius: 10px; padding: 12px; transition: .2s; }
        .equipo-item:hover { background: #f1f3f5; }
    </style>
</head>
<body>

<div class="container py-5">
    <div class="card shadow">
        <div class="card-header bg-dark text-white py-3">
            <h3 class="text-center mb-0">Crear torneo</h3>
        </div>

        <div class="card-body p-4">
            <% if (error != null && !error.isBlank()) { %>
                <div class="alert alert-danger"><%= error %></div>
            <% } %>

            <form action="<%= request.getContextPath() %>/TorneoServlet" method="post">
                <input type="hidden" name="accion" value="crear">

                <div class="mb-3">
                    <label for="nombre" class="form-label">Nombre del torneo</label>
                    <input type="text" id="nombre" name="nombre" class="form-control" maxlength="100" required>
                </div>

                <div class="mb-3">
                    <label for="imagen" class="form-label">Imagen</label>
                    <input type="text" id="imagen" name="imagen" class="form-control" maxlength="255" placeholder="URL o nombre de la imagen" required>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="fechaInicio" class="form-label">Fecha de inicio</label>
                        <input type="date" id="fechaInicio" name="fechaInicio" class="form-control" required>
                    </div>

                    <div class="col-md-6 mb-3">
                        <label for="fechaFinal" class="form-label">Fecha final</label>
                        <input type="date" id="fechaFinal" name="fechaFinal" class="form-control" required>
                    </div>
                </div>

                <div class="mb-4">
                    <label for="premio" class="form-label">Premio en colones</label>
                    <input type="number" id="premio" name="premio" class="form-control" min="0" step="0.01" required>
                </div>

                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="mb-0">Seleccione los 16 equipos aprobados</h5>
                    <span class="badge bg-dark fs-6">Seleccionados: <span id="cantidad">0</span>/16</span>
                </div>

                <div class="row g-3">
                    <% if (equipos != null) { %>
                        <% for (String[] equipo : equipos) { %>
                            <div class="col-md-4">
                                <div class="equipo-item">
                                    <div class="form-check">
                                        <input type="checkbox" class="form-check-input equipo" id="equipo<%= equipo[0] %>" name="equipos" value="<%= equipo[0] %>">
                                        <label class="form-check-label" for="equipo<%= equipo[0] %>"><%= equipo[1] %></label>
                                    </div>
                                </div>
                            </div>
                        <% } %>
                    <% } %>
                </div>

                <div class="text-center mt-4">
                    <button type="submit" id="crear" class="btn btn-dark" disabled>Crear torneo</button>
                    <a href="<%= request.getContextPath() %>/TorneoServlet?accion=listar" class="btn btn-secondary">Volver</a>
                </div>
            </form>
        </div>
    </div>
</div>

    <script>
        const equipos = document.querySelectorAll(".equipo");
        const cantidad = document.getElementById("cantidad");
        const botonCrear = document.getElementById("crear");

        equipos.forEach(function(equipo) {
            equipo.addEventListener("change", function() {
                let seleccionados = document.querySelectorAll(".equipo:checked").length;

                if (seleccionados > 16) {
                    this.checked = false;
                    seleccionados = 16;
                    alert("Solamente puede seleccionar 16 equipos.");
                }

                cantidad.textContent = seleccionados;
                botonCrear.disabled = seleccionados !== 16;
            });
        });

        document.getElementById("fechaInicio").addEventListener("change", function() {
            document.getElementById("fechaFinal").min = this.value;
        });
    </script>

</body>
</html>
