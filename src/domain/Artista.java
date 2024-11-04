package domain;

import java.util.ArrayList;
import java.util.List;

public class Artista {
	private int id;
	private String nombre;
	private List<Album> albumes;
	private List<Cancion> canciones;
	private String rutaFoto;

	public Artista(int id, String nombre, String rutaFoto) {
		this.id = id;
		this.nombre = nombre;
		this.albumes = new ArrayList<>();
		this.canciones = new ArrayList<>();
		this.rutaFoto = rutaFoto;
	}
	
	// Getters and setters
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public List<Album> getAlbumes() {
		return albumes;
	}
	
	public String getRutaFoto() {
		return rutaFoto;
	}


	public void setRutaFoto(String rutaFoto) {
		this.rutaFoto = rutaFoto;
	}
	
	// Agregar album y sus canciones
	public void agregarAlbum(Album album) {
		if (!albumes.contains(album)) {
			albumes.add(album);
			for (Cancion cancion:album.getListacanciones()) {
				if (!canciones.contains(cancion)) {
					canciones.add(cancion);
				}
			}
		}
	}
	
	public List<Cancion> getCanciones() {
		return canciones;
	}
	
	// Agregar cancion suelta
	public void agregarCancion(Cancion cancion) {
		if (!canciones.contains(cancion)) {
			canciones.add(cancion);
		}
	}
}
