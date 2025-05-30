package com.app.View;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SqlEditorView extends JFrame {
    private JTextArea txtConsulta;
    private JTable tblResultados;
    private JButton btnEjecutar;
    private JButton btnLimpiar;
    private JTextField txtBaseDeDatos;
    private JList<String> listaTablas;
    private DefaultListModel<String> modeloListaTablas;

    public SqlEditorView() {
        setTitle("Editor SQL");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Split pane vertical para dividir consulta y resultados
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.4); // parte superior ocupa 40%
        add(splitPane, BorderLayout.CENTER);

        // Panel superior
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Consulta SQL"));

        // TextArea para consulta SQL
        txtConsulta = new JTextArea(10, 60);
        txtConsulta.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollConsulta = new JScrollPane(txtConsulta);
        panelSuperior.add(scrollConsulta, BorderLayout.CENTER);

        // Panel lateral derecho (botones + BD + tablas)
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));

        // Campo no editable con nombre de la base de datos
        txtBaseDeDatos = new JTextField("Base de datos: [No seleccionada]");
        txtBaseDeDatos.setEditable(false);
        txtBaseDeDatos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        txtBaseDeDatos.setBorder(BorderFactory.createTitledBorder("Base de datos actual"));
        panelDerecho.add(txtBaseDeDatos);

        // Botones
        btnEjecutar = new JButton("Ejecutar");
        btnLimpiar = new JButton("Limpiar");
        btnEjecutar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLimpiar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelDerecho.add(Box.createVerticalStrut(10));
        panelDerecho.add(btnEjecutar);
        panelDerecho.add(Box.createVerticalStrut(10));
        panelDerecho.add(btnLimpiar);

        // Lista de tablas
        modeloListaTablas = new DefaultListModel<>();
        listaTablas = new JList<>(modeloListaTablas);
        JScrollPane scrollTablas = new JScrollPane(listaTablas);
        scrollTablas.setBorder(BorderFactory.createTitledBorder("Tablas en la base de datos"));
        scrollTablas.setPreferredSize(new Dimension(200, 150));
        panelDerecho.add(Box.createVerticalStrut(20));
        panelDerecho.add(scrollTablas);

        // Agregar panel derecho
        panelSuperior.add(panelDerecho, BorderLayout.EAST);

        // Panel inferior: resultados
        tblResultados = new JTable();
        JScrollPane scrollResultados = new JScrollPane(tblResultados);
        scrollResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));

        // Agregar paneles al splitPane
        splitPane.setTopComponent(panelSuperior);
        splitPane.setBottomComponent(scrollResultados);
    }

    // Getters
    public JButton getBtnEjecutar() { return btnEjecutar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public String getConsulta() { return txtConsulta.getText(); }

    // Métodos para actualizar UI
    public void setResultados(javax.swing.table.TableModel model) {
        tblResultados.setModel(model);
    }

    public void limpiar() {
        txtConsulta.setText("");
        tblResultados.setModel(new DefaultTableModel());
    }

    public void setBaseDeDatos(String nombreBD) {
        txtBaseDeDatos.setText("Base de datos: " + nombreBD);
    }

    public void actualizarListaTablas(java.util.List<String> tablas) {
        modeloListaTablas.clear();
        for (String tabla : tablas) {
            modeloListaTablas.addElement(tabla);
        }
    }
}
