package modulo1.vistas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.BoxLayout;

import modulo1.conexion.ConexionPostgres;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GestionClientes extends JFrame{
	
	ConexionPostgres conexion = null;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public GestionClientes() {
		setTitle("Gestion de Clientes");
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		// Confirmacion de salida.
		addWindowListener(new WindowAdapter() {
		     public void windowClosing(WindowEvent ev) {
	        	int resp = JOptionPane.showConfirmDialog(null, "¿Salir del Programa?", "Alerta!", JOptionPane.YES_NO_OPTION);
	        	// Si se sale del programa, desconecta la base de datos.
	    		if (resp == JOptionPane.OK_OPTION){	    			
	    			onClose();
	          	}
		     }
		});
		
		
		JPanel panel_0 = new JPanel();
		contentPane.add(panel_0, BorderLayout.NORTH);
		
		JButton btnConnectar = new JButton("Connectar");
		btnConnectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				conexion = new ConexionPostgres();
				String query = "INSERT INTO pais VALUES (0, 'Guate')";
				conexion.executeUpdate(query);
			}
		});
		panel_0.add(btnConnectar);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.EAST);
	}
	
	public void onClose(){
		System.out.println("Saliendo del programa...");
		if (conexion != null){
			System.out.println("Cerrando la base de datos...");
			conexion.closeDatabase();
		}
  		this.dispose();
  		System.exit(0);		
	}

}