<%-- 
    Document   : Login
    Created on : 28 jun 2026, 10:37:19 a. m.
    Author     : ddani
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
        <title>Tornedos</title>
    </head>
    <body class="bg-light">
        <div class="d-flex justify-content-center align-items-center min-vh-100">     
            <form action="LoginServlet" method="POST" class="shadow p-5 rounded bg-white" style="width: 100%; max-width: 500px; transform: translateY(-60px);">
          
            <h1 class="text-center mb-4">Bienvenido</h1>

            <div class="mb-3">
                <label for="correo" class="form-label">Correo</label>
                <input type="email" class="form-control" id="Correo" name="Correo" required>
            </div>
            <div class="mb-3">
                <label for="contrasena" class="form-label">Contraseña</label>
                <input type="password" class="form-control" id="Contrasena" name="Contrasena" required>
            </div>
            <button type="submit" class="btn btn-dark w-100">Ingresar</button>
           </form>
        </div>   
    </body>
</html>