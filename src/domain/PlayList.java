package domain;

import java.util.ArrayList;
import java.util.List;

public class PlayList {
	private String titulo;
	private List<Cancion> listacanciones;
	private String rutaFoto;
	
	public PlayList(String titulo, List<Cancion> listacanciones, String rutaFoto) {
		this.titulo = titulo;
		this.listacanciones = new ArrayList<>();
		this.rutaFoto = rutaFoto;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
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