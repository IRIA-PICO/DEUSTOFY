package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

import db.GestorBD;
import domain.Cancion;

public class VentanaCancion extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLayeredPane pCentral;
	private JPanel pNorte, pSur, pSlider, pBotones;
	private JButton btnpausa, btnatras, btnalante, btnVelocidad, btnBucle;
	private JLabel lblTitulo, lblImagen, lblDur0, lblDurTotal, lblfavorito, lblrepetir;
	private JSlider slmusica;
	private int idUsuario;
	private boolean enPausa = false;
	private int pos,durcancion, posicionsl=0;
	private Cancion c;
	private HiloSlider hilo;
	private boolean enBucle = false;
    private double multiplicadorVelocidad = 1; 
    private List<Cancion> lcan;
	private GestorBD gestorBD;

	public VentanaCancion(List<Cancion> listcan, int posicion, int idUsuario, GestorBD gestorBD) {
		this.gestorBD = gestorBD;
		lcan = listcan;
		//propiedades JFrame
		setBounds(1000, 100, 400, 600);
		pos = posicion;
		this.idUsuario = idUsuario;
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
		pSur= new JPanel(new GridLayout(5,1));
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
		lblfavorito.setSize(40, 40);
		lblfavorito.setLocation(lblImagen.getX() + lblImagen.getWidth() - lblfavorito.getWidth(), lblImagen.getY());
		if(gestorBD.getIDCancionesFavoritas(idUsuario).contains(gestorBD.getIdCancionByTitulo(c.getTitulo()))) {
		ImageIcon im2 = new ImageIcon("resources/images/estrella.png");
		ImageIcon imagenConDimensiones2 = new ImageIcon(im2.getImage().getScaledInstance(lblfavorito.getWidth(), lblfavorito.getHeight(), Image.SCALE_DEFAULT));
		lblfavorito.setIcon(imagenConDimensiones2);
		}
		
		//Cargamos nombre del artista y nombre de la cancion
		lblTitulo= new JLabel(c.reproducir());
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(fuenteTexto);
		pSlider= new JPanel();
		pSlider.setBackground(new Color(60, 60, 60));
		
		//Cargamos Slider y duracion de la cancion
		durcancion = c.getDuracion();
		
		slmusica = new JSlider(0, durcancion, 0);
		slmusica.setPaintLabels(true);
		slmusica.setMinorTickSpacing(1);
		slmusica.setBackground(new Color(60, 60, 60));
		
		lblDur0= new JLabel("0:00");
		lblDur0.setForeground(Color.WHITE);
		lblDur0.setFont(fuenteTexto);
		lblDurTotal= new JLabel(formatoDuracion(c));
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
		btnVelocidad = new JButton("x1");
		btnBucle = new JButton("Act Bucle");
		
		Color botonColor = new Color(90, 90, 90);
		btnatras.setBackground(botonColor);
        btnatras.setForeground(Color.WHITE);
        btnpausa.setBackground(botonColor);
        btnalante.setBackground(botonColor);
        btnalante.setForeground(Color.WHITE);
        btnVelocidad.setBackground(botonColor);
        btnVelocidad.setForeground(Color.WHITE);
        btnBucle.setBackground(botonColor);
        btnBucle.setForeground(Color.WHITE);
        
        pBotones.add(btnVelocidad);
        pBotones.add(btnatras);
		pBotones.add(btnpausa);
		pBotones.add(btnalante);		
		pBotones.add(btnBucle);
		
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
		slmusica.addChangeListener((e)->{
			if(slmusica.getValueIsAdjusting()) {
				posicionsl=slmusica.getValue();
			}
		});
		hilo = new HiloSlider();
        hilo.start();
        
        
		btnpausa.addActionListener((e) -> {	
			synchronized (hilo) {
				enPausa = !enPausa;
				if (enPausa) {
			        ImageIcon imgPlay = new ImageIcon("resources/images/ButonPlay.png");
			        btnpausa.setIcon(new ImageIcon(imgPlay.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
			    } else {
			        // Cambia a imagen de "pause"
			        ImageIcon imgPause = new ImageIcon("resources/images/ButtonPause.png");
			        btnpausa.setIcon(new ImageIcon(imgPause.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
			        hilo.notifyAll();
			    }
		    
			}  
		});
		
		btnalante.addActionListener((e) -> {		
			if(pos<lcan.size()-1) {
				if (!enBucle) {
					pos++;
					}
				c = lcan.get(pos);
				modificarDatos();
			}
		});
		
		btnatras.addActionListener((e) -> {		
			if(pos>0) {
				if (!enBucle) {
					pos--;
					}
				c = lcan.get(pos);
				modificarDatos();
			}
		});
		
		btnVelocidad.addActionListener(e -> {
			if (multiplicadorVelocidad == 1) {
	    		multiplicadorVelocidad = 2;
	    	} else if (multiplicadorVelocidad == 2) {
	    		multiplicadorVelocidad = 0.5;
	    	} else {
	    		multiplicadorVelocidad = 1;
	    	}
	    	btnVelocidad.setText("x" + multiplicadorVelocidad);
		});
		
        btnBucle.addActionListener(e -> {
        	enBucle = !enBucle;
        	if (enBucle) {
        		btnBucle.setText("Des Bucle");
        	} else {
        		btnBucle.setText("Act Bucle");
        	}
        });
		setVisible(true);
	}
	
	public void modificarDatos() {
		if (hilo != null && hilo.isAlive()) {
	        hilo.interrupt();
	        try {
	            hilo.join(); // Asegurarte de que el hilo actual finalice
	        } catch (InterruptedException e) {
	            // Manejo de la excepción
	        }
	    }
		
	    if (!enBucle) {    
			lblImagen.setSize(300, 300);
			ImageIcon im = new ImageIcon(c.getAlbum().getRutaFoto());
			ImageIcon imagenConDimensiones = new ImageIcon(im.getImage().getScaledInstance(lblImagen.getWidth(), lblImagen.getHeight(), Image.SCALE_DEFAULT));
			
			if(gestorBD.getIDCancionesFavoritas(idUsuario).contains(gestorBD.getIdCancionByTitulo(c.getTitulo()))) {
				ImageIcon im2 = new ImageIcon("resources/images/estrella.png");
				ImageIcon imagenConDimensiones2 = new ImageIcon(im2.getImage().getScaledInstance(lblfavorito.getWidth(), lblfavorito.getHeight(), Image.SCALE_DEFAULT));
				lblfavorito.setIcon(imagenConDimensiones2);
			} else {
				lblfavorito.setIcon(null);
			}
			
			lblImagen.setIcon(imagenConDimensiones);
			lblTitulo.setText(c.reproducir());
			lblDurTotal.setText(formatoDuracion(c));
			slmusica.setValue(0);
			
			//cambiar datos duracion cancion y reiniciar hilo
			durcancion = c.getDuracion();
			slmusica.setMaximum(durcancion);			
	    }
	    posicionsl=0;

		hilo = new HiloSlider();
		hilo.start();
	}
	
	public Cancion getCancion() {
		return c;
	}
	
	// Formato minutos:segundos
	private static String formatoDuracion(Cancion c) {
		int minutes = (int) (c.getDuracion()/60);
		int seconds = (int) (c.getDuracion()%60);
		String secondsStr = String.valueOf(seconds);
		if (seconds < 10) {
			secondsStr = "0" + seconds;
		}
		return (minutes + ":" + secondsStr);
	}
		
	private class HiloSlider extends Thread{
		@Override
        public void run() {
			try {	
				while (!this.isInterrupted()) {
					synchronized (this) {
						while(enPausa) {
							wait();
						}
		            }
					if (posicionsl >= durcancion) {
		                if (enBucle) {
		                    posicionsl = 0; // Reinicia la canción
		                } else {
		                	if(pos<lcan.size()-1) {
		        				pos++;
		        				c = lcan.get(pos);
		        				modificarDatos();
		        			} else {
		        				break;
		        			}
		                }
					} else {
						slmusica.setValue(posicionsl);  
		                Thread.sleep((int)(100/multiplicadorVelocidad));     // Espera 0.1s entre cada cambio si la velocidad esta en x1
		                posicionsl++;
					}
				}	
			} catch (InterruptedException e) {
                
            }
		}		
	}
}

