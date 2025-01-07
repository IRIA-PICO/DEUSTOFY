package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import db.GestorBD;
import domain.Usuario;

import javax.swing.JPasswordField;

public class VentanaIdentificacion extends JFrame {
	  private static final long serialVersionUID = 1L;
	  private GestorBD gestorBD;
	  private JTextField userField;
	  private JPasswordField passwordField;
	  private JButton loginButton, registerButton;
	  private JLabel forgotPasswordLabel;    


    public VentanaIdentificacion(GestorBD gestorBD) {
    	this.gestorBD = gestorBD;
    	this.setTitle("INICIO SESION");
    	this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		this.getContentPane().setBackground(new Color(30, 30, 30)); // Fondo gris oscuro
		this.setVisible(true);

      // Panel central para el formulario de login
      JPanel formPanel = new JPanel();
      formPanel.setBackground(new Color(45, 45, 45)); // Fondo gris más oscuro
      formPanel.setLayout(null); // Layout manual para control total del diseño
      this.add(formPanel, BorderLayout.CENTER);

      // Título de la app (más pequeño y elegante)
      JLabel titleLabel = new JLabel("DEUSTOFY");
      titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
      titleLabel.setForeground(Color.WHITE);
      titleLabel.setHorizontalAlignment(JLabel.CENTER);
      titleLabel.setBounds(0, 50, 800, 50); // Centrado en la parte superior
      formPanel.add(titleLabel);

      // Campo de texto para el usuario
      JLabel userLabel = new JLabel("Usuario:");
      userLabel.setForeground(Color.WHITE);
      userLabel.setBounds(250, 150, 100, 30);
      formPanel.add(userLabel);

      userField = new JTextField();
      userField.setBounds(250, 180, 300, 40);
      userField.setBackground(new Color(60, 60, 60));
      userField.setForeground(Color.WHITE);
      userField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
      userField.setFont(new Font("Arial", Font.PLAIN, 14));
      userField.setCaretColor(Color.WHITE);
      userField.addFocusListener(new FocusAdapter() {
          public void focusGained(FocusEvent e) {
              userField.setBackground(new Color(50, 50, 50)); // Efecto al enfocar
          }

          public void focusLost(FocusEvent e) {
              userField.setBackground(new Color(60, 60, 60)); // Vuelve al color original
          }

      });

      formPanel.add(userField);

      // Campo de texto para la contraseña
      JLabel passwordLabel = new JLabel("Contraseña:");
      passwordLabel.setForeground(Color.WHITE);
      passwordLabel.setBounds(250, 240, 100, 30);
      formPanel.add(passwordLabel);

      passwordField = new JPasswordField();
      passwordField.setBounds(250, 270, 300, 40);
      passwordField.setBackground(new Color(60, 60, 60));
      passwordField.setForeground(Color.WHITE);
      passwordField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
      passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
      passwordField.setCaretColor(Color.WHITE);
      passwordField.addFocusListener(new FocusAdapter() {

          public void focusGained(FocusEvent e) {
              passwordField.setBackground(new Color(50, 50, 50)); // Efecto al enfocar
          }

          public void focusLost(FocusEvent e) {
              passwordField.setBackground(new Color(60, 60, 60)); // Vuelve al color original
          }

      });

      formPanel.add(passwordField);      

      forgotPasswordLabel = new JLabel("<html><u>¿Olvidaste tu contraseña?</u></html>");
      forgotPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
      forgotPasswordLabel.setForeground(Color.WHITE);
      forgotPasswordLabel.setBounds(250, 460, 300, 20);
      forgotPasswordLabel.addMouseListener(new MouseAdapter() {

          public void mouseEntered(MouseEvent e) {
              forgotPasswordLabel.setForeground(Color.CYAN); // Efecto hover
          }

          public void mouseExited(MouseEvent e) {
              forgotPasswordLabel.setForeground(Color.WHITE); // Vuelve al color original
          }

          public void mouseClicked(MouseEvent e) {
              // Acción cuando se hace clic en "Olvidaste tu contraseña"
        	  String usuario = JOptionPane.showInputDialog(VentanaIdentificacion.this, 
        	            "Ingresa tu nombre de usuario para verificar tu identidad:", 
        	            "Verificación de identidad", 
        	            JOptionPane.PLAIN_MESSAGE);
        	        
        	        if (usuario != null && !usuario.trim().isEmpty()) {
        	            // Verificar si el nombre de usuario existe en la base de datos
        	            boolean existeUsuario = gestorBD.verificarUsuario(usuario); // Método para verificar si el usuario existe

        	            if (existeUsuario) {
        	                // Mostrar un cuadro para que el usuario ingrese la nueva contraseña
        	                String nuevaContrasena = JOptionPane.showInputDialog(VentanaIdentificacion.this, 
        	                    "Ingresa tu nueva contraseña:", 
        	                    "Cambio de contraseña", 
        	                    JOptionPane.PLAIN_MESSAGE);
        	                
        	                // Verificar que la nueva contraseña sea válida
        	                if (nuevaContrasena != null && !nuevaContrasena.trim().isEmpty()) {
        	                    // Aquí puedes agregar validaciones de la contraseña como longitud mínima, caracteres especiales, etc.

        	                    // Guardar la nueva contraseña en la base de datos
        	                    boolean cambioExitoso = gestorBD.cambiarContrasena(usuario, nuevaContrasena);

        	                    if (cambioExitoso) {
        	                        JOptionPane.showMessageDialog(VentanaIdentificacion.this, 
        	                            "Contraseña cambiada exitosamente.");
        	                    } else {
        	                        JOptionPane.showMessageDialog(VentanaIdentificacion.this, 
        	                            "Error al cambiar la contraseña.", 
        	                            "Error", 
        	                            JOptionPane.ERROR_MESSAGE);
        	                    }
        	                } else {
        	                    JOptionPane.showMessageDialog(VentanaIdentificacion.this, 
        	                        "La nueva contraseña no puede estar vacía.", 
        	                        "Error", 
        	                        JOptionPane.ERROR_MESSAGE);
        	                }
        	            } else {
        	                JOptionPane.showMessageDialog(VentanaIdentificacion.this, 
        	                    "No se ha encontrado un usuario con ese nombre de usuario.", 
        	                    "Error", 
        	                    JOptionPane.ERROR_MESSAGE);
        	            }
        	        } else {
        	            JOptionPane.showMessageDialog(VentanaIdentificacion.this, 
        	                "No se ingresó un nombre de usuario válido.", 
        	                "Error", 
        	                JOptionPane.ERROR_MESSAGE);
        	        }
        	    }

      });

      formPanel.add(forgotPasswordLabel);

      // Botón de Login con estilo moderno
      loginButton = new JButton("Iniciar Sesión");
      loginButton.setBounds(250, 340, 300, 40);
      loginButton.setBackground(Color.blue); 
      loginButton.setForeground(Color.WHITE);
      loginButton.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
      loginButton.setFocusPainted(false);
      loginButton.setBorderPainted(false);
      loginButton.addMouseListener(new MouseAdapter() {

          public void mouseEntered(MouseEvent e) {
              loginButton.setBackground(Color.BLACK); // Efecto hover
          }

          public void mouseExited(MouseEvent e) {
              loginButton.setBackground(Color.blue);
          }

      });
   // Dentro de tu clase VentanaIdentificacion
      loginButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              // Obtener los datos del formulario
              String user = userField.getText();
              String password = String.valueOf(passwordField.getPassword());

              // Verificar si el usuario existe en la base de datos
              Usuario usuarioGuardado = gestorBD.getUsuario(user);  // Obtener el usuario registrado por su nombre

              if (usuarioGuardado == null) {//Si no se ha registrado primero
                  JOptionPane.showMessageDialog( VentanaIdentificacion.this, "El usuario no está registrado. Por favor, regístrate primero.", "Usuario no registrado", JOptionPane.WARNING_MESSAGE );
              } else if (usuarioGuardado != null && password.equals(usuarioGuardado.getContr())) {
                  JOptionPane.showMessageDialog(VentanaIdentificacion.this, "Inicio de sesión exitoso", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                  
                  // Crear la nueva ventana de álbumes
                   VentanaAlbumArtista vAlbumArtista = new VentanaAlbumArtista(usuarioGuardado, gestorBD);
                   vAlbumArtista.setVisible(true);

                  VentanaIdentificacion.this.dispose();  // Cierra la ventana actual
              } else {
                  JOptionPane.showMessageDialog(VentanaIdentificacion.this, "ID o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
              }
          }
      });

