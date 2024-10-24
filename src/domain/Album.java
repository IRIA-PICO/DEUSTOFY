package domain;

import java.util.List;

public class Album {
	private String titulo;
	private double duracion;
	private Genero genero;
	private Artista Artista;
	private List<Cancion> listacanciones;
	private String rutaFoto;
	
	
	public Album(String titulo, double duracion, Genero genero, domain.Artista artista, List<Cancion> listacanciones,
			String rutaFoto) {
		super();
		this.titulo = titulo;
		this.duracion = duracion;
		this.genero = genero;
		Artista = artista;
		this.listacanciones = listacanciones;
		this.rutaFoto = rutaFoto;
	}


	public String getTitulo() {
		return titulo;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}


	public double getDuracion() {
		return duracion;
	}


	public void setDuracion(double duracion) {
		this.duracion = duracion;
	}


	public Genero getGenero() {
		return genero;
	}


	public void setGenero(Genero genero) {
		this.genero = genero;
	}


	public Artista getArtista() {
		return Artista;
	}


	public void setArtista(Artista artista) {
		Artista = artista;
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
	
}
