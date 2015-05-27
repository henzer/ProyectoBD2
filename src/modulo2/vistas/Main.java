package modulo2.vistas;

import java.io.IOException;
import java.util.Arrays;

import javax.swing.UIManager;

import modulo1.controladores.ControladorClientes;
import modulo1.vistas.Telefono;

public class Main {
	public static void main(String args[]) throws IOException{
		try{
		   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		   //Ventana v = new Ventana();
		   //v.setVisible(true);
		}
		catch (Exception e){
		   e.printStackTrace();
		}
	}
}