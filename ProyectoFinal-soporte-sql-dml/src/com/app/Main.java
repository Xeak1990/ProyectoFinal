package com.app;

import com.app.Controller.Controller;
import com.app.Model.Model;
import com.app.View.View;
import com.app.View.SqlEditorView;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        View loginView = new View();
        SqlEditorView editorView = new SqlEditorView();
        
        editorView.setVisible(false);
        new Controller(loginView, editorView, model);
    }
}