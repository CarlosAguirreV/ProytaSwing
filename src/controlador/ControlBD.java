package controlador;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import modelo.POJOProyecto;

/**
 * Esta clase maneja la tabla 'proyectos' de la base de datos 'bd_proyectos' de
 * SQLite. Esta diseniado de tal forma que solo se va a usar un solo ArrayList
 * en todo momento. Salvo para hacer pequenias comprobaciones como es el caso de
 * addRegistro que comprueba si existe alguno con ese titulo.
 *
 * @author Carlos Aguirre
 */
public class ControlBD implements ControlBDInterfaz {

    private static final String NOMBRE_ARCHIVO = "bd_proyectos";
    private static final String RUTA_BD = "jdbc:sqlite:" + NOMBRE_ARCHIVO;
    private String mensajeError;
    private Connection conexion;

    public ControlBD() {
        conectar();
    }

    // Intentar conectar
    private boolean conectar() {
        // Si no existe el archivo dejara una senial para crear las tablas despues
        boolean existeArchivo = new File(NOMBRE_ARCHIVO).exists();

        // Trata de conectarse a la BD
        try {
            this.conexion = DriverManager.getConnection(RUTA_BD);
            if (!existeArchivo) {
                this.crearTablas();
            }
            return true;
        } catch (SQLException ex) {
            System.out.println("ERROR - Al conectar. " + ex.getMessage());
            return false;
        }
    }

    // Crea las tablas de la BD.
    private boolean crearTablas() {
        try {
            PreparedStatement sentenciaCreacion = this.conexion.prepareStatement("CREATE TABLE proyectos("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "titulo TEXT NOT NULL,"
                    + "estado INTEGER NOT NULL DEFAULT 0,"
                    + "prioridad INTEGER NOT NULL DEFAULT 0,"
                    + "fechainicio TEXT,"
                    + "fechafin TEXT,"
                    + "descripcion TEXT,"
                    + "requisitos TEXT,"
                    + "destino TEXT,"
                    + "problemas TEXT,"
                    + "mejoras TEXT"
                    + ");");
            sentenciaCreacion.execute();
            return true;

        } catch (SQLException ex) {
            System.out.println("ERROR - Al crear la BD." + ex.getMessage());
            return false;
        }
    }

    private POJOProyecto getRegistro(String consulta) {
        POJOProyecto registro = null;

        try {
            // Preparar la consulta y ejecutarla
            Statement sentencia = this.conexion.createStatement();
            ResultSet resul = sentencia.executeQuery(consulta);

            // Recorre,os el resultado para visualizar cada fila
            if (resul.next()) {
                registro = new POJOProyecto(resul.getInt(1), resul.getString(2), resul.getInt(3), resul.getInt(4), resul.getString(5), resul.getString(6), resul.getString(7), resul.getString(8), resul.getString(9), resul.getString(10), resul.getString(11));
            }

            // Liberar resultado y sentencia
            resul.close();
            sentencia.close();

        } catch (Exception ex) {
            mensajeError = "No se pudo realizar la consulta.";
            System.out.println("ERROR - Al realizar la consulta." + ex.getMessage());
        }

        return registro;
    }

    @Override
    public POJOProyecto getRegistroPorId(int id) {
        return getRegistro("SELECT * FROM proyectos WHERE id = " + id);
    }

    @Override
    public POJOProyecto getUltimoRegistroCreado() {
        return getRegistro("SELECT * FROM proyectos WHERE id = (SELECT MAX(id) FROM proyectos)");
    }

    private void getRegistros(ArrayList<POJOProyecto> registros, String consulta) {
        // Vaciar el ArrayList pasado por referencia
        registros.clear();

        POJOProyecto registroTemporal;

        try {
            // Preparar la consulta y ejecutarla
            Statement sentencia = this.conexion.createStatement();
            ResultSet resul = sentencia.executeQuery(consulta);

            // Recorre,os el resultado para visualizar cada fila
            while (resul.next()) {
                registroTemporal = new POJOProyecto(resul.getInt(1), resul.getString(2), resul.getInt(3), resul.getInt(4), resul.getString(5), resul.getString(6), resul.getString(7), resul.getString(8), resul.getString(9), resul.getString(10), resul.getString(11));
                registros.add(registroTemporal);
            }

            // Liberar resultado y sentencia
            resul.close();
            sentencia.close();

        } catch (Exception ex) {
            mensajeError = "No se pudo realizar la consulta.";
            System.out.println("ERROR - Al realizar la consulta." + ex.getMessage());
        }
    }

