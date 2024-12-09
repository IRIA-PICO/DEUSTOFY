package db;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import domain.Album;
import domain.Artista;
import domain.Cancion;
import domain.Genero;
import domain.PlayList;
import domain.Usuario;


public class GestorBD {
	private final String PROPERTIES_FILE = "resources/config/app.properties";
	private final String CSV_CANCIONES = "resources/data/cancion.csv";
	private final String CSV_ALBUMES = "resources/data/album.csv";
	private final String CSV_ARTISTAS = "resources/data/artista.csv";
	
	private Properties properties;
	private String driverName;
	private String databaseFile;
	private static String connectionString;
	
	private static Logger logger = Logger.getLogger(GestorBD.class.getName());
	
	public GestorBD() {
		try (FileInputStream fis = new FileInputStream("resources/config/logger.properties")) {
			//Inicialización del Logger
			LogManager.getLogManager().readConfiguration(fis);
			
			//Lectura del fichero properties
			properties = new Properties();
			properties.load(new FileReader(PROPERTIES_FILE));
			
			driverName = properties.getProperty("driver");
			databaseFile = properties.getProperty("file");
			connectionString = properties.getProperty("connection");
			
			//Cargar el diver SQLite
			Class.forName(driverName);
		} catch (Exception ex) {
			logger.warning(String.format("Error al cargar el driver de BBDD: %s", ex.getMessage()));
		}
	}

	public void crearBBDD() {
		//Sólo se crea la BBDD si la propiedad initBBDD es true.
		if (properties.get("createBBDD").equals("true")) {
			
			String sql1 = "CREATE TABLE IF NOT EXISTS Album (\n"
	                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
	                + " titulo TEXT NOT NULL,\n"
	                + " genero TEXT NOT NULL,\n"
	                + " rutafoto TEXT, \n"
	                + " id_artista INTEGER NOT NULL,\n"
	                + " fecha TEXT NOT NULL,\n"
	                + " FOREIGN KEY (id_artista) REFERENCES Artista(id)"
	                + ");";
	
			String sql2 = "CREATE TABLE IF NOT EXISTS Artista (\n"
	                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
	                + " nombre TEXT NOT NULL,\n"
	                + " rutafoto TEXT NOT NULL"
	                + ");";
			
			String sql3 = "CREATE TABLE IF NOT EXISTS Cancion (\n"
	                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
	                + " titulo TEXT NOT NULL,\n"
	                + " duracion INTEGER NOT NULL,\n"
	                + " id_artista INTEGER NOT NULL,\n"
	                + " FOREIGN KEY (id_artista) REFERENCES Artista(id)"
	                + ");";
	
			String sql4 = "CREATE TABLE IF NOT EXISTS AlbumCancion (\n"
	                + " album_id INTEGER NOT NULL,\n"
	                + " cancion_id INTEGER NOT NULL,\n"
	                + " posicion INTEGER NOT NULL,\n"
	                + " PRIMARY KEY(album_id, cancion_id),\n"
	                + " FOREIGN KEY (album_id) REFERENCES Album(id) ON DELETE CASCADE,\n"
	                + " FOREIGN KEY (cancion_id) REFERENCES Cancion(id) ON DELETE CASCADE"
	                + ");";
			
			String sql5= "CREATE TABLE IF NOT EXISTS PlayList (\n"
	                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
					+ " nomPlayList TEXT NOT NULL,\n"
	                + " color TEXT NOT NULL,\n"
	                + " rutaFoto TEXT NOT NULL,\n"
                    + " usuario_id INTEGER NOT NULL,\n"
                    + " FOREIGN KEY (usuario_id) REFERENCES Usuario(id)"
                    + ");";
			
			String sql6= "CREATE TABLE IF NOT EXISTS Favoritos (\n"
					+ " cancion_id INTEGER NOT NULL,\n"
                    + " usuario_id INTEGER NOT NULL,\n"
                    + " FOREIGN KEY (cancion_id) REFERENCES Cancion(id),\n"
                    + " FOREIGN KEY (usuario_id) REFERENCES Usuario(id)"
                    + ");";
			
			String sql7 = "CREATE TABLE IF NOT EXISTS Usuario (\n"
	                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
	                + " nombre TEXT NOT NULL,\n"
	                + " sexo TEXT NOT NULL,\n"
	                + " edad INTEGER NOT NULL,\n"
	                + " usuario TEXT UNIQUE NOT NULL,\n"
	                + " contr TEXT NOT NULL"
	                + ");";
			
			String sql8 = "CREATE TABLE IF NOT EXISTS CancionPlayList (\n"
					+ " cancion_id INTEGER NOT NULL,\n"
                    + " playlist_id INTEGER NOT NULL,\n"
                    + " FOREIGN KEY (cancion_id) REFERENCES Cancion(id),\n"
                    + " FOREIGN KEY (playlist_id) REFERENCES PlayList(id)"
                    + ");";

	        //Se abre la conexión y se crea un PreparedStatement para crer cada tabla
			//Al abrir la conexión, si no existía el fichero por defecto, se crea.
			try (Connection con = DriverManager.getConnection(connectionString);
			    PreparedStatement pStmt1 = con.prepareStatement(sql7);
				PreparedStatement pStmt2 = con.prepareStatement(sql2);
				PreparedStatement pStmt3 = con.prepareStatement(sql1);
				PreparedStatement pStmt4 = con.prepareStatement(sql3);
				PreparedStatement pStmt5 = con.prepareStatement(sql4);
				PreparedStatement pStmt6 = con.prepareStatement(sql5);
				PreparedStatement pStmt7 = con.prepareStatement(sql6);
				PreparedStatement pStmt8 = con.prepareStatement(sql8);) {
				
				//Se ejecutan las sentencias de creación de las tablas
		        if (!pStmt1.execute() && !pStmt2.execute() && !pStmt3.execute() && !pStmt4.execute() && !pStmt5.execute()  && !pStmt6.execute() && !pStmt7.execute() && !pStmt8.execute()) {
		        	logger.info("Se han creado las tablas");
		        }
			} catch (Exception ex) {
				logger.warning(String.format("Error al crear las tablas: %s", ex.getMessage()));
			}
		}
	}
	
