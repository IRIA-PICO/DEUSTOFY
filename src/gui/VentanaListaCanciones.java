package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import domain.Album;
import domain.Artista;
import domain.Cancion;


public class VentanaListaCanciones extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private List<Cancion> canciones;
	private JTable tablaCanciones;
	private DefaultTableModel modeloDatosCanciones;
	private JTextField busqueda;
	
	public VentanaListaCanciones(Object albumArtista) {
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
        if (albumArtista instanceof Album) {
        	this.canciones = ((Album) albumArtista).getListacanciones();
        } else if (albumArtista instanceof Artista) {
        	this.canciones = ((Artista) albumArtista).getCanciones();
        }
        
        // Inicializar tabla y cargar canciones
        this.initTable();
        this.loadCanciones();
        // BorderLayout para poner la lista de canciones y el album
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(colorFondo);
        
        JPanel panelNombre = new JPanel(new BorderLayout());
        panelNombre.setBackground(colorFondo);
        panelNombre.setForeground(Color.WHITE);
        panelNombre.setFont(fuenteTexto);
        
        ImageIcon imgAlbumArtista = null;
        JLabel labelImagen = null;
        try {
        	if (albumArtista instanceof Album) {
        		imgAlbumArtista = new ImageIcon(((Album) albumArtista).getRutaFoto());
        	} else if (albumArtista instanceof Artista) {
        		imgAlbumArtista = new ImageIcon(((Artista) albumArtista).getRutaFoto());
        	}
        	Image img = imgAlbumArtista.getImage();
            
            if (img == null) {
                throw new Exception("La imagen del álbum no se ha podido cargar.");
            }
            
            labelImagen = new JLabel(new ImageIcon(img.getScaledInstance(140, 140, Image.SCALE_SMOOTH)));            
        } catch (Exception e) {
            System.err.println("Error cargando la imagen del álbum");
            imgAlbumArtista = new ImageIcon("resources/images/logo.png");
            Image img = imgAlbumArtista.getImage();
            labelImagen = new JLabel(new ImageIcon(img.getScaledInstance(140, 140, Image.SCALE_SMOOTH)));
        }

        // Agregar el JLabel al panel
        panelNombre.add(labelImagen, BorderLayout.WEST);
        Label nombre = null;
        if (albumArtista instanceof Album) {
        	nombre = new Label(((Album) albumArtista).getTitulo());
        } else if (albumArtista instanceof Artista) {
        	nombre = new Label(((Artista) albumArtista).getNombre());
        }
        
        nombre.setAlignment(Label.CENTER);
        panelNombre.add(nombre, BorderLayout.CENTER);
       
        panelCentro.add(panelNombre, BorderLayout.NORTH);
        
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
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
			}
        });
        
        panelBusqueda.add(busqueda, BorderLayout.CENTER);
        add(panelBusqueda, BorderLayout.NORTH);
        
        setVisible(true);
	}
	
	private void initTable() {
		Vector<String> cabeceraCanciones = new Vector<String>(Arrays.asList("#", "TITULO", "FAVORITOS", "ARTISTA", "DURACIÓN"));
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
	        }
	    });
		this.tablaCanciones.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				JTable table = (JTable) e.getSource();
	            int row = table.rowAtPoint(e.getPoint());
	            if (row != -1) {
	                table.setRowSelectionInterval(row, row);
	                table.repaint();
	            }
			}
		});
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
			} else if (column == 4) {
				result.setHorizontalAlignment(JLabel.CENTER);
				result.setText(value.toString());
			} else if (value instanceof Boolean) {
				result.setHorizontalAlignment(JLabel.CENTER);
				result.setText(null);
				ImageIcon imgFavorita = null;
				if ((Boolean) value) {
					imgFavorita = new ImageIcon("resources/images/logo.png");
	        	} else {
	        		imgFavorita = new ImageIcon("resources/images/logo.png");
	        	}
	        	Image img = imgFavorita.getImage();
	            JLabel labelImagen = new JLabel(new ImageIcon(img.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
	            result.setIcon(labelImagen.getIcon());
				
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
				case "FAVORITOS":
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
		
		//Saltar a VentanaCancion cuando se haga click en una canci
		this.tablaCanciones.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2) {
					Point p= e.getPoint();
					int fila= tablaCanciones.rowAtPoint(p);
					int id_cancion= (int) tablaCanciones.getValueAt(fila, 0);
					new VentanaCancion(VentanaListaCanciones.this.canciones, id_cancion -1 );
				}
			}
		});
	//Si presiona ctrl y luego "e" la ventana vuelve a VentanaAlbumartista
		this.tablaCanciones.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_E) {
		            dispose(); 
		            //new VentanaAlbumArtista(null, null); da error
		        }
				
			}
		});
	}
	
	private void loadCanciones() {
		this.modeloDatosCanciones.setRowCount(0);
		this.canciones.forEach(c -> this.modeloDatosCanciones.addRow(new Object[] {
				posicionCancion(c) + 1, c.getTitulo(), c.isFavorita(), c.getArtista(), formatoDuracion(c)}));
	}
	
	private int posicionCancion(Cancion c) {
		for (int i=0; i<canciones.size(); i++) {
			if(canciones.get(i).equals(c)) {
				return i;
			}
		}
		return 0;
	}

	private static String formatoDuracion(Cancion c) {
		int minutes = (int) (c.getDuracion());
		int seconds = (int) ((c.getDuracion() - minutes)*100);
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
						new Object[] {posicionCancion(c) + 1, c.getTitulo(), c.isFavorita(), c.getArtista(), formatoDuracion(c)} );
			}
		});
	}
}