      formPanel.add(loginButton);

      // Botón de Registro
      registerButton = new JButton("Crear Cuenta");
      registerButton.setBounds(250, 400, 300, 40);
      registerButton.setBackground(new Color(60, 60, 60));
      registerButton.setForeground(Color.WHITE);
      registerButton.setFont(new Font("Arial", Font.PLAIN, 14));
      registerButton.setFocusPainted(false);
      registerButton.setBorderPainted(false);
      registerButton.addActionListener(e -> openRegisterForm());
      formPanel.add(registerButton);
  }
    private void openRegisterForm() {
        JFrame registerFrame = new JFrame("Registro");
        registerFrame.setSize(400, 400);
        registerFrame.setLayout(null);
        registerFrame.setLocationRelativeTo(this);
        registerFrame.getContentPane().setBackground(new Color(45, 45, 45));

        JLabel nameLabel = new JLabel("Nombre:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(50, 50, 100, 30);
        registerFrame.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(150, 50, 180, 30);
        nameField.setBackground(new Color(60, 60, 60));
        nameField.setForeground(Color.WHITE);
        registerFrame.add(nameField);

        JLabel genderLabel = new JLabel("Sexo:");
        genderLabel.setForeground(Color.WHITE);
        genderLabel.setBounds(50, 100, 100, 30);
        registerFrame.add(genderLabel);

        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"M", "F"});
        genderComboBox.setBounds(150, 100, 180, 30);
        genderComboBox.setBackground(new Color(60, 60, 60));
        genderComboBox.setForeground(Color.WHITE);
        registerFrame.add(genderComboBox);

        JLabel ageLabel = new JLabel("Edad:");
        ageLabel.setForeground(Color.WHITE);
        ageLabel.setBounds(50, 150, 100, 30);
        registerFrame.add(ageLabel);

        JTextField ageField = new JTextField();
        ageField.setBounds(150, 150, 180, 30);
        ageField.setBackground(new Color(60, 60, 60));
        ageField.setForeground(Color.WHITE);
        registerFrame.add(ageField);

        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(50, 200, 100, 30);
        registerFrame.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(150, 200, 180, 30);
        userField.setBackground(new Color(60, 60, 60));
        userField.setForeground(Color.WHITE);
        registerFrame.add(userField);

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(50, 250, 100, 30);
        registerFrame.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 250, 180, 30);
        passwordField.setBackground(new Color(60, 60, 60));
        passwordField.setForeground(Color.WHITE);
        registerFrame.add(passwordField);

        JButton registerButton = new JButton("Registrar");
        registerButton.setBounds(150, 300, 180, 30);
        registerButton.setBackground(Color.BLUE);
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(e -> {
            String nombre = nameField.getText();
            String sexo = genderComboBox.getSelectedItem().toString();
            int edad;
            try {
                edad = Integer.parseInt(ageField.getText());
                if (edad <= 0) {
                    throw new NumberFormatException("La edad debe ser mayor que cero.");
                }
            } catch (NumberFormatException e2) {
                JOptionPane.showMessageDialog( this,"Por favor, ingrese un número entero válido para la edad", "Entrada inválida", JOptionPane.ERROR_MESSAGE );
                return; // Salir del método si la edad no es válida
            }
            
            String usuario = userField.getText();
            String contraseña = String.valueOf(passwordField.getPassword());
            
            gestorBD.insertarUsuario(nombre, sexo, edad, usuario, contraseña);
            
            registerFrame.dispose();

        });
        registerFrame.add(registerButton);
        registerFrame.setVisible(true);
}


}