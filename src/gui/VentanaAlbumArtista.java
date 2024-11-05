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
}