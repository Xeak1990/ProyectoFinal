package com.app;

import com.app.Controller.Controller;
import com.app.Model.Model;
import com.app.View.View;

public class Main {
    public static void main(String[] args) {
        View vista = new View();
        Model modelo = new Model();
        Controller controlador = new Controller(vista, modelo);
        controlador.iniciar();
    }
}