	public void borrarBBDD() {
		//Sólo se borra la BBDD si la propiedad deleteBBDD es true
		if (properties.get("deleteBBDD").equals("true")) {	
			String sql1 = "DROP TABLE IF EXISTS Album;";
			String sql2 = "DROP TABLE IF EXISTS Artista";
			String sql3 = "DROP TABLE IF EXISTS Cancion;";
			String sql4 = "DROP TABLE IF EXISTS AlbumCancion;";
			String sql5 = "DROP TABLE IF EXISTS PlayList;";
			String sql6 = "DROP TABLE IF EXISTS Favoritos;";
			String sql7 = "DROP TABLE IF EXISTS Usuario;";
			String sql8 = "DROP TABLE IF EXISTS CancionPlayList;";

			
	        //Se abre la conexión y se crea un PreparedStatement para borrar cada tabla
			try (Connection con = DriverManager.getConnection(connectionString);
					PreparedStatement pStmt1 = con.prepareStatement(sql1);
					PreparedStatement pStmt2 = con.prepareStatement(sql2);
					PreparedStatement pStmt3 = con.prepareStatement(sql3);
					PreparedStatement pStmt4 = con.prepareStatement(sql4);
					PreparedStatement pStmt5 = con.prepareStatement(sql5);
					PreparedStatement pStmt6 = con.prepareStatement(sql6);
					PreparedStatement pStmt7 = con.prepareStatement(sql7);
					PreparedStatement pStmt8 = con.prepareStatement(sql8);) {
				
				//Se ejecutan las sentencias de borrado de las tablas
		        if (!pStmt1.execute() && !pStmt2.execute() && !pStmt3.execute() && !pStmt4.execute() && !pStmt5.execute() && !pStmt6.execute() && !pStmt7.execute() && !pStmt8.execute()) {
		        	logger.info("Se han borrado las tablas");
		        }
			} catch (Exception ex) {
				logger.warning(String.format("Error al borrar las tablas: %s", ex.getMessage()));
			}
			
			try {
				//Se borra físicamente el fichero de la BBDD
				Files.delete(Paths.get(databaseFile));
				logger.info("Se ha borrado el fichero de la BBDD");
			} catch (Exception ex) {
				logger.warning(String.format("Error al borrar el fichero de la BBDD: %s", ex.getMessage()));
			}
		}
	}
	
	//Inicializa la BBDD leyendo los datos de los ficheros CSV 
	public void initilizeFromCSV() {
		//Sólo se inicializa la BBDD si la propiedad initBBDD es true.
		if (properties.get("loadCSV").equals("true")) {
			//Se borran los datos, si existía alguno
			//this.borrarDatos();
			List<Artista> artistas =  this.loadCVSArtista();
			this.insertarArtista(artistas.toArray(new Artista[artistas.size()]));	
			List<Cancion> canciones = this.loadCSVCancion();
			this.insertarCancion(canciones.toArray(new Cancion[canciones.size()]));
			
	        List<Album> albumes = this.loadCVSAlbum();
	        this.insertarAlbum(albumes.toArray(new Album[albumes.size()]));			
		}
	}
	
	public void borrarDatos() {
		//Sólo se borran los datos si la propiedad cleanBBDD es true
		if (properties.get("cleanBBDD").equals("true")) {	
			String sql1 = "DELETE FROM Album;";
			String sql2 = "DELETE FROM Artista;";
			String sql3 = "DELETE FROM Cancion;";
			String sql4 = "DELETE FROM AlbumCancion;";
			String sql5 = "DELETE FROM PlayList;";
			String sql6 = "DELETE FROM Favoritos;";
			String sql7 = "DELETE FROM Usuario;";
			String sql8 = "DELETE FROM CancionPlayList;";
			
	        //Se abre la conexión y se crea un PreparedStatement para borrar los datos de cada tabla
			try (Connection con = DriverManager.getConnection(connectionString);
					PreparedStatement pStmt1 = con.prepareStatement(sql1);
					PreparedStatement pStmt2 = con.prepareStatement(sql2);
					PreparedStatement pStmt3 = con.prepareStatement(sql3);
					PreparedStatement pStmt4 = con.prepareStatement(sql4);
					PreparedStatement pStmt5 = con.prepareStatement(sql5);
					PreparedStatement pStmt6 = con.prepareStatement(sql6);
					PreparedStatement pStmt7 = con.prepareStatement(sql7);
					PreparedStatement pStmt8 = con.prepareStatement(sql8)) {

					
				//Se ejecutan las sentencias de borrado de las tablas
		        if (!pStmt1.execute() && !pStmt2.execute() && !pStmt3.execute() && !pStmt4.execute() && !pStmt5.execute() && !pStmt6.execute() && !pStmt7.execute() && !pStmt8.execute()) {
		        	logger.info("Se han borrado los datos");
		        }
			} catch (Exception ex) {
				logger.warning(String.format("Error al borrar los datos: %s", ex.getMessage()));
			}
		}
	}
	
