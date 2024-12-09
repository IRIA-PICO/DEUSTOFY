package domain;

import java.util.ArrayList;
import java.util.List;

public class PlayList {
	private String titulo;
	private List<Cancion> listacanciones;
	private String rutaFoto;
	private String color;
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public PlayList(String titulo, List<Cancion> listacanciones, String rutaFoto, String color) {
		this.titulo = titulo;
		this.listacanciones = new ArrayList<>();
		this.rutaFoto = rutaFoto;
		this.color = color;
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