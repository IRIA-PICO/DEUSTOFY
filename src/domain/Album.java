package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Album {
	private String titulo;
	private double duracion;
	private Genero genero;
	private Artista artista;
	private List<Cancion> listacanciones;
	private String rutaFoto;
	
	
	public Album(String titulo, double duracion, Genero genero, Artista artista,String rutaFoto) {
		super();
		this.titulo = titulo;
		this.duracion = duracion;
		this.genero = genero;
		this.artista = artista;
		this.listacanciones = new ArrayList<>();
		this.rutaFoto = rutaFoto;
	}


	@Override
	public int hashCode() {
		return Objects.hash(artista, duracion, genero, listacanciones, rutaFoto, titulo);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Album other = (Album) obj;
		return Objects.equals(artista, other.artista)
				&& Double.doubleToLongBits(duracion) == Double.doubleToLongBits(other.duracion)
				&& genero == other.genero && Objects.equals(listacanciones, other.listacanciones)
				&& Objects.equals(rutaFoto, other.rutaFoto) && Objects.equals(titulo, other.titulo);
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
	
}
