package controlador;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import modelo.POJORegistro;

/**
 * Esta clase maneja la tabla 'proyectos' de la base de datos 'bd_proyectos' de
 * SQLite. Esta diseniado de tal forma que solo se va a usar un solo ArrayList
 * en todo momento. Salvo para hacer pequenias comprobaciones.
 *
 * @author Carlos Aguirre
 */
public class ControlBD implements ControlBDInterfaz {

    private static final String RUTA_BD = "jdbc:sqlite:.\\bd_proyectos";
    private static final String NOMBRE_ARCHIVO = "bd_proyectos";
    private String mensajeError;

    public ControlBD() {
        intentarConectar();
    }

    // Intentar conectar
    private boolean intentarConectar() {
        // Si no existe el archivo dejara una senial para crear las tablas despues
        boolean existeArchivo = new File(NOMBRE_ARCHIVO).exists();

        // Trata de conectarse a la BD
        try {
            Connection conexion = DriverManager.getConnection(RUTA_BD);
            if (!existeArchivo) {
                this.crearTablas();
            }
            System.out.println("CONSIGUE CONECTAR");
            return true;
        } catch (SQLException ex) {
            System.out.println("ERROR - Al conectar. " + ex.getMessage());
            return false;
        }
    }

    // Crea las tablas de la BD.
    private boolean crearTablas() {
        try {
            Connection conexion = DriverManager.getConnection(RUTA_BD);
            PreparedStatement sentenciaCreacion = conexion.prepareStatement("CREATE TABLE proyectos("
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
            System.out.println("CONSIGUE CREAR BD");
            return true;

        } catch (SQLException ex) {
            System.out.println("ERROR - Al crear la BD." + ex.getMessage());
            return false;
        }
    }

    private POJORegistro getRegistro(String consulta) {
        POJORegistro registro = null;

        try {
            // Cargar el driver
            Class.forName("org.sqlite.JDBC");

            // Establecer conexion con la BD
            Connection conexion = DriverManager.getConnection(RUTA_BD);

            // Preparar la consulta y ejecutarla
            Statement sentencia = conexion.createStatement();
            ResultSet resul = sentencia.executeQuery(consulta);

            // Recorre,os el resultado para visualizar cada fila
            if (resul.next()) {
                registro = new POJORegistro(resul.getInt(1), resul.getString(2), resul.getInt(3), resul.getInt(4), resul.getString(5), resul.getString(6), resul.getString(7), resul.getString(8), resul.getString(9), resul.getString(10), resul.getString(11));
            }

            // Cerrar archivos
            resul.close();
            sentencia.close();
            conexion.close();

        } catch (Exception ex) {
            mensajeError = "No se pudo realizar la consulta.";
            System.out.println("ERROR - Al realizar la consulta." + ex.getMessage());
        }

        return registro;
    }

    @Override
    public POJORegistro getRegistroPorId(int id) {
        return getRegistro("SELECT * FROM proyectos WHERE id = " + id);
    }

    @Override
    public POJORegistro getUltimoRegistroCreado() {
        return getRegistro("SELECT * FROM proyectos WHERE id = (SELECT MAX(id) FROM proyectos)");
    }

    private void getRegistros(ArrayList<POJORegistro> registros, String consulta) {
        registros.clear();
        POJORegistro registroTemporal;

        try {
            // Cargar el driver
            Class.forName("org.sqlite.JDBC");

            // Establecer conexion con la BD
            Connection conexion = DriverManager.getConnection(RUTA_BD);

            // Preparar la consulta y ejecutarla
            Statement sentencia = conexion.createStatement();
            ResultSet resul = sentencia.executeQuery(consulta);

            // Recorre,os el resultado para visualizar cada fila
            while (resul.next()) {
                registroTemporal = new POJORegistro(resul.getInt(1), resul.getString(2), resul.getInt(3), resul.getInt(4), resul.getString(5), resul.getString(6), resul.getString(7), resul.getString(8), resul.getString(9), resul.getString(10), resul.getString(11));
                registros.add(registroTemporal);
            }

            // Cerrar archivos
            resul.close();
            sentencia.close();
            conexion.close();

        } catch (Exception ex) {
            mensajeError = "No se pudo realizar la consulta.";
            System.out.println("ERROR - Al realizar la consulta." + ex.getMessage());
        }
    }

    @Override
    public void getRegistros(ArrayList<POJORegistro> registros) {
        getRegistros(registros, "SELECT * FROM proyectos");
    }

    @Override
    public void getRegistrosPorTitulo(ArrayList<POJORegistro> registros, String titulo) {
        getRegistros(registros, "SELECT * FROM proyectos WHERE titulo LIKE '%" + titulo + "%'");
    }

    @Override
    public void getRegistrosPorTituloEsp(ArrayList<POJORegistro> registros, String titulo) {
        getRegistros(registros, "SELECT * FROM proyectos WHERE titulo = '" + titulo + "'");
    }

    @Override
    public void getRegistrosEstado(ArrayList<POJORegistro> registros, int estado) {
        getRegistros(registros, "SELECT * FROM proyectos WHERE estado = " + estado);
    }

    @Override
    public void getRegistrosPrioridad(ArrayList<POJORegistro> registros, int prioridad) {
        getRegistros(registros, "SELECT * FROM proyectos WHERE prioridad = " + prioridad);
    }

    private boolean ejecutarUpdate(String update) {
        boolean correcto = true;

        try {
            // Cargar el driver
            Class.forName("org.sqlite.JDBC");

            // Establecer conexion con la BD
            Connection conexion = DriverManager.getConnection(RUTA_BD);

            // Ejecutar el update y obtener las filas afectadas
            Statement sentencia = conexion.createStatement();
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
    public boolean addRegistro(POJORegistro registro) {
        // Insertar solo si no existe un registro con ese titulo.
        ArrayList<POJORegistro> registrosTemporal = new ArrayList();
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
    public boolean modifyRegistro(POJORegistro registro) {
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

}
