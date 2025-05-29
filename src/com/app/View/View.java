package com.app.View;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame {
    public JTextArea txtQuery;
    public JButton btnEjecutar;
    public JTable tablaResultados;
    public JLabel lblEstado;

    public View() {
        setTitle("Ejecutor de Queries SQL");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        txtQuery = new JTextArea(5, 40);
        btnEjecutar = new JButton("Ejecutar");
        tablaResultados = new JTable();
        lblEstado = new JLabel("Listo");

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.add(new JScrollPane(txtQuery), BorderLayout.CENTER);
        panelTop.add(btnEjecutar, BorderLayout.EAST);

        add(panelTop, BorderLayout.NORTH);
        add(new JScrollPane(tablaResultados), BorderLayout.CENTER);
        add(lblEstado, BorderLayout.SOUTH);
    }
}