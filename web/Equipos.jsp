<%-- 
    Document   : Equipos
    Created on : 15 jul 2026, 3:04:11 p. m.
    Author     : USER
--%>

<%@page import="Database.com.Database"%>
<%@page import="java.sql.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Equipos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">

<div class="container mt-5">

    <h2 class="mb-4">Equipos Registrados</h2>

    <table class="table table-bordered table-striped align-middle">
        <thead>
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Escudo</th>
                <th>Teléfono</th>
            </tr>
        </thead>
        <tbody>
        <%
        Database db = new Database();
        Connection conn = db.getConnection();
        
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Equipo");
        ResultSet rs = ps.executeQuery();
        int contador = 1;
        
        while(rs.next()){
        %>
            <tr>
                <td><%=contador%></td>
                <td><%=rs.getString("nombre")%></td>
                <td>
                   <img src="imagenes/<%=rs.getString("escudo")%>" width="60" alt="Escudo">
                </td>
                <td><%=rs.getString("telefono")%></td>
            </tr>
        <%
            contador++;
        }

        rs.close();
        ps.close();
        db.Close();
        %>
        </tbody>
    </table>

</div>

</body>
</html>