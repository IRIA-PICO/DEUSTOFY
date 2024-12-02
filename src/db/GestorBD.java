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
	                + " genero TEXT NOT NULL,\n"
	                + " rutafoto TEXT, \n"
	                + " id_artista TEXT NOT NULL,\n"
	                + " fecha TEXT NOT NULL"
	                + ");";
	
			String sql2 = "CREATE TABLE IF NOT EXISTS Artista (\n"
	                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
	                + " nombre TEXT NOT NULL,\n"
	                + " rutafoto TEXT NOT NULL"
	                + ");";
			
			String sql3 = "CREATE TABLE IF NOT EXISTS Cancion (\n"
	                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
	                + " titulo TEXT NOT NULL,\n"
	                + " duracion INTEGER NOT NULL, \n"
	                + " id_artista TEXT NOT NULL"
	                + ");";
	
			String sql4 = "CREATE TABLE IF NOT EXISTS AlbumCancion (\n"
	                + " album_id INTEGER NOT NULL,\n"
	                + " cancion_id INTEGER NOT NULL,\n"
	                + " PRIMARY KEY(album_id, cancion_id),\n"
	                + " FOREIGN KEY (album_id) REFERENCES Album(id) ON DELETE CASCADE,\n"
	                + " FOREIGN KEY (cancion_id) REFERENCES Cancion(id) ON DELETE CASCADE"
	                + ");";
			
			String sql5= "CREATE TABLE IF NOT EXISTS UsuarioPlaylist (\n"
					+ " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
					+ " nomList TEXT UNIQUE NOT NULL,\n"
                    + " usuario_id INTEGER NOT NULL,\n"
                    + "FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE CASCADE"
                    + ");";
			
			String sql6= "CREATE TABLE IF NOT EXISTS PlaylistCancion (\n"
                    + " playlist_id INTEGER NOT NULL,\n"
                    + " cancion_id INTEGER NOT NULL,\n"
                    + " FOREIGN KEY (playlist_id) REFERENCES UsuarioPlaylist(id),\n"                   
                    + " FOREIGN KEY (cancion_id) REFERENCES Cancion(id)"
                    + ");";
			
			String sql7 = "CREATE TABLE IF NOT EXISTS Usuario (\n"
	                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
	                + " nombre TEXT NOT NULL,\n"
	                + " sexo TEXT NOT NULL,\n"
	                + " edad INTEGER NOT NULL,\n"
	                + " correo TEXT NOT NULL,\n"
	                + " contr TEXT NOT NULL"
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
				PreparedStatement pStmt7 = con.prepareStatement(sql7);) {
				
				//Se ejecutan las sentencias de creación de las tablas
		        if (!pStmt1.execute() && !pStmt2.execute() && !pStmt3.execute() && !pStmt4.execute() && !pStmt5.execute()  && !pStmt6.execute() && !pStmt7.execute()) {
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
			String sql5 = "DROP TABLE IF EXISTS UsuarioPlaylist;";
			String sql6 = "DROP TABLE IF EXISTS PlaylistCancion;";
			String sql7 = "DROP TABLE IF EXISTS Usuario;";
			
	        //Se abre la conexión y se crea un PreparedStatement para borrar cada tabla
			try (Connection con = DriverManager.getConnection(connectionString);
					PreparedStatement pStmt1 = con.prepareStatement(sql1);
					PreparedStatement pStmt2 = con.prepareStatement(sql2);
					PreparedStatement pStmt3 = con.prepareStatement(sql3);
					PreparedStatement pStmt4 = con.prepareStatement(sql4);
					PreparedStatement pStmt5 = con.prepareStatement(sql5);
					PreparedStatement pStmt6 = con.prepareStatement(sql6);
					PreparedStatement pStmt7 = con.prepareStatement(sql7);) {
				
				//Se ejecutan las sentencias de borrado de las tablas
		        if (!pStmt1.execute() && !pStmt2.execute() && !pStmt3.execute() && !pStmt4.execute() && !pStmt5.execute() && !pStmt6.execute() && !pStmt7.execute()) {
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
			String sql5 = "DELETE FROM UsuarioPlaylist;";
			String sql6 = "DELETE FROM PlaylistCancion;";
			String sql7 = "DELETE FROM Usuario;";
			
	        //Se abre la conexión y se crea un PreparedStatement para borrar los datos de cada tabla
			try (Connection con = DriverManager.getConnection(connectionString);
					PreparedStatement pStmt1 = con.prepareStatement(sql1);
					PreparedStatement pStmt2 = con.prepareStatement(sql2);
					PreparedStatement pStmt3 = con.prepareStatement(sql3);
					PreparedStatement pStmt4 = con.prepareStatement(sql4);
					PreparedStatement pStmt5 = con.prepareStatement(sql5);
					PreparedStatement pStmt6 = con.prepareStatement(sql6);
					PreparedStatement pStmt7 = con.prepareStatement(sql7)) {
					
				//Se ejecutan las sentencias de borrado de las tablas
		        if (!pStmt1.execute() && !pStmt2.execute() && !pStmt3.execute() && !pStmt4.execute() && !pStmt5.execute() && !pStmt6.execute() && !pStmt7.execute()) {
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
				c = new Cancion(-1,fragmentos[0], Double.parseDouble(fragmentos[1]), fragmentos[2]);
								
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
	        String linea; // Línea actual del CSV
	        
	        while ((linea = in.readLine()) != null) {
	            try {
	                // Dividimos la línea por punto y coma, respetando las columnas definidas
	                String[] fragmentos = linea.split(";", 6);
	                if (fragmentos.length < 6) {
	                    logger.warning("Línea con formato incorrecto: " + linea);
	                    continue;
	                }

	                // Obtenemos los datos básicos del álbum
	                String titulo = fragmentos[0];
	                Genero genero = Genero.valueOf(fragmentos[1].toUpperCase()); // Convertimos a Enum
	                String rutaFoto = fragmentos[2];
	                int anio = Integer.parseInt(fragmentos[3]);
	                String artista = fragmentos[4];
	                
	                // Creamos el objeto Album
	                Album album = new Album(
	                    -1, // ID generado automáticamente
	                    titulo,
	                    genero,
	                    rutaFoto,
	                    artista,
	                    anio
	                );
	                String[] cancionesFragmentos = fragmentos[5].split(";");
	                for (String cancionTitulo : cancionesFragmentos) {
	                    album.getListacancionestitulos().add(cancionTitulo); // Creamos objetos Cancion
	                }
	                // Añadimos el álbum a la lista
	                albumes.add(album);

	            } catch (Exception ex) {
	                logger.warning("Error procesando la línea: " + linea + " - " + ex.getMessage());
	            }
	        }

	    } catch (Exception ex) {
	        logger.warning(String.format("Error leyendo álbumes del CSV: %s", ex.getMessage()));
	    }
	    
	    return albumes;
	}
	
	public List<Artista> loadCVSArtista() {
	    List<Artista> artistas = new ArrayList<>();

	    try (BufferedReader in = new BufferedReader(new FileReader(CSV_ARTISTAS))) {
	        String linea;

	        while ((linea = in.readLine()) != null) {
	            String[] fragmentos = linea.split(";");
	            // Asegúrate de que el ID y nombre estén correctamente leídos
	            try {
	                int id = Integer.parseInt(fragmentos[0].trim());  // ID es un número
	                String nombre = fragmentos[1].trim(); // El nombre es una cadena
	                String rutaFoto = fragmentos[2].trim();
	                Artista artista = new Artista(id, nombre, rutaFoto);
	                artistas.add(artista);
	            } catch (NumberFormatException ex) {
	                logger.warning("Formato de datos incorrecto en el archivo CSV para el artista: " + linea);
	            }
	        }

	    } catch (Exception ex) {
	        logger.warning("Error leyendo artistas del CSV: " + ex.getMessage());
	    }

	    return artistas;
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
		            if (a.getArtista() == null) {
		                logger.warning("El álbum '" + a.getTitulo() + "' no tiene un artista asignado.");
		                continue;  // Salta este álbum si no tiene artista
		            }

		            pStmt.setString(1, a.getTitulo());
		            pStmt.setString(2, a.getGenero().toString());
		            pStmt.setString(3, a.getRutaFoto());
		            pStmt.setInt(4, getIdArtistaByNombre(a.getArtista().getNombre()));
		            pStmt.setInt(5, a.getAnyo());

		            if (pStmt.executeUpdate() != 1) {
		                logger.warning("No se ha insertado el álbum: " + a.getTitulo());
		            } else {
		                logger.info("Se ha insertado el álbum: " + a.getTitulo());
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
		            if (c.getArtista() == null) {
		                logger.warning("La canción '" + c.getTitulo() + "' no tiene un artista asignado.");
		                continue;  // Salta esta canción si no tiene artista
		            }

		            pStmt.setString(1, c.getTitulo());
		            pStmt.setDouble(2, c.getDuracion());
		            pStmt.setInt(3, getIdArtistaByNombre(c.getArtista().getNombre()));

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
						rs.getDouble("duracion"),
						getArtistaByID(rs.getInt("id_artista")).getNombre());
					 					 
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
							getArtistaByID(rs.getInt("id_artista")).getNombre(),
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
		public Album getAlbumByID(int id) {
		    Album album = null; // Inicializamos la variable que contendrá el álbum
		    String sql = "SELECT * FROM Album WHERE id_album = ? LIMIT 1"; // Cambiamos 'Cancion' a 'Album' y ajustamos el campo ID

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
		                    rs.getInt("id_album"),      // ID del álbum
		                    rs.getString("titulo"),     // Título del álbum
		                    Genero.valueOf(rs.getString("genero")),
		                    rs.getString("rutaFoto"),
		                    getArtistaByID(rs.getInt("id_artista")).getNombre(),
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
		    String sql = "SELECT * FROM Artista WHERE id_artista = ? LIMIT 1"; // Cambiamos la tabla y campo a 'Artista'

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
		                    rs.getInt("id_artista"),   // ID del artista
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
		
		
		public int getIdAlbumByTitulo(String titulo) {
		    int id = 0; // Variable para almacenar el ID del álbum
		    String sql = "SELECT id_album FROM Album WHERE titulo = ? LIMIT 1";

		    // Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {
		         
		        // Se define el parámetro de la sentencia SQL
		        pStmt.setString(1, titulo);

		        // Se ejecuta la sentencia y se obtiene el ResultSet con los resultados
		        try (ResultSet rs = pStmt.executeQuery()) {
		            if (rs.next()) {
		                id = rs.getInt("id_album"); // Se obtiene el ID de la columna
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
		    int id = 0; // Variable para almacenar el ID del artista
		    String sql = "SELECT id_artista FROM Artista WHERE nombre = ? LIMIT 1"; // Cambié 'titulo' a 'nombre' para que coincida con la columna correcta

		    // Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pStmt = con.prepareStatement(sql)) {
		         
		        // Se define el parámetro de la sentencia SQL
		        pStmt.setString(1, nombre);

		        // Se ejecuta la sentencia y se obtiene el ResultSet con los resultados
		        try (ResultSet rs = pStmt.executeQuery()) {
		            if (rs.next()) {
		                id = rs.getInt("id_artista"); // Se obtiene el ID de la columna 'id_artista'
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
