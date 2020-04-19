package controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import modelo.POJORegistro;
import vista.VistaEdicion;
import vista.VistaTabla;

/**
 * Esta clase controla todas las acciones de la GUI
 *
 * @author Carlos Aguirre
 */
public class Controlador {

    public final static int SIN_CREAR = -1;

    public final static int EN_PROCESO = 0;
    public final static int PAUSADOS = 1;
    public final static int TERMINADOS = 2;
    public final static int TODO = 3;

    public final static String[] CADENAS_ESTADO = {"En proceso", "Pausado", "Terminado"};
    public final static String[] CADENAS_PRIORIDAD = {"Baja", "Media", "Alta"};

    private ControlBD bd;
    private VistaTabla vistaTabla;
    private ArrayList<POJORegistro> registrosLista;

    public Controlador() {
        // Crear elementos
        this.bd = new ControlBD();
        this.vistaTabla = new VistaTabla(this);
        this.registrosLista = new ArrayList();

        // Mostrar vista tabla
        this.vistaTabla.mostrarVista(true);

        // Mostrar todos los registros
        mostrarTodo();
    }

    // Metodos de la gui tabla
    private void mostrarTodo() {
        bd.getRegistros(registrosLista);
        this.vistaTabla.mostrarRegistros(registrosLista);
    }

    private void mostrarRegistroEstado(int estado) {
        bd.getRegistrosEstado(registrosLista, estado);
        this.vistaTabla.mostrarRegistros(registrosLista);
    }

    public void accionBuscar(String cadena) {
        if (cadena != null) {

            String cadenaTratada = cadena.trim();

            if (!cadenaTratada.isEmpty()) {

                bd.getRegistrosPorTitulo(registrosLista, cadenaTratada);
                if (registrosLista.isEmpty()) {
                    vistaTabla.mostrarMensaje("No hay registros con ese titulo.");
                } else {
                    vistaTabla.mostrarRegistros(registrosLista);
                }

            }

        }
    }

    public void accionNuevoProyecto() {
        POJORegistro registroNuevo = new POJORegistro();
        registroNuevo.setFechaInicio(getFechaActual());
        new VistaEdicion(this, registroNuevo, vistaTabla, true);
    }

    public void accionEditar(int idProyecto) {
        POJORegistro registroActual = bd.getRegistroPorId(idProyecto);
        new VistaEdicion(this, registroActual, vistaTabla, false);
    }

    public void accionFiltrar(int accionFiltrar) {
        switch (accionFiltrar) {
            case TODO:
                mostrarTodo();
                break;

            case TERMINADOS:
                mostrarRegistroEstado(TERMINADOS);
                break;

            case EN_PROCESO:
                mostrarRegistroEstado(EN_PROCESO);
                break;

            case PAUSADOS:
                mostrarRegistroEstado(PAUSADOS);
                break;
        }
    }

    // Metodos de la gui edicion
    public void accionVolverCerrar() {
        accionFiltrar(vistaTabla.getFiltroSeleccionado());
    }

    public void accionGuardar(POJORegistro registroActual, VistaEdicion vista) {
        vista.setEnabled(false);

        if (registroActual.getId() == SIN_CREAR) {
            if (bd.addRegistro(registroActual)) {
                vista.setRegistroActual(bd.getUltimoRegistroCreado());
                vista.modoEdicion(false);
                vista.refrescarCampos();
            } else {
                vista.mostrarMensaje("Fallo al guardar.\n Comprueba que ese titulo no se esté usando ya.");
            }
        } else {
            if (bd.modifyRegistro(registroActual)) {
                vista.setRegistroActual(bd.getRegistroPorId(registroActual.getId()));
                vista.modoEdicion(false);
                vista.refrescarCampos();
            } else {
                vista.mostrarMensaje("Fallo al modificar");
            }
        }

        vista.setEnabled(true);
    }

    public void accionEliminar(int idRegistro, VistaEdicion vista) {
        if (idRegistro == SIN_CREAR) {
            vista.mostrarMensaje("No se puede eliminar si no se ha creado aún.");
        } else {
            if (bd.removeRegistro(idRegistro)) {
                vista.cerrarVentana();
                accionVolverCerrar();
            } else {
                vista.mostrarMensaje("Fallo al eliminar.");
            }
        }
    }
    
    private String getFechaActual(){
        Calendar calendario = Calendar.getInstance();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-YYY");
        return formatoFecha.format(calendario.getTime());
    }

}
