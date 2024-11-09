package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import domain.Album;
import domain.Artista;

import javax.swing.JPasswordField;
import javax.swing.JDialog;

public class VentanaIdentificacion extends JFrame {
    private static final long serialVersionUID = 1L;
    private JButton btnRegistrarme, btnInicioSesion;
    private JLabel lblTitulo;

    private JPanel pCentro, pNorte, pEste, pOeste, pSur;

    public VentanaIdentificacion() {
        super();
        setBounds(300, 200, 400, 350);
        setTitle("Deustofy - Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon imagen = new ImageIcon("resources/images/logo.png");
        setIconImage(imagen.getImage());
        
        // Fondo negro y layout nulo para posicionar componentes manualmente
        getContentPane().setBackground(Color.BLACK);
        setLayout(null);
        
        // DISEÑO TITULO:
        lblTitulo = new JLabel("Inicia sesión en Spotify");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setBounds(120, 30, 200, 30);
        add(lblTitulo);

        // JTextFields ID y Contraseña (barras grises)
        JTextField txtID = new JTextField();
        txtID.setBackground(Color.DARK_GRAY);
        txtID.setForeground(Color.WHITE);
        txtID.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        txtID.setBounds(90, 80, 220, 30);

        JPasswordField txtContraseña = new JPasswordField();
        txtContraseña.setBackground(Color.DARK_GRAY);
        txtContraseña.setForeground(Color.WHITE);
        txtContraseña.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        txtContraseña.setBounds(90, 120, 220, 30);
        
        add(txtID);
        add(txtContraseña);
        
        
        // Paneles:
        pCentro = new JPanel();
        pCentro.setLayout(new GridLayout(2, 1)); // botones Registrarse y IniciarSesion
        pNorte = new JPanel();
        pSur = new JPanel();
        pEste = new JPanel();
        pOeste = new JPanel();

        getContentPane().add(pNorte, BorderLayout.NORTH);
        getContentPane().add(pEste, BorderLayout.EAST);
        getContentPane().add(pOeste, BorderLayout.WEST);
        getContentPane().add(pSur, BorderLayout.SOUTH);
        getContentPane().add(pCentro, BorderLayout.CENTER);

        // Botón Iniciar Sesión con listener
        btnInicioSesion = crearBoton("Iniciar Sesión", 120, 170, 150, 35);
        btnInicioSesion.addActionListener(e -> DialogoInicioSesion(txtID.getText(), new String(txtContraseña.getPassword())));
        add(btnInicioSesion);

        // Botón Registrarse con listener
        btnRegistrarme = crearBoton("Registrarme", 120, 220, 150, 35);
        btnRegistrarme.addActionListener(e -> DialogoRegistro());
        add(btnRegistrarme);

        setVisible(true);
    }
    
    // Método para DISEÑO de botones Iniciar Sesión y registrarse
    private JButton crearBoton(String texto, int x, int y, int ancho, int alto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.PLAIN, 14));
        boton.setBackground(new Color(25, 25, 25)); // Fondo oscuro
        boton.setForeground(Color.LIGHT_GRAY); // Texto claro
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // Borde claro
        boton.setBounds(x, y, ancho, alto); // Posición específica
        return boton;
    }
    
    // Diálogo de error para inicio de sesión fallido
    private void DialogoInicioSesion(String id, String contraseña) {
        if (id.equals("") && contraseña.equals("")) {
            JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); //Cierro la ventana actual
            
            VentanaAlbumArtista vAlbumArtista = new VentanaAlbumArtista(null, null);
            vAlbumArtista.setVisible(true);
            
        } else {
            JOptionPane.showMessageDialog(this, "ID o contraseña incorrectos, vuelva a intentarlo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
        
    // DIÁLOGO DE REGISTRARME: ----------------------------------------------------------------------------------
 
    private void DialogoRegistro() {
        JDialog dialogo = new JDialog(this, "Registrarse", true);
        dialogo.setSize(350, 250);
        dialogo.setLayout(new BorderLayout());
        dialogo.setLocationRelativeTo(this);
        dialogo.getContentPane().setBackground(Color.BLACK);

        // Panel CENTER --> ID, Contraseña, Confirmar Contraseña
        JPanel panelCentral = new JPanel(new GridLayout(3, 2, 10, 10));
        panelCentral.setBackground(Color.BLACK);
        
        JLabel lblID = new JLabel("ID:");
        lblID.setForeground(Color.WHITE);
        JTextField txtID = new JTextField();
        txtID.setBackground(Color.DARK_GRAY);
        txtID.setForeground(Color.WHITE);
        
        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setForeground(Color.WHITE);
        JPasswordField txtContraseña = new JPasswordField();
        txtContraseña.setBackground(Color.DARK_GRAY);
        txtContraseña.setForeground(Color.WHITE);
        
        JLabel lblConfirmarContraseña = new JLabel("Confirmar Contraseña:");
        lblConfirmarContraseña.setForeground(Color.WHITE);
        JPasswordField txtConfirmarContraseña = new JPasswordField();
        txtConfirmarContraseña.setBackground(Color.DARK_GRAY);
        txtConfirmarContraseña.setForeground(Color.WHITE);

        panelCentral.add(lblID);
        panelCentral.add(txtID);
        panelCentral.add(lblContraseña);
        panelCentral.add(txtContraseña);
        panelCentral.add(lblConfirmarContraseña);
        panelCentral.add(txtConfirmarContraseña);

        dialogo.add(panelCentral, BorderLayout.CENTER);
        

        // Panel SOUTH --> Botones Volver y ConfirmarRegistro
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.setBackground(Color.BLACK);
        JButton btnVolver = crearBoton("Volver", 0, 0, 100, 30);
        JButton btnConfirmarRegistro = crearBoton("Confirmar Registro", 0, 0, 150, 30);

        panelBotones.add(btnVolver);
        panelBotones.add(btnConfirmarRegistro);
        dialogo.add(panelBotones, BorderLayout.SOUTH);


        // Listener CONFIRMAR REGISTRO
        btnConfirmarRegistro.addActionListener(e -> {
            String contraseña = new String(txtContraseña.getPassword());
            String confirmarContraseña = new String(txtConfirmarContraseña.getPassword());

            // Que las contraseñas coincidan:
            if (!contraseña.equals(confirmarContraseña)) {
                JOptionPane.showMessageDialog(dialogo, "Las contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialogo, "¡Registro exitoso!", "Registro", JOptionPane.INFORMATION_MESSAGE);
                dialogo.dispose();
            }
        });
        
     // Listener VOLVER:
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogo.dispose();
            }
        });

        dialogo.setVisible(true);
    }


// ------------------------------------------------------------------------------------------------------------------
    
    public static void main(String[] args) {
        VentanaIdentificacion v = new VentanaIdentificacion();
        v.setVisible(true);
    }
}

