package vista;

import controlador.Controlador;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import modelo.POJORegistro;

/**
 * Vista de modo edicion. Info:
 * https://examples.javacodegeeks.com/desktop-java/swing/jdialog/java-jdialog-example/
 *
 * @author Carlos Aguirre
 */
public class VistaEdicion extends JDialog {

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
    private final String sinId = "*";
    private final int margen = 10;
    private final int margenTexto = 5;

    private JPanel pnlGlobal, pnlNorte, pnlCentral, pnlSur; // Paneles generales
    private JPanel pnlCentralSuperior, pnlSuperiorIzq, pnlSuperiorDcha, pnlCentralInferior; // Todo del centro
    private JButton btnEditar, btnEliminar; // Arriba
    private JButton btnVolver, btnGuardar, btnCancelar; // Abajo
    private JTextField txtId, txtTitulo, txtFechaInicio, txtFechaFin, txtDescripcion, txtRequisitos, txtDestino, txtProblemas, txtMejoras;
    private JComboBox cmbEstado, cmbPrioridad;

    private String[] estado = {"En proceso", "Pausado", "Terminado"};
    private String[] prioridad = {"Baja", "Media", "Alta"};

    private Controlador controlador;
    private POJORegistro registroActual;
    private VistaTabla vistaPadre;

    public VistaEdicion(Controlador controlador, POJORegistro registro, VistaTabla vistaPadre, boolean modoEdicion) {
        super(vistaPadre, "Edición");

        // Definir que es una aplicacion modal
        setModalityType(ModalityType.APPLICATION_MODAL);

        this.controlador = controlador;
        this.registroActual = registro;
        this.vistaPadre = vistaPadre;

        // Metodos principales
        this.crearElementos();
        this.definirEstilos();
        this.crearDistribuciones();
        this.colocarElementos();
        this.eventos();

        // Definir si esta en modo edicion
        this.modoEdicion(modoEdicion);

        // Mostrar valores recibidos
        refrescarCampos();

        // Metodos de la ventana
        this.definirTamanioVentana(400, 500);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    private void crearElementos() {
        pnlGlobal = new JPanel();
        pnlNorte = new JPanel();
        pnlCentral = new JPanel();
        pnlSur = new JPanel();

        pnlCentralSuperior = new JPanel();
        pnlSuperiorIzq = new JPanel();
        pnlSuperiorDcha = new JPanel();
        pnlCentralInferior = new JPanel();

        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnVolver = new JButton("Volver");
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

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

        cmbEstado = new JComboBox(estado);
        cmbPrioridad = new JComboBox(prioridad);
    }

    private void definirEstilos() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(VistaEdicion.class.getResource("/recursos/modificar2.png")));
        btnEditar.setIcon(new ImageIcon(getClass().getResource("/recursos/modificar.png")));
        btnEliminar.setIcon(new ImageIcon(getClass().getResource("/recursos/eliminar.png")));
        btnVolver.setIcon(new ImageIcon(getClass().getResource("/recursos/volver.png")));
        btnGuardar.setIcon(new ImageIcon(getClass().getResource("/recursos/guardar.png")));
        btnCancelar.setIcon(new ImageIcon(getClass().getResource("/recursos/cancelar.png")));

        txtId.setBackground(colorAzulId);

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

        txtId.setMargin(new Insets(0, margenTexto, 0, 0));
        txtTitulo.setMargin(new Insets(0, margenTexto, 0, 0));
        txtFechaInicio.setMargin(new Insets(0, margenTexto, 0, 0));
        txtFechaFin.setMargin(new Insets(0, margenTexto, 0, 0));
        txtDescripcion.setMargin(new Insets(0, margenTexto, 0, 0));
        txtRequisitos.setMargin(new Insets(0, margenTexto, 0, 0));
        txtDestino.setMargin(new Insets(0, margenTexto, 0, 0));
        txtProblemas.setMargin(new Insets(0, margenTexto, 0, 0));
        txtMejoras.setMargin(new Insets(0, margenTexto, 0, 0));

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

