package modulo1.vistas;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import modulo1.controladores.ControladorTelefono;
import org.json.JSONException;
import org.json.JSONObject;

public class Categorias extends JFrame
{
	private JPanel contentPane;
	private JTable table;
	private ControladorTelefono control;
	private GestionClientes parent;
	
	public Categorias(GestionClientes parent) 
	{
		this.parent = parent;
		control = new ControladorTelefono();
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
					JSONObject categorias = control.getElement(i);
					System.out.println(categorias);
					int idCategorias = categorias.getInt("idcategorias");
					cerrarVentana(idCategorias);
				} 
				catch (JSONException e1)
				{
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(table, BorderLayout.CENTER);
	}
	
	private void cerrarVentana(int idCategorias)
	{
		//Aquí se asigna este valor a alguna funcion de la clase Gestión Clientes.
		this.dispose();
	}

}
