package com.app.Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 * Clase Model que gestiona la conexión a una base de datos MySQL
 * y la ejecución de consultas SQL. Sirve como capa de acceso a datos.
 */
public class Model {
    /** Conexión activa a la base de datos */
    private Connection conexion;

    /**
     * Obtiene todas las bases de datos disponibles en el servidor,
     * excluyendo las bases de datos del sistema.
     *
     * @param host     Dirección del servidor MySQL.
     * @param usuario  Usuario para autenticar la conexión.
     * @param password Contraseña correspondiente al usuario.
     * @return Lista de nombres de bases de datos accesibles.
     * @throws SQLException si ocurre un error al acceder al servidor.
     */
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

    /**
     * Establece una conexión con una base de datos MySQL específica.
     *
     * @param host     Dirección del servidor.
     * @param database Nombre de la base de datos a la que se desea conectar.
     * @param usuario  Usuario de autenticación.
     * @param password Contraseña del usuario.
     * @throws SQLException si ocurre un error al conectarse.
     */
    public void conectar(String host, String database, String usuario, String password) throws SQLException {
        conexion = DriverManager.getConnection(
            "jdbc:mysql://" + host + ":3306/" + database + "?useSSL=false",
            usuario,
            password
        );
    }

    /**
     * Cierra la conexión actual a la base de datos si está activa.
     *
     * @throws SQLException si ocurre un error al cerrar la conexión.
     */
    public void desconectar() throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
        }
    }

    /**
     * Obtiene la lista de tablas existentes en la base de datos actualmente conectada.
     *
     * @return Lista de nombres de tablas.
     * @throws SQLException si ocurre un error al obtener las tablas.
     */
    public List<String> obtenerTablasDeBaseDatos() throws SQLException {
        List<String> tablas = new ArrayList<>();
        if (conexion != null && !conexion.isClosed()) {
            DatabaseMetaData metaData = conexion.getMetaData();
            String catalogoActual = conexion.getCatalog();
            try (ResultSet rs = metaData.getTables(catalogoActual, null, "%", new String[]{"TABLE"})) {
                while (rs.next()) {
                    tablas.add(rs.getString("TABLE_NAME"));
                }
            }
        }
        return tablas;
    }

    /**
     * Ejecuta una consulta SQL sobre la base de datos conectada.
     * Puede ser una consulta SELECT o una sentencia de modificación (INSERT, UPDATE, etc.).
     *
     * @param query Cadena con la consulta SQL a ejecutar.
     * @return Un DefaultTableModel con los resultados de la consulta o mensaje del sistema.
     * @throws SQLException si ocurre un error durante la ejecución de la consulta.
     */
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
                    }
                } else {
                    modelo.addColumn("Mensaje del sistema");
                    modelo.addRow(new Object[] { "Filas afectadas: " + filasAfectadas });
                }
            }

            return modelo;
        }
    }

    /**
     * Extrae el nombre de una tabla a partir de una consulta SQL básica.
     * Aplica a sentencias como CREATE TABLE, INSERT INTO, UPDATE, DELETE FROM, TRUNCATE TABLE.
     *
     * @param query Consulta SQL en minúsculas y sin espacios iniciales.
     * @return Nombre de la tabla si se puede identificar, de lo contrario null.
     */
    private String extraerNombreTabla(String query) {
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