	private List<Cancion> loadCSVCancion() {
		List<Cancion> canciones = new ArrayList<>();
		
		try (BufferedReader in = new BufferedReader(new FileReader(CSV_CANCIONES))) {
			String linea = null;		
			
			while ((linea = in.readLine()) != null) {								
				String[] fragmentos = linea.split(";");	
				//El ID es -1 por defecto
				String artistaNombre= fragmentos[2];
                int artistaId = getIdArtistaByNombre(artistaNombre);
                if (artistaId != -1) {
                    Cancion cancion = new Cancion(-1, fragmentos[0], Integer.parseInt(fragmentos[1]), artistaId);
                    canciones.add(cancion);
                }
								
			}			
			
		} catch (Exception ex) {
			logger.warning(String.format("Error leyendo canciones del CSV: %s", ex.getMessage()));

		}
		
		return canciones;
	}
	public List<Artista> loadCVSArtista() {
	    List<Artista> artistas = new ArrayList<>();

	    try (BufferedReader in = new BufferedReader(new FileReader(CSV_ARTISTAS))) {
	        String linea;

	        while ((linea = in.readLine()) != null) {
	            String[] fragmentos = linea.split(";");
	            // Asegúrate de que el ID y nombre estén correctamente leídos
	            try {
	                String nombre = fragmentos[0];
	                String rutaFoto = fragmentos[1];
	                artistas.add(new Artista(-1, nombre, rutaFoto)); // ID will be auto-generated
	            } catch (NumberFormatException ex) {
	                logger.warning("Formato de datos incorrecto en el archivo CSV para el artista: " + linea);
	            }
	        }

	    } catch (Exception ex) {
	        logger.warning("Error leyendo artistas del CSV: " + ex.getMessage());
	    }

	    return artistas;
	}
	
	private List<Album> loadCVSAlbum() {
	    List<Album> albumes = new ArrayList<>();
	    
	    try (BufferedReader in = new BufferedReader(new FileReader(CSV_ALBUMES))) {
	        String linea; // Línea actual del CSV
	        
	        while ((linea = in.readLine()) != null) {
	            try {
	                // Dividimos la línea por punto y coma, respetando las columnas definidas
	                String[] fragmentos = linea.split(";");
	                if (fragmentos.length < 6) {
	                    logger.warning("Línea con formato incorrecto: " + linea);
	                    continue;
	                }

	                // Obtenemos los datos básicos del álbum
	                String titulo = fragmentos[0];
	                Genero genero = Genero.valueOf(fragmentos[1].toUpperCase()); // Convertimos a Enum
	                String rutaFoto = fragmentos[2];
	                int anio = Integer.parseInt(fragmentos[3]);
	                String artista_nom = fragmentos[4];
	                
	                // Creamos el objeto Album con su artista correspondiente
	                int artistaId = getIdArtistaByNombre(artista_nom);
	                if (artistaId != -1) {
	                    Album album = new Album(-1, titulo, genero, rutaFoto, artistaId, anio);
	                    albumes.add(album);
	                    
	                    //Añadimos las canciones a los albumes escogidos
	                    for (int i=5; i<fragmentos.length; i++) {
	                    	album.getTitulos().add(fragmentos[i]);
	                    }
	                }
	                

	            } catch (Exception ex) {
	                logger.warning("Error procesando la línea: " + linea + " - " + ex.getMessage());
	            }
	        }

	    } catch (Exception ex) {
	        logger.warning(String.format("Error leyendo álbumes del CSV: %s", ex.getMessage()));
	    }
	    
	    return albumes;
	}
	


		public void insertarArtista(Artista... artistas) {
			// Falta hacer que controle el ID
			
			//Se define la plantilla de la sentencia SQL
			String sql = "INSERT INTO Artista (nombre, rutaFoto) VALUES (?, ?);";
			
			//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
			try (Connection con = DriverManager.getConnection(connectionString);
				 PreparedStatement pStmt = con.prepareStatement(sql)) {
										
				//Se recorren los artistas y se insertan uno a uno
				for (Artista a : artistas) {
					//Se añaden los parámetros al PreparedStatement
					pStmt.setString(1, a.getNombre());
					pStmt.setString(2, a.getRutaFoto());
					
					if (pStmt.executeUpdate() != 1) {					
						logger.warning(String.format("No se ha insertado el Artista: %s", a));
					} else {
											
						logger.info(String.format("Se ha insertado el Artista: %s", a));
					}
				}
				
				logger.info(String.format("%d artistas insertados en la BBDD", artistas.length));
			} catch (Exception ex) {
				logger.warning(String.format("Error al insertar artistas: %s", ex.getMessage()));
			}			
		}
	
