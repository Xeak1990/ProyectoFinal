package com.app.View;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Vista principal que permite establecer la conexión con una base de datos MySQL.
 */
public class View extends JFrame {

    // Componentes de la interfaz de usuario
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbServidores;
    private JComboBox<String> cbBasesDatos;
    private JButton btnConectar;
    private JButton btnSalir;
    private JButton btnActualizarBases;
    private JLabel lblEstado;
    private JLabel lblTitulo;

    // Colores personalizados para la UI
    private final Color COLOR_FONDO = new Color(245, 245, 245);
    private final Color COLOR_PRIMARIO = new Color(0, 120, 215);
    private final Color COLOR_SECUNDARIO = new Color(100, 180, 255);
    private final Color COLOR_TEXTO = new Color(60, 60, 60);
    private final Color COLOR_ERROR = new Color(220, 50, 50);

    /**
     * Constructor que configura y construye la interfaz gráfica.
     */
    public View() {
        setTitle("Conexión a MySQL");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);

        // Configuración del panel principal con layout y bordes
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(new EmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Agrega título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        lblTitulo = new JLabel("CONEXIÓN A MYSQL");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(COLOR_PRIMARIO);
        panelPrincipal.add(lblTitulo, gbc);

        // Campo para seleccionar el servidor
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

        // Campo de usuario
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

        // Campo de contraseña
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

        // Campo para seleccionar base de datos con botón para actualizar
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

        // Etiqueta de estado (errores, mensajes)
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        lblEstado = new JLabel(" ", JLabel.CENTER);
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEstado.setForeground(COLOR_ERROR);
        panelPrincipal.add(lblEstado, gbc);

        // Botones de acción (Conectar y Salir)
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

    /**
     * Aplica estilos personalizados a los botones, incluyendo colores y efectos hover.
     * 
     * @param boton El botón a estilizar.
     * @param primario Indica si el estilo es de botón principal.
     */
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

        // Efecto hover al pasar el cursor sobre el botón
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(primario ? COLOR_PRIMARIO.darker() : COLOR_SECUNDARIO.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(primario ? COLOR_PRIMARIO : COLOR_SECUNDARIO);
            }
        });
    }

    // Métodos Getters

    /** @return Texto ingresado como nombre de usuario. */
    public String getUsuario() { return txtUsuario.getText().trim(); }

    /** @return Texto ingresado como contraseña. */
    public String getPassword() { return new String(txtPassword.getPassword()).trim(); }

    /** @return Servidor seleccionado en el combo box. */
    public String getServidor() { return (String) cmbServidores.getSelectedItem(); }

    /** @return Base de datos seleccionada. */
    public String getBaseDatosSeleccionada() { return (String) cbBasesDatos.getSelectedItem(); }

    /** @return Botón para conectar. */
    public JButton getBtnConectar() { return btnConectar; }

    /** @return Botón para salir. */
    public JButton getBtnSalir() { return btnSalir; }

    /** @return Botón para actualizar la lista de bases de datos. */
    public JButton getBtnActualizarBases() { return btnActualizarBases; }

    /**
     * Establece la lista de bases de datos en el combo box.
     * 
     * @param bases Lista de nombres de bases de datos.
     */
    public void setBasesDatos(List<String> bases) {
        cbBasesDatos.removeAllItems();
        bases.forEach(cbBasesDatos::addItem);
    }

    /**
     * Muestra un mensaje de error en la interfaz.
     * 
     * @param mensaje Texto del mensaje de error.
     */
    public void mostrarError(String mensaje) {
        lblEstado.setText(mensaje);
    }

    /**
     * Limpia el mensaje de estado.
     */
    public void limpiarEstado() {
        lblEstado.setText(" ");
    }

    /**
     * Habilita o deshabilita los componentes de la interfaz de usuario.
     * 
     * @param bloquear true para deshabilitar, false para habilitar.
     */
    public void bloquearInterfaz(boolean bloquear) {
        cmbServidores.setEnabled(!bloquear);
        txtUsuario.setEnabled(!bloquear);
        txtPassword.setEnabled(!bloquear);
        cbBasesDatos.setEnabled(!bloquear);
        btnActualizarBases.setEnabled(!bloquear);
        btnConectar.setEnabled(!bloquear);

        Color bg = bloquear ? new Color(240, 240, 240) : Color.WHITE;
        cmbServidores.setBackground(bg);
        txtUsuario.setBackground(bg);
        txtPassword.setBackground(bg);
        cbBasesDatos.setBackground(bg);
    }
}
