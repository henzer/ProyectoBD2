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
import modulo1.controladores.ControladorClientes;

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
import javax.swing.table.DefaultTableModel;
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
import java.awt.Insets;

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
	private Map<String, JCheckBox> listFieldsToShow;
	private JTextField textFieldFiltrarID;
	private JTextField textFieldLike;

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
		
		
		JPanel panelGestion = new JPanel();
		contentPane.add(panelGestion, BorderLayout.WEST);
		panelGestion.setLayout(new BorderLayout(0, 0));
		
		
		panelGestionarCliente = new JPanel();
		panelGestionarCliente.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelGestionarCliente.setPreferredSize(new Dimension(400, 10));
		panelGestion.add(panelGestionarCliente, BorderLayout.WEST);
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
					JOptionPane.showMessageDialog(null, "ID inválido..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);
					textField_1.selectAll();
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
		
		JPanel panelTabla = new JPanel();
		panelTabla.setPreferredSize(new Dimension(10, 300));
		contentPane.add(panelTabla, BorderLayout.CENTER);
		panelTabla.setLayout(new BorderLayout(0, 0));
		

		// ********************** VISTA FILTROS ********************** 
		
		// Getting columns clientes relation...
		ArrayList<String> tableColumns = conexion.getTableColumns("clientes");
		
		JPanel panelFiltros = new JPanel();
		panelTabla.add(panelFiltros, BorderLayout.NORTH);
		panelFiltros.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panelFiltros.add(panel_2, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("Filtrar : ");
		panel_2.add(lblNewLabel);
		
		JLabel lblId_1 = new JLabel("ID");
		panel_2.add(lblId_1);
		
		JComboBox comboBoxRelOp = new JComboBox();
		comboBoxRelOp.setModel(new DefaultComboBoxModel(new String[] {"default", "<", "<=", ">=", ">"}));
		panel_2.add(comboBoxRelOp);
		
		textFieldFiltrarID = new JTextField();
		panel_2.add(textFieldFiltrarID);
		textFieldFiltrarID.setColumns(10);
		
		JLabel labelComa1 = new JLabel(",");
		panel_2.add(labelComa1);
		
		JComboBox comboBoxLike = new JComboBox();
		comboBoxLike.setModel(new DefaultComboBoxModel(new String[] {"default"}));
		panel_2.add(comboBoxLike);
		
		JLabel lblLike = new JLabel("parecido a : ");
		panel_2.add(lblLike);
		
		textFieldLike = new JTextField();
		panel_2.add(textFieldLike);
		textFieldLike.setColumns(10);
		
		JLabel labelComa2 = new JLabel(", agrupado por : ");
		panel_2.add(labelComa2);
		
		JComboBox comboBoxGroupBy = new JComboBox();
		comboBoxGroupBy.setModel(new DefaultComboBoxModel(new String[] {"default"}));
		
		panel_2.add(comboBoxGroupBy);
		
		JLabel lblOrden = new JLabel(", orden : ");
		panel_2.add(lblOrden);
		
		JComboBox comboBoxOrderBy = new JComboBox();
		comboBoxOrderBy.setModel(new DefaultComboBoxModel(new String[] {"default", "ascendente", "descendente"}));
		panel_2.add(comboBoxOrderBy);
		
		JLabel label = new JLabel("          -->         ");
		panel_2.add(label);
		
		JCheckBox chckbxSelectAll = new JCheckBox("Todo");
		
		ArrayList<String> tableColumnsToShow = new ArrayList<String>();
		
		
		// ***************** CONSTRUCCION DE LA QUERY CON FILTROS *****************
		JButton btnBuscar_1 = new JButton("Buscar");
		btnBuscar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Consult construction.
				String query = "SELECT ";
				// Columns for select...				
				if (!chckbxSelectAll.isSelected()){									
					for (int i=0; i<listFieldsToShow.size(); i++){
						if (listFieldsToShow.get("checkBox" + tableColumns.get(i)).isSelected())
							query += tableColumns.get(i) + ", ";					
					}				
					query = query.substring(0, query.length()-2);		// Delete the comma at the end of the line.
				}
				else{
					query += " *";
				}
				
				query += " FROM clientes ";
				
				// ID Filter
				boolean whereExist = false;
				if (!comboBoxRelOp.getSelectedItem().equals("default")){
					// Validación de campo ID.
					try{
						int idFilter = Integer.parseInt(textFieldFiltrarID.getText());
					}
					catch (Exception error){
						JOptionPane.showMessageDialog(null, "ID inválido..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);
						textFieldFiltrarID.selectAll();
						textFieldFiltrarID.requestFocusInWindow();
						return;
					}
					
					// ID condition					 
					if (!comboBoxRelOp.getSelectedItem().toString().equals("default")){
						whereExist = true;
						query += " WHERE idcliente " + comboBoxRelOp.getSelectedItem().toString() + " " + textFieldFiltrarID.getText();
					}					
				}
					
				// SOME filter
				if (!comboBoxLike.getSelectedItem().equals("default")){
					// validation
					if (!tableColumns.contains(textFieldLike.getText())){
						JOptionPane.showMessageDialog(null, "Campo no existe en la relación Cliente..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);
						textFieldLike.selectAll();
						textFieldLike.requestFocusInWindow();
						return;
					}
					if (!whereExist)
						query += " WHERE " + comboBoxLike.getSelectedItem().toString() + " LIKE " + textFieldLike.getText();
					else
						query += " AND " + comboBoxLike.getSelectedItem().toString() + " LIKE " + textFieldLike.getText();
				}
				
				// Group By Filter
				if (!comboBoxGroupBy.getSelectedItem().equals("default")){
					query += " GROUP BY " + comboBoxGroupBy.getSelectedItem().toString();
				}
				
				// Order By Filter
				if (!comboBoxOrderBy.getSelectedItem().equals("default")){
					String filter = (comboBoxOrderBy.getSelectedItem().equals("ascendente"))? "ASC": "DSC";
					query += " ORDER BY " + filter;
				}
				
				query += ";";
				System.out.println(query);
				
				
			}
		});
		panel_2.add(btnBuscar_1);
		
		JPanel panelMostrarCampos = new JPanel();
		panelFiltros.add(panelMostrarCampos, BorderLayout.CENTER);
		panelMostrarCampos.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				
		panelMostrarCampos.add(chckbxSelectAll);
		
		// Check boxes for columns to show.
		listFieldsToShow = new HashMap<String, JCheckBox>();
		for (int i=0; i<tableColumns.size(); i++){			
			JCheckBox chckbxField = new JCheckBox(tableColumns.get(i));
			if (i < 4)
				chckbxField.setSelected(true);
			else
				chckbxField.setSelected(false);
			panelMostrarCampos.add(chckbxField);
			listFieldsToShow.put("checkBox" + tableColumns.get(i), chckbxField);
		}
		
		
		for (int i=0; i<listFieldsToShow.size(); i++){
			if (listFieldsToShow.get("checkBox" + tableColumns.get(i)).isSelected()){
				tableColumnsToShow.add(tableColumns.get(i));
			}			
		}
		
		ControladorClientes controlClientes = new ControladorClientes();
		DefaultTableModel model = controlClientes.getDataClientes(tableColumnsToShow);
		
		String query = "SELECT * FROM clientes;"; 
		JSONArray datos = conexion.executeQuery(query);		
		table = new JTable(model);
		
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
		    JTextField textField = new JTextField(hola);
		    label.setLabelFor(textField);		    
		    panelGCForm.add(textField);
		    listFormFields.put("textField" + formLabels.get(i), textField);
		}
		
		//Lay out the panel.
		SpringUtilities.makeCompactGrid(panelGCForm,
		                                numPairs, 2, //rows, cols
		                                6, 6,        //initX, initY
		                                6, 6);       //xPad, yPad							
		
	} 
}