		public void insertarAlbum(Album... albumes) {
		    String sql = "INSERT INTO Album (titulo, genero, rutaFoto, id_artista, fecha) VALUES (?, ?, ?, ?, ?);";

		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {

		        for (Album a : albumes) {
		            if (a.getArtistaID() == -1) {
		                logger.warning("El álbum '" + a.getTitulo() + "' no tiene un artista asignado.");
		                continue;  // Salta este álbum si no tiene artista
		            }

		            pStmt.setString(1, a.getTitulo());
		            pStmt.setString(2, a.getGenero().toString());
		            pStmt.setString(3, a.getRutaFoto());
		            pStmt.setInt(4, a.getArtistaID());
		            pStmt.setInt(5, a.getAnyo());

		            if (pStmt.executeUpdate() != 1) {
		                logger.warning("No se ha insertado el álbum: " + a.getTitulo());
		            } else {
		                logger.info("Se ha insertado el álbum: " + a.getTitulo());
		                int posicion = 0;
		         
		                for (String titulo : a.getTitulos()) {
			                // aqui es donde empiezo a hacer cambio en album cancion
		                	insertarAlbumCancion(getIdCancionByTitulo(titulo), getIdAlbumByTitulo(a.getTitulo()), posicion);
		                	posicion ++;

		                }
		            }
		        }

		        logger.info(String.format("%d álbumes insertados en la BBDD", albumes.length));

		    } catch (Exception ex) {
		        logger.warning("Error al insertar álbumes: " + ex.getMessage());
		    }
		}

		
		// Inserta canciones en la BBDD
		public void insertarCancion(Cancion... canciones) {
		    String sql = "INSERT INTO Cancion (titulo, duracion, id_artista) VALUES (?, ?, ?);";

		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {

		        for (Cancion c : canciones) {
		            if (c.getId() != -1) {
		                logger.warning("La canción '" + c.getTitulo() + "' no tiene un artista asignado.");
		                continue;  // Salta esta canción si no tiene artista
		            }
		            pStmt.setString(1, c.getTitulo());
		            pStmt.setInt(2, c.getDuracion());
		            pStmt.setInt(3, c.getArtistaID());

		            if (pStmt.executeUpdate() != 1) {
		                logger.warning("No se ha insertado la canción: " + c.getTitulo());
		            } else {
		                logger.info("Se ha insertado la canción: " + c.getTitulo());
		            }
		        }

		        logger.info(String.format("%d canciones insertadas en la BBDD", canciones.length));

		    } catch (Exception ex) {
		        logger.warning("Error al insertar canciones: " + ex.getMessage());
		    }
		}
		
		public void insertarAlbumCancion(int idCancion, int idAlbum, int posicion) {
		    String sql = "INSERT INTO AlbumCancion (album_id, cancion_id, posicion) VALUES (?, ?, ?);";
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {
		            pStmt.setInt(1, idAlbum);
		            pStmt.setInt(2, idCancion);
		            pStmt.setInt(3, posicion);
		            pStmt.executeUpdate();
		        logger.info(String.format("Se ha insertado la relacion albumCancion"));

		    } catch (Exception ex) {
		        logger.warning("Error al insertar la relacion albumCancion: " + ex.getMessage());
		    }
		}

