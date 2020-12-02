package controlador;

import modelo.ControlBD;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import static modelo.Constantes.*;
import static modelo.Diccionario.*;
import modelo.POJOProyecto;
import vista.VistaAcerca;
import vista.VistaEdicion;
import vista.VistaTabla;

/**
 * Esta clase controla todas las acciones de las vistas.
 *
 * @author Carlos Aguirre
 */
public class Controlador {

    // ################### CAMPOS ###################
    private ControlBD bd;
    private VistaTabla vistaTabla;
    private ArrayList<POJOProyecto> registrosLista;

    // ################### CONSTRUCTOR ###################
    public Controlador() {
        // Crear elementos
        this.bd = new ControlBD();
        this.vistaTabla = new VistaTabla(this);
        this.registrosLista = new ArrayList();

        // Mostrar vista tabla
        this.vistaTabla.mostrarVista(true);

        // Mostrar registros "En proceso" por defecto.
        accionFiltrar(1);
    }

    // ################### METODOS DE VISTA TABLA ###################
    // Si se cierra esta ventana se cierra todo el programa.
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
                // Obtener todos los registros que contengan esa cadena
                bd.getRegistrosPorTitulo(registrosLista, cadenaTratada);
                if (registrosLista.isEmpty()) {
                    vistaTabla.mostrarMensaje(NO_REGISTROS_TITULO);
                } else {
                    vistaTabla.mostrarRegistros(registrosLista);
                }
            }

        }
    }

    public void accionNuevoProyecto() {
        POJOProyecto registroNuevo = new POJOProyecto();
        registroNuevo.setFechaInicio(getFechaActual());
        new VistaEdicion(this, registroNuevo, vistaTabla, true, true);
    }

    public String getFechaActual() {
        return new SimpleDateFormat(FORMATO_FECHA_HORA).format(Calendar.getInstance().getTime());
    }

    public void accionEditar(int idProyecto) {
        POJOProyecto registroActual = bd.getRegistroPorId(idProyecto);
        new VistaEdicion(this, registroActual, vistaTabla, false, false);
    }

    public void accionFiltrar(int accionFiltrar) {
        if (accionFiltrar == 0) {
            mostrarTodo();
        } else {
            mostrarRegistroEstado(accionFiltrar - 1);
        }
    }

    // Lo que ocurrira al cerrar la aplicacion del todo
    public void accionCerrarAplicacion() {
        if (!bd.desconectar()) {
            vistaTabla.mostrarMensaje(BD_NO_CERRADA);
        }
    }

    public void accionAcercaDe() {
        new VistaAcerca(vistaTabla);
    }

    //################### METODOS DE VISTA EDICION ###################
    public void accionVolverCerrar() {
        accionFiltrar(vistaTabla.getFiltroSeleccionado());
    }

    public void accionGuardar(POJOProyecto registroActual, VistaEdicion vista) {
        vista.setEnabled(false);

        if (registroActual.getId() == SIN_CREAR) {
            // NUEVO Si el registro aun no se ha creado (no tiene id asignado [id = -1])
            if (bd.addRegistro(registroActual)) {
                vista.setRegistroActual(bd.getUltimoRegistroCreado());
                vista.setEsNuevo(false);
                vista.setHayCambios(true);
                vista.modoEdicion(false);
                vista.refrescarCampos();
            } else {
                vista.mostrarMensaje(FALLO_GUARDADO);
            }
        } else {
            // MODIFICAR Si el registro ya se habia creado (ya tiene ID)

            // Comprobar que si el titulo es nuevo o ya existe
            ArrayList<POJOProyecto> listaTemporal = new ArrayList();
            bd.getRegistrosPorTituloEsp(listaTemporal, registroActual.getTitulo());
            POJOProyecto versionAnterior = bd.getRegistroPorId(registroActual.getId());

            if (registroActual.getTitulo().equals(versionAnterior.getTitulo())) {
                // Si es el mismo titulo
                auxModificar(registroActual, vista);

            } else if (!listaTemporal.isEmpty()) {
                // Si es un titulo distinto pero coincide con otro
                vista.mostrarMensaje(TITULO_YA_USADO);

            } else {
                // Si es un titulo distinto pero no esta en la lista
                auxModificar(registroActual, vista);
            }

        }

        vista.setEnabled(true);
    }

    private void auxModificar(POJOProyecto registroActual, VistaEdicion vista) {
        if (bd.modifyRegistro(registroActual)) {
            vista.setRegistroActual(bd.getRegistroPorId(registroActual.getId()));
            vista.setHayCambios(true);
            vista.modoEdicion(false);
            vista.refrescarCampos();
        } else {
            vista.mostrarMensaje(FALLO_AL_MODIFICAR);
        }
    }

    public void accionEliminar(int idRegistro, VistaEdicion vista) {
        if (idRegistro == SIN_CREAR) {
            vista.mostrarMensaje(NO_ELIMINA_SI_NO_CREA);
        } else {
            if (bd.removeRegistro(idRegistro)) {
                vista.cerrarVentana();
                accionVolverCerrar();
            } else {
                vista.mostrarMensaje(FALLO_AL_ELIMINAR);
            }
        }
    }

}
