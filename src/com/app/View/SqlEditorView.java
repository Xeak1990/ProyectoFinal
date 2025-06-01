package com.app.View;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class SqlEditorView extends JFrame {
    private JTextArea txtConsulta;
    private JTable tblResultados;
    private JButton btnEjecutar;
    private JButton btnLimpiar;
    private JButton btnRefrescarTablas;
    private JTextField txtBaseDeDatos;
    private JList<String> listaTablas;
    private DefaultListModel<String> modeloListaTablas;
    private JLabel lblMensajeSistema;
    private JButton btnCambiarBD;
    private JButton btnActualizarBD;

    private final Color COLOR_FONDO = new Color(245, 245, 245);
    private final Color COLOR_PRIMARIO = new Color(0, 120, 215);
    private final Color COLOR_SECUNDARIO = new Color(100, 180, 255);
    private final Color COLOR_TEXTO = new Color(60, 60, 60);
    private final Color COLOR_BORDE = new Color(200, 200, 200);
    private final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font FUENTE_MONOSPACE = new Font("Consolas", Font.PLAIN, 14);
    private final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 14);

    public SqlEditorView() {
        setTitle("Editor SQL - MySQL Client");
        setSize(950, 670);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.35);
        splitPane.setDividerSize(3);
        splitPane.setBorder(null);
        add(splitPane, BorderLayout.CENTER);

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBackground(COLOR_FONDO);
        panelSuperior.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(10, 10, 5, 10),
            new TitledBorder(createLineBorder(COLOR_BORDE, 1),
            "Editor SQL", TitledBorder.LEFT, TitledBorder.TOP,
            FUENTE_TITULO, COLOR_PRIMARIO)
        ));

        txtConsulta = new JTextArea(10, 60);
        txtConsulta.setFont(FUENTE_MONOSPACE);
        txtConsulta.setTabSize(2);
        txtConsulta.setLineWrap(true);
        txtConsulta.setWrapStyleWord(true);
        txtConsulta.setBorder(new EmptyBorder(5, 5, 5, 5));

        JScrollPane scrollConsulta = new JScrollPane(txtConsulta);
        scrollConsulta.setBorder(null);
        panelSuperior.add(scrollConsulta, BorderLayout.CENTER);

        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBackground(COLOR_FONDO);
        panelDerecho.setBorder(new EmptyBorder(0, 10, 0, 0));

        txtBaseDeDatos = new JTextField();
        txtBaseDeDatos.setEditable(false);
        txtBaseDeDatos.setFont(FUENTE_NORMAL);
        txtBaseDeDatos.setBackground(new Color(230, 240, 255));
        txtBaseDeDatos.setBorder(BorderFactory.createCompoundBorder(
            createLineBorder(COLOR_BORDE, 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        txtBaseDeDatos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        setBaseDeDatos(null);
        panelDerecho.add(txtBaseDeDatos);
        panelDerecho.add(Box.createVerticalStrut(10));
        panelDerecho.add(Box.createVerticalStrut(10));

        // Panel para botones de cambio de BD
        JPanel panelCambioBD = new JPanel(new GridLayout(1, 2, 5, 0));
        panelCambioBD.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panelCambioBD.setBackground(COLOR_FONDO);

        btnCambiarBD = new JButton("Cambiar BD");
        estilizarBoton(btnCambiarBD, false);
        panelCambioBD.add(btnCambiarBD);

        panelDerecho.add(panelCambioBD);
        panelDerecho.add(Box.createVerticalStrut(10));

        // Botones principales
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnEjecutar = new JButton("Ejecutar");
        estilizarBoton(btnEjecutar, true);
        btnEjecutar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        btnLimpiar = new JButton("Limpiar");
        estilizarBoton(btnLimpiar, false);
        btnLimpiar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        btnRefrescarTablas = new JButton("Refrescar tablas");
        estilizarBoton(btnRefrescarTablas, false);
        btnRefrescarTablas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        panelBotones.add(btnEjecutar);
        panelBotones.add(Box.createVerticalStrut(10));
        panelBotones.add(btnLimpiar);
        panelBotones.add(Box.createVerticalStrut(10));
        panelBotones.add(btnRefrescarTablas);
        panelDerecho.add(panelBotones);
        panelDerecho.add(Box.createVerticalStrut(20));

        modeloListaTablas = new DefaultListModel<>();
        listaTablas = new JList<>(modeloListaTablas);
        listaTablas.setFont(FUENTE_NORMAL);
        listaTablas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTablas.setBackground(Color.WHITE);
        listaTablas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    String tablaSeleccionada = listaTablas.getSelectedValue();
                    if (tablaSeleccionada != null) {
                        String consulta = "SELECT * FROM " + tablaSeleccionada + " LIMIT 100";
                        txtConsulta.setText(consulta);
                    }
                }
            }
        });

        JScrollPane scrollTablas = new JScrollPane(listaTablas);
        scrollTablas.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder(createLineBorder(COLOR_BORDE, 1),
            "Tablas disponibles", TitledBorder.LEFT, TitledBorder.TOP,
            FUENTE_TITULO, COLOR_PRIMARIO),
            new EmptyBorder(5, 5, 5, 5)
        ));
        scrollTablas.setPreferredSize(new Dimension(220, 200));
        panelDerecho.add(scrollTablas);

        panelSuperior.add(panelDerecho, BorderLayout.EAST);
        splitPane.setTopComponent(panelSuperior);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(COLOR_FONDO);
        panelInferior.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(5, 10, 10, 10),
            new TitledBorder(createLineBorder(COLOR_BORDE, 1),
            "Resultados", TitledBorder.LEFT, TitledBorder.TOP,
            FUENTE_TITULO, COLOR_PRIMARIO)
        ));

        tblResultados = new JTable();
        tblResultados.setFont(FUENTE_NORMAL);
        tblResultados.setRowHeight(22);
        tblResultados.setShowGrid(false);
        tblResultados.setIntercellSpacing(new Dimension(0, 0));
        tblResultados.setFillsViewportHeight(true);

        JScrollPane scrollResultados = new JScrollPane(tblResultados);
        scrollResultados.setBorder(null);

        lblMensajeSistema = new JLabel(" ");
        lblMensajeSistema.setFont(FUENTE_NORMAL);
        lblMensajeSistema.setForeground(Color.DARK_GRAY);
        lblMensajeSistema.setBorder(new EmptyBorder(5, 0, 5, 0));

        panelInferior.add(lblMensajeSistema, BorderLayout.NORTH);
        panelInferior.add(scrollResultados, BorderLayout.CENTER);
        splitPane.setBottomComponent(panelInferior);
    }

    private void estilizarBoton(JButton boton, boolean primario) {
        boton.setFont(FUENTE_NORMAL.deriveFont(Font.BOLD));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (primario) {
            boton.setBackground(COLOR_PRIMARIO);
            boton.setForeground(Color.WHITE);
        } else {
            boton.setBackground(COLOR_SECUNDARIO);
            boton.setForeground(COLOR_TEXTO);
        }
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(primario ? COLOR_PRIMARIO.darker() : COLOR_SECUNDARIO.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(primario ? COLOR_PRIMARIO : COLOR_SECUNDARIO);
            }
        });
    }

    private Border createLineBorder(Color color, int thickness) {
        return BorderFactory.createLineBorder(color, thickness);
    }

    // Getters
    public JButton getBtnEjecutar() { return btnEjecutar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JButton getBtnRefrescarTablas() { return btnRefrescarTablas; }
    public JButton getBtnCambiarBD() { return btnCambiarBD; }
    public JButton getBtnActualizarBD() { return btnActualizarBD; }
    public String getConsulta() { return txtConsulta.getText(); }

    public void setResultados(javax.swing.table.TableModel model) {
        tblResultados.setModel(model);
        for (int i = 0; i < tblResultados.getColumnModel().getColumnCount(); i++) {
            tblResultados.getColumnModel().getColumn(i).setPreferredWidth(150);
        }
    }

    public void setMensajeSistema(String mensaje) {
        lblMensajeSistema.setText(mensaje);
    }

    public void limpiar() {
        txtConsulta.setText("");
        tblResultados.setModel(new DefaultTableModel());
        lblMensajeSistema.setText(" ");
    }

    public void setBaseDeDatos(String nombreBD) {
        if (nombreBD == null || nombreBD.isEmpty()) {
            txtBaseDeDatos.setText("Estado: No conectado");
            txtBaseDeDatos.setToolTipText("No hay conexión activa");
        } else {
            txtBaseDeDatos.setText("Conectado a: " + nombreBD);
            txtBaseDeDatos.setToolTipText("Conexión activa a: " + nombreBD);
        }
    }

    public void actualizarListaTablas(List<String> tablas) {
        modeloListaTablas.clear();
        if (tablas != null) {
            for (String tabla : tablas) {
                modeloListaTablas.addElement(tabla);
            }
        }
        if (tablas == null || tablas.isEmpty()) {
            listaTablas.setToolTipText("No se encontraron tablas");
        } else {
            listaTablas.setToolTipText("Doble clic para generar SELECT");
        }
    }
}
