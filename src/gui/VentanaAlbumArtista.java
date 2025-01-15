package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import db.GestorBD;
import domain.Artista;
import domain.Cancion;
import domain.PlayList;
import domain.Usuario;
import domain.Album;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VentanaAlbumArtista extends JFrame {

    private static final long serialVersionUID = 1L;
    
    private JPanel panel_artistas_albumes;
    private List<Album> albumes;
    private List<Artista> artistas;
    private Usuario user;
    private JTextField busqueda;
    private JPanel albumGrid;
    private JPanel artistaGrid;
    private JPanel sidebar;
    private JLabel premiumLabel;
    private GestorBD gestorBD;
    private JPanel panelPlaylists; 
    private HashMap<String, Color> mapa= new HashMap<>();
    
    public VentanaAlbumArtista(Usuario user1, GestorBD gestorBD) {
    	this.gestorBD = gestorBD;
    	this.albumes = new ArrayList<>();
    	this.gestorBD.getAlbum().forEach(album -> {
    	    if (album != null) { 
    	        this.albumes.add(album);
    	    }
    	});
    	
    	RecursiveBubbleSort.bubbleSortAlbums(this.albumes, this.albumes.size());

    	this.artistas = new ArrayList<>();
    	this.gestorBD.getArtistas().forEach(artista -> {
    	    if (artista != null) {
    	        this.artistas.add(artista);
    	    }
    	});
    	
    	user= user1;
        setTitle("DEUSTOFY");
        ImageIcon imagen = new ImageIcon("resources/images/logo.png");
        setIconImage(imagen.getImage());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Barra de búsqueda
        busqueda = new JTextField("Buscar artistas, álbumes...");
        busqueda.setFont(new Font("Gotham", Font.BOLD, 16));
        busqueda.setForeground(Color.GRAY);
        busqueda.setPreferredSize(new Dimension(800, 35));
        busqueda.setBackground(new Color(24, 24, 24));
        busqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        busqueda.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (busqueda.getText().equals("Buscar artistas, álbumes...")) {
                    busqueda.setForeground(Color.GRAY);
                    busqueda.setText("");
                }
            }
            public void focusLost(FocusEvent e) {
                if (busqueda.getText().isEmpty()) {
                    busqueda.setForeground(Color.GRAY);
                    busqueda.setText("Buscar artistas, álbumes...");
                }
            }
        });
        
        
        DocumentListener dl = new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (!busqueda.getText().equals("Buscar artistas, álbumes...")) {
        			buscarAlbumesArtistas();
        		}
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				buscarAlbumesArtistas();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
        };
        this.busqueda.getDocument().addDocumentListener(dl);
        
        
        
        JPanel Panelbusqueda = new JPanel(new BorderLayout());
        Panelbusqueda.setBorder(new EmptyBorder(15, 20, 15, 20));
        Panelbusqueda.setBackground(new Color(40, 40, 40));
        JButton lupa = new JButton(new ImageIcon("resources/images/lupa.png"));
        lupa.setBorder(BorderFactory.createEmptyBorder());
        lupa.setContentAreaFilled(false);
        lupa.setFocusPainted(false);
        lupa.setToolTipText("Haz clic para buscar");
        Panelbusqueda.add(lupa, BorderLayout.WEST);
        Panelbusqueda.add(busqueda, BorderLayout.CENTER);
        
        panel_artistas_albumes = new JPanel(new GridLayout(2, 1, 10, 10)); 
        panel_artistas_albumes.setBackground(new Color(18, 18, 18));
        panel_artistas_albumes.setBorder(new EmptyBorder(10, 20, 10, 20));

        // PANEL HORIZONTAL DE ÁLBUMES
        albumGrid = new JPanel();
        albumGrid.setLayout(new BoxLayout(albumGrid, BoxLayout.X_AXIS));
        albumGrid.setBackground(new Color(18, 18, 18));
        
        for (Album a:albumes) {
            albumGrid.add(createAlbumPanel(a));
        }
        
        JScrollPane albumScroll = new JScrollPane(albumGrid);
        //Para que sea Horizontal
        albumScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(18, 18, 18)), // Borde del mismo color que el fondo
                "ÁLBUMES",                                             // Título
                TitledBorder.CENTER,                                   // Centrar el título
                TitledBorder.TOP,                                      // Posición en la parte superior
                new Font("Arial", Font.BOLD, 14),                      // Fuente del título
                Color.WHITE                                            // Color del texto
        ));
        albumScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        albumScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        albumScroll.getHorizontalScrollBar().setUnitIncrement(16);
        albumScroll.setBackground(new Color(18, 18, 18));
     
        // PANEL HORIZONTAL DE ARTISTAS
        artistaGrid = new JPanel();
        artistaGrid.setLayout(new BoxLayout(artistaGrid, BoxLayout.X_AXIS));
        artistaGrid.setBackground(new Color(18, 18, 18));

        for (Artista a: artistas) {
            artistaGrid.add(createArtistaPanel(a));
        }
        

        JScrollPane artistaScroll = new JScrollPane(artistaGrid);
        artistaScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(18, 18, 18)), // Borde del mismo color que el fondo
                "ARTISTAS",                                             // Título
                TitledBorder.CENTER,                                   // Centrar el título
                TitledBorder.TOP,                                      // Posición en la parte superior
                new Font("Arial", Font.BOLD, 14),                      // Fuente del título
                Color.WHITE                                            // Color del texto
        ));
        artistaScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        artistaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        artistaScroll.getHorizontalScrollBar().setUnitIncrement(16);
        artistaScroll.setBackground(new Color(18, 18, 18));

     // Agregar los JScrollPane al panel principal
        panel_artistas_albumes.add(albumScroll);
        panel_artistas_albumes.add(artistaScroll);

        // Agregar los componentes al JFrame
        add(Panelbusqueda, BorderLayout.NORTH);
        add(panel_artistas_albumes, BorderLayout.CENTER);
        
        crearPanelLateral("NombreUsuario", "resources/images/user_icon.png", user1);
        
        // Agregar el panel lateral y el panel principal
        add(sidebar, BorderLayout.WEST);