		public void insertarUsuario(String nombre, String sexo, int edad, String usuario, String contraseña) {
	        String query = "INSERT INTO Usuario (nombre, sexo, edad, usuario, contr) VALUES (?, ?, ?, ?, ?)";
	        
	        try (Connection con = DriverManager.getConnection(connectionString);
	        	PreparedStatement stmt = con.prepareStatement(query)) {
	            // Establecer los valores en la consulta
	            stmt.setString(1, nombre);
	            stmt.setString(2, sexo);
	            stmt.setInt(3, edad);
	            stmt.setString(4, usuario);
	            stmt.setString(5, contraseña);
	            
	            // Ejecutar la actualización en la base de datos
	            stmt.executeUpdate();
	            JOptionPane.showMessageDialog(null, "Usuario registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(null, "Error al registrar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }
		
		public void insertarCancionFavorita(int idUsuario, int idCancion) { 
		    String sql = "INSERT INTO Favoritos (cancion_id, usuario_id) VALUES (?, ?);";
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {		        
		        pStmt.setInt(1, idCancion);
		        pStmt.setInt(2, idUsuario);
		        pStmt.executeUpdate();
		        logger.info(String.format("Se ha insertado la cancion con ID: %d como favorita para el usuario con ID %d", idCancion, idUsuario));
		    } catch (Exception ex) {
		        logger.warning("Error al insertar la cancion como favorita: " + ex.getMessage());
		    }
		}
		
		public void eliminarCancionDeFavoritos(int idUsuario, int idCancion) {
		    String sql = "DELETE FROM Favoritos WHERE usuario_id = ? AND cancion_id = ?;";
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {
		        pStmt.setInt(1, idUsuario);
		        pStmt.setInt(2, idCancion);
		        int rowsAffected = pStmt.executeUpdate();
		        if (rowsAffected > 0) {
		            logger.info(String.format("Cancion con ID %d eliminada de favoritos para el usuario con ID %d", idCancion, idUsuario));
		        } else {
		            logger.info(String.format("No se encontró la canción con ID %d en los favoritos del usuario con ID %d", idCancion, idUsuario));
		        }

		    } catch (Exception ex) {
		        logger.warning("Error al eliminar canción de favoritos: " + ex.getMessage());
		    }
		}
		
		public void insertarPlaylist(int idUsuario, String nombre) { 
		    String sql = "INSERT INTO PlayList (nomPlayList, usuario_id) VALUES (?, ?);";
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {		        
		        pStmt.setString(1, nombre);
		        pStmt.setInt(2, idUsuario);
		        pStmt.executeUpdate();
		        logger.info(String.format("Se ha insertado la playlist: %s para el usuario con ID %d", nombre, idUsuario));
		    } catch (Exception ex) {
		        logger.warning("Error al insertar la playlist: " + ex.getMessage());
		    }
		}
		
		public void insertarCancionPlayList(int idCancion, int idPlaylist) { 
		    String sql = "INSERT INTO CancionPlayList (cancion_id, playlist_id) VALUES (?, ?);";
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {		        
		        	pStmt.setInt(1, idCancion);
		        	pStmt.setInt(2, idPlaylist);
		        	pStmt.executeUpdate();
		        logger.info(String.format("Se ha insertado la cancion con ID: %d a la PlayList con ID %d", idCancion, idPlaylist));
		    } catch (Exception ex) {
		        logger.warning("Error al insertar la cancion a la PlayList: " + ex.getMessage());
		    }
		}
		
		public void eliminarCancionPlayList(int idCancion, int idPlaylist) {
		    String sql = "DELETE FROM CancionPlayList WHERE cancion_id = ? AND playlist_id = ?;";
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {
		    		pStmt.setInt(1, idCancion);
		    		pStmt.setInt(2, idPlaylist);;
		        int rowsAffected = pStmt.executeUpdate();
		        if (rowsAffected > 0) {
		            logger.info(String.format("Cancion con ID %d eliminada de la PlayList con ID %d", idCancion, idPlaylist));
		        } else {
		            logger.info(String.format("No se encontró la canción con ID %d en la PlayList con ID %d", idCancion, idPlaylist));
		        }

		    } catch (Exception ex) {
		        logger.warning("Error al eliminar cancion de la PlayList: " + ex.getMessage());
		    }
		}
		
		//Recupera Las Canciones de la BBDD
		public List<Cancion> getCanciones() {
			List<Cancion> canciones = new ArrayList<>();
			String sql = "SELECT * FROM Cancion";
			
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt = con.prepareStatement(sql)) {			
				
				ResultSet rs = pStmt.executeQuery();			
				Cancion cancion;
				
				while (rs.next()) {
					cancion = new Cancion(rs.getInt("id"), 
							rs.getString("titulo"), 
							rs.getInt("duracion"),
							rs.getInt("id_artista"));
					 					 
					canciones.add(cancion);
				}
				
				rs.close();
				
				logger.info(String.format("Se han recuperado %d canciones.", canciones.size()));			
			} catch (Exception ex) {
				logger.warning(String.format("Error recuperar las canciones: %s", ex.getMessage()));						
			}		
			
			return canciones;
		}
		//Recupera Los Albumes de la BBDD
		public List<Album> getAlbum(){
			List<Album> albumes = new ArrayList<>();
			String sql = "SELECT * FROM Album";
			
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt = con.prepareStatement(sql)) {			
				
				ResultSet rs = pStmt.executeQuery();			
				Album album;
				
				while (rs.next()) {
					album = new Album(
							rs.getInt("id"), 
							rs.getString("titulo"), 
							Genero.valueOf(rs.getString("genero")), 
							rs.getString("rutafoto"),
							rs.getInt("id_artista"),
							rs.getInt("fecha"));

					albumes.add(album);
				}
				
				rs.close();
				
				logger.info(String.format("Se han recuperado %d albumes.", albumes.size()));			
			} catch (Exception ex) {
				logger.warning(String.format("Error recuperar las albumes: %s", ex.getMessage()));						
			}		
			
			return albumes;
		}
		//Recupera los Artistas de la BBDD
		public List<Artista> getArtistas() {
		    List<Artista> artistas = new ArrayList<>();
		    String sql = "SELECT * FROM Artista";
		    
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {
		         
		        ResultSet rs = pStmt.executeQuery();
		        Artista artista;
		        
		        while (rs.next()) {
		            artista = new Artista(rs.getInt("id"), 
		                                  rs.getString("nombre"), 
		                                  rs.getString("rutafoto"));
		            artistas.add(artista);
		        }
		        
		        rs.close();
		        logger.info(String.format("Se han recuperado %d artistas.", artistas.size()));
		    } catch (Exception ex) {
		        logger.warning(String.format("Error al recuperar los artistas: %s", ex.getMessage()));
		    }
		    
		    return artistas;
		}
		//Recupera Usuarios
		public Usuario getUsuario(String nombreUsuario) {
		    String sql = "SELECT * FROM Usuario WHERE usuario = ?";
		    
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {
		    	pStmt.setString(1, nombreUsuario);
		         
		        ResultSet rs = pStmt.executeQuery();
		        
		        
		        while (rs.next()) {
		        	String nombre = rs.getString("nombre");
	                String sexo = rs.getString("sexo");
	                int edad = rs.getInt("edad");
	                
	                String contraseña = rs.getString("contr");
	                return new Usuario(nombre, sexo, edad, nombreUsuario, contraseña);
		        }
		        
		        rs.close();
		    }catch (SQLException ex) {
	            ex.printStackTrace();
	        }
		    
		    return null;
		}

