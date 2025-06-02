package com.app;

import com.app.Controller.Controller;
import com.app.Model.Model;
import com.app.View.View;
import com.app.View.SqlEditorView;

/**
 * Clase principal que actúa como punto de entrada para la aplicación.
 * 
 * <p>Inicializa el modelo, la vista de inicio de sesión y la vista del editor SQL,
 * configura la visibilidad inicial de las vistas y crea el controlador que gestiona
 * la interacción entre modelo y vistas.</p>
 */
public class Main {

    /**
     * Método principal que inicia la ejecución de la aplicación.
     *
     * @param args los argumentos de línea de comandos (no utilizados en esta implementación)
     */
    public static void main(String[] args) {
        // Se instancia el modelo de la aplicación
        Model model = new Model();

        // Se instancia la vista principal (por ejemplo, de inicio de sesión)
        View loginView = new View();

        // Se instancia la vista del editor SQL
        SqlEditorView editorView = new SqlEditorView();

        // Se oculta inicialmente la vista del editor SQL
        editorView.setVisible(false);

        // Se crea el controlador, que conecta el modelo y las vistas
        new Controller(loginView, editorView, model);
    }
}
