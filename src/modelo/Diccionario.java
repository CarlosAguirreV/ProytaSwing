package modelo;

import static modelo.Constantes.*;

/**
 * Aqui se recogen todas las cadenas de texto de la aplicacion.
 *
 * @author Carlos Aguirre
 */
public interface Diccionario {

    String NOMBRE_APLICACION = "Proyta";
    String NO_REGISTROS_TITULO = "No hay registros con ese titulo.";
    String BD_NO_CERRADA = "La BD no se ha cerrado.";
    String FALLO_GUARDADO = "Fallo al guardar.\n Comprueba que ese titulo no se esté usando ya.";
    String TITULO_YA_USADO = "Ese título ya ha sido asignado a otro proyecto. Escribe otro.";
    String FALLO_AL_MODIFICAR = "Fallo al modificar.";
    String FALLO_AL_ELIMINAR = "Fallo al eliminar.";
    String FALLO_AL_CONSULTAR = "No se pudo realizar la consulta.";
    String FALLO_OPERACION = "No se pudo realizar la operación";
    String NO_ELIMINA_SI_NO_CREA = "No se puede eliminar si no se ha creado aún.";
    String ERROR_CONEXION = "ERROR - Al conectar. ";
    String ERROR_CREAR_BD = "ERROR - Al crear la BD. ";
    String ERROR_CONSULTA = "ERROR - Al realizar la consulta. ";
    String ERROR_UPDATE = "ERROR - Al realizar el update. ";
    String ERROR = "ERROR ";
    String SIN_ID = "*";

    String TODO = "Todo";
    String[] CADENAS_ESTADO = {"En proceso", "Pausado", "Terminado", "Idea"};
    String[] CADENAS_PRIORIDAD = {"Baja", "Media", "Alta"};

    String ID = "ID";
    String ESTADO = "Estado";
    String PRIORIDAD = "Prioridad";
    String TITULO = "Título";
    String FECHA_INICIO = "Fecha inicio";
    String FECHA_FIN = "Fecha fin";
    String REQUISITOS = "Requisitos";
    String DESTINO = "Destino";
    String PROBLEMAS = "Problemas";
    String MEJORAS = "Mejoras";
    String DESCRIPCION = "Descripción";

    String PREGUNTA_ELIMINAR = "¿Deseas eliminar este proyecto?";
    String ESCRIBE_TITULO = "Escribe un título para este proyecto.";
    String CONFIRMAR = "Confirmar";
    String INFORMACION = "Información";
    String SEL_FILA_ANTES_EDITAR = "Selecciona una fila antes de editar.";
    String TITULO_PARTE = "Titulo o parte del titulo";
    String BUSQUEDA = "Busqueda";

    String EDITAR = "Editar";
    String ELIMINAR = "Eliminar";
    String VOLVER = "Volver";
    String GUARDAR = "Guardar";
    String CANCELAR = "Cancelar";
    String BUSCAR = "Buscar";
    String NUEVO_PROYECTO = "Nuevo proyecto";
    String VER_EDITAR = "Ver / Editar";

    String CERRAR = "Cerrar";
    String INVITAR_CAFE = "Invitar a un café";
    String GIT_HUB = "Git Hub";
    String DONATIVO = "Donativo";
    String ACERCA_DE = "Acerca de";

    String FALLO_NAVEGADOR = "Por algún motivo no se ha podido abrir el navegador.\nNo importa, te pongo el link aquí abajo.";
    String ACERCA = "<html>"
            + "<h3>Título</h3>"
            + "<p>" + NOMBRE_APLICACION + " (Swing) </p><p></p>"
            + "<h3>Desarrollador</h3>"
            + "<p>Carlos Aguirre</p><p></p>"
            + "<h3>Sobre esta aplicación</h3>"
            + "<p>Versión: " + VERSION + "</p>"
            + "<p>Este programa te permite gestionar tus proyectos e ideas de manera sencilla y ordenada.</p>"
            + "<p>Para almacenar la información se emplea una pequeña base de datos SQLite. La base de datos se almacena en el archivo " + NOMBRE_ARCHIVO + "."
            + "<p>Si te gusta el proyecto y quieres contribuir puedes hacer un pequeño donativo haciendo click en el botón <em>Invitar a un cafe</em>.</p><p></p>"
            + "<p>Muchas gracias por usar esta aplicación.</p>"
            + "</html>";

}
