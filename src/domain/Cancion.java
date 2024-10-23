package domain;

public class Cancion {
	 private String titulo;
	 private double duracion;
	 private Album album;
	 private Artista artista;
	 
	 
	public Cancion(String titulo, double duracion, Album album, Artista artista) {
		super();
		this.titulo = titulo;
        this.duracion = duracion;
        this.album = album;
        this.artista = artista;
	}

	//Getters and Setters
	public String getTitulo() {
		return titulo;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}


	public double getDuracion() {
		return duracion;
	}


	public void setDuracion(float duracion) {
		this.duracion = duracion;
	}


	public Album getAlbum() {
		return album;
	}


	public void setAlbum(Album album) {
		this.album = album;
	}


	public Artista getArtista() {
		return artista;
	}


	public void setArtista(Artista artista) {
		this.artista = artista;
	}

	@Override
	public String toString() {
		return "Cancion [titulo=" + titulo + ", duracion=" + duracion + ", album=" + album + ", artista=" + artista
				+ "]";
	}
	 
	//String de que se esta reproduciendo
	public String reproducir() {
		return this.titulo; //TODO falta añadir el nombre del artista
	}
}
