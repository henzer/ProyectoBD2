package modulo2.vistas;

import java.io.IOException;
import java.util.Arrays;

import javax.swing.UIManager;

import modulo1.controladores.ControladorClientes;

public class Main {
	public static void main(String args[]) throws IOException{
		try{
		   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		   //Ventana v = new Ventana();
		   //v.setVisible(true);
		   ControladorClientes c = new ControladorClientes();
		   c.getDataClientes(Arrays.asList("nombres", "apellidos", "correo"));
		}
		catch (Exception e){
		   e.printStackTrace();
		}
	}
}