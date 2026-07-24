<%-- 
    Document   : torneos.jsp
    Created on : 24 jul 2026, 9:59:05 a. m.
    Author     : Usuario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Torneos.com.Torneo" %>
<%@ page import="java.util.*, Torneos.com.*" %>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>Gestión de Torneos</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

</head>

<body class="bg-light">

<div class="container mt-5">

    <div class="card shadow">

        <div class="card-header bg-primary text-white">

            <h2 class="text-center">Gestión de Torneos</h2>

        </div>

        <div class="d-flex align-items-center gap-2 mb-3">
    <button type="button" class="btn btn-secondary py-2 px-3 d-flex align-items-center" onclick="window.history.back()">
        ⬅ Regresar
    </button>
    
    <a href="formularioTorneo.jsp" class="btn btn-success py-2 px-3 d-flex align-items-center">
        Nuevo Torneo
    </a>

            <table class="table table-bordered table-hover text-center">

                <thead class="table-dark">

                <tr>

                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Deporte</th>
                    <th>Categoría</th>
                    <th>Fecha Inicio</th>
                    <th>Fecha Final</th>
                    <th>Premio</th>
                    <th>Estado</th>
                    <th>Campeón</th>
                    <th>Acciones</th>

                </tr>

                </thead>

                <tbody>

                <%

                    List<Torneo> lista =
                    (List<Torneo>)request.getAttribute("torneos");

                    if(lista!=null){

                        for(Torneo t:lista){

                %>

                <tr>

                    <td><%=t.getTorneo_id()%></td>

                    <td><%=t.getNombre()%></td>

                    <td><%=t.getDeporte()%></td>

                    <td><%=t.getCategoria()%></td>

                    <td><%=t.getFechaInicio()%></td>

                    <td><%=t.getFechaFinal()%></td>

                    <td>₡ <%=t.getPremio()%></td>

                    <td><%=t.getEstado()%></td>

                    <td>

                        <%=t.getNombreCampeon()==null ?
                        "Sin campeón" :
                        t.getNombreCampeon()%>

                    </td>

                    <td>

                        <a href="TorneoCRUDServlet?accion=editar&id=<%=t.getTorneo_id()%>"
                           class="btn btn-warning btn-sm">

                            Editar

                        </a>

                        <a href="TorneoCRUDServlet?accion=eliminar&id=<%=t.getTorneo_id()%>"
                           class="btn btn-danger btn-sm"
                           onclick="return confirm('¿Desea eliminar este torneo?')">

                            Eliminar

                        </a>

                    </td>

                </tr>

                <%

                        }

                    }

                %>

                </tbody>

            </table>

        </div>

    </div>

</div>

</body>
</html>
