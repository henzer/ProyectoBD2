package modulo1.vistas;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Rectangle;

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

import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Dimension;

import javax.swing.JCheckBox;

import java.awt.GridLayout;

import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.SpringLayout;

import java.awt.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.UIManager;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;

public class GestionClientes extends JFrame{
	
	ConexionPostgres conexion = null;
	private JPanel contentPane;
	private JTable table;
	private JTextField textField_1;	
	private JPanel panelGCForm;
	private JScrollPane scrollPaneGCForm;
	private JPanel panelGestionarCliente;
	private String objectToShow = "cliente";
	private Map<String, JTextField> listFormFields;

	/**
	 * Create the frame.
	 */
	public GestionClientes() {
		setTitle("Gestion de Clientes");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		
		conexion = new ConexionPostgres();
		
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
		
		
		JPanel panelEncabezado = new JPanel();
		contentPane.add(panelEncabezado, BorderLayout.NORTH);
		
		JButton btnConnectar = new JButton("Connectar");
		btnConnectar.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String query = "SELECT * FROM estado;";
				JSONArray result = conexion.executeQuery(query);
				for (int i=0; i<result.length(); i++){
					try {
						System.out.println(result.get(i).toString());
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
						
			}
			
		});
		panelEncabezado.add(btnConnectar);
		
		
		JPanel panelGestion = new JPanel();
		contentPane.add(panelGestion, BorderLayout.CENTER);
		panelGestion.setLayout(new BorderLayout(0, 0));
		
		
		panelGestionarCliente = new JPanel();
		panelGestionarCliente.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelGestionarCliente.setPreferredSize(new Dimension(400, 10));
		panelGestion.add(panelGestionarCliente, BorderLayout.EAST);
		panelGestionarCliente.setLayout(new BorderLayout(0, 0));		
		
		// Paneles de Gestionar Cliente.
		JPanel panelGCBuscar = new JPanel();		
		JPanel panelGCGestionar = new JPanel();
		
		
		// ********** Panel de busqueda **********
		
		JLabel lblMostrar = new JLabel("Mostrar : ");
		panelGCBuscar.add(lblMostrar);
		
		// Combo Box para el elemento a mostrar en el formulario.
		JComboBox comboBoxMostrar = new JComboBox();
		comboBoxMostrar.setModel(new DefaultComboBoxModel(new String[] {"Cliente", "Categoria", "Estado", "Pais", "Departamento", "Telefono"}));
		panelGCBuscar.add(comboBoxMostrar);
		
		JLabel lblId = new JLabel("con ID : ");
		panelGCBuscar.add(lblId);
		
		textField_1 = new JTextField();
		panelGCBuscar.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Getting id...
				int id;
				try{
					id = Integer.parseInt(textField_1.getText());
				}
				catch (Exception err){
					JOptionPane.showMessageDialog(null, "ID inválido", "CRM Clientes", JOptionPane.ERROR_MESSAGE);
					textField_1.requestFocusInWindow();
					return;
				}
				
				
				objectToShow = comboBoxMostrar.getSelectedItem().toString().toLowerCase();
		    	String tableName = objectToShow;
		    	if (tableName.equals("cliente"))
		    		tableName = "clientes";		    	

