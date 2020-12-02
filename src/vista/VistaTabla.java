package vista;

import controlador.Controlador;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import static modelo.Diccionario.*;
import modelo.POJOProyecto;

/**
 * Vista que muestra la tabla con todos los proyectos.
 *
 * @author Carlos Aguirre
 */
public class VistaTabla extends JFrame {

    // Colores de la aplicacion
    private final Color colorPanel = new Color(24, 85, 130);
    private final Color colorPanelCentral = new Color(18, 62, 95);
    private final Color colorBoton = new Color(33, 133, 183);
    private final Color colorLetra = Color.white;

    // Fuente de la aplicacion
    private final Font fuenteTabla = new Font("Default", 1, 15);

    // Elementos Swing
    private JPanel pnlGlobal, pnlNorte, pnlSur;
    private JScrollPane pnlCentralScroll;
    private JButton btnBuscar, btnNuevoProyecto, btnEditar, btnAcerca; // Arriba.
    private JComboBox cmbFiltro; // Abajo
    private JLabel lblVer;

    // Margen de los paneles
    private final int margen = 10;

    // Valores del combo box: "En proceso", "Pausados", "Terminados", "Todo".
    private String[] cadenasFiltro;

    // La tabla
    private JTable tabla;

    // Elemento de control
    private final Controlador controlador;

    // Icono de la aplicacion
    protected final ArrayList<Image> icono;

    // ########################## CONSTRUCTOR PREVIO ##########################
    {
        icono = new ArrayList();
        icono.add(Toolkit.getDefaultToolkit().getImage(VistaTabla.class.getResource("/recursos/icono1.png")));
        icono.add(Toolkit.getDefaultToolkit().getImage(VistaTabla.class.getResource("/recursos/icono2.png")));
        icono.add(Toolkit.getDefaultToolkit().getImage(VistaTabla.class.getResource("/recursos/icono3.png")));
        icono.add(Toolkit.getDefaultToolkit().getImage(VistaTabla.class.getResource("/recursos/icono4.png")));
        this.setIconImages(icono);
        this.setTitle(NOMBRE_APLICACION);
    }

    // ########################## CONSTRUCTOR ##########################
    public VistaTabla(Controlador controlador) {
        this.controlador = controlador;

        // Metodos principales
        this.crearElementos();
        this.definirEstilos();
        this.crearDistribuciones();
        this.colocarElementos();
        this.eventos();

        // Metodos de la ventana
        this.definirTamanioVentana(450, 600);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setResizable(true);
        super.setLocationRelativeTo(null);
    }

    // ########################## METODOS ##########################
    private void crearElementos() {
        pnlGlobal = new JPanel();
        pnlNorte = new JPanel();
        this.tabla = new JTable();
        pnlCentralScroll = new JScrollPane(tabla);
        pnlSur = new JPanel();

        btnBuscar = new JButton(BUSCAR);
        btnNuevoProyecto = new JButton(NUEVO_PROYECTO);
        btnEditar = new JButton(VER_EDITAR);
        btnAcerca = new JButton();
        lblVer = new JLabel(new ImageIcon(getClass().getResource("/recursos/filtro.png")));

        // Rellenar el array de cadenas para el filtro (combo box)
        cadenasFiltro = new String[CADENAS_ESTADO.length + 1];
        cadenasFiltro[0] = TODO;

        // Copiar los elementos que quedan al array
        System.arraycopy(CADENAS_ESTADO, 0, cadenasFiltro, 1, CADENAS_ESTADO.length);

        // Asignarle esos valores al combo box
        cmbFiltro = new JComboBox(cadenasFiltro);
        cmbFiltro.setSelectedIndex(1);
    }

