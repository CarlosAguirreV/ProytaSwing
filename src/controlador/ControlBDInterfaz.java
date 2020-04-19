package controlador;

import java.util.ArrayList;
import modelo.POJORegistro;

/**
 *
 * @author Carlos Aguirre
 */
public interface ControlBDInterfaz {
    public POJORegistro getRegistroPorId(int id);
    public POJORegistro getUltimoRegistroCreado();
    public void getRegistros(ArrayList<POJORegistro> registros);
    public void getRegistrosPorTitulo(ArrayList<POJORegistro> registros, String titulo);
    public void getRegistrosPorTituloEsp(ArrayList<POJORegistro> registros, String titulo);
    public void getRegistrosEstado(ArrayList<POJORegistro> registros, int estado);
    public void getRegistrosPrioridad(ArrayList<POJORegistro> registros, int prioridad);
    public boolean addRegistro(POJORegistro registro);
    public boolean removeRegistro(int id);
    public boolean modifyRegistro(POJORegistro registro);
    public String getMensajeError();
}
