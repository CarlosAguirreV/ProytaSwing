package vista;

import static modelo.Constantes.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.net.URI;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import static modelo.Diccionario.*;

/**
 * Ventana que contiene informacion del proyecto.
 *
 * @author Carlos Aguirre
 */
public final class VistaAcerca extends JDialog {

    // ################### CAMPOS ###################
    private JPanel pnlGlobal, pnlSur;
    private JLabel lblLogo, lblAcerca;
    private JButton btnCerrar, btnDonar, btnGitHub;

    private final Color COLOR_BACK = new Color(254, 249, 251);
    private final Color COLOR_DONAR = new Color(24, 85, 130);
    private final Color COLOR_GITHUB = new Color(38, 41, 46);
    private final Color COLOR_CERRAR = new Color(54, 2, 21);

    private final Font FUENTE_NEGRITA = new Font("Default", Font.BOLD, 15);

    // ########################## CONSTRUCTOR ##########################
    public VistaAcerca(VistaTabla vistaPadre) {
        super(vistaPadre, ACERCA_DE);

        // Definir que es una aplicacion modal.
        setModalityType(ModalityType.APPLICATION_MODAL);

        // Pasos para la creacion de la ventana.
        this.crearElementos();
        this.crearDistribucion();
        this.colocarElementos();
        this.definirEstilos();
        this.eventos();

        // Propiedades de la ventana.
        this.setResizable(false);
        this.definirTamanioVentana(360, 360);
        this.setLocationRelativeTo(null);
        super.setVisible(true);
    }

    // ########################## METODOS ##########################
    protected void crearElementos() {
        pnlGlobal = new JPanel();
        pnlSur = new JPanel();

        lblLogo = new JLabel(new ImageIcon(getClass().getResource("/recursos/logo.png")));
        lblAcerca = new JLabel(ACERCA);

        btnCerrar = new JButton(CERRAR);
        btnDonar = new JButton(INVITAR_CAFE);
        btnGitHub = new JButton(GIT_HUB);
    }

    protected void crearDistribucion() {
        pnlGlobal.setLayout(new BorderLayout());
        pnlSur.setLayout(new GridLayout(1, 3, 5, 0));
    }

    protected void colocarElementos() {
        super.getContentPane().add(pnlGlobal);

        pnlGlobal.add(lblLogo, BorderLayout.NORTH);
        pnlGlobal.add(lblAcerca, BorderLayout.CENTER);
        pnlGlobal.add(pnlSur, BorderLayout.SOUTH);

        pnlSur.add(btnCerrar);
        pnlSur.add(btnDonar);
        pnlSur.add(btnGitHub);
    }

    protected void definirEstilos() {
        // Poner un icono.
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(VistaEdicion.class.getResource("/recursos/icono4.png")));

        btnCerrar.setIcon(new ImageIcon(getClass().getResource("/recursos/cerrar.png")));
        btnDonar.setIcon(new ImageIcon(getClass().getResource("/recursos/donar.png")));
        btnGitHub.setIcon(new ImageIcon(getClass().getResource("/recursos/github.png")));

        pnlGlobal.setBackground(COLOR_BACK);
        btnCerrar.setBackground(COLOR_CERRAR);
        btnCerrar.setForeground(COLOR_BACK);
        btnDonar.setBackground(COLOR_DONAR);
        btnDonar.setForeground(COLOR_BACK);
        btnGitHub.setBackground(COLOR_GITHUB);
        btnGitHub.setForeground(COLOR_BACK);

        btnCerrar.setFont(FUENTE_NEGRITA);
        btnDonar.setFont(FUENTE_NEGRITA);
        btnGitHub.setFont(FUENTE_NEGRITA);

        lblLogo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        lblAcerca.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnlSur.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorderPainted(false);
        btnDonar.setFocusPainted(false);
        btnDonar.setBorderPainted(false);
        btnGitHub.setFocusPainted(false);
        btnGitHub.setBorderPainted(false);

        pnlSur.setOpaque(false);
    }

    protected void eventos() {
        btnCerrar.addActionListener((ActionEvent e) -> {
            this.dispose();
        });

        btnDonar.addActionListener((ActionEvent e) -> {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI(WEB_DONATIVO));
                } catch (Exception ex) {
                    mostrarLinkWeb(DONATIVO, WEB_DONATIVO);
                }
            }
        });

        btnGitHub.addActionListener((ActionEvent e) -> {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI(WEB_GITHUB));
                } catch (Exception ex) {
                    mostrarLinkWeb(GIT_HUB, WEB_GITHUB);
                }
            }
        });
    }

    private void definirTamanioVentana(double pxAlto, double pxAncho) {
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();

        double altoFinal = pantalla.height * pxAlto / 720;
        double anchoFinal = pantalla.width * pxAncho / 1280;

        setSize(new Dimension((int) anchoFinal, (int) altoFinal));
    }

    private void mostrarLinkWeb(String titulo, String url) {
        JOptionPane.showInputDialog(this, FALLO_NAVEGADOR, titulo, JOptionPane.INFORMATION_MESSAGE, null, null, url);
    }

}
