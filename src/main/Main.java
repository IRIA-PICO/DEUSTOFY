package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import domain.Album;
import domain.Artista;
import domain.Cancion;
import domain.Genero;
import gui.VentanaAlbumArtista;

public class Main {
	
	static List<Artista> artistas = new ArrayList<>();
    static List<Album> albumes = new ArrayList<>();
    List<Cancion> canciones = new ArrayList<>();
	public void cargarCSV(){
		File f = new File("resources/data/musica_.csv");
		
		if(f.exists()) {
			try {
				Scanner sc = new Scanner(f);
				while(sc.hasNextLine()) { 
					String linea = sc.nextLine(); 
					String [] datos = linea.split(";");
					
					//sacamos los datos del csv
					int idArtista = Integer.parseInt(datos[0]);
                    String nombreArtista = datos[1];
                    String rutaFotoArtista = datos[2];
                    String tituloAlbum = datos[3];
                    double duracionAlbum = Double.parseDouble(datos[4]);
                    String generoAlbum = datos[5].toUpperCase();
                    String rutaFotoAlbum = datos[6];
                    String tituloCancion = datos[7];
                    double duracionCancion = Double.parseDouble(datos[8]);
                    
                    Artista artista = buscarArtista(idArtista);
                    if (artista == null) {
                        artista = new Artista(idArtista, nombreArtista, rutaFotoArtista);
                        artistas.add(artista);
                    }

                    // Buscar si el álbum ya existe
                    Album album = buscarAlbum(tituloAlbum, artista);
                    if (album == null) {
                        album = new Album(tituloAlbum, duracionAlbum, Genero.valueOf(generoAlbum), artista, rutaFotoAlbum);
                        albumes.add(album);
                        artista.getAlbumes().add(album); // Añadir el álbum a la lista de álbumes del artista
                    }

                    // Crear la canción y añadirla al álbum y a la lista general de canciones
                    Cancion cancion = new Cancion(tituloCancion, duracionCancion, album, artista);
                    canciones.add(cancion);
                    album.getListacanciones().add(cancion);
				}
				sc.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	
	}
	
	private Artista buscarArtista(int idArtista) {
        for (Artista a : artistas) {
            if (a.getId() == idArtista) {
                return a;
            }
        }
        return null;
    }

    // Método para buscar un álbum por título y artista
    private Album buscarAlbum(String titulo, Artista artista) {
        for (Album a : albumes) {
            if (a.getTitulo().equalsIgnoreCase(titulo) && a.getArtista().equals(artista)) {
                return a;
            }
        }
        return null;
    }
    
	public static void main(String[] args) {
		new Main().cargarCSV();
        SwingUtilities.invokeLater(() -> new VentanaAlbumArtista(albumes, artistas));
	}
}

