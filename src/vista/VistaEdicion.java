package vista;

import controlador.Controlador;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import modelo.POJOProyecto;
import static modelo.Constantes.*;
import static modelo.Diccionario.*;

/**
 * Vista desde la cual se pueden ver los detalles del proyecto y ademas editar
 * los campos. Todo ello gestionado por el controlador. Es un JDialog (ventana
 * de dialogo), esta depende de la vista VistaTabla. Sus acciones son
 * controladas desde el controlador.
 *
 * @author Carlos Aguirre
 */
public class VistaEdicion extends JDialog {

    // ################### CAMPOS ###################
    // Colores de la aplicacion
    private final Color colorPanel = new Color(24, 85, 130);
    private final Color colorBoton = new Color(33, 133, 183);
    private final Color colorLetra = Color.white;
    private final Color colorAzulId = new Color(205, 227, 254);
    private final Color colorGrisBloqueo = new Color(238, 238, 238);
    private final Color colorNegro = new Color(0, 0, 0);
    private final Color colorAmarilloEdicion = new Color(255, 255, 220);
    private final Color colorGuardar = new Color(220, 255, 185);
    private final Color colorCancelar = new Color(255, 185, 185);
    private final Color colorEliminar = new Color(130, 23, 9);
    private final Font fuenteCampos = new Font("Default", 1, 15);

    // Lo que se mostrara si el id es -1 o lo que es lo mismo que aun no tiene id asociado
    private final String sinId = SIN_ID;

    // Margen de las lineas invisibles
    private final int margen = 10;

    // Margen izquierdo de los cuadros de texto JTextField
    private final int margenTexto = 5;

    // Elementos Swing a usar
    private JPanel pnlGlobal, pnlNorte, pnlCentral, pnlSur; // Paneles generales
    private JPanel pnlCentralSuperior, pnlSuperiorIzq, pnlSuperiorDcha, pnlCentralInferior; // Todo del centro
    private JButton btnEditar, btnEliminar; // Arriba
    private JButton btnVolver, btnGuardar, btnCancelar; // Abajo
    private JTextField txtId, txtTitulo, txtFechaInicio, txtFechaFin, txtDescripcion, txtRequisitos, txtDestino, txtProblemas, txtMejoras;
    private JComboBox cmbEstado, cmbPrioridad;

    // Elementos de control
    private final Controlador controlador;
    private POJOProyecto registroActual;
    private boolean nuevoProyecto;
    private boolean hayCambios;

    protected final ArrayList<Image> icono;

    // ########################## CONSTRUCTOR PREVIO ##########################
    {
        icono = new ArrayList();
        icono.add(Toolkit.getDefaultToolkit().getImage(VistaEdicion.class.getResource("/recursos/icono1.png")));
        icono.add(Toolkit.getDefaultToolkit().getImage(VistaEdicion.class.getResource("/recursos/icono2.png")));
        icono.add(Toolkit.getDefaultToolkit().getImage(VistaEdicion.class.getResource("/recursos/icono3.png")));
        icono.add(Toolkit.getDefaultToolkit().getImage(VistaEdicion.class.getResource("/recursos/icono4.png")));
        this.setIconImages(icono);
    }

    // ########################## CONSTRUCTOR ##########################
    public VistaEdicion(Controlador controlador, POJOProyecto registro, VistaTabla vistaPadre, boolean modoEdicion, boolean nuevoProyecto) {
        super(vistaPadre, "Edición");
        this.controlador = controlador;
        this.registroActual = registro;
        this.nuevoProyecto = nuevoProyecto;
        this.hayCambios = false;

        // Definir que es una aplicacion modal.
        setModalityType(ModalityType.APPLICATION_MODAL);

        // Metodos principales.
        this.crearElementos();
        this.definirEstilos();
        this.crearDistribuciones();
        this.colocarElementos();
        this.eventos();

        // Definir si esta en modo edicion.
        this.modoEdicion(modoEdicion);

        // Mostrar valores recibidos.
        refrescarCampos();

        // Metodos de la ventana.
        this.definirTamanioVentana(400, 500);
        super.setResizable(true);
        super.setLocationRelativeTo(null);
        super.setVisible(true);
    }

