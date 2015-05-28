package modulo1.vistas;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;

import org.json.JSONException;
import org.json.JSONObject;

import modulo1.controladores.ControladorTelefono;

import javax.swing.ListSelectionModel;

public class Telefono extends JFrame 
{
	private JPanel contentPane;
	private JTable table;
	private ControladorTelefono control;
	private GestionClientes parent;
	
	public Telefono(GestionClientes parent) 
	{
		this.parent = parent;
		control = new ControladorTelefono();
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(control.getData());
		table.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				int i = table.getSelectedRow();
				try
				{
					JSONObject telefono = control.getElement(i);
					System.out.println(telefono);
					int idTelefono = telefono.getInt("idtelefono");
					System.out.println("ID seleccionado: " + idTelefono);
					parent.modificarCampo(String.valueOf(idTelefono));
					cerrarVentana();
				} 
				catch (JSONException e1) 
				{
					e1.printStackTrace();
				}
			}
			
		});
		addWindowListener(new WindowAdapter(){
			 public void windowClosing(WindowEvent we){
				 cerrarVentana();
			 }
		});
		contentPane.add(table, BorderLayout.CENTER);
	}
	
	private void cerrarVentana() {
		
		this.dispose();
	}
	
	
	
	

}
