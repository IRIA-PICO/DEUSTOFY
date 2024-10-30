package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VentanaAlbumArtista extends JFrame {

    private static final long serialVersionUID = 1L;

    public VentanaAlbumArtista() {
        setTitle("DEUSTOFY");
        ImageIcon imagen = new ImageIcon("resources/images/logo.png");
        setIconImage(imagen.getImage());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Barra de búsqueda
        JTextField busqueda = new JTextField("Buscar artistas, álbumes...");
        busqueda.setFont(new Font("Gotham", Font.BOLD, 16));
        busqueda.setForeground(Color.GRAY);
        busqueda.setPreferredSize(new Dimension(800, 35));
        busqueda.setBackground(new Color(24, 24, 24));
        busqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JPanel Panelbusqueda = new JPanel(new BorderLayout());
        Panelbusqueda.setBorder(new EmptyBorder(15, 20, 15, 20));
        Panelbusqueda.setBackground(new Color(40, 40, 40));
        JLabel imagen_lupa = new JLabel(new ImageIcon(new ImageIcon("resources/images/lupa.png")
                .getImage().getScaledInstance(26, 24, Image.SCALE_SMOOTH)));
        imagen_lupa.setBorder(new EmptyBorder(0,10,0,10));
        Panelbusqueda.add(imagen_lupa, BorderLayout.WEST);
        Panelbusqueda.add(busqueda, BorderLayout.CENTER);

        // PANEL GRID ALBUM
        JPanel albumArtistaGrid = new JPanel(new GridLayout(2, 3, 15, 15)); // Dos filas: una para álbumes y otra para artistas
        albumArtistaGrid.setBackground(new Color(18, 18, 18));
        albumArtistaGrid.setBorder(new EmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(albumArtistaGrid);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(18, 18, 18));

        for (int i = 0; i < 3; i++) {
            JPanel albumPanel = createAlbumPanel("Álbum " + (i + 1));
            albumArtistaGrid.add(albumPanel);
        }

        for (int i = 0; i < 3; i++) {
            JPanel artistaPanel = createArtistaPanel("Artista " + (i + 1));
            albumArtistaGrid.add(artistaPanel);
        }

        add(Panelbusqueda, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createAlbumPanel(String albumName) {
        JPanel albumPanel = new JPanel(new BorderLayout());
        albumPanel.setBackground(new Color(24, 24, 24));
        albumPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel albumCover = new JLabel();
        albumCover.setIcon(new ImageIcon(new ImageIcon("\resources\\images\\nadie_sabe.jpg\"")
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

        JLabel albumLabel = new JLabel(albumName, SwingConstants.CENTER);
        albumLabel.setForeground(Color.WHITE);
        albumLabel.setBorder(new EmptyBorder(10, 0, 0, 0));

        albumPanel.add(albumCover, BorderLayout.CENTER);
        albumPanel.add(albumLabel, BorderLayout.SOUTH);
        return albumPanel;
    }

    private JPanel createArtistaPanel(String artistaName) {
        JPanel artistaPanel = new JPanel(new BorderLayout());
        artistaPanel.setBackground(new Color(24, 24, 24));
        artistaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel artistaCover = new JLabel();
        artistaCover.setIcon(new ImageIcon(new ImageIcon("https://via.placeholder.com/150")
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

        JLabel artistaLabel = new JLabel(artistaName, SwingConstants.CENTER);
        artistaLabel.setForeground(Color.WHITE);
        artistaLabel.setBorder(new EmptyBorder(10, 0, 0, 0));

        artistaPanel.add(artistaCover, BorderLayout.CENTER);
        artistaPanel.add(artistaLabel, BorderLayout.SOUTH);
        return artistaPanel;
    }

    public static void main(String[] args) {
        // Ejecutar la aplicación
        SwingUtilities.invokeLater(() -> new VentanaAlbumArtista());
    }
}