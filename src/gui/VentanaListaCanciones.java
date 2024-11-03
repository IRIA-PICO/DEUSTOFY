package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
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
	public VentanaListaCanciones(Album album) {
		
		//Configuracion del JFrame
		setTitle("DEUSTOFY");
		ImageIcon imagen = new ImageIcon("resources/images/logo.png");
        setIconImage(imagen.getImage());
		setSize(800, 600);
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
        
        JPanel panelNombreAlbum = new JPanel(new BorderLayout());
       
        ImageIcon imgAlbum = new ImageIcon(album.getRutaFoto());
        Image img = imgAlbum.getImage();
        ImageIcon imagenAlbum = new ImageIcon(img.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
        JLabel labelImagen = new JLabel(imagenAlbum);
        panelNombreAlbum.add(labelImagen, BorderLayout.WEST);
        
        Label nombreAlbum = new Label(album.getTitulo());
        panelNombreAlbum.add(nombreAlbum, BorderLayout.CENTER);
       
        panelCentro.add(panelNombreAlbum, BorderLayout.NORTH);
        
        JScrollPane panelCanciones = new JScrollPane(this.tablaCanciones);
        this.tablaCanciones.setFillsViewportHeight(true);
        panelCentro.add(panelCanciones, BorderLayout.CENTER);
        
        add(panelCentro, BorderLayout.CENTER);
        // Creamos el panel de búsqueda  
        JPanel panelBusqueda = new JPanel(new BorderLayout());
	    panelBusqueda.setBorder(new EmptyBorder(15, 20, 15, 20));
	    panelBusqueda.setBackground(new Color(40, 40, 40));
	    
	    // Barra de búsqueda 
        JTextField busqueda = new JTextField("Buscar canciones...");
        busqueda.setForeground(Color.GRAY);
        busqueda.setPreferredSize(new Dimension(800, 35));
        busqueda.setBackground(new Color(24, 24, 24));
        busqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        busqueda.addFocusListener(new FocusAdapter() {
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
        panelBusqueda.add(busqueda, BorderLayout.CENTER);
        add(panelBusqueda, BorderLayout.NORTH);
        
	}
	
	private void initTable() {
		Vector<String> cabeceraCanciones = new Vector<String>(Arrays.asList("TITULO", "DURACIÓN", "ARTISTA"));
		this.modeloDatosCanciones = new DefaultTableModel(new Vector<Vector<Object>>(), cabeceraCanciones);
		this.tablaCanciones = new JTable(this.modeloDatosCanciones) {
			private static final long serialVersionUID = 1L;
			
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		TableCellRenderer cellRenderer = (table, value, isSelected, hasFocus, row, column) -> {
			JLabel result = new JLabel(value.toString());
			result.setBackground(table.getBackground());
			result.setForeground(table.getForeground());
			
			if (value instanceof Number) {
				result.setHorizontalAlignment(JLabel.CENTER);
			} else if (value instanceof Artista) {
				result.setText(((Artista)value).getNombre());
			} else {
				result.setText(value.toString());
			}
			// Añadir aqui modificaciones de color por filas/columnas o si esta seleccioando
			result.setOpaque(true);
			return result;
		};
		
		TableCellRenderer headerRenderer = (table, value, isSelected, hasFocus, row, column) -> {
			JLabel result = new JLabel(value.toString());
			result.setBackground(table.getBackground());
			result.setForeground(table.getForeground());
			result.setOpaque(true);
			return result;
		};
		
		this.tablaCanciones.setRowHeight(20);
		this.tablaCanciones.getTableHeader().setReorderingAllowed(false);
		this.tablaCanciones.getTableHeader().setResizingAllowed(false);
		this.tablaCanciones.getTableHeader().setDefaultRenderer(headerRenderer);
		this.tablaCanciones.setDefaultRenderer(Object.class, cellRenderer);
		
		this.tablaCanciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		this.tablaCanciones.getSelectionModel().addListSelectionListener(e -> {
//			if (tablaCanciones.getSelectedRow() != -1) {
//				// Abre la ventana de esa cancion
//			}
//		});
	}
	private void loadCanciones() {
		this.modeloDatosCanciones.setRowCount(0);
		this.canciones.forEach(c -> this.modeloDatosCanciones.addRow(new Object[] {c.getTitulo(), c.getDuracion(), c.getArtista()}));
	}
	
	public static void main(String[] args) {
		List<Cancion> cancionesPrueba = new ArrayList<>();
		Album albumPrueba = new Album("Nadie sabe lo que va a pasar mañana", 2760, Genero.TRAP, null, cancionesPrueba, "resources/images/nadie_sabe.jpg");
		Artista artistaPrueba = new Artista(1L, "Bad Bunny", Genero.TRAP);
		for (int i = 1; i <= 10; i++) {
			albumPrueba.getListacanciones().add(new Cancion("cancion" + i, (Math.random()*4 + 1)*60, albumPrueba, artistaPrueba));
		}
		SwingUtilities.invokeLater(() -> {
			VentanaListaCanciones ventanaListaCanciones = new VentanaListaCanciones(albumPrueba);
			ventanaListaCanciones.setVisible(true);
	    });
		
	}
}