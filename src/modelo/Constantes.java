package modelo;

/**
 * Aqui estan todas las constantes usadas por la aplicacion.
 *
 * @author Carlos Aguirre
 */
public interface Constantes {

    // Valores generales.
    public static final String VERSION = "02-12-2020";
    public static final String WEB_DONATIVO = "https://ko-fi.com/carlosaguirrev";
    public static final String WEB_GITHUB = "https://github.com/CarlosAguirreV/ProytaSwing.git";

    // Para poner limites a las cadenas String, ya que SQLite no las pone.
    public static final int LIMITE_TITULO = 100;
    public static final int LIMITE_FECHA = 50;
    public static final int LIMITE_DESCRIPCION = 300;
    public static final int LIMITE_REQUISITOS = 150;
    public static final int LIMITE_DESTINO = 150;
    public static final int LIMITE_PROBLEMAS = 200;
    public static final int LIMITE_MEJORAS = 200;

    // Texto por defecto en caso de no haber nada, en sustitucion de null.
    public static final String CADENA_VACIA = "";

    // Lo que significa que un id tenga un -1.
    public final static int SIN_CREAR = -1;

    // Otros.
    public static final String FORMATO_FECHA_HORA = "dd-MM-yyyy";
    public static final String NOMBRE_ARCHIVO = "bd_proyectos";
    public static final String RUTA_BD = "jdbc:sqlite:" + NOMBRE_ARCHIVO;
}
