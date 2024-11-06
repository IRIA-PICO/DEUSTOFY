package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JDialog;

public class VentanaIdentificacion extends JFrame {
    private static final long serialVersionUID = 1L;
    private JButton btnRegistrarme, btnInicioSesion;
    private JLabel lblTitulo;

    private JPanel pCentro, pNorte, pEste, pOeste, pSur;

    public VentanaIdentificacion() {
        super();
        setBounds(300, 200, 600, 400);
        setTitle("DEUSTOFY - Ventana Inicial");
        ImageIcon imagen = new ImageIcon("img/icono.png");  // --- CAMBIAR IMAGEN !!!!!!
        setIconImage(imagen.getImage());

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

        // Botones:
        btnRegistrarme = new JButton("Registrarme");
        btnInicioSesion = new JButton("Iniciar Sesión");
        btnRegistrarme.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));  // ---- CANBIAR LETRA !!!!!!
        btnInicioSesion.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18)); // ---- CANBIAR LETRA !!!!!!

        lblTitulo = new JLabel("DEUSTOFY - Identifícate");

        // Añado botones:
        pNorte.add(lblTitulo);
        pCentro.add(btnRegistrarme);
        pCentro.add(btnInicioSesion);

        // Listeners de los botones:
        btnInicioSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogoInicioSesion(); // método nuevo
            }
        });

        btnRegistrarme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogoRegistro(); // método nuevo
            }
        });

        setVisible(true);
    }
    

    // DIÁLOGO DE INICIOSESION: ----------------------------------------------------------------------------------
    
    private void DialogoInicioSesion() {
        JDialog dialogo = new JDialog(this, "Iniciar Sesión", true);
        dialogo.setSize(350, 200);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());

        // Panel CENTER --> ID y Contraseña
        JPanel panel_id_contr = new JPanel(new GridLayout(2, 2)); // panel id_contr
        JLabel lblID = new JLabel("ID:");
        JTextField txtID = new JTextField();
        JLabel lblContraseña = new JLabel("Contraseña:");
        JPasswordField txtContraseña = new JPasswordField();

        panel_id_contr.add(lblID);
        panel_id_contr.add(txtID);
        panel_id_contr.add(lblContraseña);
        panel_id_contr.add(txtContraseña);

        dialogo.add(panel_id_contr, BorderLayout.CENTER);

        // Panel SOUTH --> Confirmar y Volver
        JPanel panel_volver_conf = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnConfirmar = new JButton("Confirmar");
        JButton btnVolver = new JButton("Volver");

        panel_volver_conf.add(btnVolver);
        panel_volver_conf.add(btnConfirmar);
        
        dialogo.add(panel_volver_conf, BorderLayout.SOUTH);

        
        // Listener CONFIRMAR
        btnConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtID.getText();
                String contraseña = new String(txtContraseña.getPassword());
                
                if (id.equals("Ane") && contraseña.equals("contraseña123")) { // ID y CONTRASEÑA correctos
                    System.out.println("Inicio de sesión exitoso");
                    dialogo.dispose();
                    //
                    //
                    // ENTRAR A DEUSTOFY !!!!!!!!!!!!!!!! --> Ventana de Iria
                    //
                    //
                    //
                    
                } else {
                    System.out.println("ID o contraseña incorrectos, vuelva a intentarlo."); // IDEA --> Crear otro dialog !!!!!!!!!
                }
            }
        });

        // Listener VOLVER
        btnVolver.addActionListener(e -> dialogo.dispose());
        dialogo.setVisible(true);
    }


    // DIÁLOGO DE REGISTRARME: ----------------------------------------------------------------------------------
 
    private void DialogoRegistro() {
        JDialog dialogo = new JDialog(this, "Registrarse", true);
        dialogo.setSize(300, 250);
        dialogo.setLayout(new BorderLayout());
        dialogo.setLocationRelativeTo(this);

        // Panel CENTER --> ID, Contraseña, Confirmar Contraseña
        JPanel panelCentral = new JPanel(new GridLayout(3, 2));
        JLabel lblID = new JLabel("ID:");
        JTextField txtID = new JTextField();
        JLabel lblContraseña = new JLabel("Contraseña:");
        JPasswordField txtContraseña = new JPasswordField();
        JLabel lblConfirmarContraseña = new JLabel("Confirmar Contraseña:");
        JPasswordField txtConfirmarContraseña = new JPasswordField();

        panelCentral.add(lblID);
        panelCentral.add(txtID);
        panelCentral.add(lblContraseña);
        panelCentral.add(txtContraseña);
        panelCentral.add(lblConfirmarContraseña);
        panelCentral.add(txtConfirmarContraseña);
        
        dialogo.add(panelCentral, BorderLayout.CENTER);

        // Panel SOUTH --> Volver y Registrarse
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnVolver = new JButton("Volver");
        JButton btnConfirmarRegistro = new JButton("Confirmar Registro");


        panelBotones.add(btnVolver);
        panelBotones.add(btnConfirmarRegistro);

        dialogo.add(panelBotones, BorderLayout.SOUTH);


        // Listener CONFIRMAR REGISTRO
        btnConfirmarRegistro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtID.getText();
                String contraseña = new String(txtContraseña.getPassword());
                String confirmarContraseña = new String(txtConfirmarContraseña.getPassword());

                // Que ID y contraseña coincidan
                if (!contraseña.equals(confirmarContraseña)) {
                    System.out.println("Las contraseñas no coinciden");
                } else {
                    System.out.println("¡Registro con exito!: " + "El usuario"+ id + " se ha registrado correctamente");
                    dialogo.dispose();
                }
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

