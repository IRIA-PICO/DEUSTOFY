package gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import db.GestorBD;
import domain.Cancion;
public class Recursividad {
	  public static List<List<Cancion>> combinaciones(List<Cancion> elementos, int tiempo) {
	    	//Se inicializa la lista de combinaciones que se devolverá como resultado.
	    	List<List<Cancion>> result = new ArrayList<>();
	    	//COMPLETA EL MÉTODO
	    	//Se invoca al método recursivo
	    	combinaciones(result, elementos, tiempo, new ArrayList<>());
	    	//Se devuelve el resultado.
	    	return result;
	    	
	    }
	    private static void combinaciones(List<List<Cancion>> result, List<Cancion> elementos, int tiempo, List<Cancion> temp) {
	        // Caso base. n=0 
	        if (tiempo>=0 && tiempo <=60) {//menos de un minuto
	        	List<Cancion> aux = new ArrayList<>(temp);
	        	Comparator<Cancion> comp= new Comparator<Cancion>() {
					
					@Override
					public int compare(Cancion o1, Cancion o2) {
						return Integer.compare(o1.getId(), o2.getId());
					}
				};
				aux.sort(comp);
	 
	        	if(!result.contains(aux)) {
	        		result.add(aux);   
	        	}
	        }else if(tiempo <0){
	        	return;
	        }else {
	            // Caso recursivo. Por cada elemento        	
	        	for(Cancion c : elementos) {
	        		//Se añade el elemento a la lista temporal
	        		temp.add(c);
	        		//Se realiza la invocación recursiva para n-1 elementos y una lista temporal
	        		combinaciones(result, elementos, tiempo-c.getDuracion(), temp);
	        		//Se elimina el último de la lista temporal
	        		temp.remove(temp.size()-1);
	        	}
	        }
	    }
	    public static void main(String[] args) {
	    	GestorBD gb= new GestorBD();
	    	List<Cancion> elementos = gb.getCanciones();
	    	int tiempo = 10*60;//Multiplicamos por 60 ya que en la BBDD los datos se guardan en segundos
	    	
	    	List<List<Cancion>> result = combinaciones(elementos, tiempo);
	    	System.out.format("Combinaciones de canciones que duren menos de %d ", tiempo);
	    	result.forEach(r -> System.out.format("\n%s", r));
		} 
}
