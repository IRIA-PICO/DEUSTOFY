package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

import domain.Album;
import domain.Artista;
import domain.Cancion;

public class VentanaCancion extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel pSur, pCentral, pSlider;
	private JButton btnpausa, btnatras, btnalante;
	private JLabel lblTitulo, lblImagen, lblDur0, lblDurTotal;
	private JSlider slmusica;
	private boolean enPausa = false;
	private int pos;
	private Cancion c;
	public VentanaCancion(List<Cancion> lcan, int posicion) {
		//propiedades JFrame
		setBounds(1000, 100, 350, 600);
		pos = posicion;
		setTitle("DEUSTOFY - Cancion");
		ImageIcon imagen = new ImageIcon("resources/images/logo.png");
		Font fuenteTexto = new Font("Gotham", Font.BOLD, 16);
		
        setIconImage(imagen.getImage());
        getContentPane().setBackground(new Color(60, 60, 60));
		
        //Cargamos la cancion que vamos a estar usando
		c= lcan.get(pos);
		
		pCentral = new JPanel(new GridLayout(3,1));
		pCentral.setBackground(new Color(60, 60, 60)); 
		pSur= new JPanel();
		pSur.setBackground(new Color(60, 60, 60)); 
		
		//Cargamos Foto del album, nombre del artista y nombre de la cancion
		lblImagen= new JLabel(new ImageIcon(c.getAlbum().getRutaFoto()));
		lblTitulo= new JLabel(c.reproducir());
		lblTitulo.setForeground(Color.WHITE);
		pSlider= new JPanel();
		pSlider.setBackground(new Color(60, 60, 60));
		
		//Cargamos Slider y duracion de la cancion
		slmusica = new JSlider(0, 1000, 0);
		slmusica.setPaintLabels(true);
		slmusica.setMinorTickSpacing(1);
		slmusica.setBackground(new Color(60, 60, 60));
		
		lblDur0= new JLabel("0.00");
		lblDur0.setForeground(Color.WHITE);
		lblDurTotal= new JLabel(String.valueOf(c.getDuracion()));
		lblDurTotal.setForeground(Color.WHITE);
	
		
		//Añadir al JFrame
		pSlider.add(lblDur0);
		pSlider.add(slmusica);
		pSlider.add(lblDurTotal);
		
		pCentral.add(lblImagen);
		pCentral.add(lblTitulo);
		pCentral.add(pSlider);
		
		//Creacion de botones
		btnatras= new JButton("<<");
		ImageIcon imgOriginal=new ImageIcon("resources/images/ButonPlay.png");
		btnpausa= new JButton(new ImageIcon(imgOriginal.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
		btnalante= new JButton(">>");	
		
		Color botonColor = new Color(90, 90, 90);
		btnatras.setBackground(botonColor);
        btnatras.setForeground(Color.WHITE);
        btnpausa.setBackground(botonColor);
        btnalante.setBackground(botonColor);
        btnalante.setForeground(Color.WHITE);
        
		pSur.add(btnatras);
		pSur.add(btnpausa);
		pSur.add(btnalante);
		
		getContentPane().add(pCentral, BorderLayout.CENTER);
		getContentPane().add(pSur, BorderLayout.SOUTH);
		//LISTENERS
		btnpausa.addActionListener((e) -> {		
			if (enPausa) {
		        ImageIcon imgPlay = new ImageIcon("resources/images/ButonPlay.png");
		        btnpausa.setIcon(new ImageIcon(imgPlay.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
		    } else {
		        // Cambia a imagen de "pause"
		        ImageIcon imgPause = new ImageIcon("resources/images/ButtonPause.png");
		        btnpausa.setIcon(new ImageIcon(imgPause.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
		    }
		    
		    enPausa = !enPausa;
		});
		
		btnalante.addActionListener((e) -> {		
			if(pos<lcan.size()-1) {
				pos++;
				c = lcan.get(pos);
				modificarDatos();
			}else {
				JOptionPane.showMessageDialog(null, "La lista de reproducciones ha finalizado");
			}
		});
		btnatras.addActionListener((e) -> {		
			if(pos>0) {
				pos--;
				c = lcan.get(pos);
				modificarDatos();
			} else {
				JOptionPane.showMessageDialog(null, "La lista de reproducciones ha finalizado");
			}
		});
		
		setVisible(true);
	}
	
	private void modificarDatos() {
		lblImagen.setIcon(new ImageIcon(c.getAlbum().getRutaFoto()));
		lblTitulo.setText(c.reproducir());
		lblDurTotal.setText(String.valueOf(c.getDuracion()));
		slmusica.setValue(0);
	}
	/*
	public static void main(String[] args) {
		Artista art1= new Artista(1, "pepe", null);
		Artista art2= new Artista(2, "maria", null);
		
		Album a1= new Album("a", 48.03, null, art1, "resources/images/imgPrueba.jpg");
		Album a2= new Album("b", 9.03, null, art2, "resources/images/img2.jpg");
		
		Cancion nuevo= new Cancion("CancionPrueba", 3.06, a1, art1);
		Cancion n1= new Cancion("CancionPrueba1", 2.43, a2, art2);
		Cancion n2= new Cancion("CancionPrueba2", 3.46, a1, art1);
		List<Cancion> lcanciones= new ArrayList<Cancion>();
		lcanciones.add(nuevo);
		lcanciones.add(n1);
		lcanciones.add(n2);
		 new VentanaCancion(lcanciones, 0);
	}*/
}

