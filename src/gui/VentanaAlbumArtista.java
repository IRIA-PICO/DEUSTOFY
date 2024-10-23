package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VentanaAlbumArtista extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VentanaAlbumArtista() {
        setTitle("DEUSTOFY");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTextField busqueda = new JTextField("Buscar artistas, álbumes...");
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
                .getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
        Panelbusqueda.add(imagen_lupa, BorderLayout.WEST);
        Panelbusqueda.add(busqueda, BorderLayout.CENTER);
        
        JPanel albumGrid = new JPanel(new GridLayout(0, 3, 15, 15));
        albumGrid.setBackground(new Color(18, 18, 18));
        albumGrid.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(albumGrid);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(new Color(18, 18, 18));

        for (int i = 0; i < 12; i++) {
            JPanel albumPanel = createAlbumPanel("Álbum " + (i + 1));
            albumGrid.add(albumPanel);
        }
        
        add(Panelbusqueda, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

	private JPanel createAlbumPanel(String albumName) {
	    JPanel albumPanel = new JPanel(new BorderLayout());
	    albumPanel.setBackground(new Color(24, 24, 24));
	    albumPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	    // Imagen de álbum
	    JLabel albumCover = new JLabel();
	    albumCover.setIcon(new ImageIcon(new ImageIcon("https://via.placeholder.com/150")
	            .getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
	    albumCover.setHorizontalAlignment(SwingConstants.CENTER);

	    albumCover.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            albumPanel.setBackground(new Color(34, 34, 34));  // Cambio de color al pasar el mouse
	        }

	        @Override
	        public void mouseExited(MouseEvent e) {
	            albumPanel.setBackground(new Color(24, 24, 24));  // Volver al color original
	        }
	    });
	    
	    JLabel albumLabel = new JLabel(albumName, SwingConstants.CENTER);
        albumLabel.setForeground(Color.WHITE);
        albumLabel.setBorder(new EmptyBorder(10, 0, 0, 0));

        albumPanel.add(albumCover, BorderLayout.CENTER);
        albumPanel.add(albumLabel, BorderLayout.SOUTH);
        return albumPanel;
	}
    public static void main(String[] args) {
        // Ejecutar la aplicación
        SwingUtilities.invokeLater(() -> new VentanaAlbumArtista());
    }
}

