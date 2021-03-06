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

import modulo1.controladores.ControladorEstado;
import modulo1.controladores.ControladorTelefono;

import org.json.JSONException;
import org.json.JSONObject;

public class Estado extends JFrame
{
	private JPanel contentPane;
	private JTable table;
	private ControladorEstado control;
	private GestionClientes parent;
	
	public Estado(GestionClientes parent) 
	{
		this.parent = parent;
		control = new ControladorEstado();
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
					JSONObject estado = control.getElement(i);
					System.out.println(estado);
					int idEstado = estado.getInt("idestado");
					parent.modificarCampo(String.valueOf(idEstado));
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
