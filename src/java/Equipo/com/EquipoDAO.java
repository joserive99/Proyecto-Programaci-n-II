/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipo.com;

import Database.com.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipoDAO {

    public List<Equipo> listarEquipos() {

        List<Equipo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Equipo";
        try {
            Database db = new Database();
            Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM Equipo ORDER BY nombre");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Equipo equipo = new Equipo();
               equipo.setEquipoID(rs.getInt("id_equipo"));
                equipo.setNombre(rs.getString("nombre"));
                equipo.setEscudo(rs.getString("escudo"));
                equipo.setTelefono(rs.getString("telefono"));
                equipo.setUsuarioID(rs.getInt("UsuarioID"));

                lista.add(equipo);

            }

            rs.close();
            ps.close();
            db.Close();

        } catch (SQLException ex) {

            ex.printStackTrace();
        }
        return lista;
    }

    public boolean guardarEquipo(Equipo equipo) {

        try {
            Database db = new Database();

            PreparedStatement ps = db.getConnection().prepareStatement(
                    "INSERT INTO Equipo(nombre,escudo,telefono,UsuarioID) VALUES(?,?,?,?)");

            ps.setString(1, equipo.getNombre());
            ps.setString(2, equipo.getEscudo());
            ps.setString(3, equipo.getTelefono());
            ps.setInt(4, equipo.getUsuarioID());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {

            ex.printStackTrace();

        }

        return false;

    }

    public Equipo buscarEquipo(int id) {

        Equipo equipo = null;
        try {
            Database db = new Database();

            PreparedStatement ps = db.getConnection().prepareStatement(
                    "SELECT * FROM Equipo WHERE id_equipo=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                equipo = new Equipo();
                equipo.setEquipoID(rs.getInt("id_equipo"));
                equipo.setNombre(rs.getString("nombre"));
                equipo.setEscudo(rs.getString("escudo"));
                equipo.setTelefono(rs.getString("telefono"));
                equipo.setUsuarioID(rs.getInt("UsuarioID"));

            }

            rs.close();
            ps.close();
            db.Close();

        } catch (SQLException ex) {

            ex.printStackTrace();

        }

        return equipo;

    }

    public boolean actualizarEquipo(Equipo equipo) {
        try {
            Database db = new Database();

            PreparedStatement ps = db.getConnection().prepareStatement(
                    "UPDATE Equipo SET nombre=?,escudo=?,telefono=? WHERE id_equipo=?");

            ps.setString(1, equipo.getNombre());
            ps.setString(2, equipo.getEscudo());
            ps.setString(3, equipo.getTelefono());
            ps.setInt(4, equipo.getEquipoID());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {

            ex.printStackTrace();
        }
        return false;

    }
    public boolean eliminarEquipo(int id) {

        try {
            Database db = new Database();
            PreparedStatement ps = db.getConnection().prepareStatement(
                    "DELETE FROM Equipo WHERE id_equipo=?");
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;

    }

}