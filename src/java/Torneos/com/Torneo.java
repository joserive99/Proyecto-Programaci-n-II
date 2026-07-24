/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Torneos.com;

import java.sql.Date;

public class Torneo {
    private int torneo_id;
    private String nombre;
    private String deporte;
    private String categoria;
    private String imagen;
    private Date fechaInicio;
    private Date fechaFinal;
    private double premio;
    private String estado;
    private Integer campeon_id;

    // Este campo es opcional y servirá para mostrar el nombre del campeón
    private String nombreCampeon;

    // Constructor vacío
    public Torneo() {
    }

    // Constructor con parámetros
    public Torneo(int torneo_id, String nombre, String deporte, String categoria,
                  String imagen, Date fechaInicio, Date fechaFinal,
                  double premio, String estado, Integer campeon_id) {

        this.torneo_id = torneo_id;
        this.nombre = nombre;
        this.deporte = deporte;
        this.categoria = categoria;
        this.imagen = imagen;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.premio = premio;
        this.estado = estado;
        this.campeon_id = campeon_id;
    }

    // Getters y Setters

    public int getTorneo_id() {
        return torneo_id;
    }

    public void setTorneo_id(int torneo_id) {
        this.torneo_id = torneo_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public double getPremio() {
        return premio;
    }

    public void setPremio(double premio) {
        this.premio = premio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getCampeon_id() {
        return campeon_id;
    }

    public void setCampeon_id(Integer campeon_id) {
        this.campeon_id = campeon_id;
    }

    public String getNombreCampeon() {
        return nombreCampeon;
    }

    public void setNombreCampeon(String nombreCampeon) {
        this.nombreCampeon = nombreCampeon;
    }
}
