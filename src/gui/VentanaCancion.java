package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.Cancion;

public class VentanaCancion extends JFrame {
	private JPanel pSur, pCentral;
	private JButton btnpausa, btnatras, btnalante;
	private JLabel lblTitulo, lblImagen;
	
	public VentanaCancion(Cancion c) {
		setBounds(500, 200, 400, 600);		//TODO poner que se te ponga en el centro automaticamente
		
		pCentral = new JPanel(new GridLayout(1,2));
		pSur= new JPanel();
		
		
		lblImagen= new JLabel(new ImageIcon("resources/images/imgPrueba.png"));
		lblTitulo= new JLabel(c.getTitulo());
		
		pCentral.add(lblImagen);
		pCentral.add(lblTitulo);
		
		btnatras= new JButton("<<");
		btnpausa= new JButton("Para");
		btnalante= new JButton(">>");
		
		pSur.add(btnatras);
		pSur.add(btnpausa);
		pSur.add(btnalante);
		
		getContentPane().add(pCentral, BorderLayout.CENTER);
		getContentPane().add(pSur, BorderLayout.SOUTH);
		//LISTENERS
		
		
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		Cancion nuevo= new Cancion("a", 3.46, null, null);
		 new VentanaCancion(nuevo);
	}
}

