package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

import domain.Cancion;
import domain.Usuario;

public class VentanaCancion extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLayeredPane pCentral;
	private JPanel pNorte, pSur, pSlider, pBotones;
	private JButton btnpausa, btnatras, btnalante;
	private JLabel lblTitulo, lblImagen, lblDur0, lblDurTotal, lblfavorito, lblrepetir;
	private JSlider slmusica;
	private Usuario user;
	private boolean enPausa = false;
	private int pos;
	private Cancion c;
	public VentanaCancion(List<Cancion> lcan, int posicion, Usuario userAnt) {
		
		//propiedades JFrame
		setBounds(1000, 100, 400, 600);
		pos = posicion;
		user= userAnt;
		setTitle("DEUSTOFY - Cancion");
		ImageIcon imagen = new ImageIcon("resources/images/logo.png");
		Font fuenteTexto = new Font("Gotham", Font.BOLD, 16);
		
        setIconImage(imagen.getImage());
        getContentPane().setBackground(new Color(60, 60, 60));
		
        //Cargamos la cancion que vamos a estar usando
		c= lcan.get(pos);
		
		pCentral = new JLayeredPane();
		pCentral.setPreferredSize(new Dimension(400, 400));
		pCentral.setBackground(new Color(60, 60, 60)); 
		pSur= new JPanel(new GridLayout(3,1));
		pSur.setBackground(new Color(60, 60, 60)); 
		pNorte= new JPanel();
		pNorte.setBackground(new Color(60, 60, 60)); 
		
		//Cargamos Foto del album
		lblImagen = new JLabel();
		lblImagen.setSize(300, 300);
		lblImagen.setLocation(50, 50);
		ImageIcon im = new ImageIcon(c.getAlbum().getRutaFoto());
		ImageIcon imagenConDimensiones = new ImageIcon(im.getImage().getScaledInstance(lblImagen.getWidth(), lblImagen.getHeight(), Image.SCALE_DEFAULT));
		lblImagen.setIcon(imagenConDimensiones);
		
		//Cargar estrella
		lblfavorito= new JLabel();
		lblfavorito.setSize(20, 20);
		lblfavorito.setLocation(lblImagen.getX() + lblImagen.getWidth() - lblfavorito.getWidth(), lblImagen.getY());
		if(user.getCancionesFavoritas().contains(c)) {
		ImageIcon im2 = new ImageIcon("resources/images/estrella.png");
		ImageIcon imagenConDimensiones2 = new ImageIcon(im2.getImage().getScaledInstance(lblfavorito.getWidth(), lblfavorito.getHeight(), Image.SCALE_DEFAULT));
		lblfavorito.setIcon(imagenConDimensiones2);
		}else {
			ImageIcon im2 = new ImageIcon("resources/images/estrellaFavorita.png");
			ImageIcon imagenConDimensiones2 = new ImageIcon(im2.getImage().getScaledInstance(lblfavorito.getWidth(), lblfavorito.getHeight(), Image.SCALE_DEFAULT));
			lblfavorito.setIcon(imagenConDimensiones2);
		}
		
		//CArgamos nombre del artista y nombre de la cancion
		lblTitulo= new JLabel(c.reproducir());
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(fuenteTexto);
		pSlider= new JPanel();
		pSlider.setBackground(new Color(60, 60, 60));
		
		//Cargamos Slider y duracion de la cancion
		slmusica = new JSlider(0, 1000, 0);
		slmusica.setPaintLabels(true);
		slmusica.setMinorTickSpacing(1);
		slmusica.setBackground(new Color(60, 60, 60));
		
		lblDur0= new JLabel("0.00");
		lblDur0.setForeground(Color.WHITE);
		lblDur0.setFont(fuenteTexto);
		lblDurTotal= new JLabel(String.valueOf(c.getDuracion()));
		lblDurTotal.setForeground(Color.WHITE);
		lblDurTotal.setFont(fuenteTexto);
	
		//Añadir al panelSlider
		pSlider.add(lblDur0);
		pSlider.add(slmusica);
		pSlider.add(lblDurTotal);
		
		//Creacion de botones
		pBotones= new JPanel();
		pBotones.setBackground(new Color(60, 60, 60));
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
		pBotones.add(btnatras);
		pBotones.add(btnpausa);
		pBotones.add(btnalante);
		
		//Para repetir la cancion		
		lblrepetir= new JLabel();
		lblrepetir.setSize(20, 20);
		ImageIcon im3 = new ImageIcon("resources/images/bucle.jpg");
		ImageIcon imagenConDimensiones3= new ImageIcon(im3.getImage().getScaledInstance(lblrepetir.getWidth(), lblrepetir.getHeight(), Image.SCALE_DEFAULT));
		lblrepetir.setIcon(imagenConDimensiones3);
		
		//Añadimos a los paneles;
		pCentral.add(lblImagen, Integer.valueOf(1)); 
		pCentral.add(lblfavorito, Integer.valueOf(2));
		pSur.add(lblTitulo);
		lblTitulo.setAlignmentX(JLabel.CENTER);
		pSur.add(pSlider);
		pSur.add(pBotones);
		
	
		getContentPane().add(pNorte, BorderLayout.NORTH);
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
		lblImagen.setSize(300, 300);
		ImageIcon im = new ImageIcon(c.getAlbum().getRutaFoto());
		ImageIcon imagenConDimensiones = new ImageIcon(im.getImage().getScaledInstance(lblImagen.getWidth(), lblImagen.getHeight(), Image.SCALE_DEFAULT));
		
		lblImagen.setIcon(imagenConDimensiones);
		lblTitulo.setText(c.reproducir());
		lblDurTotal.setText(String.valueOf(c.getDuracion()));
		slmusica.setValue(0);
		
	}
	
}

