package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import domain.Album;
import domain.Artista;
import domain.Cancion;
import domain.Genero;

public class Main {
	
	
	public void cargarCSV(){
		File f = new File("resources/data/musica_.csv");
		List<Artista> artistas = new ArrayList<>();
	    List<Album> albumes = new ArrayList<>();
	    List<Cancion> canciones = new ArrayList<>();
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
                    String generoAlbum = datos[5];
                    String rutaFotoAlbum = datos[6];
                    String tituloCancion = datos[7];
                    double duracionCancion = Double.parseDouble(datos[8]);
                    
                    //Asignamos los datos
                    Artista artista= new Artista(idArtista, nombreArtista, rutaFotoArtista);
                    Album album= new Album(tituloAlbum, duracionAlbum, Genero.valueOf(generoAlbum), artista, rutaFotoAlbum);
                    Cancion cancion= new Cancion(tituloCancion, duracionCancion, album, artista);
                    //Añadimos a el artista a el arraylist de sus albumes y a el album arraylist de sus canciones
                    if(!artista.getAlbumes().contains(album) && album.getArtista().equals(artista) ) {
						artista.getAlbumes().add(album);
					} else {
						if(!album.getListacanciones().contains(cancion)) {
							album.getListacanciones().add(cancion);
						}
					}
                    
                    //Añadimos a las listas con los datos mezclados
                    if(!artistas.contains(artista)) {
                    	artistas.add(artista);
                    }
                    
                    
					if(!albumes.contains(album)) {
						albumes.add(album);
					} 
					
                    
                    if(!canciones.contains(cancion)) {
                    	canciones.add(cancion);
                    }
                    
					
				}
				sc.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	
	}
	}
