package com.app.Controller;

import com.app.Model.Model;
import com.app.View.SqlEditorView;
import com.app.View.View;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Controller {
    private final View loginView;
    private final SqlEditorView editorView;
    private final Model modelo;

    public Controller(View loginView, SqlEditorView editorView, Model model) {
        this.loginView = loginView;
        this.editorView = editorView;
        this.modelo = model;

        configurarListeners();
        loginView.setVisible(true);
    }

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
    }

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

    private void limpiarEditor() {
        editorView.limpiar();
    }
}
