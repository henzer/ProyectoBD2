package modulo1.vistas;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import modulo1.controladores.ControladorDepartamento;
import modulo1.controladores.ControladorTelefono;

import org.json.JSONException;
import org.json.JSONObject;


public class Departamento extends JFrame
{
	private JPanel contentPane;
	private JTable table;
	private ControladorDepartamento control;
	private GestionClientes parent;
	
	public Departamento(GestionClientes parent) 
	{
		this.parent = parent;
		control = new ControladorDepartamento();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
					JSONObject departamento = control.getElement(i);
					System.out.println(departamento);
					int idDepartamento = departamento.getInt("iddepartamento");
					parent.modificarCampo(String.valueOf(idDepartamento));
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