		public int getIdUsuarioByUsuario(String usuario) {
		    int idUsuario = -1;
		    String sql = "SELECT id FROM Usuario WHERE usuario = ? LIMIT 1;";
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {
		        pStmt.setString(1, usuario);
		        try (ResultSet rs = pStmt.executeQuery()) {
		            if (rs.next()) {
		                idUsuario = rs.getInt("id");
		                logger.info(String.format("Se ha encontrado el ID %d para el usuario '%s'", idUsuario, usuario));
		            } else {
		                logger.warning(String.format("No se encontró un ID para el usuario '%s'", usuario));
		            }
		        }
		    } catch (Exception ex) {
		        logger.warning("Error al obtener ID de usuario: " + ex.getMessage());
		    }
		    return idUsuario;
		}

		public Album getAlbumByID(int id) {
		    Album album = null; // Inicializamos la variable que contendrá el álbum
		    String sql = "SELECT * FROM Album WHERE id = ? LIMIT 1"; // Cambiamos 'Cancion' a 'Album' y ajustamos el campo ID

		    // Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {

		        // Establecemos el parámetro de la consulta
		        pStmt.setInt(1, id);

		        // Ejecutamos la consulta y procesamos el ResultSet
		        try (ResultSet rs = pStmt.executeQuery()) {
		            if (rs.next()) {
		                // Creamos el objeto Album a partir de los datos del ResultSet
		                album = new Album(
		                    rs.getInt("id"),      // ID del álbum
		                    rs.getString("titulo"),     // Título del álbum
		                    Genero.valueOf(rs.getString("genero")),
		                    rs.getString("rutaFoto"),
		                    rs.getInt("id_artista"),
		                    rs.getInt("fecha")
		                );
		            }
		        }

		        // Registro informativo de que se recuperó el álbum
		        logger.info(String.format("Se ha recuperado el álbum con ID %d: %s", id, album));

		    } catch (Exception ex) {
		        // Registro de advertencia en caso de error
		        logger.warning(String.format("Error al recuperar el álbum con ID %d: %s", id, ex.getMessage()));
		    }

