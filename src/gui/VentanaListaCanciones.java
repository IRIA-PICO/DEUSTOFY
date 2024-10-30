package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class VentanaListaCanciones extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public VentanaListaCanciones() {
		setTitle("DEUSTOFY");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        
     // Barra de búsqueda 
        JTextField busqueda = new JTextField("Buscar canciones...");
        busqueda.setForeground(Color.GRAY);
        busqueda.setPreferredSize(new Dimension(800, 35));
        busqueda.setBackground(new Color(24, 24, 24));
        busqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
	}
}
