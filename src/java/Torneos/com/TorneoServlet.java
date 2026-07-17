/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Torneos.com;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Types;
import Database.com.Database;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.sql.Connection;

/**
 *
 * @author USER
 */
public class TorneoServlet extends HttpServlet {

    

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
            
       
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
            String action = request.getParameter("action");
        
        if ("crear".equals(action)) {
            String nombre = request.getParameter("nombre");
            String imagenUrl = request.getParameter("imagenUrl");
            String fechaInicio = request.getParameter("fechaInicio");
            String fechaFin = request.getParameter("fechaFin");
            double premio = Double.parseDouble(request.getParameter("premio"));
            String[] equiposSeleccionados = request.getParameterValues("equipos");

            // Validar que sean exactamente 16 equipos
            if (equiposSeleccionados == null || equiposSeleccionados.length != 16) {
                request.setAttribute("error", "Debe seleccionar exactamente 16 equipos.");
                request.getRequestDispatcher("dashboard-admin.jsp").forward(request, response);
                return;
            }

            Database db = new Database();
            Connection conn = null;
            try {
                 conn = db.getConnection();
                 conn.setAutoCommit(false);

                // Verificar si ya existe un torneo activo
                PreparedStatement psCheck =
                conn.prepareStatement(
                "SELECT COUNT(*) FROM Torneo WHERE estado='ACTIVO'");
                ResultSet rsCheck = psCheck.executeQuery();
                if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                    throw new Exception("Ya existe un torneo activo en el sistema.");
                }

                // 1. Insertar el Torneo
                String sqlTorneo =
                "INSERT INTO Torneo (nombre, imagen, fechaInicio, fechaFinal, premio, estado) VALUES (?, ?, ?, ?, ?, 'ACTIVO')";
                PreparedStatement psTorneo = conn.prepareStatement(sqlTorneo, Statement.RETURN_GENERATED_KEYS);
                psTorneo.setString(1, nombre);
                psTorneo.setString(2, imagenUrl);
                psTorneo.setString(3, fechaInicio);
                psTorneo.setString(4, fechaFin);
                psTorneo.setDouble(5, premio);
                psTorneo.executeUpdate();
                System.out.println("Torneo creado correctamente.");
                ResultSet rsKey = psTorneo.getGeneratedKeys();
                int torneoId = 0;
                if (rsKey.next()) torneoId = rsKey.getInt(1);
                System.out.println("ID Torneo: " + torneoId);
                // 2. Mezclar los 16 equipos aleatoriamente
                List<Integer> listaEquipos = new ArrayList<>();
                for (String eqId : equiposSeleccionados) {
                    listaEquipos.add(Integer.parseInt(eqId));
                }
                Collections.shuffle(listaEquipos);

                // 3. Crear la estructura vacía de llaves de arriba hacia abajo (Final, Semis, Cuartos) 
                // para poder asignar las llaves destino (siguiente_partido_id).
                
                // Partido de la Gran Final (Ronda 4, Posición 1)
                int idFinal = crearPartidoEstructura(conn, torneoId, 4, 1, null, null);
                
                // Partidos de Semifinal (Ronda 3, Posiciones 1 y 2)
                int idSemi1 = crearPartidoEstructura(conn, torneoId, 3, 1, idFinal, "LOCAL");
                int idSemi2 = crearPartidoEstructura(conn, torneoId, 3, 2, idFinal, "VISITA");
                
                // Partidos de Cuartos de Final (Ronda 2, Posiciones 1 al 4)
                int idCuarto1 = crearPartidoEstructura(conn, torneoId, 2, 1, idSemi1, "LOCAL");
                int idCuarto2 = crearPartidoEstructura(conn, torneoId, 2, 2, idSemi1, "VISITA");
                int idCuarto3 = crearPartidoEstructura(conn, torneoId, 2, 3, idSemi2, "LOCAL");
                int idCuarto4 = crearPartidoEstructura(conn, torneoId, 2, 4, idSemi2, "VISITA");
                
                // Partidos de Ronda Preliminar (Ronda 1, Posiciones 1 al 8) y asignar los 16 equipos mezclados
                int[] destinosCuartos = {idCuarto1, idCuarto1, idCuarto2, idCuarto2, idCuarto3, idCuarto3, idCuarto4, idCuarto4};
                String[] lados = {"LOCAL", "VISITA", "LOCAL", "VISITA", "LOCAL", "VISITA", "LOCAL", "VISITA"};
                
                int equipoIndex = 0;
                for (; equipoIndex < 16; equipoIndex += 2) {
    int eqLocal = listaEquipos.get(equipoIndex);
    int eqVisita = listaEquipos.get(equipoIndex + 1);
    
    // Calcular la posición del partido (de la llave 1 a la 8)
    int posicionLlave = (equipoIndex / 2) + 1; 

    String sqlPreliminar =
    "INSERT INTO Partidos (torneo_id, ronda, posicion_llave, equipo_local_id, equipo_visita_id, siguiente_partido_id, posicion_siguiente_local_visita) VALUES (?,1,?,?,?,?,?)";
    PreparedStatement psPrelim = conn.prepareStatement(sqlPreliminar);
    psPrelim.setInt(1, torneoId);
    psPrelim.setInt(2, posicionLlave); // <--- Usamos el cálculo aquí
    psPrelim.setInt(3, eqLocal);
    psPrelim.setInt(4, eqVisita);
    psPrelim.setInt(5, destinosCuartos[posicionLlave - 1]); // <--- Ajuste de índice del array
    psPrelim.setString(6, lados[posicionLlave - 1]);       // <--- Ajuste de índice del array
    psPrelim.executeUpdate();
    System.out.println(
    "Partido creado: "
    + posicionLlave
    + " Local: "
    + eqLocal
    + " Visita: "
    + eqVisita
);
}

                conn.commit(); // Confirmar cambios si todo sale bien
                System.out.println("Todo se guardó correctamente.");
                response.sendRedirect("ver-llaves.jsp");
                
            } catch (Exception e) {
                if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
                request.setAttribute("error", "Error al estructurar el torneo: " + e.getMessage());
                request.getRequestDispatcher("dashboard-admin.jsp").forward(request, response);
            } finally {
    if (conn != null) {
        try {
            db.Close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
        }
    }

    private int crearPartidoEstructura(Connection conn, int torneoId, int ronda, int pos, Integer sigPartidoId, String lado) throws SQLException {
        String sql =
        "INSERT INTO Partidos (torneo_id,ronda,posicion_llave,siguiente_partido_id,posicion_siguiente_local_visita) VALUES (?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, torneoId);
        ps.setInt(2, ronda);
        ps.setInt(3, pos);
        if (sigPartidoId != null) ps.setInt(4, sigPartidoId); else ps.setNull(4, Types.INTEGER);
        if (lado != null) ps.setString(5, lado); else ps.setNull(5, Types.VARCHAR);
        ps.executeUpdate();
        
        ResultSet rs = ps.getGeneratedKeys();
        return rs.next() ? rs.getInt(1) : 0;
    }
        
    }

    