		    return album; // Retornamos el objeto Album o null si no se encontró
		}
		
		public Artista getArtistaByID(int id) {
		    Artista artista = null; // Inicializamos la variable que contendrá el artista
		    String sql = "SELECT * FROM Artista WHERE id = ? LIMIT 1"; // Cambiamos la tabla y campo a 'Artista'

		    // Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {

		        // Establecemos el parámetro de la consulta
		        pStmt.setInt(1, id);

		        // Ejecutamos la consulta y procesamos el ResultSet
		        try (ResultSet rs = pStmt.executeQuery()) {
		            if (rs.next()) {
		                // Creamos el objeto Artista a partir de los datos del ResultSet
		                artista = new Artista(
		                    rs.getInt("id"),   // ID del artista
		                    rs.getString("nombre"),    // Nombre del artista
		                    rs.getString("rutaFoto")
		                );
		                
		            }

		        }

		        // Registro informativo de que se recuperó el artista
		        logger.info(String.format("Se ha recuperado el artista con ID %d: %s", id, artista));

		    } catch (Exception ex) {
		        // Registro de advertencia en caso

		    }
			return artista;
		}
		
		public int getIdCancionByTitulo(String titulo) {
		    int id = 0; // Variable para almacenar el ID del álbum
		    String sql = "SELECT * FROM Cancion WHERE titulo = ? LIMIT 1";

		    // Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {
		         
		        // Se define el parámetro de la sentencia SQL
		        pStmt.setString(1, titulo);

		        // Se ejecuta la sentencia y se obtiene el ResultSet con los resultados
		        try (ResultSet rs = pStmt.executeQuery()) {
		            if (rs.next()) {
		                id = rs.getInt("id"); // Se obtiene el ID de la columna
		            } else {
		                logger.warning(String.format("No se encontró un álbum con el título: %s", titulo));
		            }
		        }

		        logger.info(String.format("Se ha recuperado el álbum con título %s y ID %d", titulo, id));

		    } catch (Exception ex) {
		        logger.warning(String.format("Error al recuperar el álbum con título %s: %s", titulo, ex.getMessage()));
		    }

		    return id; // Retorna el ID del álbum, será 0 si no se encontró
		}
		
		public int getIdAlbumByTitulo(String titulo) {
		    int id = 0; // Variable para almacenar el ID del álbum
		    String sql = "SELECT * FROM Album WHERE titulo = ? LIMIT 1";

		    // Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {
		         
		        // Se define el parámetro de la sentencia SQL
		        pStmt.setString(1, titulo);

		        // Se ejecuta la sentencia y se obtiene el ResultSet con los resultados
		        try (ResultSet rs = pStmt.executeQuery()) {
		            if (rs.next()) {
		                id = rs.getInt("id"); // Se obtiene el ID de la columna
		            } else {
		                logger.warning(String.format("No se encontró un álbum con el título: %s", titulo));
		            }
		        }

		        logger.info(String.format("Se ha recuperado el álbum con título %s y ID %d", titulo, id));

		    } catch (Exception ex) {
		        logger.warning(String.format("Error al recuperar el álbum con título %s: %s", titulo, ex.getMessage()));
		    }

		    return id; // Retorna el ID del álbum, será 0 si no se encontró
		}
		
		public int getIdArtistaByNombre(String nombre) {
		    int id = -1; // Variable para almacenar el ID del artista
		    String sql = "SELECT * FROM Artista WHERE nombre = ? LIMIT 1"; // Cambié 'titulo' a 'nombre' para que coincida con la columna correcta

		    // Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {
		         
		        // Se define el parámetro de la sentencia SQL
		        pStmt.setString(1, nombre);

		        // Se ejecuta la sentencia y se obtiene el ResultSet con los resultados
		        try (ResultSet rs = pStmt.executeQuery()) {
		            if (rs.next()) {
		                id = rs.getInt("id"); // Se obtiene el ID de la columna 'id_artista'
		            } else {
		                logger.warning(String.format("No se encontró un artista con el nombre: %s", nombre)); 
		            }
		        }

		        logger.info(String.format("Se ha recuperado el artista con nombre %s y ID %d", nombre, id)); 

		    } catch (Exception ex) {
		        logger.warning(String.format("Error al recuperar el artista con nombre %s: %s", nombre, ex.getMessage())); 
		    }

		    return id; 
		}

		//GETS VENTANALISTA CANCIONES
		public List<Cancion> getCancionesByIDAlbum(Album album){
			List<Cancion> canciones = new ArrayList<>(); 
		    String sql = "SELECT * FROM AlbumCancion WHERE album_id = ?"; 

		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {
		        
		        // Configura el parámetro de la consulta
		        pStmt.setInt(1, album.getId());

		        // Ejecuta la consulta
		        try (ResultSet rs = pStmt.executeQuery()) {
		            while (rs.next()) {
		                int id=rs.getInt("cancion_id");
		                canciones.add(getCancionByID(id));
		                
		            }
		        }
		    } catch (SQLException ex) {
		        // Log de error en caso de excepciones
		        logger.warning(String.format("Error al recuperar canciones con el album_id %d: %s",  album.getId(), ex.getMessage()));
		    }

		    // Devuelve la lista de canciones
		    return canciones;
		}
		public Cancion getCancionByID(int id) {
			String sql = "SELECT * FROM Cancion WHERE id = ?"; 
			Cancion cancion= null;
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {
		        
		        // Configura el parámetro de la consulta
		        pStmt.setInt(1, id);

		        // Ejecuta la consulta
		        try (ResultSet rs = pStmt.executeQuery()) {
		            while (rs.next()) {
		            	cancion = new Cancion(rs.getInt("id"),
		            			rs.getString("titulo"),
		            			rs.getInt("duracion"),
		            			rs.getInt("id_artista")
		            			);
		            	cancion.setArtista(getArtistaByID(cancion.getArtistaID()));
			            cancion.setAlbum(getAlbumByID(getIDAlbumByIDCancion(cancion.getId())));
		            }
		        }
		    } catch (SQLException ex) {
		        // Log de error en caso de excepciones
		        logger.warning(String.format("Error al recuperar cancion con el id %d: %s", id, ex.getMessage()));
		    }
			return cancion;
		}
		public List<Cancion> getCancionesByIDArtista(Artista artista){
			List<Cancion> canciones = new ArrayList<>(); 
		    String sqlCanciones = "SELECT * FROM Cancion WHERE id_artista = ?";

		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmtCanciones = con.prepareStatement(sqlCanciones)) {
		        
		        pStmtCanciones.setInt(1, artista.getId());

		        // Ejecuta la consulta para obtener las canciones del artista
		        try (ResultSet rsCanciones = pStmtCanciones.executeQuery()) {
		            while (rsCanciones.next()) {
		            	Cancion cancion = new Cancion(rsCanciones.getInt("id"),
                        		rsCanciones.getString("titulo"),
                        		rsCanciones.getInt("duracion"),
                        		rsCanciones.getInt("id_artista"));
		            
		            cancion.setArtista(getArtistaByID(cancion.getArtistaID()));
		            cancion.setAlbum(getAlbumByID(getIDAlbumByIDCancion(cancion.getId())));
                    canciones.add(cancion);		                
		            }
		        }
		    } catch (SQLException ex) {
		        logger.warning(String.format("Error al recuperar canciones para el artista con ID %d: %s",artista.getId(), ex.getMessage()));
		    }

		    return canciones;
		}
		public int getIDAlbumByIDCancion(int id){
			int id_album= 0;
		    String sql = "SELECT * FROM AlbumCancion WHERE cancion_id = ?"; 

		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {
		        
		        // Configura el parámetro de la consulta
		        pStmt.setInt(1, id);

		        // Ejecuta la consulta
		        try (ResultSet rs = pStmt.executeQuery()) {
		        	if(rs.next()) {
		               id_album= rs.getInt(1);
		            }
		        }
		    } catch (SQLException ex) {
		        // Log de error en caso de excepciones
		        logger.warning(String.format("Error al recuperar album con el cancion_id %d: %s",  id, ex.getMessage()));
		    }

		   return id_album; 
		}
		
		// GET PARA LAS CANCIONES FAVORITAS
		public List <Integer> getIDCancionesFavoritas(int idUsuario) {
		    List <Integer> idCanciones = new ArrayList<>();
		    String sql = "SELECT cancion_id FROM Favoritos WHERE usuario_id = ?";
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {
		        pStmt.setInt(1, idUsuario);
		        try (ResultSet rs = pStmt.executeQuery()) {
		            while (rs.next()) {
		            	idCanciones.add(rs.getInt("cancion_id"));
		            }
		        }
		        logger.info(String.format("Se han recuperado %d canciones favoritas para el usuario con ID %d", idCanciones.size(), idUsuario));
		    } catch (Exception ex) {
		        logger.warning("Error al recuperar canciones favoritas: " + ex.getMessage());
		    }

		    return idCanciones; 
		}
		
		public List<PlayList> getPlayListByIdUsuario(int idUsuario) {
		    List<PlayList> PlayLists = new ArrayList<>();
		    String sql = "SELECT nomPlayList, rutaFoto, color FROM PlayList WHERE usuario_id = ?";

		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {

		        pStmt.setInt(1, idUsuario);

		        try (ResultSet rs = pStmt.executeQuery()) {
		            while (rs.next()) {
		                // Crear un objeto PlayList por cada fila del resultado
		                String titulo = rs.getString("nomPlayList");
		                String rutaFoto = rs.getString("rutaFoto");
		                String color = rs.getString("color");

		                // Crear el objeto PlayList y añadirlo a la lista
		                PlayList playlist = new PlayList(titulo, new ArrayList<>(), rutaFoto, color);
		                PlayLists.add(playlist);
		            }
		        }
		        logger.info(String.format("Se han recuperado %d PlayLists para el usuario con ID %d", PlayLists.size(), idUsuario));
		    } catch (Exception ex) {
		        logger.warning("Error al recuperar PlayLists: " + ex.getMessage());
		    }

		    return PlayLists;
		}

		
		public boolean crearPlaylist(String titulo, String color, String rutaFoto, int idUsuario) {
		    String sql = "INSERT INTO Playlist (nomPlaylist, color, rutaFoto, usuario_id) VALUES (?, ?, ?, ?)";
		    try (Connection con = DriverManager.getConnection(connectionString);
			         PreparedStatement stmt = con.prepareStatement(sql)) {
		        stmt.setString(1, titulo);
		        stmt.setString(2, color);
		        stmt.setString(3, rutaFoto);
		        stmt.setInt(4, idUsuario);

		        int filasAfectadas = stmt.executeUpdate();
		        return filasAfectadas > 0; 
		    } catch (SQLException e) {
		        e.printStackTrace(); 
		        return false;
		    }
		}
		
		public List <String> getPlayListByIdUsuarioIdCancion(int idUsuario, int idCancion) {
		    List <String> PlayLists = new ArrayList<>();
		    String sql = "SELECT p.nomPlayList FROM CancionPlayList cp, PlayList p WHERE cp.playlist_id = p.id "
		    		+ " AND p.usuario_id = ? AND cp.cancion_id = ?";
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {
		        	pStmt.setInt(1, idUsuario);
		        	pStmt.setInt(2, idCancion);
		        try (ResultSet rs = pStmt.executeQuery()) {
		            while (rs.next()) {
		            	PlayLists.add(rs.getString("nomPlayList"));
		            }
		        }
		        logger.info(String.format("Se han recuperado %d PlayLists que contienen la cancion con ID &d para el usuario con ID %d", PlayLists.size(), idCancion, idUsuario));
		    } catch (Exception ex) {
		        logger.warning("Error al recuperar PlayLists: " + ex.getMessage());
		    }

		    return PlayLists; 
		}
		
		public int getIdPlayListByNombre(String nombre) {
		    int id = -1; // Variable para almacenar el ID del artista
		    String sql = "SELECT * FROM PlayList WHERE nomPlayList = ? LIMIT 1"; // Cambié 'titulo' a 'nombre' para que coincida con la columna correcta
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {
		        pStmt.setString(1, nombre);
		        try (ResultSet rs = pStmt.executeQuery()) {
		            if (rs.next()) {
		                id = rs.getInt("id"); // Se obtiene el ID de la columna 'id_artista'
		            } else {
		                logger.warning(String.format("No se encontró una PlayList con el nombre: %s", nombre)); 
		            }
		        }
		        logger.info(String.format("Se ha recuperado la PlayList con nombre %s y ID %d", nombre, id)); 
		    } catch (Exception ex) {
		        logger.warning(String.format("Error al recuperar la PlayList con nombre %s: %s", nombre, ex.getMessage())); 
		    }
		    return id; 
		}
		
		public List<Cancion> getIdCancionesByIdPlayList(int idPlayList) {
			List<Cancion> canciones = new ArrayList<>(); 
		    String sqlCanciones = "SELECT * FROM CancionPlayList WHERE playlist_id = ?";

		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmtCanciones = con.prepareStatement(sqlCanciones)) {
		        
		        pStmtCanciones.setInt(1, idPlayList);

		        // Ejecuta la consulta para obtener las canciones del artista
		        try (ResultSet rs = pStmtCanciones.executeQuery()) {
		            while (rs.next()) {
		            	canciones.add(this.getCancionByID(rs.getInt("cancion_id")));                
		            }
		        }
		    } catch (SQLException ex) {
		        logger.warning(String.format("Error al recuperar canciones para la PlayList con ID %d:", idPlayList, ex.getMessage()));
		    }

		    return canciones;
		}
		
}
