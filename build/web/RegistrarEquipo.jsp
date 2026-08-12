<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect(request.getContextPath() + "/Login.jsp");
        return;
    }

    String contexto = request.getContextPath();
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="es">

<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Registrar Equipo</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>

        body {
            background: #f5f6fa;
        }

        .formulario {
            max-width: 750px;
            margin: 50px auto;
        }

        .card {
            border: none;
            border-radius: 15px;
        }

        .card-header {
            background: #212529;
            color: white;
            padding: 20px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="formulario">
        <div class="card shadow">
            <div class="card-header">
                <h3 class="text-center mb-0">Registrar equipo</h3>
           </div>
          <div class="card-body p-4">

    <% if (error != null && !error.isBlank()) { %>

        <div class="alert alert-danger"><%= error %></div>

    <% } %>
    <form action="<%= request.getContextPath() %>/EquipoServlet" method="post">
        <input type="hidden" name="accion" value="guardar">
        <div class="mb-3">
            <label for="nombre" class="form-label">Nombre del equipo</label>
            <input type="text" id="nombre" name="nombre" class="form-control" maxlength="100" placeholder="Ejemplo: Deportivo Saprissa" required>
        </div>
        <div class="mb-3">
            <label for="escudo" class="form-label">URL del escudo</label>
            <input type="url" id="escudo" name="escudo" class="form-control" maxlength="500" placeholder="https://ejemplo.com/escudo.png" required>
            <div class="form-text">Ingrese el enlace directo de una imagen PNG, JPG o WEBP.</div>
        </div>
        <div class="mb-3">
            <label for="telefono" class="form-label">Teléfono</label>
           <input type="text" id="telefono" name="telefono" class="form-control" maxlength="30" placeholder="Ejemplo: 88888888" required>
        </div>
        <div class="d-flex justify-content-center gap-2 mt-4">
            <button type="submit" class="btn btn-success">Guardar equipo</button>
            <a href="<%= contexto %>/EquipoServlet?accion=listar" class="btn btn-secondary">Volver</a>
        </div>
     </form>
    </div>
</body>
</html>
