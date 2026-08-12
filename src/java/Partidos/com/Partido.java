package Partidos.com;

public class Partido {

    private int id_partidos;
    private int torneo_id;
    private int ronda;
    private int posicion_llave;
    private Integer equipo_local_id;
    private Integer equipo_visita_id;
    private Integer marcador_local;
    private Integer marcador_visita;
    private String estado;
    private Integer ganador_id;
    private Integer siguiente_partido_id;
    private String posicion_siguiente_local_visita;
    private String nombreLocal;
    private String nombreVisita;
    private String nombreGanador;

    public Partido() {
    }

    public int getId_partidos() {
        return id_partidos;
    }

    public void setId_partidos(int id_partidos) {
        this.id_partidos = id_partidos;
    }

    public int getTorneo_id() {
        return torneo_id;
    }

    public void setTorneo_id(int torneo_id) {
        this.torneo_id = torneo_id;
    }

    public int getRonda() {
        return ronda;
    }

    public void setRonda(int ronda) {
        this.ronda = ronda;
    }

    public int getPosicion_llave() {
        return posicion_llave;
    }

    public void setPosicion_llave(int posicion_llave) {
        this.posicion_llave = posicion_llave;
    }

    public Integer getEquipo_local_id() {
        return equipo_local_id;
    }

    public void setEquipo_local_id(Integer equipo_local_id) {
        this.equipo_local_id = equipo_local_id;
    }

    public Integer getEquipo_visita_id() {
        return equipo_visita_id;
    }

    public void setEquipo_visita_id(Integer equipo_visita_id) {
        this.equipo_visita_id = equipo_visita_id;
    }

    public Integer getMarcador_local() {
        return marcador_local;
    }

    public void setMarcador_local(Integer marcador_local) {
        this.marcador_local = marcador_local;
    }

    public Integer getMarcador_visita() {
        return marcador_visita;
    }

    public void setMarcador_visita(Integer marcador_visita) {
        this.marcador_visita = marcador_visita;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getGanador_id() {
        return ganador_id;
    }

    public void setGanador_id(Integer ganador_id) {
        this.ganador_id = ganador_id;
    }

    public Integer getSiguiente_partido_id() {
        return siguiente_partido_id;
    }

    public void setSiguiente_partido_id(Integer siguiente_partido_id) {
        this.siguiente_partido_id = siguiente_partido_id;
    }

    public String getPosicion_siguiente_local_visita() {
        return posicion_siguiente_local_visita;
    }

    public void setPosicion_siguiente_local_visita(String posicion_siguiente_local_visita) {
        this.posicion_siguiente_local_visita = posicion_siguiente_local_visita;
    }

    public String getNombreLocal() {
        return nombreLocal;
    }

    public void setNombreLocal(String nombreLocal) {
        this.nombreLocal = nombreLocal;
    }

    public String getNombreVisita() {
        return nombreVisita;
    }

    public void setNombreVisita(String nombreVisita) {
        this.nombreVisita = nombreVisita;
    }

    public String getNombreGanador() {
        return nombreGanador;
    }

    public void setNombreGanador(String nombreGanador) {
        this.nombreGanador = nombreGanador;
    }
}