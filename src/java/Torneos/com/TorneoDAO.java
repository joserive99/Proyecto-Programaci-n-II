package Torneos.com;

import Database.com.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class TorneoDAO {

    private final Database db = new Database();

    public int crearConLlaves(Torneo torneo, List<Integer> equipos) throws SQLException {
        
        validarEquipos(equipos);

        try (Connection con = db.getConnection()) {
            try {
                con.setAutoCommit(false);

                if (existeTorneoActivo(con)) {
                    throw new SQLException("Ya existe un torneo activo en el sistema.");
                }

                validarEquiposAprobados(con, equipos);
                torneo.setEstado("ACTIVO");
                torneo.setCampeon_id(null);

                int torneoId = insertarTorneo(con, torneo);
                guardarParticipantes(con, torneoId, equipos);

                List<Integer> equiposMezclados = new ArrayList<>(equipos);
                Collections.shuffle(equiposMezclados);
                crearEstructuraLlaves(con, torneoId, equiposMezclados);

                con.commit();
                return torneoId;
            } catch (Exception e) {
                con.rollback();
                if (e instanceof SQLException) {
                    throw (SQLException) e;
                }
                throw new SQLException("Error al crear el torneo: " + e.getMessage(), e);
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    private int insertarTorneo(Connection con, Torneo torneo) throws SQLException {
        
        String sql = "INSERT INTO Torneo (nombre, imagen, fechaInicio, fechaFinal, premio, estado, campeon_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, torneo.getNombre());
            ps.setString(2, torneo.getImagen());
            ps.setDate(3, torneo.getFechaInicio());
            ps.setDate(4, torneo.getFechaFinal());
            ps.setDouble(5, torneo.getPremio());
            ps.setString(6, "ACTIVO");
            ps.setNull(7, Types.INTEGER);
            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    return claves.getInt(1);
                }
                throw new SQLException("No se pudo obtener el ID del torneo.");
            }
        }
    }

    private void guardarParticipantes(Connection con, int torneoId, List<Integer> equipos) throws SQLException {
        
        String sql = "INSERT INTO TorneoEquipo (torneo_id, equipo_id) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (Integer equipoId : equipos) {
                ps.setInt(1, torneoId);
                ps.setInt(2, equipoId);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void crearEstructuraLlaves(Connection con, int torneoId, List<Integer> equipos) throws SQLException {
        
        int idFinal = crearPartidoEstructura(con, torneoId, 4, 1, null, null);
        int idSemi1 = crearPartidoEstructura(con, torneoId, 3, 1, idFinal, "LOCAL");
        int idSemi2 = crearPartidoEstructura(con, torneoId, 3, 2, idFinal, "VISITA");
        int idCuarto1 = crearPartidoEstructura(con, torneoId, 2, 1, idSemi1, "LOCAL");
        int idCuarto2 = crearPartidoEstructura(con, torneoId, 2, 2, idSemi1, "VISITA");
        int idCuarto3 = crearPartidoEstructura(con, torneoId, 2, 3, idSemi2, "LOCAL");
        int idCuarto4 = crearPartidoEstructura(con, torneoId, 2, 4, idSemi2, "VISITA");
        int[] destinosCuartos = {idCuarto1, idCuarto1, idCuarto2, idCuarto2, idCuarto3, idCuarto3, idCuarto4, idCuarto4};
        String[] posiciones = {"LOCAL", "VISITA", "LOCAL", "VISITA", "LOCAL", "VISITA", "LOCAL", "VISITA"};
        crearPartidosPrimeraRonda(con, torneoId, equipos, destinosCuartos, posiciones);
    }

    private void crearPartidosPrimeraRonda(Connection con, int torneoId, List<Integer> equipos, int[] destinos, String[] posiciones) throws SQLException {
        
        String sql = "INSERT INTO Partidos (torneo_id, ronda, posicion_llave, equipo_local_id, equipo_visita_id, siguiente_partido_id, posicion_siguiente_local_visita) VALUES (?, 1, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < 16; i += 2) {
                int posicionLlave = (i / 2) + 1;
                ps.setInt(1, torneoId);
                ps.setInt(2, posicionLlave);
                ps.setInt(3, equipos.get(i));
                ps.setInt(4, equipos.get(i + 1));
                ps.setInt(5, destinos[posicionLlave - 1]);
                ps.setString(6, posiciones[posicionLlave - 1]);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private int crearPartidoEstructura(Connection con, int torneoId, int ronda, int posicion, Integer siguientePartidoId, String lado) throws SQLException {
        
        String sql = "INSERT INTO Partidos (torneo_id, ronda, posicion_llave, siguiente_partido_id, posicion_siguiente_local_visita) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, torneoId);
            ps.setInt(2, ronda);
            ps.setInt(3, posicion);

            if (siguientePartidoId == null) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, siguientePartidoId);
            }

            if (lado == null) {
                ps.setNull(5, Types.VARCHAR);
            } else {
                ps.setString(5, lado);
            }

            ps.executeUpdate();

            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    return claves.getInt(1);
                }
                throw new SQLException("No se pudo obtener el ID del partido.");
            }
        }
    }

    private boolean existeTorneoActivo(Connection con) throws SQLException {
        
        String sql = "SELECT torneo_id FROM Torneo WHERE estado = 'ACTIVO' LIMIT 1";

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            return rs.next();
        }
    }

    private void validarEquipos(List<Integer> equipos) {
        
        if (equipos == null || equipos.size() != 16) {
            throw new IllegalArgumentException("Debe seleccionar exactamente 16 equipos.");
        }
        if (new HashSet<>(equipos).size() != 16) {
            throw new IllegalArgumentException("No se pueden seleccionar equipos repetidos.");
        }
    }

    private void validarEquiposAprobados(Connection con, List<Integer> equipos) throws SQLException {
        
        String sql = "SELECT COUNT(*) FROM Equipo WHERE estado = 'APROBADO' AND id_equipo IN (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < equipos.size(); i++) {
                ps.setInt(i + 1, equipos.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                if (rs.getInt(1) != 16) {
                    throw new SQLException("Todos los equipos seleccionados deben estar aprobados.");
                }
            }
        }
    }

    public List<String[]> listarEquiposAprobados() throws SQLException {
        
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT id_equipo, nombre FROM Equipo WHERE estado = 'APROBADO' ORDER BY nombre";

        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{rs.getString("id_equipo"), rs.getString("nombre")});
            }
        }
        return lista;
    }

    public List<Torneo> listar() throws SQLException {
        
        List<Torneo> lista = new ArrayList<>();
        String sql = "SELECT t.*, e.nombre AS nombre_campeon FROM Torneo t LEFT JOIN Equipo e ON t.campeon_id = e.id_equipo ORDER BY t.torneo_id DESC";

        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Torneo torneo = mapearTorneo(rs);
                torneo.setNombreCampeon(rs.getString("nombre_campeon"));
                lista.add(torneo);
            }
        }
        return lista;
    }

    public List<Torneo> buscar(String nombre) throws SQLException {
        
        List<Torneo> lista = new ArrayList<>();
        String sql = "SELECT t.*, e.nombre AS nombre_campeon FROM Torneo t LEFT JOIN Equipo e ON t.campeon_id = e.id_equipo WHERE t.nombre LIKE ? ORDER BY t.nombre";

        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Torneo torneo = mapearTorneo(rs);
                    torneo.setNombreCampeon(rs.getString("nombre_campeon"));
                    lista.add(torneo);
                }
            }
        }
        return lista;
    }

    private Torneo mapearTorneo(ResultSet rs) throws SQLException {
        
        Torneo torneo = new Torneo();
        torneo.setTorneo_id(rs.getInt("torneo_id"));
        torneo.setNombre(rs.getString("nombre"));
        torneo.setImagen(rs.getString("imagen"));
        torneo.setFechaInicio(rs.getDate("fechaInicio"));
        torneo.setFechaFinal(rs.getDate("fechaFinal"));
        torneo.setPremio(rs.getDouble("premio"));
        torneo.setEstado(rs.getString("estado"));

        int campeonId = rs.getInt("campeon_id");
        torneo.setCampeon_id(rs.wasNull() ? null : campeonId);
        return torneo;
    }
}