    private void definirEstilos() {

        // Poner iconos a los botones
        btnBuscar.setIcon(new ImageIcon(getClass().getResource("/recursos/consultar.png")));
        btnNuevoProyecto.setIcon(new ImageIcon(getClass().getResource("/recursos/alta.png")));
        btnEditar.setIcon(new ImageIcon(getClass().getResource("/recursos/modificar.png")));
        btnAcerca.setIcon(new ImageIcon(getClass().getResource("/recursos/acerca.png")));

        // Quitar el recuadro de foco al boton.
        btnBuscar.setFocusPainted(false);
        btnNuevoProyecto.setFocusPainted(false);
        btnEditar.setFocusPainted(false);
        btnAcerca.setFocusPainted(false);

        // Poner color al fondo y al texto de los botones y combo box.
        auxColor(colorBoton, colorLetra, btnBuscar, btnNuevoProyecto, btnEditar, cmbFiltro, btnAcerca);

        lblVer.setForeground(colorLetra);

        // Poner color de fondo a los paneles.
        pnlNorte.setBackground(colorPanel);
        pnlCentralScroll.setBackground(colorPanelCentral);
        pnlSur.setBackground(colorPanel);

        // Definir los margenes de los paneles y etiquetas.
        pnlNorte.setBorder(BorderFactory.createEmptyBorder(margen, margen, margen, margen));
        pnlSur.setBorder(BorderFactory.createEmptyBorder(margen, margen, margen, margen));
        lblVer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, margen));

        // Definir una fuente para la tabla.
        tabla.setFont(fuenteTabla);

        // Para que solo se pueda seleccionar una fila a la vez.
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    // Metodo auxiliar que pone color de fondo y de texto a los elementos Swing pasados por parametro.
    private void auxColor(Color ColorFondo, Color colorLetra, JComponent... elementosSwing) {
        for (JComponent elemento : elementosSwing) {
            elemento.setBackground(ColorFondo);
            elemento.setForeground(colorLetra);
        }
    }

    private void crearDistribuciones() {
        pnlGlobal.setLayout(new BorderLayout());
        pnlNorte.setLayout(new GridLayout(1, 3, 20, 0));
        pnlSur.setLayout(new BorderLayout());
    }

    private void colocarElementos() {
        getContentPane().add(pnlGlobal);
        pnlGlobal.add(pnlNorte, BorderLayout.NORTH);
        pnlGlobal.add(pnlCentralScroll, BorderLayout.CENTER);
        pnlGlobal.add(pnlSur, BorderLayout.SOUTH);

        pnlNorte.add(btnBuscar);
        pnlNorte.add(btnNuevoProyecto);
        pnlNorte.add(btnEditar);

        pnlSur.add(lblVer, BorderLayout.WEST);
        pnlSur.add(cmbFiltro, BorderLayout.CENTER);
        pnlSur.add(btnAcerca, BorderLayout.EAST);
    }

    private void definirTamanioVentana(double pxAlto, double pxAncho) {
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();

        double altoFinal = pantalla.height * pxAlto / 720;
        double anchoFinal = pantalla.width * pxAncho / 1280;

        setSize(new Dimension((int) anchoFinal, (int) altoFinal));
    }

    private void eventos() {
        btnBuscar.addActionListener((ActionEvent ae) -> {
            controlador.accionBuscar(mostrarBusqueda());
        });

        btnNuevoProyecto.addActionListener((ActionEvent ae) -> {
            controlador.accionNuevoProyecto();
        });

        btnEditar.addActionListener((ActionEvent ae) -> {
            int idSeleccionado = getIdRegistroSeleccionado();

            // El -1 ocurre si no hay una fila seleccionada.
            if (idSeleccionado == -1) {
                mostrarMensaje(SEL_FILA_ANTES_EDITAR);
            } else {
                controlador.accionEditar(idSeleccionado);
            }
        });

        // Al hacer clic en la tabla.
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                // Esto permite detectar los dobles click.
                if (evt.getClickCount() == 2 && tabla.getSelectedRow() != -1) {
                    controlador.accionEditar(getIdRegistroSeleccionado());
                }
            }
        });

        // Al cambiar algun valor del combo box.
        cmbFiltro.addActionListener((ActionEvent ae) -> {
            controlador.accionFiltrar(getFiltroSeleccionado());
        });

        // Al pulsar en el boton acerca de.
        btnAcerca.addActionListener((ActionEvent ae) -> {
            controlador.accionAcercaDe();
        });

        // Al cerrar la ventana.
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controlador.accionCerrarAplicacion();
            }
        });

    }

    // Obtiene el id de proyecto de la fila seleccionada, -1 es que no hay fila seleccionada
    private int getIdRegistroSeleccionado() {
        int numFila = tabla.getSelectedRow();
        int idRegistro;

        if (numFila != -1) {
            idRegistro = (Integer) tabla.getValueAt(numFila, 0);
        } else {
            idRegistro = -1;
        }

        return idRegistro;
    }

    // Muestra el cuadro de dialogo de buscar, retorna lo que se ha escrito, si se cierra sin mas retorna null
    private String mostrarBusqueda() {
        return JOptionPane.showInputDialog(this, TITULO_PARTE, BUSQUEDA, JOptionPane.INFORMATION_MESSAGE);
    }

    // Muestra un mensaje en esta ventana
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, INFORMACION, JOptionPane.INFORMATION_MESSAGE);
    }

    // Muestra los registros sobre la tabla
    public void mostrarRegistros(ArrayList<POJOProyecto> registros) {
        // Rellenar la tabla para mostrar los resultados
        rellenarTabla(registros);

        // Repintar todo para refrescar, por si acaso
        this.repaint();
    }

    // Metodo que rellena la tabla, lo que hace es cambiar el modelo
    private void rellenarTabla(ArrayList<POJOProyecto> registros) {
        Vector columnas = new Vector();
        columnas.add(ID);
        columnas.add(TITULO);
        columnas.add(ESTADO);
        columnas.add(PRIORIDAD);

        Vector filas = new Vector();
        POJOProyecto registroActual;
        Vector registroTemporal;
        for (int i = 0; i < registros.size(); i++) {
            registroTemporal = new Vector();

            registroActual = registros.get(i);
            registroTemporal.add(registroActual.getId());
            registroTemporal.add(registroActual.getTitulo());
            registroTemporal.add(CADENAS_ESTADO[registroActual.getEstado()]);
            registroTemporal.add(CADENAS_PRIORIDAD[registroActual.getPrioridad()]);

            filas.add(registroTemporal);
        }

        // Modificar para que no se pueda editar las celdas
        DefaultTableModel tableModel = new DefaultTableModel(filas, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Definir el modelo con los datos ya incluidos
        tabla.setModel(tableModel);

        // Para centrar el texto de las celdas
        DefaultTableCellRenderer centrarRender = new DefaultTableCellRenderer();
        centrarRender.setHorizontalAlignment(JLabel.CENTER);
        tabla.getColumnModel().getColumn(0).setCellRenderer(centrarRender);
        tabla.getColumnModel().getColumn(1).setCellRenderer(centrarRender);
        tabla.getColumnModel().getColumn(2).setCellRenderer(centrarRender);
        tabla.getColumnModel().getColumn(3).setCellRenderer(centrarRender);

        // Para poner la columna del id mas pequenia
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(500);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100);

        // Para no permitir reordenar las columnas
        tabla.getTableHeader().setReorderingAllowed(false);
    }

    // Muestra esta vista
    public void mostrarVista(boolean mostrar) {
        this.setVisible(mostrar);
    }

    // Obtiene el indice del combo box del filtro
    public int getFiltroSeleccionado() {
        return cmbFiltro.getSelectedIndex();
    }

}