// -----------------------------------------------------------------------------------
     // KeyListener --> Ctrl + F
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_F) {
                	JPanel panel = new JPanel();
                    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                    JTextField tituloField = new JTextField();
                    JComboBox<String> colorComboBox = new JComboBox<>(new String[]{"Rojo", "Azul", "Verde", "Amarillo", "Rosa", "Naranja","Predeterminado"});

                    panel.add(new JLabel("Introduce el título de la playlist:"));
                    panel.add(tituloField);
                    panel.add(new JLabel("Selecciona un color para la playlist:"));
                    panel.add(colorComboBox);
                    
                    
                    int result = JOptionPane.showConfirmDialog(
                            VentanaAlbumArtista.this,
                            panel,
                            "Crear Playlist",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

                    if (result == JOptionPane.OK_OPTION) {
                        String tituloPlaylist = tituloField.getText().trim();
                        String colorSeleccionado = (String) colorComboBox.getSelectedItem();
                        String rutaFoto = "resources/images/" + colorSeleccionado +".png";

                        if (tituloPlaylist != null && !tituloPlaylist.isEmpty()) {
                            boolean success = gestorBD.crearPlaylist(tituloPlaylist, colorSeleccionado, rutaFoto, gestorBD.getIdUsuarioByUsuario(user.getUsuario()));
                            actualizarVistaPlaylists();
                            if (success) {
                            	
                                JOptionPane.showMessageDialog(
                                        VentanaAlbumArtista.this,
                                        "Se ha creado la playlist: " + tituloPlaylist + " con el color " + colorSeleccionado,
                                        "Playlist Creada",
                                        JOptionPane.INFORMATION_MESSAGE
                                );
                            } else {
                                JOptionPane.showMessageDialog(
                                        VentanaAlbumArtista.this,
                                        "Hubo un error al guardar la playlist.",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE
                                );
                            }
                        } else {
                            JOptionPane.showMessageDialog(
                                    VentanaAlbumArtista.this,
                                    "El título no puede estar vacío.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
            }
        });

        // Hacer que el foco esté en el JFrame para capturar teclas
        setFocusable(true);
        setVisible(true);
    

// -----------------------------------------------------------------------------------
    }

 
    
	private JPanel createAlbumPanel(Album album) {
        JPanel albumPanel = new JPanel(new BorderLayout());
        albumPanel.setBackground(new Color(24, 24, 24));
        albumPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 3, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10) 
        ));
        albumPanel.setPreferredSize(new Dimension(200, 200));

        JLabel albumCover = new JLabel();
        albumCover.setIcon(new ImageIcon(new ImageIcon(album.getRutaFoto())
                .getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        albumCover.setHorizontalAlignment(SwingConstants.CENTER);

        albumCover.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                albumPanel.setBackground(new Color(34, 34, 34));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                albumPanel.setBackground(new Color(24, 24, 24));
            }
            //Saltar a la clase VentanaListaCanciones
            @Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==1) { 
	                  VentanaListaCanciones vListaCanciones = new VentanaListaCanciones(false, album, user, gestorBD);
	                  vListaCanciones.setVisible(true);
	                  VentanaAlbumArtista.this.dispose(); 
				}
			}
        });

        JLabel albumLabel = new JLabel(album.getTitulo(), SwingConstants.CENTER);
        albumLabel.setForeground(Color.WHITE);
        albumLabel.setBorder(new EmptyBorder(10, 0, 0, 0));

        albumPanel.add(albumCover, BorderLayout.CENTER);
        albumPanel.add(albumLabel, BorderLayout.SOUTH);
        return albumPanel;
    }

    private JPanel createArtistaPanel(Artista artista) {
        JPanel artistaPanel = new JPanel(new BorderLayout());
        artistaPanel.setBackground(new Color(24, 24, 24));
        artistaPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 3, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10) 
        ));
        artistaPanel.setPreferredSize(new Dimension(200, 200));

        JLabel artistaCover = new JLabel();
        artistaCover.setIcon(new ImageIcon(new ImageIcon(artista.getRutaFoto())
                .getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        artistaCover.setHorizontalAlignment(SwingConstants.CENTER);

        artistaCover.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                artistaPanel.setBackground(new Color(34, 34, 34));  // Cambio de color al pasar el mouse
            }

            @Override
            public void mouseExited(MouseEvent e) {
                artistaPanel.setBackground(new Color(24, 24, 24));  // Volver al color original
            }
          //Saltar a la clase VentanaListaCanciones
            @Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==1) {
	                VentanaListaCanciones vListaCanciones =new VentanaListaCanciones(false, artista, user, gestorBD);
	                vListaCanciones.setVisible(true);
	                VentanaAlbumArtista.this.dispose(); 
				}
			}
        });

        JLabel artistaLabel = new JLabel(artista.getNombre(), SwingConstants.CENTER);
        artistaLabel.setForeground(Color.WHITE);
        artistaLabel.setBorder(new EmptyBorder(10, 0, 0, 0));

        artistaPanel.add(artistaCover, BorderLayout.CENTER);
        artistaPanel.add(artistaLabel, BorderLayout.SOUTH);
        return artistaPanel;
    }
    
    
    private void buscarAlbumesArtistas() {
    	albumGrid.removeAll();
        artistaGrid.removeAll();
        
        albumGrid.setLayout(new BoxLayout(albumGrid, BoxLayout.X_AXIS));
        albumGrid.setBackground(new Color(18, 18, 18));

        albumes.forEach(a -> {
            Artista artista = gestorBD.getArtistaByID(a.getArtistaID()); 
            if (a.getTitulo().toLowerCase().contains(this.busqueda.getText().toLowerCase()) ||
                (artista != null && artista.getNombre().toLowerCase().contains(this.busqueda.getText().toLowerCase()))) {
                albumGrid.add(createAlbumPanel(a));
            }
        });

        artistaGrid.setLayout(new BoxLayout(artistaGrid, BoxLayout.X_AXIS));
        artistaGrid.setBackground(new Color(18, 18, 18));

        artistas.forEach(a -> {
            if (a.getNombre().toLowerCase().contains(this.busqueda.getText().toLowerCase())) {
                artistaGrid.add(createArtistaPanel(a));
            }
        });
		albumGrid.revalidate();
	    albumGrid.repaint();
	    artistaGrid.revalidate();
	    artistaGrid.repaint();
	}
    private void crearPanelLateral(String nombreUsuario, String rutaFotoUsuario, Usuario user) {
        // Panel lateral
        sidebar = new JPanel();
        sidebar.setLayout(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBackground(new Color(24, 24, 24));
        
        // Panel para la sección del usuario
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBackground(new Color(32, 32, 32));
        userPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Foto del usuario
        JLabel userIcon = new JLabel();
        ImageIcon icon = new ImageIcon(new ImageIcon("resources/images/user.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        userIcon.setIcon(icon);
        userIcon.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Nombre del usuario
        JLabel userNameLabel = new JLabel(user.getNombre(), SwingConstants.CENTER);
        userNameLabel.setForeground(Color.WHITE);
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Agregar componentes al panel de usuario
        userPanel.add(userIcon, BorderLayout.WEST);
        userPanel.add(userNameLabel, BorderLayout.CENTER);

        // Panel de navegación
        JPanel navPanel = new JPanel();
        navPanel.setBackground(new Color(24, 24, 24));
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        
        // Botones de navegación
        navPanel.add(crearBotonNavegacion("Inicio", "resources/images/casa.png", false));
        navPanel.add(crearBotonNavegacion("Explorar", "resources/images/explorar.png", false));
        
        JLabel playlistsLabel = new JLabel("Listas de Reproducción");
        playlistsLabel.setForeground(Color.GRAY);
        playlistsLabel.setBorder(new EmptyBorder(10, 0, 5, 0));
        navPanel.add(playlistsLabel);
        
        navPanel.add(crearBotonNavegacion("Mis Favoritas", "resources/images/estrella.png", true));
        
        initializePremiumLabel();
        navPanel.add(premiumLabel);

        JLabel playlists = new JLabel("Mis Playlists");
        playlists.setForeground(Color.GRAY);
        playlists.setBorder(new EmptyBorder(10, 0, 5, 0));
        navPanel.add(playlists);
        playlists.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(null, "Si quieres crear una playlist, pulsa CTRL+F");
			}
        });
        panelPlaylists = new JPanel();
        panelPlaylists.setLayout(new BoxLayout(panelPlaylists, BoxLayout.Y_AXIS));
        panelPlaylists.setBackground(new Color(30, 30, 30)); // Ajusta el color de fondo
        panelPlaylists.setBorder(new EmptyBorder(10, 10, 10, 10));
        actualizarVistaPlaylists();
        JScrollPane scrollPane = new JScrollPane(panelPlaylists);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(200, 300)); // Ajusta el tamaño del scroll
            
        // Añadir el JScrollPane al panel de navegación
        navPanel.add(scrollPane);
        //Añadir Boton Que genera una playlist random, llamando a la clase Recursividad
        JButton btnRandomPlaylist= crearBotonNavegacion("Random Playlist", null, false);
        btnRandomPlaylist.addActionListener((e)->{
        	JPanel paneltxt= new JPanel(new GridLayout(4, 1));
        	paneltxt.add(new JLabel("Introduce el titulo de la playlist:"));
        	JTextField txt= new JTextField();
        	paneltxt.add(txt);
        	paneltxt.add(new JLabel("Introduce los minutos que quieres que dure la playlist:"));
        	JTextField txtmin= new JTextField();
        	paneltxt.add(txtmin);

        	int result = JOptionPane.showConfirmDialog(
                    VentanaAlbumArtista.this,
                    paneltxt,
                    "Crear una Playlist al azar",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );
        	if(result== JOptionPane.OK_OPTION) {
        		List<Cancion> lcanRandom=Recursividad.combinacionAleatoria(gestorBD.getCanciones(), Integer.parseInt(txtmin.getText()));
        		gestorBD.crearPlaylist(txt.getText(), "Predeterminado", "", gestorBD.getIdUsuarioByUsuario(user.getUsuario()));
        		for(Cancion c: lcanRandom) { 
        			gestorBD.insertarCancionPlayList(c.getId(), gestorBD.getIdPlayListByNombre(txt.getText()));
        			actualizarVistaPlaylists();
        		}
        	}
        });
        navPanel.add(btnRandomPlaylist);
        navPanel.revalidate();
        navPanel.repaint();
        // Añadir secciones al sidebar
        sidebar.add(userPanel, BorderLayout.NORTH);
        sidebar.add(navPanel, BorderLayout.CENTER);
        
         //Creamos el panel para premium
        JPanel premiumAd = new JPanel();
        premiumAd.setLayout(new BorderLayout());
        premiumAd.setBackground(Color.BLUE); 

        // Texto del anuncio
        JLabel adText = new JLabel("<html><center>¡Disfruta sin anuncios!<br>Actualízate a Premium</center></html>");
        adText.setForeground(Color.WHITE);
        adText.setFont(new Font("Arial", Font.BOLD, 14));
        adText.setHorizontalAlignment(SwingConstants.CENTER);
        premiumAd.add(adText, BorderLayout.CENTER);

        // Botón de actualización
        JButton upgradeButton = new JButton("Actualizar ahora");
        upgradeButton.setFont(new Font("Arial", Font.BOLD, 12));
        upgradeButton.setForeground(Color.WHITE);
        upgradeButton.setBackground(new Color(24, 24, 24));
        upgradeButton.setFocusPainted(false);
        upgradeButton.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        upgradeButton.setToolTipText("Haz clic para actualizar a Premium");

        upgradeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                upgradeButton.setBackground(new Color(40, 40, 40)); 
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "¿Estás seguro de que deseas actualizar a Premium?",
                    "Confirmación de actualización",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );

                if (confirm == JOptionPane.OK_OPTION) {
                    String cuenta = JOptionPane.showInputDialog(null, "INTRODUCIR NUMERO DE CUENTA", "IBAN:", JOptionPane.PLAIN_MESSAGE);
                    gestorBD.updatePremium(gestorBD.getIdUsuarioByUsuario(user.getNombre()));
                    if (cuenta != null &&gestorBD.getIfPremiumByUsuario(user.getNombre())==1) {
                        JOptionPane.showMessageDialog(null, "Cuenta registrada: " + cuenta + "\n¡Actualización a premium exitosa!");
                        
                        // Marca al usuario como premium y cambia el borde
                        gestorBD.updatePremium(gestorBD.getIdUsuarioByUsuario(user.getNombre()));
                        cambiosPremium();                        
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                upgradeButton.setBackground(new Color(24, 24, 24)); // Volver al color original
            }
        });

        premiumAd.add(upgradeButton, BorderLayout.SOUTH);

        // Añadir el anuncio premium al sur del sidebar
        sidebar.add(premiumAd, BorderLayout.SOUTH);
        cambiosPremium();//Si es premium se ve reflejado en la pantalla
    }

    private JButton crearBotonNavegacion(String texto, String iconoRuta, boolean favorito) {
        JButton button = new JButton(texto);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(200, 40));
        button.setMaximumSize(new Dimension(200, 40));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(50,50,50));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        ImageIcon icon = new ImageIcon(new ImageIcon(iconoRuta).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        button.setIcon(icon);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(45, 45, 45));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 50, 50));
            }
        });
        
        if (favorito) {
        	button.addMouseListener(new MouseAdapter() {
        		 @Override
     			public void mouseClicked(MouseEvent e) {
     				if(e.getClickCount()==1) {
    	                VentanaListaCanciones vListaCanciones =new VentanaListaCanciones(true, null, user, gestorBD);
    	                vListaCanciones.setVisible(true);
    	                VentanaAlbumArtista.this.dispose();  
     				}
     			}
        	});
        }
        return button;
    }
   private void cambiosPremium() {
	   if(gestorBD.getIfPremiumByUsuario(user.getNombre())==1) {
           getRootPane().setBorder(BorderFactory.createLineBorder(new Color(0, 122, 255), 5)); // Borde azul de 5 px para premium       
            premiumLabel.setVisible(true);
	   }
   }
    private void initializePremiumLabel() {
        premiumLabel = new JLabel("PREMIUM", SwingConstants.CENTER);
        premiumLabel.setOpaque(true);
        premiumLabel.setBackground(new Color(0, 122, 255)); // Azul distintivo
        premiumLabel.setForeground(Color.WHITE);             // Texto en blanco
        premiumLabel.setFont(new Font("Arial", Font.BOLD, 12));
        premiumLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        premiumLabel.setVisible(false); // Solo se mostrará si el usuario es premium
    }
    
    private JPanel createPlaylistPanel(PlayList playlist) {
    	JPanel playlistPanel = new JPanel();
        playlistPanel.setLayout(new BorderLayout());
        playlistPanel.setPreferredSize(new Dimension(200, 40));
        playlistPanel.setMaximumSize(new Dimension(200, 40));
        playlistPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        playlistPanel.addMouseListener(new MouseAdapter() {            
            @Override
			public void mouseClicked(MouseEvent e) {
            	if (e.getButton() == MouseEvent.BUTTON3) {
            		// Dar opcion de eliminar playList o cambiar Nombre
    				if (e.getClickCount()==1) {
    			        JTextField nombre = new JTextField(playlist.getTitulo());
    					JComboBox<String> optionsBox = new JComboBox<>(new String[] { "Cambiar Nombre", "Eliminar Playlist"});
    			        
    					optionsBox.addActionListener(event -> {
    			            if (optionsBox.getSelectedIndex() == 1) { 
    			                nombre.setEnabled(false); 
    			            } else {
    			                nombre.setEnabled(true);
    			            }
    			        });
    					
    					JPanel panel = new JPanel();
    			        panel.setLayout(new GridLayout(2, 2));
    			        panel.add(new JLabel("Nombre:"));
    			        panel.add(nombre);
    			        panel.add(new JLabel("Seleccione una accion:"));
    			        panel.add(optionsBox);		        

    			        int result = JOptionPane.showConfirmDialog(
                                VentanaAlbumArtista.this,
                                panel,
                                "Accion sobre PlayList",
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.PLAIN_MESSAGE
                        );

                        if (result == JOptionPane.OK_OPTION) {
                        	if (optionsBox.getSelectedIndex() == 0) {
        			        	playlist.setTitulo(nombre.getText());
        			        	gestorBD.cambiarNombrePlayList(nombre.getText(), playlist.getId());
        			        } else {
        			        	gestorBD.eliminarPlayList(playlist.getId());
        			        }
                        	actualizarVistaPlaylists();
                        }
    				}
            	} else if(e.getClickCount()==1) { //Saltar a la clase VentanaListaCanciones
					VentanaListaCanciones vListaCanciones =new VentanaListaCanciones(false, playlist, user, gestorBD);
	                vListaCanciones.setVisible(true);
	                VentanaAlbumArtista.this.dispose(); 
				}
			}
        });
        // Configurar el color del fondo basado en el atributo "color"
        colorMapa();//cargamos mapa con los colores
        String color = playlist.getColor();
        Color colorfondo = mapa.getOrDefault(color, new Color(50, 50, 50));
        playlistPanel.setBackground(colorfondo);
        
        // Añadir un JLabel para el título de la playlist
        JLabel tituloLabel = new JLabel(playlist.getTitulo());
        tituloLabel.setForeground(Color.BLACK);
        tituloLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        // Añadir un ícono si la playlist tiene una ruta de foto
        if (playlist.getRutaFoto() != null && !playlist.getRutaFoto().isEmpty()) {
            ImageIcon icono = new ImageIcon(new ImageIcon("resources/images/playlist.png")
                    .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
            tituloLabel.setIcon(icono);
        }
        // Agregar el JLabel al panel de la playlist
        playlistPanel.add(tituloLabel, BorderLayout.CENTER);
        playlistPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, "Has seleccionado la playlist: " + playlist.getTitulo());
            }
        });
        panelPlaylists.add(playlistPanel);        
        return playlistPanel;        
    }
    
    
    private void actualizarVistaPlaylists() {
    	panelPlaylists.removeAll();
        for (PlayList p : gestorBD.getPlayListByIdUsuario(gestorBD.getIdUsuarioByUsuario(user.getUsuario()))) {
        	panelPlaylists.add(createPlaylistPanel(p));
        }
        panelPlaylists.revalidate(); 
        panelPlaylists.repaint();
    }
    private void colorMapa(){
    	mapa.put("Rojo", Color.RED);
    	mapa.put("Azul", Color.BLUE);
    	mapa.put("Verde", Color.GREEN);
    	mapa.put("Amarillo", Color.YELLOW);
    	mapa.put("Rosa", Color.PINK);
    	mapa.put("Naranja", Color.ORANGE);
    }
    public class RecursiveBubbleSort {
        public static void bubbleSortAlbums(List<Album> albums, int n) {
            if (n == 1) return;

            for (int i = 0; i < n - 1; i++) {
                if (albums.get(i).getAnyo() > albums.get(i + 1).getAnyo()) {
                    Album temp = albums.get(i);
                    albums.set(i, albums.get(i + 1));
                    albums.set(i + 1, temp);
                }
            }

            bubbleSortAlbums(albums, n - 1);
        }
    }
}