package main;



import javax.swing.SwingUtilities;
import db.GestorBD;
import gui.VentanaIdentificacion;

public class Main {
    
	public static void main(String[] args) {
		GestorBD gestorBD = new GestorBD();
		
		//Se crea la BBDD
		gestorBD.crearBBDD();
		
		//Se cargan los datos y se inicializa la BBDD
		gestorBD.initilizeFromCSV();
						
		//Lambda expression para abrir la ventana Principal
		SwingUtilities.invokeLater(() -> new VentanaIdentificacion(gestorBD));
		
		gestorBD.borrarBBDD();
	}
}