    @Override
    public void getRegistros(ArrayList<POJOProyecto> registros) {
        getRegistros(registros, "SELECT * FROM proyectos");
    }

    @Override
    public void getRegistrosPorTitulo(ArrayList<POJOProyecto> registros, String titulo) {
        getRegistros(registros, "SELECT * FROM proyectos WHERE titulo LIKE '%" + titulo + "%'");
    }

    @Override
    public void getRegistrosPorTituloEsp(ArrayList<POJOProyecto> registros, String titulo) {
        getRegistros(registros, "SELECT * FROM proyectos WHERE titulo = '" + titulo + "'");
    }

    @Override
    public void getRegistrosEstado(ArrayList<POJOProyecto> registros, int estado) {
        getRegistros(registros, "SELECT * FROM proyectos WHERE estado = " + estado);
    }

    @Override
    public void getRegistrosPrioridad(ArrayList<POJOProyecto> registros, int prioridad) {
        getRegistros(registros, "SELECT * FROM proyectos WHERE prioridad = " + prioridad);
    }

    private boolean ejecutarUpdate(String update) {
        boolean correcto = true;

        try {
            // Ejecutar el update y obtener las filas afectadas
            Statement sentencia = this.conexion.createStatement();
            int filasAfectadas = sentencia.executeUpdate(update);
            correcto = filasAfectadas > 0;

        } catch (Exception ex) {
            correcto = false;
            mensajeError = "No se pudo realizar la operación";
            System.out.println("ERROR - Al realizar el update." + ex.getMessage());
        }

        return correcto;
    }

    @Override
    public boolean addRegistro(POJOProyecto registro) {
        // Insertar solo si no existe un registro con ese titulo.
        ArrayList<POJOProyecto> registrosTemporal = new ArrayList();
        getRegistrosPorTituloEsp(registrosTemporal, registro.getTitulo());

        if (registrosTemporal.isEmpty()) {
            // Preparar el update
            String sql = String.format("INSERT INTO proyectos VALUES (null, '%s', %s, %s, '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                    registro.getTitulo(),
                    registro.getEstado(),
                    registro.getPrioridad(),
                    registro.getFechaInicio(),
                    registro.getFechaFin(),
                    registro.getDescripcion(),
                    registro.getRequisitos(),
                    registro.getDestino(),
                    registro.getProblemas(),
                    registro.getMejoras());

            // Ejecutarlo y retornar el resultado de si se hizo o no
            return ejecutarUpdate(sql);

        } else {
            mensajeError = "Ya hay un registro con ese titulo, escribe otro.";
            return false;
        }
    }

    @Override
    public boolean removeRegistro(int id) {
        return ejecutarUpdate("DELETE FROM proyectos WHERE id = " + id);
    }

    @Override
    public boolean modifyRegistro(POJOProyecto registro) {
        String sql = String.format("UPDATE proyectos SET titulo = '%s', estado = %s, prioridad = %s, fechaInicio = '%s', fechaFin = '%s', descripcion = '%s', requisitos = '%s', destino = '%s', problemas = '%s', mejoras = '%s' WHERE id = %s",
                registro.getTitulo(),
                registro.getEstado(),
                registro.getPrioridad(),
                registro.getFechaInicio(),
                registro.getFechaFin(),
                registro.getDescripcion(),
                registro.getRequisitos(),
                registro.getDestino(),
                registro.getProblemas(),
                registro.getMejoras(),
                registro.getId());
        return ejecutarUpdate(sql);
    }

    @Override
    public String getMensajeError() {
        return mensajeError;
    }

    @Override
    public boolean desconectar() {
        try {
            this.conexion.close();
            return true;
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
            return false;
        }
    }

}
