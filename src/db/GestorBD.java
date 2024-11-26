package db;


import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;



public class GestorBD {
	private final String PROPERTIES_FILE = "resources/config/app.properties";
	
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
	                + " rutafoto TEXT NOT NULL,\n"
	                + " UNIQUE(titulo, rutafoto));";
	
			String sql2 = "CREATE TABLE IF NOT EXISTS Artista (\n"
	                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
	                + " nombre TEXT NOT NULL,\n"
	                + " rutafoto TEXT UNIQUE NOT NULL\n"
	                + ");";
			
			String sql3 = "CREATE TABLE IF NOT EXISTS Cancion (\n"
	                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
	                + " titulo TEXT NOT NULL,\n"
	                + " duracion TEXT UNIQUE NOT NULL\n"
	                + ");";
	
			String sql4 = "CREATE TABLE IF NOT EXISTS AlbumCancion (\n"
	                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
	                + " album_id INTEGER NOT NULL,\n"
	                + " cancion_id INTEGER NOT NULL,\n"
	                + " PRIMARY KEY(album_id, cancion_id),\n"
	                + " FOREIGN KEY (album_id) REFERENCES Album(id) ON DELETE CASCADE\n"
	                + " FOREIGN KEY (cancion_id) REFERENCES Cancion(id) ON DELETE CASCADE\n"
	                + ");";
			
			String sql5 = "CREATE TABLE IF NOT EXISTS ArtistaAlbum (\n"
	                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
	                + " artista_id INTEGER NOT NULL,\n"
	                + " album_id INTEGER NOT NULL, \n"
	                + " PRIMARY KEY(album_id, artista_id)\n"
	                + " FOREIGN KEY (artista_id) REFERENCES Artista(id) ON DELETE CASCADE\n"
	                + " FOREIGN KEY (album_id) REFERENCES Album(id) ON DELETE CASCADE\n"
	                + ");";
			
	        //Se abre la conexión y se crea un PreparedStatement para crer cada tabla
			//Al abrir la conexión, si no existía el fichero por defecto, se crea.
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt1 = con.prepareStatement(sql1);
				 PreparedStatement pStmt2 = con.prepareStatement(sql2);
				 PreparedStatement pStmt3 = con.prepareStatement(sql3);
				PreparedStatement pStmt4 = con.prepareStatement(sql4);
					PreparedStatement pStmt5 = con.prepareStatement(sql5))	{
				
				//Se ejecutan las sentencias de creación de las tablas
		        if (!pStmt1.execute() && !pStmt2.execute() && !pStmt3.execute() && !pStmt4.execute() && !pStmt5.execute()) {
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
			
	        //Se abre la conexión y se crea un PreparedStatement para borrar cada tabla
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt1 = con.prepareStatement(sql1);
				 PreparedStatement pStmt2 = con.prepareStatement(sql2);
				 PreparedStatement pStmt3 = con.prepareStatement(sql3);
					PreparedStatement pStmt4 = con.prepareStatement(sql4);
					 PreparedStatement pStmt5 = con.prepareStatement(sql5)){
				
				//Se ejecutan las sentencias de borrado de las tablas
		        if (!pStmt1.execute() && !pStmt2.execute() && !pStmt3.execute() && !pStmt4.execute() && !pStmt5.execute()) {
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
}
