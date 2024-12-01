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

import domain.Album;
import domain.Artista;
import domain.Cancion;
import domain.Genero;
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
	                + " duracion TEXT NOT NULL,\n"
	                + " genero TEXT NOT NULL,\n"
	                + " rutafoto TEXT\n"
	                + ");";
	
			String sql2 = "CREATE TABLE IF NOT EXISTS Artista (\n"
	                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
	                + " nombre TEXT NOT NULL,\n"
	                + " rutafoto TEXT NOT NULL\n"
	                + ");";
			
			String sql3 = "CREATE TABLE IF NOT EXISTS Cancion (\n"
	                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
	                + " titulo TEXT NOT NULL,\n"
	                + " duracion TEXT NOT NULL\n"
	                + ");";
	
			String sql4 = "CREATE TABLE IF NOT EXISTS AlbumCancion (\n"
	                + " id INTEGER,\n"
	                + " album_id INTEGER NOT NULL,\n"
	                + " cancion_id INTEGER NOT NULL,\n"
	                + " PRIMARY KEY(album_id, cancion_id),\n"
	                + " FOREIGN KEY (album_id) REFERENCES Album(id) ON DELETE CASCADE,\n"
	                + " FOREIGN KEY (cancion_id) REFERENCES Cancion(id) ON DELETE CASCADE\n"
	                + ");";
			
			String sql5 = "CREATE TABLE IF NOT EXISTS ArtistaAlbum (\n"
	                + " id INTEGER,\n"
	                + " artista_id INTEGER NOT NULL,\n"
	                + " album_id INTEGER NOT NULL, \n"
	                + " PRIMARY KEY(album_id, artista_id)\n"
	                + " FOREIGN KEY (artista_id) REFERENCES Artista(id) ON DELETE CASCADE\n"
	                + " FOREIGN KEY (album_id) REFERENCES Album(id) ON DELETE CASCADE\n"
	                + ");";
			String sql6 = "CREATE TABLE IF NOT EXISTS Usuario (\n"
	                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
	                + " nombre TEXT NOT NULL,\n"
	                + " sexo TEXT NOT NULL,\n"
	                + " edad INTEGER NOT NULL,\n"
	                + " correo TEXT NOT NULL,\n"
	                + " contr TEXT NOT NULL\n"
	                + ");";
			String sql7= "CREATE TABLE IF NOT EXISTS UsuarioPlaylist (\n"
					+ " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
					+ " nomList TEXT UNIQUE NOT NULL,\n"
                    + " usuario_id INTEGER NOT NULL,\n"
                    + " FOREIGN KEY (usuario_id) REFERENCES Usuario(id)\n"
                    + ");";
			String sql8= "CREATE TABLE IF NOT EXISTS PlaylistCancion (\n"
                    + " playlist_id INTEGER NOT NULL,\n"
                    + " cancion_id INTEGER NOT NULL,\n"
                    + " FOREIGN KEY (playlist_id) REFERENCES Playlist(id),\n"                   
                    + " FOREIGN KEY (cancion_id) REFERENCES Cancion(id)\n"
                    + ");";


	        //Se abre la conexión y se crea un PreparedStatement para crer cada tabla
			//Al abrir la conexión, si no existía el fichero por defecto, se crea.
			try (Connection con = DriverManager.getConnection(connectionString);
			    PreparedStatement pStmt1 = con.prepareStatement(sql1);
				PreparedStatement pStmt2 = con.prepareStatement(sql2);
				PreparedStatement pStmt3 = con.prepareStatement(sql3);
				PreparedStatement pStmt4 = con.prepareStatement(sql4);
				PreparedStatement pStmt5 = con.prepareStatement(sql5);
				PreparedStatement pStmt6 = con.prepareStatement(sql6);
				PreparedStatement pStmt7 = con.prepareStatement(sql7);
				PreparedStatement pStmt8 = con.prepareStatement(sql8)) {
				
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
			String sql5 = "DROP TABLE IF EXISTS ArtistaAlbum;";
			String sql6 = "DROP TABLE IF EXISTS Usuario;";
			String sql7 = "DROP TABLE IF EXISTS UsuarioPlaylist;";
			String sql8 = "DROP TABLE IF EXISTS PlaylistCancion;";
			
	        //Se abre la conexión y se crea un PreparedStatement para borrar cada tabla
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
			
			// Se leen las canciones del CSV
			List<Cancion> canciones = this.loadCSVCancion();
			//Se insertan las canciones en la BBDD
			this.insertarCancion(canciones.toArray(new Cancion[canciones.size()]));
			
			// Se leen los albumes del CSV
			List<Album> albumes = this.loadCVSAlbum();
			//lambda expression: enlaza las canciones con los albumes porque al leer los
			//albumes sólo se recuperan los nombres de la canciones y faltan el resto de
			//datos.
			albumes.forEach(a -> updateCancion(a, canciones));
			//Se insertan los albumes en la BBDD
			this.insertarAlbum(albumes.toArray(new Album[albumes.size()]));
			
			// Se leen los artistas del CSV
			List<Artista> artistas = this.loadCVSArtista();
			//lambda expression: enlaza las canciones con los albumes porque al leer los
			//albumes sólo se recuperan los nombres de la canciones y faltan el resto de
			//datos.
			artistas.forEach(a -> updateAlbumes(a, albumes));
			//Se insertan los artistas en la BBDD
			this.insertarArtista(artistas.toArray(new Artista[artistas.size()]));				
		}
	}
	
	public void borrarDatos() {
		//Sólo se borran los datos si la propiedad cleanBBDD es true
		if (properties.get("cleanBBDD").equals("true")) {	
			String sql1 = "DELETE FROM Album;";
			String sql2 = "DELETE FROM Artista;";
			String sql3 = "DELETE FROM Cancion;";
			String sql4 = "DELETE FROM AlbumCancion;";
			String sql5 = "DELETE FROM ArtistaAlbum;";
			String sql6 = "DELETE FROM Usuario;";
			String sql7 = "DELETE FROM UsuarioPlaylist;";
			String sql8 = "DELETE FROM PlaylistCancion;";
			
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
			Cancion c = null;		
			
			while ((linea = in.readLine()) != null) {								
				String[] fragmentos = linea.split(";");	
				//El ID es -1 por defecto
				c = new Cancion(-1,fragmentos[0], Double.parseDouble(fragmentos[1]));
								
				if (c != null) {
					canciones.add(c);
				}
			}			
			
		} catch (Exception ex) {
			logger.warning(String.format("Error leyendo canciones del CSV: %s", ex.getMessage()));

		}
		
		return canciones;
	}
	
	private List<Album> loadCVSAlbum() {
	    List<Album> albumes = new ArrayList<>();
	    
	    try (BufferedReader in = new BufferedReader(new FileReader(CSV_ALBUMES))) {
	        String linea = null;
	        Album a = null;     
	        
	        while ((linea = in.readLine()) != null) {                
	            String[] fragmentos = linea.split(";");
	            
	            // Validar si el campo rutafoto está presente
	            if (fragmentos.length > 3 && fragmentos[3] != null && !fragmentos[3].isEmpty()) {
	            	Genero genero=  Genero.valueOf(fragmentos[2]);
	                a = new Album(
	                    -1, // ID generado automáticamente
	                    fragmentos[0], // Título
	                    Double.parseDouble(fragmentos[1]), // Duración
	                    genero, // Género
	                    fragmentos[3] // Ruta de la foto
	                );

	                if (a != null) {
	                    albumes.add(a);
	                }
	            } else {
	                logger.warning("La ruta de la foto es nula o está vacía: " + linea);
	            }
	        }
	        
	    } catch (Exception ex) {
	        logger.warning(String.format("Error leyendo albumes del CSV: %s", ex.getMessage()));
	    }
	    
	    return albumes;
	}

	
	private List<Artista> loadCVSArtista() {
		List<Artista> artistas = new ArrayList<>();
		
		try (BufferedReader in = new BufferedReader(new FileReader(CSV_ARTISTAS))) {
			String linea = null;
			Artista a = null;	
			
			while ((linea = in.readLine()) != null) {				
				String[] fragmentos = linea.split(";");					
				a = new Artista(Integer.valueOf(fragmentos[0]), fragmentos[1],fragmentos[2]);
				
				if (a != null) {
					artistas.add(a);
				}
			}			
			
		} catch (Exception ex) {
			logger.warning(String.format("Error leyendo artistas del CSV: %s", ex.getMessage()));
		}
		
		return artistas;
	}

	// Inserta canciones en la BBDD
	public void insertarCancion(Cancion... canciones) {
		// Falta hacer que controle el ID
		
		//Se define la plantilla de la sentencia SQL
		String sql = "INSERT INTO Cancion (titulo, duracion) VALUES (?, ?);";
		
		//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement pStmt = con.prepareStatement(sql)) {					
			//Se recorren las canciones y se insertan una a una
			for (Cancion c : canciones) {
				//Se añaden los parámetros al PreparedStatement
				pStmt.setString(1, c.getTitulo());
				pStmt.setDouble(2, c.getDuracion());
				
				if (pStmt.executeUpdate() != 1) {					
					logger.warning(String.format("No se ha insertado la Cancion: %s", c));
				} else {
					//TODO el ID
					logger.info(String.format("Se ha insertado la Cancion: %s", c));
				}
			}
			
			logger.info(String.format("%d canciones insertados en la BBDD", canciones.length));
		} catch (Exception ex) {
			logger.warning(String.format("Error al insertar canciones: %s", ex.getMessage()));
		}			
	}
	
	// Inserta albumes en la BBDD
		public void insertarAlbum(Album... albumes) {
			// Falta hacer que controle el ID
			
			//Se define la plantilla de la sentencia SQL
			String sql = "INSERT INTO Album (titulo, duracion, genero, rutaFoto) VALUES (?, ?, ?, ?);";
			
			//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
			try (Connection con = DriverManager.getConnection(connectionString);
				 PreparedStatement pStmt = con.prepareStatement(sql)) {
										
				//Se recorren los albumes y se insertan uno a uno
				for (Album a : albumes) {
					//Se añaden los parámetros al PreparedStatement
					pStmt.setString(1, a.getTitulo());
					pStmt.setDouble(2, a.getDuracion());
					pStmt.setString(3, a.getGenero().toString());
					pStmt.setString(4, a.getRutaFoto());
					
					if (pStmt.executeUpdate() != 1) {					
						logger.warning(String.format("No se ha insertado el Album: %s", a));
					} else {
						//TODO el ID
							for(Cancion c: a.getListacanciones()) {
								this.insertarAlbumCancion(a.getId(), c.getId());
							}
						logger.info(String.format("Se ha insertado el Album: %s", a));
					}
				}
				
				logger.info(String.format("%d albumes insertados en la BBDD", albumes.length));
			} catch (Exception ex) {
				logger.warning(String.format("Error al insertar albumes: %s", ex.getMessage()));
			}			
		}
		
		// Inserta artista en la BBDD
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
		
		public void insertarAlbumCancion(int idAlbum, int idCancion) {
			String sql = "INSERT INTO AlbumCancion (album_id, cancion_id) VALUES (?, ?);";
			
			//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
			try (Connection con = DriverManager.getConnection(connectionString);
				 PreparedStatement pStmt = con.prepareStatement(sql)) {
					
				//Se añaden los parámetros al PreparedStatement
				pStmt.setInt(1, idAlbum);
				pStmt.setInt(2, idCancion);
					
				if (pStmt.executeUpdate() != 1) {					
					logger.warning(String.format("No se ha insertado la cancion %d del album %d.", idAlbum, idCancion));
				} else {
					logger.info(String.format("Se ha insertado la cancion %d del album %d.", idAlbum, idCancion));
				}
			} catch (Exception ex) {
				logger.warning(String.format("Error al insertar la cancion del album: %s", ex.getMessage()));
			}				
		}
		
		public void insertarArtistaAlbum(int idArtista, int idAlbum) {
			String sql = "INSERT INTO ArtistaAlbum (artista_id, album_id) VALUES (?, ?);";
			
			//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
			try (Connection con = DriverManager.getConnection(connectionString);
				 PreparedStatement pStmt = con.prepareStatement(sql)) {
					
				//Se añaden los parámetros al PreparedStatement
				pStmt.setInt(1, idArtista);
				pStmt.setInt(2, idAlbum);
					
				if (pStmt.executeUpdate() != 1) {					
					logger.warning(String.format("No se ha insertado el album %d del artista %d.", idArtista, idAlbum));
				} else {
					logger.info(String.format("Se ha insertado el album %d del artista %d.", idArtista, idAlbum));
				}
			} catch (Exception ex) {
				logger.warning(String.format("Error al insertar el album del artista: %s", ex.getMessage()));
			}				
		}
		
		public void insertarUsuarioPlaylist(int idPlaylist, int idUsuario) {
			String sql = "INSERT INTO UsuarioPlaylist (id, usuario_id) VALUES (?, ?);";
			
			//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
			try (Connection con = DriverManager.getConnection(connectionString);
				 PreparedStatement pStmt = con.prepareStatement(sql)) {
					
				//Se añaden los parámetros al PreparedStatement
				pStmt.setInt(1, idPlaylist);
				pStmt.setInt(2, idUsuario);
					
				if (pStmt.executeUpdate() != 1) {					
					logger.warning(String.format("No se ha insertado la playlist %d del usuario %d.", idPlaylist, idUsuario));
				} else {
					logger.info(String.format("Se ha insertado la playlist %d del usuario %d.", idPlaylist, idUsuario));
				}
			} catch (Exception ex) {
				logger.warning(String.format("Error al insertar la playlist del usuario: %s", ex.getMessage()));
			}				
		}
		
		public void insertarPlaylistCancion(int idPlaylist, int idCancion) {
			String sql = "INSERT INTO PlaylistCancion (playlist_id, cancion_id) VALUES (?, ?);";
			
			//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
			try (Connection con = DriverManager.getConnection(connectionString);
				 PreparedStatement pStmt = con.prepareStatement(sql)) {
					
				//Se añaden los parámetros al PreparedStatement
				pStmt.setInt(1, idPlaylist);
				pStmt.setInt(2, idCancion);
					
				if (pStmt.executeUpdate() != 1) {					
					logger.warning(String.format("No se ha insertado la cancion %d de la playlist %d.", idPlaylist, idCancion));
				} else {
					logger.info(String.format("Se ha insertado la cancion %d de la playlist %d.", idPlaylist, idCancion));
				}
			} catch (Exception ex) {
				logger.warning(String.format("Error al insertar la cancion de la playlist: %s", ex.getMessage()));
			}				
		}

		public void insertarUsuario(Usuario usuario) {
			//Se define la plantilla de la sentencia SQL			
			String sqlInsert = "INSERT INTO Usuario (nombre, sexo, edad, correo, contr) VALUES (?, ?, ?, ?, ?);";

			// Prepare the statement
			try (Connection con = DriverManager.getConnection(connectionString);
					PreparedStatement stmt = con.prepareStatement(sqlInsert)) {
			    // Set the parameters for the statement
			    stmt.setString(1, usuario.getNombre());   
			    stmt.setString(2, usuario.getSexo());      
			    stmt.setInt(3, usuario.getEdad());         
			    stmt.setString(4, usuario.getCorreo());  
			    stmt.setString(5, usuario.getContr());   

			    // Execute the statement
			    stmt.executeUpdate();
			} catch (SQLException e) {
			    e.printStackTrace(); 
			}			
		}
		