				// Getting properties for this id.
				String query = "SELECT * FROM " + tableName + " WHERE id" + objectToShow + " = " + id + ";" ;
				JSONArray jsonRow = conexion.executeQuery(query);
				System.out.println("jsonRoww: " + jsonRow.length());
		    	
		    	
		    	ArrayList<String> formLabels = conexion.getTableColumns(tableName);
		    	System.out.println("formLabelsSize: " + formLabels.size());
		    	System.out.println("listFormFieldsSize: " + listFormFields.size());
				for (int i=0; i < formLabels.size(); i++){
					String propertie = "null";
					try {
						propertie = jsonRow.getJSONObject(0).getString(formLabels.get(i));
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						return;
					}
					listFormFields.get("textField" + formLabels.get(i)).setText(propertie);
				}
				
				
			}
		});		
		panelGCBuscar.add(btnBuscar);		
		panelGestionarCliente.add(panelGCBuscar, BorderLayout.NORTH);
		
		
		comboBoxMostrar.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	objectToShow = comboBoxMostrar.getSelectedItem().toString().toLowerCase();
		    	panelGestionarCliente.removeAll();		    					
		    	panelGestionarCliente.add(panelGCBuscar, BorderLayout.NORTH);
		    	String tableName = objectToShow;
		    	if (tableName.equals("cliente"))
		    		tableName = "clientes";
		    	createFormShow(tableName);
				scrollPaneGCForm = new JScrollPane(panelGCForm);
				panelGestionarCliente.add(scrollPaneGCForm, BorderLayout.CENTER);
				panelGestionarCliente.add(panelGCGestionar, BorderLayout.SOUTH);
				panelGestionarCliente.updateUI();
		    }
		});
		
		
		// ********** Panel del Formulario **********
		
		// Creacion del formulario para mostrar un cliente.
		this.createFormShow("clientes");
		scrollPaneGCForm = new JScrollPane(panelGCForm);
		panelGestionarCliente.add(scrollPaneGCForm, BorderLayout.CENTER);
		panelGCGestionar.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panelGCGestionarNavegar = new JPanel();
		panelGCGestionar.add(panelGCGestionarNavegar);
		
		JButton button = new JButton("<<");
		panelGCGestionarNavegar.add(button);
		
		JButton button_1 = new JButton("<");
		panelGCGestionarNavegar.add(button_1);
		
		JButton button_2 = new JButton(">");
		panelGCGestionarNavegar.add(button_2);
		
		JButton button_3 = new JButton(">>");
		panelGCGestionarNavegar.add(button_3);
		
		JPanel panelGCGestionarGestionar = new JPanel();
		panelGCGestionar.add(panelGCGestionarGestionar);
		
		
		// ********** Panel de Gestión **********
		
		JButton btnNuevo = new JButton("Nuevo");
		panelGCGestionarGestionar.add(btnNuevo);
		JButton btnEditar = new JButton("Editar");
		panelGCGestionarGestionar.add(btnEditar);
		JButton btnGuardar = new JButton("Guardar");
		panelGCGestionarGestionar.add(btnGuardar);
		JButton btnEliminar = new JButton("Eliminar");
		panelGCGestionarGestionar.add(btnEliminar);
		
		panelGestionarCliente.add(panelGCGestionar, BorderLayout.SOUTH);
		
		
		
		
		JPanel panelGCFiltrar = new JPanel();
		panelGCFiltrar.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelGestion.add(panelGCFiltrar, BorderLayout.CENTER);
		
		JPanel panelTabla = new JPanel();
		panelTabla.setPreferredSize(new Dimension(10, 300));
		contentPane.add(panelTabla, BorderLayout.SOUTH);
		panelTabla.setLayout(new BorderLayout(0, 0));
		
		
	
		String[] columnNames = {"First Name",
                "Last Name",
                "Sport",
                "# of Years",
                "Vegetarian"};
		Object[][] data = {
			    {"Kathy", "Smith",
			     "Snowboarding", new Integer(5), new Boolean(false)},
			    {"John", "Doe",
			     "Rowing", new Integer(3), new Boolean(true)},
			    {"Sue", "Black",
			     "Knitting", new Integer(2), new Boolean(false)},
			    {"Jane", "White",
			     "Speed reading", new Integer(20), new Boolean(true)},
			    {"Joe", "Brown",
			     "Pool", new Integer(10), new Boolean(false)}
			};
		
		JPanel panel = new JPanel();
		panelTabla.add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JCheckBox chckbxTodo = new JCheckBox("Todo");
		panel.add(chckbxTodo);
		
		JCheckBox chckbxBsico = new JCheckBox("B\u00E1sico");
		panel.add(chckbxBsico);
		
		JCheckBox chckbxID = new JCheckBox("ID");
		panel.add(chckbxID);
		
		JCheckBox chckbxNombre = new JCheckBox("Nombres");
		panel.add(chckbxNombre);
		
		JCheckBox chckbxApellido = new JCheckBox("Apellidos");
		panel.add(chckbxApellido);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Direcci\u00F3n");
		panel.add(chckbxNewCheckBox);
		
		JCheckBox chckbxCorreo = new JCheckBox("Correo");
		panel.add(chckbxCorreo);
		
		JCheckBox chckbxFechaNacimiento = new JCheckBox("Fecha Nacimiento");
		panel.add(chckbxFechaNacimiento);
		
		JCheckBox chckbxGnero = new JCheckBox("G\u00E9nero");
		panel.add(chckbxGnero);
		
		JCheckBox chckbxCrdito = new JCheckBox("Cr\u00E9dito");
		panel.add(chckbxCrdito);
		
		JCheckBox chckbxCuenta = new JCheckBox("Cuenta");
		panel.add(chckbxCuenta);
		
		JCheckBox chckbxCategora = new JCheckBox("Categor\u00EDa");
		panel.add(chckbxCategora);
		
		JCheckBox chckbxEstado = new JCheckBox("Estado");
		panel.add(chckbxEstado);
		
		JCheckBox chckbxDepartamento = new JCheckBox("Departamento");
		panel.add(chckbxDepartamento);
		
		JCheckBox chckbxTelfono = new JCheckBox("Tel\u00E9fono");
		panel.add(chckbxTelfono);
		
		table = new JTable(data, columnNames);		
		
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);		
		panelTabla.add(scrollPane, BorderLayout.CENTER);
	}
	
	private void onClose(){
		System.out.println("Saliendo del programa...");
		if (conexion != null){
			System.out.println("Cerrando la base de datos...");
			conexion.closeDatabase();
		}
  		this.dispose();
  		System.exit(0);		
	}
	
	public void createFormShow(String tableName){

		// Getting first row from table.
		String query = "SELECT * FROM " + tableName + " WHERE id" + objectToShow + " = 0;" ;
		JSONArray jsonFirstRow = conexion.executeQuery(query);
				
		ArrayList<String> formLabels = conexion.getTableColumns(tableName);
		int numPairs = formLabels.size();
		//Create and populate the panel.
		panelGCForm = new JPanel(new SpringLayout());
		listFormFields = new HashMap<String, JTextField>();
		System.out.println("numPairsSize construt: " + numPairs);
		for (int i = 0; i < numPairs; i++) {
		    JLabel label = new JLabel(formLabels.get(i) + " : ", JLabel.TRAILING);		    
		    panelGCForm.add(label);
		    String hola = "null";
		    try {
				hola = jsonFirstRow.getJSONObject(0).getString(formLabels.get(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    System.out.println("hola: " + hola);
		    JTextField textField = new JTextField(hola);
		    label.setLabelFor(textField);		    
		    panelGCForm.add(textField);
		    listFormFields.put("textField" + formLabels.get(i), textField);
		}
		System.out.println("listFormFieldsSize construt: " + listFormFields.size());
		
		//Lay out the panel.
		SpringUtilities.makeCompactGrid(panelGCForm,
		                                numPairs, 2, //rows, cols
		                                6, 6,        //initX, initY
		                                6, 6);       //xPad, yPad		
		
		
		
		
	} 
}