package modelo;

import java.util.ArrayList;

/**
 * Interfaz con los metodos necesarios para manejar la BD.
 *
 * @author Carlos Aguirre
 */
public interface ControlBDInterfaz {

    public POJOProyecto getRegistroPorId(int id);
    public POJOProyecto getUltimoRegistroCreado();
    public void getRegistros(ArrayList<POJOProyecto> registros);
    public void getRegistrosPorTitulo(ArrayList<POJOProyecto> registros, String titulo);
    public void getRegistrosPorTituloEsp(ArrayList<POJOProyecto> registros, String titulo);
    public void getRegistrosEstado(ArrayList<POJOProyecto> registros, int estado);
    public void getRegistrosPrioridad(ArrayList<POJOProyecto> registros, int prioridad);
    public boolean addRegistro(POJOProyecto registro);
    public boolean removeRegistro(int id);
    public boolean modifyRegistro(POJOProyecto registro);
    public String getMensajeError();
    public boolean desconectar();
}