    // ########################## METODOS ##########################
    private void crearElementos() {
        // Paneles
        pnlGlobal = new JPanel();
        pnlNorte = new JPanel();
        pnlCentral = new JPanel();
        pnlSur = new JPanel();
        pnlCentralSuperior = new JPanel();
        pnlSuperiorIzq = new JPanel();
        pnlSuperiorDcha = new JPanel();
        pnlCentralInferior = new JPanel();

        // Botones.
        btnEditar = new JButton(EDITAR);
        btnEliminar = new JButton(ELIMINAR);
        btnVolver = new JButton(VOLVER);
        btnGuardar = new JButton(GUARDAR);
        btnCancelar = new JButton(CANCELAR);

        // Campos de texto.
        txtId = new JTextField();
        txtId.setEnabled(false);
        txtTitulo = new JTextField();
        txtFechaInicio = new JTextField();
        txtFechaFin = new JTextField();
        txtDescripcion = new JTextField();
        txtRequisitos = new JTextField();
        txtDestino = new JTextField();
        txtProblemas = new JTextField();
        txtMejoras = new JTextField();

        // Combo box.
        cmbEstado = new JComboBox(CADENAS_ESTADO);
        cmbPrioridad = new JComboBox(CADENAS_PRIORIDAD);
    }

    private void definirEstilos() {
        // Iconos de los botones.
        btnEditar.setIcon(new ImageIcon(getClass().getResource("/recursos/modificar.png")));
        btnEliminar.setIcon(new ImageIcon(getClass().getResource("/recursos/eliminar.png")));
        btnVolver.setIcon(new ImageIcon(getClass().getResource("/recursos/volver.png")));
        btnGuardar.setIcon(new ImageIcon(getClass().getResource("/recursos/guardar.png")));
        btnCancelar.setIcon(new ImageIcon(getClass().getResource("/recursos/cancelar.png")));

        // Color del campo ID.
        txtId.setBackground(colorAzulId);

        // Definir fuente de todos los campos y combo box.
        txtId.setFont(fuenteCampos);
        txtTitulo.setFont(fuenteCampos);
        cmbEstado.setFont(fuenteCampos);
        cmbPrioridad.setFont(fuenteCampos);
        txtFechaInicio.setFont(fuenteCampos);
        txtFechaFin.setFont(fuenteCampos);
        txtDescripcion.setFont(fuenteCampos);
        txtRequisitos.setFont(fuenteCampos);
        txtDestino.setFont(fuenteCampos);
        txtProblemas.setFont(fuenteCampos);
        txtMejoras.setFont(fuenteCampos);

        // Aniadir margen izquierdo a los campos de texto.
        txtId.setMargin(new Insets(0, margenTexto, 0, 0));
        txtTitulo.setMargin(new Insets(0, margenTexto, 0, 0));
        txtFechaInicio.setMargin(new Insets(0, margenTexto, 0, 0));
        txtFechaFin.setMargin(new Insets(0, margenTexto, 0, 0));
        txtDescripcion.setMargin(new Insets(0, margenTexto, 0, 0));
        txtRequisitos.setMargin(new Insets(0, margenTexto, 0, 0));
        txtDestino.setMargin(new Insets(0, margenTexto, 0, 0));
        txtProblemas.setMargin(new Insets(0, margenTexto, 0, 0));
        txtMejoras.setMargin(new Insets(0, margenTexto, 0, 0));

        // Definir el color de los campos deshabilitados, para verlo mejor.
        txtId.setDisabledTextColor(colorNegro);
        txtTitulo.setDisabledTextColor(colorNegro);
        cmbEstado.setForeground(colorNegro);
        cmbPrioridad.setForeground(colorNegro);
        txtFechaInicio.setDisabledTextColor(colorNegro);
        txtFechaFin.setDisabledTextColor(colorNegro);
        txtDescripcion.setDisabledTextColor(colorNegro);
        txtRequisitos.setDisabledTextColor(colorNegro);
        txtDestino.setDisabledTextColor(colorNegro);
        txtProblemas.setDisabledTextColor(colorNegro);
        txtMejoras.setDisabledTextColor(colorNegro);

        // Definir el color de fondo y de texto de los botones.
        auxColorBotones(colorBoton, colorLetra, btnEditar, btnVolver);
        auxColorBotones(colorEliminar, colorLetra, btnEliminar);
        auxColorBotones(colorGuardar, Color.BLACK, btnGuardar);
        auxColorBotones(colorCancelar, Color.BLACK, btnCancelar);

        // Definir el color de fondo del los paneles.
        pnlNorte.setBackground(colorPanel);
        pnlSur.setBackground(colorPanel);
    }

