package com.app.Model;

import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class Model {
    private Connection conexion;

    public void conectar(String url, String usuario, String contrasena) throws SQLException {
        conexion = DriverManager.getConnection(url, usuario, contrasena);
    }

    public DefaultTableModel ejecutarConsulta(String query) throws SQLException {
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ResultSetMetaData meta = rs.getMetaData();

        DefaultTableModel modelo = new DefaultTableModel();
        int columnas = meta.getColumnCount();
        for (int i = 1; i <= columnas; i++) {
            modelo.addColumn(meta.getColumnName(i));
        }

        while (rs.next()) {
            Object[] fila = new Object[columnas];
            for (int i = 0; i < columnas; i++) {
                fila[i] = rs.getObject(i + 1);
            }
            modelo.addRow(fila);
        }

        rs.close();
        stmt.close();
        return modelo;
    }
}