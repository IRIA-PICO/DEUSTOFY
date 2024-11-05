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
                    
                    Artista artista = new Artista(idArtista, nombreArtista, rutaFotoArtista);
                    if (!artistas.contains(artista)) artistas.add(artista);

                    Album album = new Album(tituloAlbum, duracionAlbum, Genero.valueOf(generoAlbum), artista, rutaFotoAlbum);
                    if (!albumes.contains(album)) albumes.add(album);
                    
                    Cancion cancion = new Cancion(tituloCancion, duracionCancion, album, artista);
                    canciones.add(cancion);

                    if (!artista.getAlbumes().contains(album)) artista.getAlbumes().add(album);
                    if (!album.getListacanciones().contains(cancion)) album.getListacanciones().add(cancion);
				}
				sc.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	
	}
	public static void main(String[] args) {
		new Main().cargarCSV();
        SwingUtilities.invokeLater(() -> new VentanaAlbumArtista(albumes, artistas));
	}
	
	}