//		private void updateCancion(Album album, List<Cancion> canciones) {
//		    String sql = "INSERT INTO AlbumCancion (album_id, cancion_id) VALUES (?, ?);";
//
//		    try (Connection con = DriverManager.getConnection(connectionString);
//		         PreparedStatement pStmt = con.prepareStatement(sql)) {
//		         
//		        for (Cancion cancion : canciones) {
//		            if (album.getListacanciones().contains(cancion.getTitulo())) {
//		                //pStmt.setInt(1, album.getId());
//		                //pStmt.setInt(2, cancion.getId());
//		                pStmt.executeUpdate();
//		            }
//		        }
//		    } catch (Exception ex) {
//		        logger.warning(String.format("Error al actualizar relación Canción-Álbum: %s", ex.getMessage()));
//		    }
//		}
		
		private void updateCancion(Album album, List<Cancion> canciones) {		
			// Relacionar albumes y sus canciones con los artistas
			for (int i = 0; i < album.getListacanciones().size(); i++) {
				for(Cancion c: canciones) {
					if( album.getListacanciones().get(i).getTitulo().equals(c.getTitulo())) {
						album.getListacanciones().set(i, c);
					}
				}
			}
		}
		
		private void updateAlbumes(Artista artista, List<Album> albumes) {		
			// Relacionar albumes y sus canciones con los artistas
			for (int i = 0; i < artista.getAlbumes().size(); i++) {
				for(Album a: albumes) {
					if( artista.getAlbumes().get(i).getTitulo().equals(a.getTitulo())) {
						artista.getAlbumes().set(i, a);
					}
				}
			}
		}

		//Recupera Las CAnciones de la BBDD
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
						rs.getDouble("duracion"));
					 					 
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
					album = new Album(rs.getInt("id"), 
							rs.getString("titulo"), 
							rs.getDouble("duracion"), 
							Genero.valueOf(rs.getString("genero")), 
							rs.getString("rutafoto"));
				List<Integer> idsCanciones= this.getIDsCancionesdelAlbum(album);
				for(int id: idsCanciones) {
					album.addCancion(this.getCancionByID(id));
				}
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
		public List<Usuario> getUsuarios() {
		    List<Usuario> usuarios = new ArrayList<>();
		    String sql = "SELECT * FROM Usuario";
		    
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {
		         
		        ResultSet rs = pStmt.executeQuery();
		        Usuario usuario;
		        
		        while (rs.next()) {
		            usuario = new Usuario(rs.getString("nombre"), 
		                                  rs.getString("sexo"), 
		                                  rs.getInt("edad"), 
		                                  rs.getString("correo"), 
		                                  rs.getString("contr"));
		            usuarios.add(usuario);
		        }
		        
		        rs.close();
		        logger.info(String.format("Se han recuperado %d usuarios.", usuarios.size()));
		    } catch (Exception ex) {
		        logger.warning(String.format("Error al recuperar los usuarios: %s", ex.getMessage()));
		    }
		    
		    return usuarios;
		}

		//Recupera un ArrayList<Cancion> del Object que recibe
		public Cancion getCancionByID(int id) {
			Cancion cancion=null;
			String sql = "SELECT * FROM Cancion WHERE id = ? LIMIT 1";
			
			//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt = con.prepareStatement(sql)) {			
				
				pStmt.setInt(1, id);
				ResultSet rs = pStmt.executeQuery();			

				if (rs.next()) {
					cancion = new Cancion(rs.getInt("id"), 
							rs.getString("titulo"), 
							rs.getDouble("duracion"));
				}
				
				rs.close();
				
				logger.info(String.format("Se ha recuperado el personaje %s", cancion));			
			} catch (Exception ex) {
				logger.warning(String.format("Error recuperar los personajes con id %d: %s", id, ex.getMessage()));						
			}		
			return cancion;
		}
		public List<Integer> getIDsCancionesdelAlbum(Album album){
			List<Integer> idsCancion = new ArrayList<>();		
			String sql = "SELECT cancion_id FROM AlbumCancion WHERE album_id = ?";
			
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt = con.prepareStatement(sql)) {			
				
				pStmt.setInt(1, album.getId());
				ResultSet rs = pStmt.executeQuery();			

				while (rs.next()) {
					idsCancion.add(rs.getInt("cancion_id"));
				}
				
				rs.close();
				
				logger.info(String.format("Se han recuperado %d ids de los personajes del comic %s", 
						idsCancion.size(), album.getTitulo()));			
			} catch (Exception ex) {
				logger.warning(String.format("Error recuperar los ids de los personajes del comic %d: %s",
						album.getTitulo(), ex.getMessage()));						
			}		
			
			return idsCancion;
		}
}