        auxColorBotones(colorBoton, colorLetra, btnEditar, btnVolver);
        auxColorBotones(colorEliminar, colorLetra, btnEliminar);
        auxColorBotones(colorGuardar, Color.BLACK, btnGuardar);
        auxColorBotones(colorCancelar, Color.BLACK, btnCancelar);

        pnlNorte.setBackground(colorPanel);
        pnlSur.setBackground(colorPanel);
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

        // Panel Central
        pnlCentral.add(pnlCentralSuperior);
        pnlCentral.add(pnlCentralInferior);

        pnlCentralSuperior.add(pnlSuperiorIzq);
        pnlCentralSuperior.add(pnlSuperiorDcha);

        pnlSuperiorIzq.add(generarPanelEtiquetas("ID", "Estado", "Prioridad"), BorderLayout.WEST);
        pnlSuperiorIzq.add(generarPanelCampos(txtId, cmbEstado, cmbPrioridad), BorderLayout.CENTER);

        pnlSuperiorDcha.add(generarPanelEtiquetas("Titulo", "Fecha inicio", "Fecha fin"), BorderLayout.WEST);
        pnlSuperiorDcha.add(generarPanelCampos(txtTitulo, txtFechaInicio, txtFechaFin), BorderLayout.CENTER);

        pnlCentralInferior.add(generarPanelEtiquetas("Requisitos", "Destino", "Problemas", "Mejoras", "Descripcion"), BorderLayout.WEST);
        pnlCentralInferior.add(generarPanelCampos(txtRequisitos, txtDestino, txtProblemas, txtMejoras, txtDescripcion), BorderLayout.CENTER);

        // Panel Sur
//        pnlSur.add(btnVolver);
        pnlSur.add(btnGuardar);
        pnlSur.add(btnEliminar);
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

        if (modoEdicion) {
            btnEditar.setVisible(false);
            btnEliminar.setVisible(true);
            btnVolver.setVisible(false);
            btnGuardar.setVisible(true);
            btnCancelar.setVisible(true);

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
            btnEditar.setVisible(true);
            btnEliminar.setVisible(false);
            btnVolver.setVisible(true);
            btnGuardar.setVisible(false);
            btnCancelar.setVisible(false);

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

    private void auxColorBotones(Color ColorFondo, Color colorLetra, JButton... botones) {
        for (JButton boton : botones) {
            boton.setBackground(ColorFondo);
            boton.setForeground(colorLetra);
        }
    }

    private void eventos() {
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                modoEdicion(true);
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (registroActual.getId() == Controlador.SIN_CREAR) {
                    mostrarMensaje("No existe todavia, no se puede eliminar.");
                } else {
                    if (pedirConfirmacion("¿Deseas eliminar este proyecto?")) {
                        controlador.accionEliminar(registroActual.getId(), VistaEdicion.this);
                    }
                }
            }
        });

        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (controlador == null) {
                    System.out.println("ES NULL");
                } else {
                    controlador.accionVolverCerrar();
                    VistaEdicion.this.dispose();
                }

            }
        });

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                refrescarRegistroActual();
                if (isTituloVacio()) {
                    txtTitulo.setBackground(colorCancelar);
                    mostrarMensaje("Pon un titulo al proyecto");
                } else {
                    controlador.accionGuardar(registroActual, VistaEdicion.this);
                }
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                modoEdicion(false);
                refrescarCampos();
            }
        });
        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                controlador.accionVolverCerrar();
            }
        });

    }

    private boolean isTituloVacio() {
        return txtTitulo.getText().trim().isEmpty();
    }

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

    private JPanel generarPanelCampos(JComponent... elementos) {
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new GridLayout(elementos.length, 1, 0, 15));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(margen, margen, margen, margen));

        for (JComponent com : elementos) {
            panelCampos.add(com);
        }

        return panelCampos;
    }

    public void setRegistroActual(POJORegistro registroActual) {
        this.registroActual = registroActual;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean pedirConfirmacion(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(this, mensaje, "Confirmar", JOptionPane.INFORMATION_MESSAGE);
        return respuesta == JOptionPane.YES_OPTION;
    }

    public void cerrarVentana() {
        this.dispose();
    }
    
    
}