    private void auxColorBotones(Color ColorFondo, Color colorLetra, JButton... botones) {
        for (JButton boton : botones) {
            boton.setBackground(ColorFondo);
            boton.setForeground(colorLetra);
        }
    }

    private void crearDistribuciones() {
        pnlGlobal.setLayout(new BorderLayout());
        pnlNorte.setLayout(new FlowLayout());
        pnlCentral.setLayout(new GridLayout(2, 1, 0, 0));

        pnlCentralSuperior.setLayout(new GridLayout(1, 2, 0, 15));
        pnlSuperiorIzq.setLayout(new BorderLayout());
        pnlSuperiorDcha.setLayout(new BorderLayout());
        pnlCentralInferior.setLayout(new BorderLayout());

        pnlSur.setLayout(new FlowLayout());
    }

    private void colocarElementos() {
        // Paneles generales
        getContentPane().add(pnlGlobal);
        pnlGlobal.add(pnlNorte, BorderLayout.NORTH);
        pnlGlobal.add(pnlCentral, BorderLayout.CENTER);
        pnlGlobal.add(pnlSur, BorderLayout.SOUTH);

        // Panel Norte
        pnlNorte.add(btnVolver);
        pnlNorte.add(btnEditar);
        pnlNorte.add(btnCancelar);

        // Panel Central (tiene 2 paneles: Superior [tiene otros 2] e Inferior).
        pnlCentral.add(pnlCentralSuperior);
        pnlCentral.add(pnlCentralInferior);

        pnlCentralSuperior.add(pnlSuperiorIzq);
        pnlCentralSuperior.add(pnlSuperiorDcha);

        pnlSuperiorIzq.add(generarPanelEtiquetas(ID, ESTADO, PRIORIDAD), BorderLayout.WEST);
        pnlSuperiorIzq.add(generarPanelCampos(txtId, cmbEstado, cmbPrioridad), BorderLayout.CENTER);

        pnlSuperiorDcha.add(generarPanelEtiquetas(TITULO, FECHA_INICIO, FECHA_FIN), BorderLayout.WEST);
        pnlSuperiorDcha.add(generarPanelCampos(txtTitulo, txtFechaInicio, txtFechaFin), BorderLayout.CENTER);

        pnlCentralInferior.add(generarPanelEtiquetas(REQUISITOS, DESTINO, PROBLEMAS, MEJORAS, DESCRIPCION), BorderLayout.WEST);
        pnlCentralInferior.add(generarPanelCampos(txtRequisitos, txtDestino, txtProblemas, txtMejoras, txtDescripcion), BorderLayout.CENTER);

        // Panel Sur
        pnlSur.add(btnGuardar);
        pnlSur.add(btnEliminar);
    }

