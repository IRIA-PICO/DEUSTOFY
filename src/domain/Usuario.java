package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Usuario {
	private String nombre;
	private String sexo;
	private int edad;
	private String usuario; // usuario
	private String contr; // contraseña
	private List<Cancion> cancionesFavoritas;
	
	
	public Usuario(String nombre, String sexo, int edad, String usuario, String contr) {
		super();
		this.nombre = nombre;
		this.sexo = sexo;
		this.edad = edad;
		this.usuario = usuario;
		this.contr = contr;
		this.cancionesFavoritas = new ArrayList<>();
	}
	
	// Getters and setters
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getContr() {
		return contr;
	}
	public void setContr(String contr) {
		this.contr = contr;
	}
	public List<Cancion> getCancionesFavoritas() {
		return cancionesFavoritas;
	}
	public void updateCancionFavorita(Cancion cancion) {
		if (cancionesFavoritas.contains(cancion)) {
			cancionesFavoritas.remove(cancion);
		} else {
			cancionesFavoritas.add(cancion);
		}
	}
	@Override
	public String toString() {
		return "Usuario [nombre=" + nombre + ", sexo=" + sexo + ", edad=" + edad + ", usuario=" + usuario + ", contr="
				+ contr + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(contr, usuario, edad, nombre, sexo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(contr, other.contr) && Objects.equals(usuario, other.usuario) && edad == other.edad
				&& Objects.equals(nombre, other.nombre) && Objects.equals(sexo, other.sexo);
	}
	
	
}
