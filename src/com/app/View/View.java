package com.app.View;

import java.awt.*;
import java.util.List;
import javax.swing.*;

public class View extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbServidores;
    private JComboBox<String> cbBasesDatos;
    private JButton btnConectar;
    private JButton btnSalir;
    private JButton btnActualizarBases;
    private JLabel lblEstado;

    public View() {
        setTitle("Conexión a MySQL");
        setSize(500, 350); // Aumentado para incluir estado
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Servidor
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelPrincipal.add(new JLabel("Servidor:"), gbc);

        gbc.gridx = 1;
        cmbServidores = new JComboBox<>(new String[]{"localhost", "127.0.0.1"});
        panelPrincipal.add(cmbServidores, gbc);

        // Usuario
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelPrincipal.add(new JLabel("Usuario:"), gbc);

        gbc.gridx = 1;
        txtUsuario = new JTextField(20);
        panelPrincipal.add(txtUsuario, gbc);

        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelPrincipal.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        panelPrincipal.add(txtPassword, gbc);

        // Base de datos
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelPrincipal.add(new JLabel("Base de datos:"), gbc);

        gbc.gridx = 1;
        JPanel panelBases = new JPanel(new BorderLayout());
        cbBasesDatos = new JComboBox<>();
        panelBases.add(cbBasesDatos, BorderLayout.CENTER);
        
        btnActualizarBases = new JButton("Actualizar");
        panelBases.add(btnActualizarBases, BorderLayout.EAST);
        panelPrincipal.add(panelBases, gbc);

        // Estado
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        lblEstado = new JLabel(" ", JLabel.CENTER);
        lblEstado.setForeground(Color.RED);
        panelPrincipal.add(lblEstado, gbc);

        // Botones
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnConectar = new JButton("Conectar");
        btnSalir = new JButton("Salir");
        panelBotones.add(btnConectar);
        panelBotones.add(btnSalir);
        panelPrincipal.add(panelBotones, gbc);

        add(panelPrincipal);
    }

    // Getters
    public String getUsuario() { return txtUsuario.getText().trim(); }
    public String getPassword() { return new String(txtPassword.getPassword()).trim(); }
    public String getServidor() { return (String) cmbServidores.getSelectedItem(); }
    public String getBaseDatosSeleccionada() { return (String) cbBasesDatos.getSelectedItem(); }
    public JButton getBtnConectar() { return btnConectar; }
    public JButton getBtnSalir() { return btnSalir; }
    public JButton getBtnActualizarBases() { return btnActualizarBases; }

    // Métodos para manejo de UI
    public void setBasesDatos(List<String> bases) {
        cbBasesDatos.removeAllItems();
        bases.forEach(cbBasesDatos::addItem);
    }

    public void mostrarError(String mensaje) {
        lblEstado.setText(mensaje);
    }

    public void limpiarEstado() {
        lblEstado.setText(" ");
    }

    public void bloquearInterfaz(boolean bloquear) {
        cmbServidores.setEnabled(!bloquear);
        txtUsuario.setEnabled(!bloquear);
        txtPassword.setEnabled(!bloquear);
        cbBasesDatos.setEnabled(!bloquear);
        btnActualizarBases.setEnabled(!bloquear);
        btnConectar.setEnabled(!bloquear);
    }
}