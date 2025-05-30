package com.app.View;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SqlEditorView extends JFrame {
    private JTextArea txtConsulta;
    private JTable tblResultados;
    private JButton btnEjecutar;
    private JButton btnLimpiar;

    public SqlEditorView() {
        setTitle("Editor SQL");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());
        
        // Panel superior para la consulta
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Consulta SQL"));
        txtConsulta = new JTextArea();
        txtConsulta.setFont(new Font("Monospaced", Font.PLAIN, 14));
        panelSuperior.add(new JScrollPane(txtConsulta), BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel();
        btnEjecutar = new JButton("Ejecutar");
        btnLimpiar = new JButton("Limpiar");
        panelBotones.add(btnEjecutar);
        panelBotones.add(btnLimpiar);
        
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.NORTH);
        
        // Tabla de resultados
        tblResultados = new JTable();
        JScrollPane scrollResultados = new JScrollPane(tblResultados);
        scrollResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));
        add(scrollResultados, BorderLayout.CENTER);
    }

    // Getters y setters
    public JButton getBtnEjecutar() { return btnEjecutar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public String getConsulta() { return txtConsulta.getText(); }
    
    public void setResultados(javax.swing.table.TableModel model) {
        tblResultados.setModel(model);
    }
    
    // Método para limpiar el editor
    public void limpiar() {
        txtConsulta.setText("");
        tblResultados.setModel(new DefaultTableModel());
    }
}