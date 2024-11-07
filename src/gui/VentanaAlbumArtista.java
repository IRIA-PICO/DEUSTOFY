package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import domain.Artista;
import domain.Album;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class VentanaAlbumArtista extends JFrame {

    private static final long serialVersionUID = 1L;
    
    private JPanel panel_artistas_albumes;
    private List<Album> albumes;
    private List<Artista> artistas;
    private JTextField busqueda;
    private JPanel albumGrid;
    private JPanel artistaGrid;
    private JPanel sidebar;
    
    public VentanaAlbumArtista(List<Album> albumes1, List<Artista> artistas1) {
    	albumes = albumes1;
    	artistas = artistas1;
    	
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
				// TODO Auto-generated method stub
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
        
        crearPanelLateral("NombreUsuario", "resources/images/user_icon.png");
        
        // Agregar el panel lateral y el panel principal
        add(sidebar, BorderLayout.WEST);

        setVisible(true);
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
					new VentanaListaCanciones(album,albumes, artistas);
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
					new VentanaListaCanciones(artista, albumes, artistas);
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

        this.albumes.forEach(a -> {
            if (a.getTitulo().toLowerCase().contains(this.busqueda.getText().toLowerCase()) ||
                a.getArtista().getNombre().toLowerCase().contains(this.busqueda.getText().toLowerCase())) {
                albumGrid.add(createAlbumPanel(a));
            }
        });

        artistaGrid.setLayout(new BoxLayout(artistaGrid, BoxLayout.X_AXIS));
        artistaGrid.setBackground(new Color(18, 18, 18));

        this.artistas.forEach(a -> {
            if (a.getNombre().toLowerCase().contains(this.busqueda.getText().toLowerCase())) {
                artistaGrid.add(createArtistaPanel(a));
            }
        });
		albumGrid.revalidate();
	    albumGrid.repaint();
	    artistaGrid.revalidate();
	    artistaGrid.repaint();
	}
    private void crearPanelLateral(String nombreUsuario, String rutaFotoUsuario) {
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
        ImageIcon icon = new ImageIcon(new ImageIcon(rutaFotoUsuario).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        userIcon.setIcon(icon);
        userIcon.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Nombre del usuario
        JLabel userNameLabel = new JLabel(nombreUsuario, SwingConstants.CENTER);
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
        navPanel.add(crearBotonNavegacion("Inicio", "resources/images/casa.png"));
        navPanel.add(crearBotonNavegacion("Explorar", "resources/images/explorar.png"));
        navPanel.add(crearBotonNavegacion("Tus Canciones", "resources/images/estrella.png"));
        
        // Añadir secciones al sidebar
        sidebar.add(userPanel, BorderLayout.NORTH);
        sidebar.add(navPanel, BorderLayout.CENTER);
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

        upgradeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                upgradeButton.setBackground(new Color(40, 40, 40)); // Gris oscuro al hacer hover
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
                if (confirm ==JOptionPane.OK_OPTION) {
                	String cuenta = JOptionPane.showInputDialog(null, "INTRODUCIR NUMERO DE CUENTA", "IBAN:", JOptionPane.PLAIN_MESSAGE);
                	if (cuenta != null) {
                		JOptionPane.showMessageDialog(null, "Cuenta registrada: " + cuenta + "\n¡Actualización a premium exitosa!");
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
    }

    private JButton crearBotonNavegacion(String texto, String iconoRuta) {
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

        return button;
    }
}