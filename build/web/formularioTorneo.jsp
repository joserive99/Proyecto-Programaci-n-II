<%-- 
    Document   : formularioTorneo
    Created on : 24 jul 2026, 10:04:41 a. m.
    Author     : Usuario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Torneos.com.Torneo"%>
<%@page import="java.util.List"%>

<%
    Torneo torneo = (Torneo) request.getAttribute("torneo");

    boolean editar = torneo != null;

    List<String[]> equipos =
            (List<String[]>) request.getAttribute("equipos");
%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>

<%= editar ? "Editar Torneo" : "Nuevo Torneo" %>

</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet">

</head>

<body class="bg-light">

<div class="container mt-5">

<div class="card shadow">

<div class="card-header bg-primary text-white">

<h3 class="text-center">

<%= editar ? "Editar Torneo" : "Registrar Torneo" %>

</h3>

</div>

<div class="card-body">

<form action="TorneoCRUDServlet" method="post">

<input type="hidden"
       name="accion"
       value="<%= editar ? "actualizar" : "guardar" %>">

<% if(editar){ %>

<input type="hidden"
       name="torneo_id"
       value="<%= torneo.getTorneo_id() %>">

<% } %>
<div class="row">

    <div class="col-md-6 mb-3">

        <label class="form-label">Nombre</label>

        <input type="text"
               name="nombre"
               class="form-control"
               required
               value="<%= editar ? torneo.getNombre() : "" %>">

    </div>

    <div class="col-md-6 mb-3">

        <label class="form-label">Deporte</label>

        <input type="text"
               name="deporte"
               class="form-control"
               required
               value="<%= editar ? torneo.getDeporte() : "" %>">

    </div>

</div>

<div class="row">

    <div class="col-md-6 mb-3">

        <label class="form-label">Categoría</label>

        <input type="text"
               name="categoria"
               class="form-control"
               required
               value="<%= editar ? torneo.getCategoria() : "" %>">

    </div>

    <div class="col-md-6 mb-3">

        <label class="form-label">Imagen (URL)</label>

        <input type="text"
               name="imagen"
               class="form-control"
               value="<%= editar ? torneo.getImagen() : "" %>">

    </div>

</div>

<div class="row">

    <div class="col-md-6 mb-3">

        <label class="form-label">Fecha Inicio</label>

        <input type="date"
               name="fechaInicio"
               class="form-control"
               required
               value="<%= editar ? torneo.getFechaInicio() : "" %>">

    </div>

    <div class="col-md-6 mb-3">

        <label class="form-label">Fecha Final</label>

        <input type="date"
               name="fechaFinal"
               class="form-control"
               required
               value="<%= editar ? torneo.getFechaFinal() : "" %>">

    </div>

</div>

<div class="row">

    <div class="col-md-6 mb-3">

        <label class="form-label">Premio</label>

        <input type="number"
               step="0.01"
               min="0"
               name="premio"
               class="form-control"
               required
               value="<%= editar ? torneo.getPremio() : "" %>">

    </div>

    <div class="col-md-6 mb-3">

        <label class="form-label">Estado</label>

        <select name="estado" class="form-select">

            <option value="ACTIVO"
                <%= editar && "ACTIVO".equals(torneo.getEstado()) ? "selected" : "" %>>
                ACTIVO
            </option>

            <option value="PENDIENTE"
                <%= editar && "PENDIENTE".equals(torneo.getEstado()) ? "selected" : "" %>>
                PENDIENTE
            </option>

            <option value="FINALIZADO"
                <%= editar && "FINALIZADO".equals(torneo.getEstado()) ? "selected" : "" %>>
                FINALIZADO
            </option>

        </select>

    </div>

</div>

<div class="mb-3">

    <label class="form-label">Campeón</label>

    <select class="form-select" name="campeon_id">

        <option value="">Seleccione un equipo</option>

        <%
            if(equipos!=null){

                for(String[] e : equipos){

                    String selected="";

                    if(editar &&
                       torneo.getCampeon_id()!=null &&
                       torneo.getCampeon_id()==Integer.parseInt(e[0])){

                        selected="selected";

                    }
        %>

        <option value="<%=e[0]%>" <%=selected%>>

            <%=e[1]%>

        </option>

        <%

                }

            }

        %>

    </select>

</div>

<div class="text-center mt-4">

    <button type="submit"
            class="btn btn-success">

        <%= editar ? "Actualizar" : "Guardar" %>

    </button>

    <a href="TorneoCRUDServlet?accion=listar"
       class="btn btn-secondary">

        Volver

    </a>

</div>

</form>

</div>

</div>

</div>

</body>

</html>
