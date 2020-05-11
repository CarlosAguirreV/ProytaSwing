package vista;

import controlador.Controlador;
import controlador.Main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
    private JButton btnBuscar, btnNuevoProyecto, btnEditar; // Arriba
    private JComboBox cmbFiltro; // Abajo

    // Margen de los paneles
    private final int margen = 10;

    // Valores del combo box
    private String[] cadenasFiltro; //= {"En proceso", "Pausados", "Terminados", "Todo"};

    // La tabla
    private JTable tabla;

    // Elemento de control
    private Controlador controlador;

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
        this.setTitle("Control proyectos (v" + Main.VERSION_APLICACION + ")");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
    }

    private void crearElementos() {
        pnlGlobal = new JPanel();
        pnlNorte = new JPanel();
        this.tabla = new JTable();
        pnlCentralScroll = new JScrollPane(tabla);
        pnlSur = new JPanel();

        btnBuscar = new JButton("Buscar");
        btnNuevoProyecto = new JButton("Nuevo proyecto");
        btnEditar = new JButton("Ver / Editar");

        // Rellenar el array de cadenas para el filtro (combo box)
        cadenasFiltro = new String[POJOProyecto.CADENAS_ESTADO.length + 1];
        cadenasFiltro[0] = "Todo";

        for (int i = 0; i < POJOProyecto.CADENAS_ESTADO.length; i++) {
            cadenasFiltro[i + 1] = POJOProyecto.CADENAS_ESTADO[i];
            System.out.println("" + POJOProyecto.CADENAS_ESTADO[i]);
        }

        // Asignarle esos valores al combo box
        cmbFiltro = new JComboBox(cadenasFiltro);
        cmbFiltro.setSelectedIndex(0);
    }

    private void definirEstilos() {
        // Poner un icono a la aplicacion
        setIconImage(Toolkit.getDefaultToolkit().getImage(VistaTabla.class.getResource("/recursos/logo.png")));

        // Poner iconos a los botones
        btnBuscar.setIcon(new ImageIcon(getClass().getResource("/recursos/consultar.png")));
        btnNuevoProyecto.setIcon(new ImageIcon(getClass().getResource("/recursos/alta.png")));
        btnEditar.setIcon(new ImageIcon(getClass().getResource("/recursos/modificar.png")));

        // Poner color al fondo y al texto de los botones y combo box
        auxColor(colorBoton, colorLetra, btnBuscar, btnNuevoProyecto, btnEditar, cmbFiltro);

        // Poner color de fondo a los paneles
        pnlNorte.setBackground(colorPanel);
        pnlCentralScroll.setBackground(colorPanelCentral);
        pnlSur.setBackground(colorPanel);

        // Definir los margenes de los paneles
        pnlNorte.setBorder(BorderFactory.createEmptyBorder(margen, margen, margen, margen));
        pnlSur.setBorder(BorderFactory.createEmptyBorder(margen, margen, margen, margen));

        // Definir una fuente para la tabla
        tabla.setFont(fuenteTabla);

        // Para que solo se pueda seleccionar una fila a la vez
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    // Metodo auxiliar que pone color de fondo y de texto a los elementos Swing pasados por parametro
    private void auxColor(Color ColorFondo, Color colorLetra, JComponent... elementosSwing) {
        for (JComponent elemento : elementosSwing) {
            elemento.setBackground(ColorFondo);
            elemento.setForeground(colorLetra);
        }
    }

    private void crearDistribuciones() {
        pnlGlobal.setLayout(new BorderLayout());
        pnlNorte.setLayout(new GridLayout(1, 3, 20, 0));
        pnlSur.setLayout(new BoxLayout(pnlSur, BoxLayout.Y_AXIS));
    }

    private void colocarElementos() {
        getContentPane().add(pnlGlobal);
        pnlGlobal.add(pnlNorte, BorderLayout.NORTH);
        pnlGlobal.add(pnlCentralScroll, BorderLayout.CENTER);
        pnlGlobal.add(pnlSur, BorderLayout.SOUTH);

        pnlNorte.add(btnBuscar);
        pnlNorte.add(btnNuevoProyecto);
        pnlNorte.add(btnEditar);

        pnlSur.add(cmbFiltro);
    }

    private void definirTamanioVentana(double pxAlto, double pxAncho) {
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();

        double altoFinal = pantalla.height * pxAlto / 720;
        double anchoFinal = pantalla.width * pxAncho / 1280;

        setSize(new Dimension((int) anchoFinal, (int) altoFinal));
    }

    private void eventos() {
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                controlador.accionBuscar(mostrarBusqueda());
            }
        });

        btnNuevoProyecto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                controlador.accionNuevoProyecto();
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                int idSeleccionado = getIdRegistroSeleccionado();

                // El -1 ocurre si no hay una fila seleccionada
                if (idSeleccionado == -1) {
                    mostrarMensaje("Selecciona una fila antes de editar.");
                } else {
                    controlador.accionEditar(idSeleccionado);
                }
            }
        });

        // Al hacer clic en la tabla
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                // Esto permite detectar los dobles click
                if (evt.getClickCount() == 2 && tabla.getSelectedRow() != -1) {
                    controlador.accionEditar(getIdRegistroSeleccionado());
                }
            }
        });

        // Al cambiar algun valor del combo box
        cmbFiltro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                controlador.accionFiltrar(getFiltroSeleccionado());
            }
        });

        // Al cerrar la ventana
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                controlador.accionCerrarAplicacion();
            }
        });

    }

    // Obtiene el id de proyecto de la fila seleccionada, -1 es que no hay fila seleccionada
    private int getIdRegistroSeleccionado() {
        int numFila = tabla.getSelectedRow();
        int idRegistro = 0;

        if (numFila != -1) {
            idRegistro = (Integer) tabla.getValueAt(numFila, 0);
        } else {
            idRegistro = -1;
        }

        return idRegistro;
    }

    // Muestra el cuadro de dialogo de buscar, retorna lo que se ha escrito, si se cierra sin mas retorna null
    private String mostrarBusqueda() {
        return JOptionPane.showInputDialog(this, "Titulo o parte del titulo", "Busqueda", JOptionPane.INFORMATION_MESSAGE);
    }

    // Muestra un mensaje en esta ventana
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
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
        columnas.add("ID");
        columnas.add("Titulo");
        columnas.add("Estado");
        columnas.add("Prioridad");

        Vector filas = new Vector();
        POJOProyecto registroActual;
        Vector registroTemporal;
        for (int i = 0; i < registros.size(); i++) {
            registroTemporal = new Vector();

            registroActual = registros.get(i);
            registroTemporal.add(registroActual.getId());
            registroTemporal.add(registroActual.getTitulo());
            registroTemporal.add(POJOProyecto.CADENAS_ESTADO[registroActual.getEstado()]);
            registroTemporal.add(POJOProyecto.CADENAS_PRIORIDAD[registroActual.getPrioridad()]);

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