    // Metodo auxiliar que genera un panel con etiquetas mediante los nombres pasados por parametro.
    private JPanel generarPanelEtiquetas(String... nombreEtiquetas) {
        JPanel panelEtiquetas = new JPanel();
        panelEtiquetas.setLayout(new GridLayout(nombreEtiquetas.length, 1, 0, 15));
        panelEtiquetas.setBorder(BorderFactory.createEmptyBorder(margen, margen, margen, 0));

        JLabel lblTemporal;
        for (String cadena : nombreEtiquetas) {
            lblTemporal = new JLabel(cadena);
            panelEtiquetas.add(lblTemporal);
        }

        return panelEtiquetas;
    }

    // Metodo auxiliar que genera un panel con los campos y/o combo box pasados por parametro.
    private JPanel generarPanelCampos(JComponent... elementos) {
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new GridLayout(elementos.length, 1, 0, 15));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(margen, margen, margen, margen));

        for (JComponent com : elementos) {
            panelCampos.add(com);
        }

        return panelCampos;
    }

    public void modoEdicion(boolean modoEdicion) {
        txtTitulo.setEnabled(modoEdicion);
        cmbEstado.setEnabled(modoEdicion);
        cmbPrioridad.setEnabled(modoEdicion);
        txtFechaInicio.setEnabled(modoEdicion);
        txtFechaFin.setEnabled(modoEdicion);
        txtDescripcion.setEnabled(modoEdicion);
        txtRequisitos.setEnabled(modoEdicion);
        txtDestino.setEnabled(modoEdicion);
        txtProblemas.setEnabled(modoEdicion);
        txtMejoras.setEnabled(modoEdicion);

        btnEditar.setVisible(!modoEdicion);
        btnVolver.setVisible(!modoEdicion);
        btnGuardar.setVisible(modoEdicion);
        btnCancelar.setVisible(modoEdicion);
        if (nuevoProyecto) {
            btnEliminar.setVisible(false);
        } else {
            btnEliminar.setVisible(modoEdicion);
        }

        if (modoEdicion) {
            txtTitulo.setBackground(colorAmarilloEdicion);
            cmbEstado.setBackground(colorAmarilloEdicion);
            cmbPrioridad.setBackground(colorAmarilloEdicion);
            txtFechaInicio.setBackground(colorAmarilloEdicion);
            txtFechaFin.setBackground(colorAmarilloEdicion);
            txtDescripcion.setBackground(colorAmarilloEdicion);
            txtRequisitos.setBackground(colorAmarilloEdicion);
            txtDestino.setBackground(colorAmarilloEdicion);
            txtProblemas.setBackground(colorAmarilloEdicion);
            txtMejoras.setBackground(colorAmarilloEdicion);
        } else {
            txtTitulo.setBackground(colorGrisBloqueo);
            cmbEstado.setBackground(colorGrisBloqueo);
            cmbPrioridad.setBackground(colorGrisBloqueo);
            txtFechaInicio.setBackground(colorGrisBloqueo);
            txtFechaFin.setBackground(colorGrisBloqueo);
            txtDescripcion.setBackground(colorGrisBloqueo);
            txtRequisitos.setBackground(colorGrisBloqueo);
            txtDestino.setBackground(colorGrisBloqueo);
            txtProblemas.setBackground(colorGrisBloqueo);
            txtMejoras.setBackground(colorGrisBloqueo);
        }
    }

    private void definirTamanioVentana(double pxAlto, double pxAncho) {
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();

        double altoFinal = pantalla.height * pxAlto / 720;
        double anchoFinal = pantalla.width * pxAncho / 1280;

        setSize(new Dimension((int) anchoFinal, (int) altoFinal));
    }

    private void eventos() {
        btnEditar.addActionListener((ActionEvent ae) -> {
            modoEdicion(true);
        });

        btnEliminar.addActionListener((ActionEvent ae) -> {
            if (registroActual.getId() == SIN_CREAR) {
                mostrarMensaje(NO_ELIMINA_SI_NO_CREA);
            } else {
                if (pedirConfirmacion(PREGUNTA_ELIMINAR)) {
                    controlador.accionEliminar(registroActual.getId(), VistaEdicion.this);
                }
            }
        });

        btnVolver.addActionListener((ActionEvent ae) -> {
            if (!nuevoProyecto && hayCambios) {
                controlador.accionVolverCerrar();
            }
            cerrarVentana();
        });

        btnGuardar.addActionListener((ActionEvent ae) -> {
            refrescarRegistroActual();

            if (isTituloVacio()) {
                txtTitulo.setBackground(colorCancelar);
                mostrarMensaje(ESCRIBE_TITULO);
            } else {
                controlador.accionGuardar(registroActual, VistaEdicion.this);
            }
        });

        btnCancelar.addActionListener((ActionEvent ae) -> {
            if (nuevoProyecto) {
                cerrarVentana();
            } else {
                modoEdicion(false);
                refrescarCampos();
            }
        });

        cmbEstado.addActionListener((ActionEvent e) -> {
            if (cmbEstado.getSelectedIndex() == 2) {
                txtFechaFin.setText(controlador.getFechaActual());
            }
        });

        // Al cerrar esta ventana (JDialog).
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!nuevoProyecto && hayCambios) {
                    controlador.accionVolverCerrar();
                }
            }
        });

    }

    public void setEsNuevo(boolean esNuevo) {
        this.nuevoProyecto = esNuevo;
    }

    public void setHayCambios(boolean hayCambios) {
        this.hayCambios = hayCambios;
    }

    // Comprueba si el campo de texto titulo esta vacio.
    private boolean isTituloVacio() {
        return txtTitulo.getText().trim().isEmpty();
    }

    // Pide confirmacion y devuelve el resultado.
    public boolean pedirConfirmacion(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(this, mensaje, CONFIRMAR, JOptionPane.INFORMATION_MESSAGE);
        return respuesta == JOptionPane.YES_OPTION;
    }

    // Refresca todos los campos de texto incluyendo los combo box.
    public void refrescarCampos() {
        txtId.setText(registroActual.getId() == -1 ? sinId : Integer.toString(registroActual.getId()));
        txtTitulo.setText(registroActual.getTitulo());
        cmbEstado.setSelectedIndex(registroActual.getEstado());
        cmbPrioridad.setSelectedIndex(registroActual.getPrioridad());
        txtFechaInicio.setText(registroActual.getFechaInicio());
        txtFechaFin.setText(registroActual.getFechaFin());
        txtDescripcion.setText(registroActual.getDescripcion());
        txtRequisitos.setText(registroActual.getRequisitos());
        txtDestino.setText(registroActual.getDestino());
        txtProblemas.setText(registroActual.getProblemas());
        txtMejoras.setText(registroActual.getMejoras());
    }

    // Obtiene todos los valores de los campos de texto y los combo box y actualiza el POJOProyecto actual.
    private void refrescarRegistroActual() {
        registroActual.setTitulo(txtTitulo.getText());
        registroActual.setEstado(cmbEstado.getSelectedIndex());
        registroActual.setPrioridad(cmbPrioridad.getSelectedIndex());
        registroActual.setFechaInicio(txtFechaInicio.getText());
        registroActual.setFechaFin(txtFechaFin.getText());
        registroActual.setRequisitos(txtRequisitos.getText());
        registroActual.setDestino(txtDestino.getText());
        registroActual.setProblemas(txtProblemas.getText());
        registroActual.setMejoras(txtMejoras.getText());
        registroActual.setDescripcion(txtDescripcion.getText());
    }

    // Define el registro con el que se esta trabajando.
    public void setRegistroActual(POJOProyecto registroActual) {
        this.registroActual = registroActual;
    }

    // Muestra una ventana con un mensaje.
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, INFORMACION, JOptionPane.INFORMATION_MESSAGE);
    }

    // Cierra esta ventana (llamado desde Controlador).
    public void cerrarVentana() {
        this.dispose();
    }

}
