package com.app.View;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class View extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbServidores;
    private JComboBox<String> cbBasesDatos;
    private JButton btnConectar;
    private JButton btnSalir;
    private JButton btnActualizarBases;
    private JLabel lblEstado;
    private JLabel lblTitulo;

    // Colores
    private final Color COLOR_FONDO = new Color(245, 245, 245);
    private final Color COLOR_PRIMARIO = new Color(0, 120, 215);
    private final Color COLOR_SECUNDARIO = new Color(100, 180, 255);
    private final Color COLOR_TEXTO = new Color(60, 60, 60);
    private final Color COLOR_ERROR = new Color(220, 50, 50);

    public View() {
        setTitle("Conexión a MySQL");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        
        // Panel principal con borde y margen
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(new EmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        lblTitulo = new JLabel("CONEXIÓN A MYSQL");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(COLOR_PRIMARIO);
        panelPrincipal.add(lblTitulo, gbc);

        // Servidor
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JLabel lblServidor = new JLabel("Servidor:");
        lblServidor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelPrincipal.add(lblServidor, gbc);

        gbc.gridx = 1;
        cmbServidores = new JComboBox<>(new String[]{"localhost", "127.0.0.1"});
        cmbServidores.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbServidores.setBackground(Color.WHITE);
        panelPrincipal.add(cmbServidores, gbc);

        // Usuario
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelPrincipal.add(lblUsuario, gbc);

        gbc.gridx = 1;
        txtUsuario = new JTextField(20);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtUsuario.setBackground(Color.WHITE);
        panelPrincipal.add(txtUsuario, gbc);

        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelPrincipal.add(lblPassword, gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPassword.setBackground(Color.WHITE);
        panelPrincipal.add(txtPassword, gbc);

        // Base de datos
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel lblBaseDatos = new JLabel("Base de datos:");
        lblBaseDatos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelPrincipal.add(lblBaseDatos, gbc);

        gbc.gridx = 1;
        JPanel panelBases = new JPanel(new BorderLayout());
        panelBases.setBackground(COLOR_FONDO);
        cbBasesDatos = new JComboBox<>();
        cbBasesDatos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbBasesDatos.setBackground(Color.WHITE);
        panelBases.add(cbBasesDatos, BorderLayout.CENTER);
        
        btnActualizarBases = new JButton("Actualizar");
        estilizarBoton(btnActualizarBases, false);
        panelBases.add(btnActualizarBases, BorderLayout.EAST);
        panelPrincipal.add(panelBases, gbc);

        // Estado
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        lblEstado = new JLabel(" ", JLabel.CENTER);
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEstado.setForeground(COLOR_ERROR);
        panelPrincipal.add(lblEstado, gbc);

        // Botones
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(COLOR_FONDO);
        
        btnConectar = new JButton("Conectar");
        estilizarBoton(btnConectar, true);
        
        btnSalir = new JButton("Salir");
        estilizarBoton(btnSalir, false);
        
        panelBotones.add(btnConectar);
        panelBotones.add(btnSalir);
        panelPrincipal.add(panelBotones, gbc);

        add(panelPrincipal);
    }

    private void estilizarBoton(JButton boton, boolean primario) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        
        if (primario) {
            boton.setBackground(COLOR_PRIMARIO);
            boton.setForeground(Color.WHITE);
            boton.setPreferredSize(new Dimension(120, 35));
        } else {
            boton.setBackground(COLOR_SECUNDARIO);
            boton.setForeground(COLOR_TEXTO);
            boton.setPreferredSize(new Dimension(100, 35));
        }
        
        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(primario ? COLOR_PRIMARIO.darker() : COLOR_SECUNDARIO.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(primario ? COLOR_PRIMARIO : COLOR_SECUNDARIO);
            }
        });
    }

    // Getters (se mantienen igual)
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
        
        // Cambiar apariencia cuando está deshabilitado
        Color bg = bloquear ? new Color(240, 240, 240) : Color.WHITE;
        cmbServidores.setBackground(bg);
        txtUsuario.setBackground(bg);
        txtPassword.setBackground(bg);
        cbBasesDatos.setBackground(bg);
    }
}