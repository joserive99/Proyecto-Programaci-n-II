package Partidos.com;

import Database.com.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PartidoDAO {

    private final Database db = new Database();
    
    public List<Partido> listarPorTorneo(int torneoId) throws SQLException {

        List<Partido> lista = new ArrayList<>();

        String sql = "SELECT p.*,el.nombre AS nombre_local,ev.nombre AS nombre_visita,g.nombre AS nombre_ganador FROM Partidos p LEFT JOIN Equipo el ON p.equipo_local_id = el.id_equipo LEFT JOIN Equipo ev ON p.equipo_visita_id = ev.id_equipo LEFT JOIN Equipo g ON p.ganador_id = g.id_equipo WHERE p.torneo_id = ? ORDER BY p.ronda,p.posicion_llave";

        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, torneoId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(mapearPartido(rs));
                }
            }
        }

        return lista;
    }

    //listar partidos
    public List<Partido> listarPorRonda(int torneoId, int ronda) throws SQLException {

        List<Partido> lista = new ArrayList<>();

        String sql = "SELECT p.*,el.nombre AS nombre_local,ev.nombre AS nombre_visita,g.nombre AS nombre_ganador FROM Partidos p LEFT JOIN Equipo el ON p.equipo_local_id = el.id_equipo LEFT JOIN Equipo ev ON p.equipo_visita_id = ev.id_equipo LEFT JOIN Equipo g ON p.ganador_id = g.id_equipo WHERE p.torneo_id = ? AND p.ronda = ? ORDER BY p.posicion_llave";

        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, torneoId);
            ps.setInt(2, ronda);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(mapearPartido(rs));
                }
            }
        }

        return lista;
    }

    //obtenemos el nombre del torneo
    public String obtenerNombreTorneo(int torneoId) throws SQLException {

        String sql = "SELECT nombre FROM Torneo WHERE torneo_id = ?";

        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, torneoId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getString("nombre");
                }
            }
        }

        return null;
    }

   //registra resultado
    public void registrarResultado(int partidoId, int marcadorLocal, int marcadorVisita) throws SQLException {

        if (marcadorLocal < 0 || marcadorVisita < 0) {
            throw new IllegalArgumentException("Los marcadores no pueden ser negativos.");
        }

        if (marcadorLocal == marcadorVisita) {
            throw new IllegalArgumentException("No se permiten empates.");
        }

        try (Connection con = db.getConnection()) {

            try {
                con.setAutoCommit(false);

                String sqlPartido = "SELECT torneo_id,equipo_local_id,equipo_visita_id,estado,siguiente_partido_id,posicion_siguiente_local_visita FROM Partidos WHERE id_partidos = ? FOR UPDATE";

                int torneoId;
                int equipoLocalId;
                int equipoVisitaId;
                String estado;
                Integer siguientePartidoId;
                String posicionSiguiente;

                try (PreparedStatement ps = con.prepareStatement(sqlPartido)) {

                    ps.setInt(1, partidoId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException("El partido solicitado no existe.");
                        }

                        torneoId = rs.getInt("torneo_id");

                        equipoLocalId = rs.getInt("equipo_local_id");

                        if (rs.wasNull()) {
                            throw new SQLException("El equipo local todavía no está definido.");
                        }

                        equipoVisitaId = rs.getInt("equipo_visita_id");

                        if (rs.wasNull()) {
                            throw new SQLException("El equipo visitante todavía no está definido.");
                        }

                        estado = rs.getString("estado");

                        int siguienteId = rs.getInt("siguiente_partido_id");

                        if (rs.wasNull()) {
                            siguientePartidoId = null;
                        } else {
                            siguientePartidoId = siguienteId;
                        }

                        posicionSiguiente = rs.getString("posicion_siguiente_local_visita");
                    }
                }

                if ("FINALIZADO".equalsIgnoreCase(estado)) {
                    throw new SQLException("Este partido ya fue finalizado.");
                }

                int ganadorId;

                if (marcadorLocal > marcadorVisita) {
                    ganadorId = equipoLocalId;
                } else {
                    ganadorId = equipoVisitaId;
                }

                guardarMarcador(con, partidoId, marcadorLocal, marcadorVisita, ganadorId);

                if (siguientePartidoId != null) {
                    avanzarGanador(con, siguientePartidoId, posicionSiguiente, ganadorId);
                } else {
                    finalizarTorneo(con, torneoId, ganadorId);
                }

                con.commit();

            } catch (Exception e) {

                con.rollback();

                if (e instanceof SQLException) {
                    throw (SQLException) e;
                }

                if (e instanceof IllegalArgumentException) {
                    throw (IllegalArgumentException) e;
                }

                throw new SQLException("Error al registrar el resultado: " + e.getMessage(), e);

            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    //guarda ganador y marcador
    private void guardarMarcador(Connection con, int partidoId, int marcadorLocal, int marcadorVisita, int ganadorId) throws SQLException {

        String sql = "UPDATE Partidos SET marcador_local = ?,marcador_visita = ?,estado = 'FINALIZADO',ganador_id = ? WHERE id_partidos = ? AND estado = 'PENDIENTE'";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, marcadorLocal);
            ps.setInt(2, marcadorVisita);
            ps.setInt(3, ganadorId);
            ps.setInt(4, partidoId);

            if (ps.executeUpdate() != 1) {
                throw new SQLException("No fue posible guardar el resultado del partido.");
            }
        }
    }

    // metodo avanza el ganador del partido
    private void avanzarGanador(Connection con, int siguientePartidoId, String posicionSiguiente, int ganadorId) throws SQLException {

        String columna;

        if ("LOCAL".equalsIgnoreCase(posicionSiguiente)) {
            columna = "equipo_local_id";
        } else if ("VISITA".equalsIgnoreCase(posicionSiguiente)) {
            columna = "equipo_visita_id";
        } else {
            throw new SQLException("La posición del siguiente partido no es válida.");
        }

        String sql = "UPDATE Partidos SET " + columna + " = ? WHERE id_partidos = ? AND estado = 'PENDIENTE' AND " + columna + " IS NULL";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ganadorId);
            ps.setInt(2, siguientePartidoId);

            if (ps.executeUpdate() != 1) {
                throw new SQLException("No fue posible avanzar al ganador al siguiente partido.");
            }
        }
    }

   //finaliza tornedo y da ganador
    private void finalizarTorneo(Connection con, int torneoId, int ganadorId) throws SQLException {

        String sql = "UPDATE Torneo SET estado = 'FINALIZADO',campeon_id = ? WHERE torneo_id = ? AND estado = 'ACTIVO'";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ganadorId);
            ps.setInt(2, torneoId);

            if (ps.executeUpdate() != 1) {
                throw new SQLException("No fue posible finalizar el torneo.");
            }
        }
    }

    
    private Partido mapearPartido(ResultSet rs) throws SQLException {

        Partido partido = new Partido();

        partido.setId_partidos(rs.getInt("id_partidos"));
        partido.setTorneo_id(rs.getInt("torneo_id"));
        partido.setRonda(rs.getInt("ronda"));
        partido.setPosicion_llave(rs.getInt("posicion_llave"));

        partido.setEquipo_local_id(obtenerInteger(rs, "equipo_local_id"));
        partido.setEquipo_visita_id(obtenerInteger(rs, "equipo_visita_id"));
        partido.setMarcador_local(obtenerInteger(rs, "marcador_local"));
        partido.setMarcador_visita(obtenerInteger(rs, "marcador_visita"));
        partido.setGanador_id(obtenerInteger(rs, "ganador_id"));
        partido.setSiguiente_partido_id(obtenerInteger(rs, "siguiente_partido_id"));

        partido.setEstado(rs.getString("estado"));
        partido.setPosicion_siguiente_local_visita(rs.getString("posicion_siguiente_local_visita"));
        partido.setNombreLocal(rs.getString("nombre_local"));
        partido.setNombreVisita(rs.getString("nombre_visita"));
        partido.setNombreGanador(rs.getString("nombre_ganador"));

        return partido;
    }

  
    private Integer obtenerInteger(ResultSet rs, String columna) throws SQLException {

        int valor = rs.getInt(columna);

        if (rs.wasNull()) {
            return null;
        }

        return valor;
    }
    public List<String[]> listarClasificados(int torneoId) {

        List<String[]> clasificados = new ArrayList<>();

        String sql = "SELECT e.nombre, CASE p.ronda WHEN 1 THEN 'CUARTOS DE FINAL' WHEN 2 THEN 'SEMIFINAL' WHEN 3 THEN 'FINAL' WHEN 4 THEN 'CAMPEÓN' END AS clasificado_a FROM Partidos p INNER JOIN Equipo e ON p.ganador_id = e.id_equipo WHERE p.torneo_id = ? AND p.estado = 'FINALIZADO' ORDER BY p.ronda, p.posicion_llave";

        Database db = new Database();

        try {

            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, torneoId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String[] clasificado = new String[2];

                clasificado[0] = rs.getString("nombre");
                clasificado[1] = rs.getString("clasificado_a");

                clasificados.add(clasificado);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            db.Close();
        }

        return clasificados;
    }
}