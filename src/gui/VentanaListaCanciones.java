package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import domain.Album;
import domain.Artista;
import domain.Cancion;
import domain.Genero;

public class VentanaListaCanciones extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private List<Cancion> canciones;
	private JTable tablaCanciones;
	private DefaultTableModel modeloDatosCanciones;
	private JTextField busqueda;
	
	public VentanaListaCanciones(Album album) {
		Color colorFondo = new Color(40, 40, 40);
        Font fuenteTexto = new Font("Gotham", Font.BOLD, 16);
		//Configuracion del JFrame
		setTitle("DEUSTOFY");
		ImageIcon imagen = new ImageIcon("resources/images/logo.png");
        setIconImage(imagen.getImage());
		setSize(800, 600);
		setBackground(colorFondo);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
     
        // Asignamos la lista de canciones a la variable local canciones
        this.canciones = album.getListacanciones();
        
        // Inicializar tabla y cargar canciones
        this.initTable();
        this.loadCanciones();
        // BorderLayout para poner la lista de canciones y el album
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(colorFondo);
        
        JPanel panelNombreAlbum = new JPanel(new BorderLayout());
        panelNombreAlbum.setBackground(colorFondo);
        panelNombreAlbum.setForeground(Color.WHITE);
        panelNombreAlbum.setFont(fuenteTexto);
        
        ImageIcon imgAlbum;
        JLabel labelImagen;
        try {
            imgAlbum = new ImageIcon(album.getRutaFoto());
            Image img = imgAlbum.getImage();
            
            if (img == null) {
                throw new Exception("La imagen del álbum no se ha podido cargar.");
            }
            
            labelImagen = new JLabel(new ImageIcon(img.getScaledInstance(140, 140, Image.SCALE_SMOOTH)));            
        } catch (Exception e) {
            System.err.println("Error cargando la imagen del álbum");
            imgAlbum = new ImageIcon("resources/images/logo.png");
            Image img = imgAlbum.getImage();
            labelImagen = new JLabel(new ImageIcon(img.getScaledInstance(140, 140, Image.SCALE_SMOOTH)));
        }

        // Agregar el JLabel al panel
        panelNombreAlbum.add(labelImagen, BorderLayout.WEST);
        
        Label nombreAlbum = new Label(album.getTitulo());
        nombreAlbum.setAlignment(Label.CENTER);
        panelNombreAlbum.add(nombreAlbum, BorderLayout.CENTER);
       
        panelCentro.add(panelNombreAlbum, BorderLayout.NORTH);
        
        JScrollPane panelCanciones = new JScrollPane(this.tablaCanciones);
        this.tablaCanciones.setFillsViewportHeight(true);
        panelCanciones.setBackground(colorFondo);
        panelCentro.add(panelCanciones, BorderLayout.CENTER);
        
        add(panelCentro, BorderLayout.CENTER);
        
        // Creamos el panel de búsqueda  
        JPanel panelBusqueda = new JPanel(new BorderLayout());
	    panelBusqueda.setBorder(new EmptyBorder(15, 20, 15, 20));
	    panelBusqueda.setBackground(colorFondo);
	    
	    // Barra de búsqueda 
        this.busqueda = new JTextField("Buscar canciones");
        this.busqueda.setForeground(Color.GRAY);
        this.busqueda.setFont(fuenteTexto);
        this.busqueda.setPreferredSize(new Dimension(800, 35));
        this.busqueda.setBackground(new Color(24, 24, 24));
        this.busqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        this.busqueda.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (busqueda.getText().equals("Buscar canciones")) {
                	busqueda.setForeground(Color.GRAY);
                    busqueda.setText("");
                }
            }
            public void focusLost(FocusEvent e) {
                if (busqueda.getText().isEmpty()) {
                    busqueda.setForeground(Color.GRAY);
                    busqueda.setText("Buscar canciones");
                }
            }
        });
        
        this.busqueda.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (!busqueda.getText().equals("Buscar canciones")) {
        			buscarCanciones();
        		}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				buscarCanciones();
				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
        });
        
        panelBusqueda.add(busqueda, BorderLayout.CENTER);
        add(panelBusqueda, BorderLayout.NORTH);
        
        
        
	}
	
	private void initTable() {
		Vector<String> cabeceraCanciones = new Vector<String>(Arrays.asList("#", "TITULO", "ARTISTA", "DURACIÓN"));
		this.modeloDatosCanciones = new DefaultTableModel(new Vector<Vector<Object>>(), cabeceraCanciones);
		this.tablaCanciones = new JTable(this.modeloDatosCanciones) {
			private static final long serialVersionUID = 1L;
			
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		
		this.tablaCanciones.setRowHeight(40);
		this.tablaCanciones.getTableHeader().setReorderingAllowed(false);
		this.tablaCanciones.getTableHeader().setResizingAllowed(false);
		this.tablaCanciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		this.tablaCanciones.getSelectionModel().addListSelectionListener(e -> {
//			if (tablaCanciones.getSelectedRow() != -1) {
//				// Abre la ventana de esa cancion
//			}
//		});
		this.tablaCanciones.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
	            JTable table = (JTable) e.getSource();
	            int row = table.rowAtPoint(e.getPoint());
	            if (row != -1) {
	                table.setRowSelectionInterval(row, row);
	                table.repaint();
	            }
	        }

	        @Override
	        public void mouseExited(MouseEvent e) {
	            JTable table = (JTable) e.getSource();
	            table.clearSelection();
	            table.repaint();
	        }		});
		
		TableCellRenderer cellRenderer = (table, value, isSelected, hasFocus, row, column) -> {
			JLabel result = new JLabel(value.toString());
			result.setForeground(Color.WHITE);
			result.setOpaque(true);
			
			if (table.isRowSelected(row)) {
                result.setBackground(new Color(60, 60, 60));
            } else {
                result.setBackground(new Color(40, 40, 40));
            }
			
			if (value instanceof Number) {
				result.setHorizontalAlignment(JLabel.CENTER);
			} else if (value instanceof Artista) {
				result.setText(((Artista)value).getNombre());
			} else if (column == 3) {
				result.setHorizontalAlignment(JLabel.CENTER);
				result.setText(value.toString());
			} else {
				result.setText(value.toString());
			}
			
			return result;
		};
				
		TableCellRenderer headerRenderer = (table, value, isSelected, hasFocus, row, column) -> {
			JLabel result = new JLabel(value.toString());
			switch (value.toString()) {
				case "#":
					result.setHorizontalAlignment(JLabel.CENTER);
				case "DURACIÓN":
					result.setHorizontalAlignment(JLabel.CENTER);
			}
			result.setBackground(new Color(40, 40, 40));
			result.setFont(new Font("Gotham", Font.BOLD, 16));
			result.setForeground(Color.WHITE);
			result.setOpaque(true);
			return result;
		};
		
		this.tablaCanciones.getTableHeader().setDefaultRenderer(headerRenderer);
		this.tablaCanciones.setDefaultRenderer(Object.class, cellRenderer);
	}
	
	private void loadCanciones() {
		this.modeloDatosCanciones.setRowCount(0);
		this.canciones.forEach(c -> this.modeloDatosCanciones.addRow(new Object[] {
				this.modeloDatosCanciones.getRowCount() + 1, c.getTitulo(), c.getArtista(), formatoDuracion(c)}));
	}
	
	private static String formatoDuracion(Cancion c) {
		int minutes = (int) (c.getDuracion()/60);
		int seconds = (int) (c.getDuracion()%60);
		String secondsStr = String.valueOf(seconds);
		if (seconds < 10) {
			secondsStr = "0" + seconds;
		}
		return (minutes + ":" + secondsStr);
	}
	
	private void buscarCanciones() {
		this.modeloDatosCanciones.setRowCount(0);
		
		this.canciones.forEach(c -> {
			if (c.getTitulo().toLowerCase().contains(this.busqueda.getText().toLowerCase())) {
				this.modeloDatosCanciones.addRow(
						new Object[] {this.modeloDatosCanciones.getRowCount() + 1, c.getTitulo(), c.getArtista(), formatoDuracion(c)} );
			}
		});
	}
	
	public static void main(String[] args) {
		List<Cancion> cancionesPrueba = new ArrayList<>();
		Album albumPrueba = new Album("Nadie sabe lo que va a pasar mañana", 2760, Genero.TRAP, null, cancionesPrueba, "resources/images/nadie_sabe.jpg");
		Artista artistaPrueba = new Artista(1, "Bad Bunny", Genero.TRAP);
		for (int i = 1; i <= 30; i++) {
			albumPrueba.getListacanciones().add(new Cancion("cancion" + i, (Math.random()*4 + 1)*60, albumPrueba, artistaPrueba));
		}
		SwingUtilities.invokeLater(() -> {
			VentanaListaCanciones ventanaListaCanciones = new VentanaListaCanciones(albumPrueba);
			ventanaListaCanciones.setVisible(true);
	    });
		
	}
}