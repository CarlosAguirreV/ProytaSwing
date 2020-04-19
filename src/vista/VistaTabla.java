package vista;

import controlador.Controlador;
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
import modelo.POJORegistro;

/**
 * Vista que muestra la tabla con todos los proyectos.
 *
 * @author Carlos Aguirre
 */
public class VistaTabla extends JFrame {

    private final Color colorPanel = new Color(24, 85, 130);
    private final Color colorPanelCentral = new Color(18, 62, 95);
    private final Color colorBoton = new Color(33, 133, 183);
    private final Color colorLetra = Color.white;

    private final Font fuenteTabla = new Font("Default", 1, 15);

    private JPanel pnlGlobal, pnlNorte, pnlSur;
    private JScrollPane pnlCentralScroll;
    private JButton btnBuscar, btnNuevoProyecto, btnEditar; // Arriba
    private JComboBox cmbFiltro; // Abajo

    private final int margen = 10;

    private ArrayList<POJORegistro> registros;

    private String[] prioridad = {"En proceso", "Pausados", "Terminados", "Todo"};
    private JTable tabla;
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
        this.setTitle("Control proyectos");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
    }

    private void crearElementos() {
        pnlGlobal = new JPanel();

        pnlNorte = new JPanel();
        crearTabla();
        pnlCentralScroll = new JScrollPane(tabla);
        pnlSur = new JPanel();

        btnBuscar = new JButton("Buscar");
        btnNuevoProyecto = new JButton("Nuevo proyecto");
        btnEditar = new JButton("Editar / Ver");
        cmbFiltro = new JComboBox(prioridad);
        cmbFiltro.setSelectedIndex(3);
    }

    private void crearTabla() {
        this.tabla = new JTable();

        // Para que solo se pueda seleccionar una fila a la vez
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void definirEstilos() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(VistaTabla.class.getResource("/recursos/bd.png")));
        btnBuscar.setIcon(new ImageIcon(getClass().getResource("/recursos/consultar.png")));
        btnNuevoProyecto.setIcon(new ImageIcon(getClass().getResource("/recursos/alta.png")));
        btnEditar.setIcon(new ImageIcon(getClass().getResource("/recursos/modificar.png")));

        auxColor(colorBoton, colorLetra, btnBuscar, btnNuevoProyecto, btnEditar, cmbFiltro);

        pnlNorte.setBackground(colorPanel);
        pnlCentralScroll.setBackground(colorPanelCentral);
        pnlSur.setBackground(colorPanel);

        pnlNorte.setBorder(BorderFactory.createEmptyBorder(margen, margen, margen, margen));
        pnlSur.setBorder(BorderFactory.createEmptyBorder(margen, margen, margen, margen));

        tabla.setFont(fuenteTabla);
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

    private void auxColor(Color ColorFondo, Color colorLetra, JComponent... elementosSwing) {
        for (JComponent elemento : elementosSwing) {
            elemento.setBackground(ColorFondo);
            elemento.setForeground(colorLetra);
        }
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

                if (idSeleccionado == -1) {
                    mostrarMensaje("Selecciona una fila antes de editar.");
                } else {
                    controlador.accionEditar(idSeleccionado);
                }
            }
        });

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if(evt.getClickCount() == 2 && tabla.getSelectedRow() != -1){
                    controlador.accionEditar(getIdRegistroSeleccionado());
                }
            }
        });

        cmbFiltro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                controlador.accionFiltrar(getFiltroSeleccionado());
            }
        });

    }

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

    private String mostrarBusqueda() {
        return JOptionPane.showInputDialog(this, "Titulo o parte del titulo", "Busqueda", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void rellenarTabla(ArrayList<POJORegistro> registros) {
        Vector columnas = new Vector();
        columnas.add("ID");
        columnas.add("Titulo");
        columnas.add("Estado");
        columnas.add("Prioridad");

        Vector filas = new Vector();
        POJORegistro registroActual;
        Vector registroTemporal;
        for (int i = 0; i < registros.size(); i++) {
            registroTemporal = new Vector();

            registroActual = registros.get(i);
            registroTemporal.add(registroActual.getId());
            registroTemporal.add(registroActual.getTitulo());
            registroTemporal.add(Controlador.CADENAS_ESTADO[registroActual.getEstado()]);
            registroTemporal.add(Controlador.CADENAS_PRIORIDAD[registroActual.getPrioridad()]);

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

    public void mostrarVista(boolean mostrar) {
        this.setVisible(mostrar);
    }

    public void mostrarRegistros(ArrayList<POJORegistro> registros) {
        // Rellenar la tabla para mostrar los resultados
        rellenarTabla(registros);

        // Repintar todo para refrescar, por si acaso
        this.repaint();
    }

    public int getFiltroSeleccionado() {
        return cmbFiltro.getSelectedIndex();
    }

}
