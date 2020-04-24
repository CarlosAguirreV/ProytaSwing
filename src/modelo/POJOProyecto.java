package modelo;

public class POJOProyecto {

    // Lo que significa que un id tenga un -1
    public final static int SIN_CREAR = -1;

    // Los estados
    public final static String[] CADENAS_ESTADO = {"En proceso", "Pausado", "Terminado"};
    public final static String[] CADENAS_PRIORIDAD = {"Baja", "Media", "Alta"};

    // Para los valores de estado y prioridad
    private static final int VALOR_MAX = 2;
    private static final int VALOR_MIN = 0;

    // Para poner limites a las cadenas String, ya que SQLite no las pone
    private static final int LIMITE_TITULO = 20;
    private static final int LIMITE_FECHA = 15;
    private static final int LIMITE_DESCRIPCION = 300;
    private static final int LIMITE_REQUISITOS = 150;
    private static final int LIMITE_DESTINO = 150;
    private static final int LIMITE_PROBLEMAS = 200;
    private static final int LIMITE_MEJORAS = 200;

    // Texto por defecto en caso de no haber nada, en sustitucion de null
    private static final String CADENA_VACIA = "";

    private int id;
    private String titulo;
    private int estado;
    private int prioridad;
    private String fechaInicio;
    private String fechaFin;
    private String descripcion;
    private String requisitos;
    private String destino;
    private String problemas;
    private String mejoras;

    public POJOProyecto() {
        this.id = -1;
        this.titulo = CADENA_VACIA;
        this.estado = 0;
        this.prioridad = 0;
    }

    public POJOProyecto(int id, String titulo, int estado, int prioridad, String fechaInicio, String fechaFin, String descripcion, String requisitos, String destino, String problemas, String mejoras) {
        setId(id);
        setTitulo(titulo);
        setEstado(estado);
        setPrioridad(prioridad);
        setFechaInicio(fechaInicio);
        setFechaFin(fechaFin);
        setDescripcion(descripcion);
        setRequisitos(requisitos);
        setDestino(destino);
        setProblemas(problemas);
        setMejoras(mejoras);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        String tituloTratado = limitarCadenas(titulo, LIMITE_TITULO);

        if (tituloTratado.isEmpty()) {
            tituloTratado = CADENA_VACIA;
        }

        this.titulo = tituloTratado;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = limitarValor(estado);
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = limitarValor(prioridad);
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = limitarCadenas(fechaInicio, LIMITE_FECHA);
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = limitarCadenas(fechaFin, LIMITE_FECHA);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = limitarCadenas(descripcion, LIMITE_DESCRIPCION);
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = limitarCadenas(requisitos, LIMITE_REQUISITOS);
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = limitarCadenas(destino, LIMITE_DESTINO);
    }

    public String getProblemas() {
        return problemas;
    }

    public void setProblemas(String problemas) {
        this.problemas = limitarCadenas(problemas, LIMITE_PROBLEMAS);
    }

    public String getMejoras() {
        return mejoras;
    }

    public void setMejoras(String mejoras) {
        this.mejoras = limitarCadenas(mejoras, LIMITE_MEJORAS);
    }

    private int limitarValor(int valor) {
        return (valor > VALOR_MAX || valor < VALOR_MIN) ? 0 : valor;
    }

    private String limitarCadenas(String cadena, int limite) {
        String cadenaActual = cadena;

        if (cadena == null) {
            cadenaActual = CADENA_VACIA;
        } else {
            cadenaActual = cadena.trim();

            if (cadenaActual.length() > limite) {
                cadenaActual = cadenaActual.substring(0, limite);
            }
        }

        return cadenaActual;
    }

    @Override
    public String toString() {
        return "POJORegistro{" + "id=" + id + ", titulo=" + titulo + ", estado=" + estado + ", prioridad=" + prioridad + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaInicio + ", descripcion=" + descripcion + ", requisitos=" + requisitos + ", destino=" + destino + ", problemas=" + problemas + ", mejoras=" + mejoras + '}';
    }
}
