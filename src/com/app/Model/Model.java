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
            String catalogoActual = conexion.getCatalog(); // O el nombre de la BD usada en la conexión
            try (ResultSet rs = metaData.getTables(catalogoActual, null, "%", new String[]{"TABLE"})) {
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
            DefaultTableModel modelo = new DefaultTableModel();

            if (trimmedQuery.startsWith("select")) {
                    try (ResultSet rs = stmt.executeQuery(query)) {
                    ResultSetMetaData meta = rs.getMetaData();
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
                }
            } else {
                int filasAfectadas = stmt.executeUpdate(query);

                String tabla = extraerNombreTabla(trimmedQuery);

                if (tabla != null && !tabla.isEmpty()) {
                    try (ResultSet rs = stmt.executeQuery("SELECT * FROM " + tabla)) {
                        ResultSetMetaData meta = rs.getMetaData();
                        int columnas = meta.getColumnCount();

                        for (int i = 1; i <= columnas; i++) {
                            modelo.addColumn(meta.getColumnName(i));
                        }

                        boolean tieneDatos = false;
                        while (rs.next()) {
                            tieneDatos = true;
                            Object[] fila = new Object[columnas];
                            for (int i = 0; i < columnas; i++) {
                                fila[i] = rs.getObject(i + 1);
                            }
                            modelo.addRow(fila);
                        }
                        if (!tieneDatos) {
                        }
                    }
                } else {
                    modelo.addColumn("Mensaje del sistema");
                    modelo.addRow(new Object[] { "Filas afectadas: " + filasAfectadas });
                }
            }

            return modelo;
        }
    }

    private String extraerNombreTabla(String query) {
        // Esta es una versión simple, asume formato básico:
        if (query.startsWith("create table")) {
            String[] partes = query.split("\\s+");
            if (partes.length >= 3) {
                return partes[2].replaceAll("`", "");
            }
        } else if (query.startsWith("insert into")) {
            String[] partes = query.split("\\s+");
            if (partes.length >= 3) {
                return partes[2].replaceAll("`", "");
            }
        } else if (query.startsWith("update")) {
            String[] partes = query.split("\\s+");
            if (partes.length >= 2) {
                return partes[1].replaceAll("`", "");
            }
        } else if (query.startsWith("delete from")) {
            String[] partes = query.split("\\s+");
            if (partes.length >= 3) {
                return partes[2].replaceAll("`", "");
            }
        } else if (query.startsWith("truncate table")) {
            String[] partes = query.split("\\s+");
            if (partes.length >= 3) {
                return partes[2].replaceAll("`", "");
            }
        }
        return null;
    }
}
