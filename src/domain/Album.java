package domain;

import java.util.ArrayList;
import java.util.List;


public class Album {
	private int id;
	private String titulo;
	private Genero genero;
	private Artista artista;
	private List<Cancion> listacanciones;
	private String rutaFoto;
	private int artistaID;
	private int anyo;
	

	public int getAnyo() {
		return anyo;
	}

	public void setAnyo(int anyo) {
		this.anyo = anyo;
	}

	public Album(String titulo, Genero genero, Artista artista,String rutaFoto) {
		super();
		this.titulo = titulo;
		this.genero = genero;
		this.artista = artista;
		this.listacanciones = new ArrayList<>();
		this.rutaFoto = rutaFoto;
	}
	
	public Album(int id, String titulo, Genero genero, String rutaFoto, int artistaID, int anyo) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.genero = genero;
		this.listacanciones = new ArrayList<>();
		this.rutaFoto = rutaFoto;
		this.artistaID = artistaID;
		this.anyo = anyo;
	}

	

	

	public int getArtistaID() {
		return artistaID;
	}

	public void setArtistaID(int artistaID) {
		this.artistaID = artistaID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitulo() {
		return titulo;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Genero getGenero() {
		return genero;
	}


	public void setGenero(Genero genero) {
		this.genero = genero;
	}


	public Artista getArtista() {
		return artista;
	}


	public void setArtista(Artista artista) {
		this.artista = artista;
	}


	public List<Cancion> getListacanciones() {
		return listacanciones;
	}


	public void setListacanciones(List<Cancion> listacanciones) {
		this.listacanciones = listacanciones;
	}


	public String getRutaFoto() {
		return rutaFoto;
	}


	public void setRutaFoto(String rutaFoto) {
		this.rutaFoto = rutaFoto;
	}
	public void addCancion(Cancion cancion) {
		if (cancion != null && !this.listacanciones.contains(cancion)) {
			listacanciones.add(cancion);
		}
	}
}
