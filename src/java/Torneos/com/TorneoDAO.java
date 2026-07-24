/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Torneos.com;

import Database.com.Database;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class TorneoDAO {

    Database db = new Database();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    //==========================
    // AGREGAR TORNEO
    //==========================
    public boolean agregar(Torneo t) {

        String sql = "INSERT INTO Torneo(nombre,deporte,categoria,imagen,fechaInicio,fechaFinal,premio,estado,campeon_id) VALUES(?,?,?,?,?,?,?,?,?)";

        try {

            con = db.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNombre());
            ps.setString(2, t.getDeporte());
            ps.setString(3, t.getCategoria());
            ps.setString(4, t.getImagen());
            ps.setDate(5, t.getFechaInicio());
            ps.setDate(6, t.getFechaFinal());
            ps.setDouble(7, t.getPremio());
            ps.setString(8, t.getEstado());

            if (t.getCampeon_id() == null) {
                ps.setNull(9, Types.INTEGER);
            } else {
                ps.setInt(9, t.getCampeon_id());
            }

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

    e.printStackTrace();

} finally {

    db.Close();

}

return false;

    }

    //==========================
    // LISTAR TORNEOS
    //==========================
    public List<Torneo> listar() {

        List<Torneo> lista = new ArrayList<>();

        String sql = "SELECT t.*, e.nombre AS campeon "
                + "FROM Torneo t "
                + "LEFT JOIN Equipo e "
                + "ON t.campeon_id=e.id_equipo "
                + "ORDER BY t.torneo_id";

        try {

            con = db.getConnection();

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {

                Torneo t = new Torneo();

                t.setTorneo_id(rs.getInt("torneo_id"));
                t.setNombre(rs.getString("nombre"));
                t.setDeporte(rs.getString("deporte"));
                t.setCategoria(rs.getString("categoria"));
                t.setImagen(rs.getString("imagen"));
                t.setFechaInicio(rs.getDate("fechaInicio"));
                t.setFechaFinal(rs.getDate("fechaFinal"));
                t.setPremio(rs.getDouble("premio"));
                t.setEstado(rs.getString("estado"));

                int idCampeon = rs.getInt("campeon_id");

                if (rs.wasNull()) {
                    t.setCampeon_id(null);
                } else {
                    t.setCampeon_id(idCampeon);
                }

                t.setNombreCampeon(rs.getString("campeon"));

                lista.add(t);

            }

        } catch (Exception e) {

    e.printStackTrace();

} finally {

    db.Close();

}

return lista;

    }

    //==========================
    // BUSCAR POR ID
    //==========================
    public Torneo buscarPorId(int id) {

        String sql = "SELECT * FROM Torneo WHERE torneo_id=?";

        Torneo t = new Torneo();

        try {

            con = db.getConnection();

            ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {

                t.setTorneo_id(rs.getInt("torneo_id"));
                t.setNombre(rs.getString("nombre"));
                t.setDeporte(rs.getString("deporte"));
                t.setCategoria(rs.getString("categoria"));
                t.setImagen(rs.getString("imagen"));
                t.setFechaInicio(rs.getDate("fechaInicio"));
                t.setFechaFinal(rs.getDate("fechaFinal"));
                t.setPremio(rs.getDouble("premio"));
                t.setEstado(rs.getString("estado"));

                int campeon = rs.getInt("campeon_id");

                if (rs.wasNull()) {
                    t.setCampeon_id(null);
                } else {
                    t.setCampeon_id(campeon);
                }

            }

        } catch (Exception e) {

    e.printStackTrace();

} finally {

    db.Close();

}

return t;
    }
    public boolean actualizar(Torneo t) {

        String sql = "UPDATE Torneo SET "
                + "nombre=?, "
                + "deporte=?, "
                + "categoria=?, "
                + "imagen=?, "
                + "fechaInicio=?, "
                + "fechaFinal=?, "
                + "premio=?, "
                + "estado=?, "
                + "campeon_id=? "
                + "WHERE torneo_id=?";

        try {

            con = db.getConnection();

            ps = con.prepareStatement(sql);

            ps.setString(1, t.getNombre());
            ps.setString(2, t.getDeporte());
            ps.setString(3, t.getCategoria());
            ps.setString(4, t.getImagen());
            ps.setDate(5, t.getFechaInicio());
            ps.setDate(6, t.getFechaFinal());
            ps.setDouble(7, t.getPremio());
            ps.setString(8, t.getEstado());

            if (t.getCampeon_id() == null) {
                ps.setNull(9, Types.INTEGER);
            } else {
                ps.setInt(9, t.getCampeon_id());
            }

            ps.setInt(10, t.getTorneo_id());

            return ps.executeUpdate() > 0;

       } catch (Exception e) {

    e.printStackTrace();

} finally {

    db.Close();

}

return false;

    }

    //==========================
    // ELIMINAR TORNEO
    //==========================
    public boolean eliminar(int id) {

        String sql = "DELETE FROM Torneo WHERE torneo_id=?";

        try {

            con = db.getConnection();

            ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

    e.printStackTrace();

} finally {

    db.Close();

}

return false;
    }

    //==========================
    // LISTAR EQUIPOS
    //==========================
    public List<String[]> listarEquipos() {

        List<String[]> lista = new ArrayList<>();

        String sql = "SELECT id_equipo, nombre FROM Equipo ORDER BY nombre";

        try {

            con = db.getConnection();

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {

                String[] equipo = new String[2];

                equipo[0] = rs.getString("id_equipo");
                equipo[1] = rs.getString("nombre");

                lista.add(equipo);

            }

        } catch (Exception e) {

    e.printStackTrace();

} finally {

    db.Close();

}

return lista;
    }
public List<Torneo> buscar(String nombre) {

    List<Torneo> lista = new ArrayList<>();

    String sql = "SELECT * FROM Torneo WHERE nombre LIKE ?";

    try {

        con = db.getConnection();

        ps = con.prepareStatement(sql);

        ps.setString(1, "%" + nombre + "%");

        rs = ps.executeQuery();

        while (rs.next()) {

            Torneo t = new Torneo();

            t.setTorneo_id(rs.getInt("torneo_id"));
            t.setNombre(rs.getString("nombre"));
            t.setDeporte(rs.getString("deporte"));
            t.setCategoria(rs.getString("categoria"));
            t.setImagen(rs.getString("imagen"));
            t.setFechaInicio(rs.getDate("fechaInicio"));
            t.setFechaFinal(rs.getDate("fechaFinal"));
            t.setPremio(rs.getDouble("premio"));
            t.setEstado(rs.getString("estado"));

            lista.add(t);
        }

    } catch (Exception e) {

        e.printStackTrace();

    } finally {

        db.Close();

    }

    return lista;
}
public int contar() {

    int total = 0;

    String sql = "SELECT COUNT(*) FROM Torneo";

    try {

        con = db.getConnection();

        ps = con.prepareStatement(sql);

        rs = ps.executeQuery();

        if (rs.next()) {

            total = rs.getInt(1);

        }

    } catch (Exception e) {

        e.printStackTrace();

    } finally {

        db.Close();

    }

    return total;
}
}