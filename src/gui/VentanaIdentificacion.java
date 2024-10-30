package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class VentanaIdentificacion extends JFrame{
	private static final long serialVersionUID = 1L;
	private JButton btnRegistrarme, btnInicioSesion;
	private JLabel lblTitulo;

	private JPanel pCentro, pNorte, pEste, pOeste, pSur;
	
	public VentanaIdentificacion() {
		super();
		
		//Establecemos las propiedades de la ventana
		setBounds(300, 200, 600, 400);
		
		//Cambiar el título de la ventana
		setTitle("DEUSTOFY - Ventana Inicial");
		//Cambiar el icono de la ventana (Esquina superior izquierda)
		ImageIcon imagen = new ImageIcon("img/icono.png");                 // --- CAMBIAR IMAGEN
		setIconImage(imagen.getImage());
		
		//Instanciamos los paneles
		pCentro = new JPanel();
		//Modificamos el Layout del panel centro
		pCentro.setLayout(new GridLayout(2, 1)); //Formato de matriz de 1x2
		pNorte = new JPanel();
		pSur = new JPanel();
		pEste = new JPanel();
		pOeste = new JPanel();
		
		//Añadimos los paneles al panel principal de la ventana
		getContentPane().add(pNorte, BorderLayout.NORTH);
		getContentPane().add(pEste, BorderLayout.EAST);
		getContentPane().add(pOeste, BorderLayout.WEST);
		getContentPane().add(pSur, BorderLayout.SOUTH);
		getContentPane().add(pCentro, BorderLayout.CENTER);
		
		//Instanciamos los componentes botón
		btnRegistrarme = new JButton("Registrarme");
		btnInicioSesion = new JButton("Iniciar Sesión");
		//Cambiamos el tipo de letra en los botones
		btnRegistrarme.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		btnInicioSesion.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

		
		lblTitulo = new JLabel("DEUSTOFY - Identifícate");
		
		//Añadimos el botones:
		pCentro.add(btnRegistrarme);
		pCentro.add(btnInicioSesion);
		
		pNorte.add(lblTitulo);
		
	}
}
