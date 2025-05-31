package com.app.Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class Model {
    private Connection conexion;

    public List<String> obtenerTodasLasBasesDatos(String host, String usuario, String password) throws SQLException {
        List<String> basesDatos = new ArrayList<>();
        String url = "jdbc:mysql://" + host + ":3306/?useSSL=false";
        
        try (Connection conn = DriverManager.getConnection(url, usuario, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SHOW DATABASES")) {
            
            while (rs.next()) {
                String nombreBD = rs.getString(1);
                if (!nombreBD.matches("information_schema|mysql|performance_schema|sys")) {
                    basesDatos.add(nombreBD);
                }
            }
        }
        return basesDatos;
    }

    public void conectar(String host, String database, String usuario, String password) throws SQLException {
        conexion = DriverManager.getConnection(
            "jdbc:mysql://" + host + ":3306/" + database + "?useSSL=false",
            usuario,
            password
        );
    }

    public void desconectar() throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
        }
    }

    public List<String> obtenerTablasDeBaseDatos() throws SQLException {
        List<String> tablas = new ArrayList<>();
        if (conexion != null && !conexion.isClosed()) {
            DatabaseMetaData metaData = conexion.getMetaData();
            try (ResultSet rs = metaData.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (rs.next()) {
                    tablas.add(rs.getString("TABLE_NAME"));
                }
            }
        }
        return tablas;
    }

    public DefaultTableModel ejecutarConsulta(String query) throws SQLException {
        try (Statement stmt = conexion.createStatement()) {
            String trimmedQuery = query.trim().toLowerCase();
    
            if (trimmedQuery.startsWith("select")) {
                try (ResultSet rs = stmt.executeQuery(query)) {
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
                    return modelo;
                }
            } else {
                int filasAfectadas = stmt.executeUpdate(query);
                DefaultTableModel modelo = new DefaultTableModel();
                modelo.addColumn("Resultado");
                modelo.addRow(new Object[] { "Filas afectadas: " + filasAfectadas });
                return modelo;
            }
        }
    }
}