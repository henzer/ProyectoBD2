package modulo1.vistas;

import java.io.IOException;

import javax.swing.UIManager;

import modulo2.vistas.Ventana;

public class MainClientes {

	public static void main(String args[]) throws IOException{
		try{
		   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		   GestionClientes ventanaClientes = new GestionClientes();
		   ventanaClientes.setVisible(true);
		}
		catch (Exception e){
		   e.printStackTrace();
		}
	}
	
}
