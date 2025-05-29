package com.app.Controller;

import com.app.Model.Model;
import com.app.View.View;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    private View vista;
    private Model modelo;

    public Controller(View vista, Model modelo) {
        this.vista = vista;
        this.modelo = modelo;

        this.vista.btnEjecutar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarQuery();
            }
        });
    }

    public void iniciar() {
        vista.setVisible(true);
        try {
            modelo.conectar("jdbc:mysql://localhost:3306/tu_base_datos", "axel", "0101");
            vista.lblEstado.setText("Conexión exitosa.");
        } catch (Exception e) {
            vista.lblEstado.setText("Error de conexión: " + e.getMessage());
        }
    }

    private void ejecutarQuery() {
        String query = vista.txtQuery.getText();
        try {
            DefaultTableModel modeloTabla = modelo.ejecutarConsulta(query);
            vista.tablaResultados.setModel(modeloTabla);
            vista.lblEstado.setText("Consulta ejecutada correctamente.");
        } catch (Exception ex) {
            vista.lblEstado.setText("Error en ejecución: " + ex.getMessage());
        }
    }
}