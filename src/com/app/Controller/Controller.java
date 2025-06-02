package com.app.Controller;

import com.app.Model.Model;
import com.app.View.SqlEditorView;
import com.app.View.View;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Controlador principal de la aplicación que gestiona la interacción
 * entre las vistas (login y editor SQL) y el modelo de datos.
 * 
 * <p>Define la lógica de conexión, validación, ejecución de consultas SQL y actualización de datos.</p>
 * 
 * @author [Tu Nombre]
 */
public class Controller {
    private final View loginView;
    private final SqlEditorView editorView;
    private final Model modelo;

    /**
     * Constructor del controlador que inicializa las vistas y el modelo.
     * También configura los listeners para los componentes gráficos.
     *
     * @param loginView la vista de inicio de sesión
     * @param editorView la vista del editor SQL
     * @param model el modelo de acceso a datos
     */
    public Controller(View loginView, SqlEditorView editorView, Model model) {
        this.loginView = loginView;
        this.editorView = editorView;
        this.modelo = model;

        configurarListeners();
        loginView.setVisible(true);
    }

    /**
     * Configura todos los listeners de los botones en las vistas.
     * Define el comportamiento de la interfaz de usuario.
     */
    private void configurarListeners() {
        loginView.getBtnActualizarBases().addActionListener(e -> {
            loginView.limpiarEstado();
            validarCamposLogin(true);
            cargarBasesDatos();
        });

        loginView.getBtnConectar().addActionListener(e -> {
            loginView.limpiarEstado();
            if(validarCamposLogin(false)) {
                conectarABaseDatos();
            }
        });

        loginView.getBtnSalir().addActionListener(e -> System.exit(0));

        editorView.getBtnEjecutar().addActionListener(e -> ejecutarConsulta());
        editorView.getBtnLimpiar().addActionListener(e -> limpiarEditor());
        editorView.getBtnRefrescarTablas().addActionListener(e -> refrescarTablas());

        editorView.getBtnCambiarBD().addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(editorView,
                "¿Desea desconectarse y cambiar de base de datos?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                desconectar();
            }
        });
    }

    /**
     * Valida los campos del formulario de login.
     *
     * @param soloCredenciales si es true, se valida solo usuario y contraseña; si es false, también se valida la base de datos seleccionada
     * @return true si los campos son válidos, false en caso contrario
     */
    private boolean validarCamposLogin(boolean soloCredenciales) {
        if(loginView.getUsuario().isEmpty()) {
            loginView.mostrarError("El usuario es requerido");
            return false;
        }

        if(loginView.getPassword().isEmpty()) {
            loginView.mostrarError("La contraseña es requerida");
            return false;
        }

        if(!soloCredenciales && loginView.getBaseDatosSeleccionada() == null) {
            loginView.mostrarError("Debe seleccionar una base de datos");
            return false;
        }

        return true;
    }

    /**
     * Carga la lista de bases de datos disponibles desde el servidor.
     * Utiliza un SwingWorker para realizar la operación en segundo plano.
     */
    private void cargarBasesDatos() {
        if(!validarCamposLogin(true)) return;

        loginView.bloquearInterfaz(true);

        SwingWorker<List<String>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<String> doInBackground() throws Exception {
                return modelo.obtenerTodasLasBasesDatos(
                    loginView.getServidor(),
                    loginView.getUsuario(),
                    loginView.getPassword()
                );
            }

            @Override
            protected void done() {
                loginView.bloquearInterfaz(false);
                try {
                    List<String> bases = get();

                    if (bases.isEmpty()) {
                        loginView.mostrarError("No se encontraron bases de datos");
                    } else {
                        loginView.setBasesDatos(bases);
                        JOptionPane.showMessageDialog(loginView,
                            "Bases de datos actualizadas correctamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    loginView.mostrarError("Error: " + ex.getMessage());
                }
            }
        };

        worker.execute();
    }

    /**
     * Conecta a la base de datos seleccionada usando los datos del formulario de login.
     * También actualiza la interfaz para mostrar el editor SQL y sus tablas.
     */
    private void conectarABaseDatos() {
        loginView.bloquearInterfaz(true);

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                modelo.conectar(
                    loginView.getServidor(),
                    loginView.getBaseDatosSeleccionada(),
                    loginView.getUsuario(),
                    loginView.getPassword()
                );
                return true;
            }

            @Override
            protected void done() {
                loginView.bloquearInterfaz(false);
                try {
                    get();
                    String nombreBD = loginView.getBaseDatosSeleccionada();
                    editorView.setBaseDeDatos(nombreBD);

                    List<String> tablas = modelo.obtenerTablasDeBaseDatos();
                    editorView.actualizarListaTablas(tablas);

                    loginView.setVisible(false);
                    editorView.setVisible(true);
                } catch (Exception ex) {
                    editorView.setBaseDeDatos(null);
                    editorView.actualizarListaTablas(Collections.emptyList());
                    loginView.mostrarError("Error de conexión: " + ex.getMessage());
                }
            }
        };

        worker.execute();
    }

    /**
     * Refresca la lista de tablas disponibles en la base de datos actual.
     * Se ejecuta en segundo plano usando SwingWorker.
     */
    private void refrescarTablas() {
        SwingWorker<List<String>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<String> doInBackground() throws Exception {
                return modelo.obtenerTablasDeBaseDatos();
            }

            @Override
            protected void done() {
                try {
                    List<String> tablas = get();
                    editorView.actualizarListaTablas(tablas);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(editorView,
                        "Error al obtener tablas: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    /**
     * Desconecta la sesión actual de la base de datos y vuelve a mostrar la vista de login.
     */
    private void desconectar() {
        try {
            modelo.desconectar();
            editorView.setBaseDeDatos(null);
            editorView.setVisible(false);
            loginView.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(editorView,
                "Error al desconectar: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Ejecuta la consulta SQL ingresada en el editor.
     * Muestra los resultados en una tabla o un mensaje de error si la consulta falla.
     */
    private void ejecutarConsulta() {
        if(editorView.getConsulta().trim().isEmpty()) {
            JOptionPane.showMessageDialog(editorView,
                "Ingrese una consulta SQL",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SwingWorker<DefaultTableModel, Void> worker = new SwingWorker<>() {
            @Override
            protected DefaultTableModel doInBackground() throws Exception {
                return modelo.ejecutarConsulta(editorView.getConsulta());
            }

            @Override
            protected void done() {
                try {
                    DefaultTableModel modelo = get();
                    editorView.setResultados(modelo);
                    editorView.setMensajeSistema("Consulta ejecutada correctamente");
                } catch (Exception ex) {
                    editorView.setMensajeSistema("Error: " + ex.getMessage());
                    JOptionPane.showMessageDialog(editorView,
                        "Error en la consulta: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    /**
     * Limpia el contenido del editor SQL.
     */
    private void limpiarEditor() {
        editorView.limpiar();
    }
}
