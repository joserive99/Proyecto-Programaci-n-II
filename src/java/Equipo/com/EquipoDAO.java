package Equipo.com;

import Database.com.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipoDAO {

    public List<Equipo> listarEquipos() {

        List<Equipo> lista = new ArrayList<>();

        String sql = "SELECT id_equipo, nombre, escudo, telefono, UsuarioID FROM Equipo ORDER BY nombre";

        Database db = new Database();

        try {

            Connection conn = db.getConnection();

            try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Equipo equipo = new Equipo();

                    equipo.setEquipoID(rs.getInt("id_equipo"));
                    equipo.setNombre(rs.getString("nombre"));
                    equipo.setEscudo(rs.getString("escudo"));
                    equipo.setTelefono(rs.getString("telefono"));
                    equipo.setUsuarioID(rs.getInt("UsuarioID"));

                    lista.add(equipo);
                }
            }

        } catch (SQLException ex) {

            ex.printStackTrace();

        } finally {

            db.Close();
        }

        return lista;
    }

    public boolean guardarEquipo(Equipo equipo) {

        String sql = "INSERT INTO Equipo(nombre, escudo, telefono, UsuarioID) VALUES (?, ?, ?, ?)";

        Database db = new Database();

        try {

            Connection conn = db.getConnection();

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, equipo.getNombre());
                ps.setString(2, equipo.getEscudo());
                ps.setString(3, equipo.getTelefono());
                ps.setInt(4, equipo.getUsuarioID());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException ex) {

            ex.printStackTrace();
            return false;

        } finally {

            db.Close();
        }
    }

    public Equipo buscarEquipo(int id) {

        Equipo equipo = null;

        String sql = "SELECT id_equipo, nombre, escudo, telefono, UsuarioID FROM Equipo WHERE id_equipo = ?";

        Database db = new Database();

        try {

            Connection conn = db.getConnection();

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        equipo = new Equipo();

                        equipo.setEquipoID(rs.getInt("id_equipo"));
                        equipo.setNombre(rs.getString("nombre"));
                        equipo.setEscudo(rs.getString("escudo"));
                        equipo.setTelefono(rs.getString("telefono"));
                        equipo.setUsuarioID(rs.getInt("UsuarioID"));
                    }
                }
            }

        } catch (SQLException ex) {

            ex.printStackTrace();

        } finally {

            db.Close();
        }

        return equipo;
    }

    public boolean actualizarEquipo(Equipo equipo) {

        String sql = "UPDATE Equipo SET nombre = ?, escudo = ?, telefono = ? WHERE id_equipo = ?";

        Database db = new Database();

        try {

            Connection conn = db.getConnection();

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, equipo.getNombre());
                ps.setString(2, equipo.getEscudo());
                ps.setString(3, equipo.getTelefono());
                ps.setInt(4, equipo.getEquipoID());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException ex) {

            ex.printStackTrace();
            return false;

        } finally {

            db.Close();
        }
    }

    public boolean eliminarEquipo(int id) {

        String sql = "DELETE FROM Equipo WHERE id_equipo = ?";

        Database db = new Database();

        try {

            Connection conn = db.getConnection();

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, id);

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException ex) {

            ex.printStackTrace();
            return false;

        } finally {

            db.Close();
        }
    }
}