<<<<<<< HEAD
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
import org.json.JSONObject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ListSelectionModel;

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
	private ArrayList<String> tableColumns;
	private ControladorClientes controlClientes;
	private JComboBox comboBoxMostrar;		// Selección de la tabla a mostrar.

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
		
		// Conexión con postgress...
		conexion = ConexionPostgres.getInstancia();
		
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
		comboBoxMostrar = new JComboBox();
		comboBoxMostrar.setModel(new DefaultComboBoxModel(new String[] {"Cliente", "Categoria", "Estado", "Pais", "Departamento", "Telefono"}));
		panelGCBuscar.add(comboBoxMostrar);
		
		JLabel lblId = new JLabel("con ID : ");
		panelGCBuscar.add(lblId);
		
		textField_1 = new JTextField();
		panelGCBuscar.add(textField_1);
		textField_1.setColumns(10);
		
		
		JButton btnBuscar = new JButton("Buscar"); 	
		panelGCBuscar.add(btnBuscar);		
		panelGestionarCliente.add(panelGCBuscar, BorderLayout.NORTH);
		
		
		
		// ********** Obteción de las columnas de la tabla por default **********		
    	String tableName = objectToShow;
    	if (tableName.equals("cliente"))
    		tableName = "clientes";
    	// Getting columns ot the table...
		tableColumns = conexion.getTableColumns(tableName);		
		
		scrollPaneGCForm = new JScrollPane(panelGCForm);
		panelGestionarCliente.add(scrollPaneGCForm, BorderLayout.CENTER);
		panelGCGestionar.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panelGCGestionarNavegar = new JPanel();
		panelGCGestionar.add(panelGCGestionarNavegar);
		
		JButton buttonFirst = new JButton("<<");
		buttonFirst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JSONObject jsonRow;
				try {
					jsonRow = controlClientes.getFirst();
					if (jsonRow == null){
						JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nobtener el elemento de la tabla..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);							
						return;
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nobtener el elemento de la tabla..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);							
					return;
				}
				
				// Si no no hubo error, actualizar el formulario.
				updateForm(jsonRow);				
			}
		});
		panelGCGestionarNavegar.add(buttonFirst);
		
		JButton buttonPrevious = new JButton("<");
		buttonPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int indice = controlClientes.getPosActual() - 1;				
				JSONObject jsonRow;
				try {
					jsonRow = controlClientes.getElement(indice);
					if (jsonRow == null){
						JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nobtener el elemento de la tabla..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);							
						return;
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nobtener el elemento de la tabla..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);							
					return;
				}
				
				// Si no no hubo error, actualizar el formulario.
				updateForm(jsonRow);
			}
		});
		panelGCGestionarNavegar.add(buttonPrevious);
		
		JButton buttonNext = new JButton(">");
		buttonNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int indice = controlClientes.getPosActual() + 1;
				JSONObject jsonRow;
				try {
					jsonRow = controlClientes.getElement(indice);
					if (jsonRow == null){
						JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nobtener el elemento de la tabla..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);							
						return;
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nobtener el elemento de la tabla..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);							
					return;
				}
				
				// Si no no hubo error, actualizar el formulario.
				updateForm(jsonRow);
			}
		});
		panelGCGestionarNavegar.add(buttonNext);
		
		JButton buttonLast = new JButton(">>");
		buttonLast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JSONObject jsonRow;
				try {
					jsonRow = controlClientes.getLast();
					if (jsonRow == null){
						JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nobtener el elemento de la tabla..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);							
						return;
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nobtener el elemento de la tabla..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);							
					return;
				}
				
				// Si no no hubo error, actualizar el formulario.
				updateForm(jsonRow);					
			}
		});
		panelGCGestionarNavegar.add(buttonLast);
		
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
		
		JPanel panelFiltros = new JPanel();
		panelTabla.add(panelFiltros, BorderLayout.NORTH);
		panelFiltros.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panelFiltros.add(panel_2, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("Filtrar Clientes : ");
		panel_2.add(lblNewLabel);
		
		JLabel lblId_1 = new JLabel("ID");
		panel_2.add(lblId_1);
		
		JComboBox comboBoxRelOp = new JComboBox();
		comboBoxRelOp.setModel(new DefaultComboBoxModel(new String[] {"default", "<", "<=", ">=", ">"}));
		panel_2.add(comboBoxRelOp);
		
		textFieldFiltrarID = new JTextField();
		textFieldFiltrarID.setColumns(10);
		panel_2.add(textFieldFiltrarID);
		
		JLabel labelComa1 = new JLabel(",");
		panel_2.add(labelComa1);
		
		
		ArrayList<String> tableColumns2 = new ArrayList<String>();
		tableColumns2.add("default");
		tableColumns2.addAll(tableColumns);
		
		JComboBox comboBoxLike = new JComboBox();
		Object[] model = tableColumns2.toArray();
		comboBoxLike.setModel(new DefaultComboBoxModel(model));		
		panel_2.add(comboBoxLike);
		
		JLabel lblLike = new JLabel("parecido a : ");
		panel_2.add(lblLike);
		
		textFieldLike = new JTextField();
		panel_2.add(textFieldLike);
		textFieldLike.setColumns(10);
		
		JLabel lblOrden = new JLabel(", orden : ");
		panel_2.add(lblOrden);
		
		JComboBox comboBoxOrderBy = new JComboBox();		
		comboBoxOrderBy.setModel(new DefaultComboBoxModel(model));
		panel_2.add(comboBoxOrderBy);
		
		JComboBox comboBoxOrder = new JComboBox();
		comboBoxOrder.setModel(new DefaultComboBoxModel(new String[] {"asc", "desc"}));
		panel_2.add(comboBoxOrder);
		
		
		// ********** COMBO BOX PARA SELECCIONAR TODO LOS CAMPOS ********** 
		JCheckBox chckbxSelectAll = new JCheckBox("Todo");
		chckbxSelectAll.setSelected(true);
		chckbxSelectAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (chckbxSelectAll.isSelected()){
					for (int i=0; i<listFieldsToShow.size(); i++)
						listFieldsToShow.get("checkBox" + tableColumns.get(i)).setSelected(true);
				}
				else{
					for (int i=0; i<listFieldsToShow.size(); i++)
						listFieldsToShow.get("checkBox" + tableColumns.get(i)).setSelected(false);
					listFieldsToShow.get("checkBoxidcliente").setSelected(true);
					listFieldsToShow.get("checkBoxnombres").setSelected(true);
					listFieldsToShow.get("checkBoxapellidos").setSelected(true);
					listFieldsToShow.get("checkBoxdireccion").setSelected(true);
					listFieldsToShow.get("checkBoxcorreo").setSelected(true);
				}
			}
		});

		
		// Botón de búsqueda con filtros...
		JButton btnBuscar_1 = new JButton("Buscar");
		btnBuscar_1.setPreferredSize(new Dimension(100, 23));		
		panel_2.add(btnBuscar_1);
		
		JPanel panelMostrarCampos = new JPanel();
		panelFiltros.add(panelMostrarCampos, BorderLayout.CENTER);
		panelMostrarCampos.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));				
		panelMostrarCampos.add(chckbxSelectAll);
		
		// AGREGACION DE LOS CHECK BOX PARA SELECCIONAR CAMPOS A MOSTRAR...
		listFieldsToShow = new HashMap<String, JCheckBox>();
		for (int i=0; i<tableColumns.size(); i++){			
			JCheckBox chckbxField = new JCheckBox(tableColumns.get(i));
			chckbxField.setSelected(true);
			panelMostrarCampos.add(chckbxField);
			listFieldsToShow.put("checkBox" + tableColumns.get(i), chckbxField);
		}
		
		
		ArrayList<String> tableColumnsToShow = new ArrayList<String>();		
		for (int i=0; i<listFieldsToShow.size(); i++){
			if (listFieldsToShow.get("checkBox" + tableColumns.get(i)).isSelected()){
				tableColumnsToShow.add(tableColumns.get(i));
			}			
		}
					
		
		
		// ********* TABLA DE DATOS ********* 
		
		controlClientes = ControladorClientes.getInstancia();
		String query = "SELECT * FROM clientes;";
		DefaultTableModel dataModel = controlClientes.getDataClientes(tableColumnsToShow, query);		
		table = new JTable(dataModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
						
		// ********* SCROLL PANE PARA LA TABLA *********
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		panelTabla.add(scrollPane, BorderLayout.CENTER);
		
		
		
		// ********** CONSTRUCCION DEL FORMULARIO POR DEFAULT **********	
		JSONObject firstObject = null;
		try {
			firstObject = controlClientes.getFirst();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    	
		// Creating form for the first row of the table.
    	createFormShow(tableName, firstObject);
		scrollPaneGCForm = new JScrollPane(panelGCForm);
		panelGestionarCliente.add(scrollPaneGCForm, BorderLayout.CENTER);
		panelGestionarCliente.add(panelGCGestionar, BorderLayout.SOUTH);
		panelGestionarCliente.updateUI();
		
		
		
		
		JPanel panelOtrasGestiones = new JPanel();
		panelTabla.add(panelOtrasGestiones, BorderLayout.SOUTH);
		
		
		
		
		/**
		 * ***************** CONDIGO DE LOS EVENTOS PRINCIPALES *****************
		 */ 
		
		
		// ***************** EVENTO DEL BOTON DE BUSQUEDA DEL FORMULARIO *****************
		
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

				// Get item selected from combo box.
				objectToShow = comboBoxMostrar.getSelectedItem().toString().toLowerCase();
		    	String tableName = objectToShow;
		    	if (tableName.equals("cliente"))
		    		tableName = "clientes";		    	

				// Getting properties for this id.
				String query = "SELECT * FROM " + tableName + " WHERE id" + objectToShow + " = " + id + ";" ;
				JSONArray jsonRow = conexion.executeQuery(query);
				if (jsonRow.length() == 0){
					JOptionPane.showMessageDialog(null, "No se encontró ningún resultado\ncon el ID especificado..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);
					textField_1.selectAll();
					textField_1.requestFocusInWindow();
					return;
				}
				
				// Update form
		    	ArrayList<String> formLabels = conexion.getTableColumns(tableName);
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
		
		
		
		// ********** EVENTO DEL COMBO BOX AL SELECCIONAR UNA TABLA ********** 
		
		comboBoxMostrar.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    					
		    	objectToShow = comboBoxMostrar.getSelectedItem().toString().toLowerCase();
		    	panelGestionarCliente.removeAll();		    					
		    	panelGestionarCliente.add(panelGCBuscar, BorderLayout.NORTH);
		    	String tableName = objectToShow;
		    	if (tableName.equals("cliente"))
		    		tableName = "clientes";

		    	// ************** CONSTRUCCION DE LA TABLA **************
		    	// Getting columns ot the table...
				tableColumns = conexion.getTableColumns(tableName);
				// Getting the model for the table
				String query = "SELECT * FROM " + tableName + ";";				
				DefaultTableModel dataModel = controlClientes.getDataClientes(tableColumns, query);				
				table.setModel(dataModel);
				table.updateUI();
				
				// Cuando se seleccione la tabla clientes, se actualiza los checkboxes de seleccion.
				if (tableName.equals("clientes")){
					chckbxSelectAll.setSelected(true);
					for (int i=0; i<listFieldsToShow.size(); i++)
						listFieldsToShow.get("checkBox" + tableColumns.get(i)).setSelected(true);
				}
				
		    	
				// Getting first row of the table.
				JSONObject firstObject = null;
				try {
					firstObject = controlClientes.getFirst();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    	
				// Creating form for the first row of the table.
		    	createFormShow(tableName, firstObject);
				scrollPaneGCForm = new JScrollPane(panelGCForm);
				panelGestionarCliente.add(scrollPaneGCForm, BorderLayout.CENTER);
				panelGestionarCliente.add(panelGCGestionar, BorderLayout.SOUTH);
				panelGestionarCliente.updateUI();
								
		    }
		});

		
		
		// ***************** EVENTO DEL BOTON CON CONSULTA CON FILTROS *****************		
		
		btnBuscar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// La búsqueda con filtros sólo funciona sobre la tabla clientes..!
				if (!objectToShow.equals("cliente")){
					JOptionPane.showMessageDialog(null, "La búsqueda con filtros sólo\nfunciona sobre la tabla clientes..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);					
					comboBoxMostrar.requestFocusInWindow();
					return;
				}
				ArrayList<String> tableColumnsToShow = new ArrayList<String>();				
				// Consult construction.
				String query = "SELECT ";
				// Columns for select...				
				if (!chckbxSelectAll.isSelected()){									
					for (int i=0; i<listFieldsToShow.size(); i++)
						if (listFieldsToShow.get("checkBox" + tableColumns.get(i)).isSelected())							
							tableColumnsToShow.add(tableColumns.get(i));
				}
				else{
					tableColumnsToShow.addAll(tableColumns);					
				}
				
				/**
				 * En la query se le dice que seleccione todo porque el metodo que crea el modelo
				 * para la tabla filtra las columnas en base a la variable tableColumnsToShow.
				 */
				query += " * FROM clientes ";			
				
				
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
					
				// LIKE filter
				if (!comboBoxLike.getSelectedItem().equals("default")){
					if (!whereExist)
						query += " WHERE " + comboBoxLike.getSelectedItem().toString() + " ILIKE '%" + textFieldLike.getText() + "%'";
					else
						query += " AND " + comboBoxLike.getSelectedItem().toString() + " ILIKE '%" + textFieldLike.getText() + "%'";
				}
				
				// Order By Filter
				if (!comboBoxOrderBy.getSelectedItem().equals("default")){					
					query += " ORDER BY " + comboBoxOrderBy.getSelectedItem() + " " + comboBoxOrder.getSelectedItem();
				}
				
				query += ";";
				System.out.println(query);

				// Getting the model for the table
				DefaultTableModel dataModel = controlClientes.getDataClientes(tableColumnsToShow, query);				
				table.setModel(dataModel);
				table.updateUI();
			}
		});
		
		
		
		// ***************** EVENTO DE SELECCION DE UNA FILA DE LA TABLA *****************
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int indice = table.getSelectedRow();
				JSONObject jsonRow;
				try {
					jsonRow = controlClientes.getElement(indice);
					if (jsonRow == null){
						JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nobtener el elemento de la tabla..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);							
						return;
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nobtener el elemento de la tabla..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);							
					return;
				}
				
				// Si no no hubo error, actualizar el formulario.
				updateForm(jsonRow);
			}
		});
		
		
	}
	
	
	
	
	/**
	 * Método que se ejecuta cuando se cierra el programa. Sirve para cerrar la conexión.
	 */
	private void onClose(){
		System.out.println("Saliendo del programa...");
		if (conexion != null){
			System.out.println("Cerrando la base de datos...");
			conexion.closeDatabase();
		}
  		this.dispose();
  		System.exit(0);		
	}
	
	
	/**
	 * Crea el formulario para mostrar un objecto que podrá ser gestionado.
	 * @param tableName
	 * @param object
	 */
	public void createFormShow(String tableName, JSONObject object){		
		ArrayList<String> formLabels = conexion.getTableColumns(tableName);
		int numPairs = formLabels.size();
		//Create and populate the panel.
		panelGCForm = new JPanel(new SpringLayout());
		listFormFields = new HashMap<String, JTextField>();
		for (int i = 0; i < numPairs; i++) {
		    JLabel label = new JLabel(formLabels.get(i) + " : ", JLabel.TRAILING);		    
		    panelGCForm.add(label);
		    String rowLabel = "null";
		    try {
				rowLabel = object.getString(formLabels.get(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nllenar el formulario..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);				
				return;
			}
		    JTextField textField = new JTextField(rowLabel);
		    textField.setEditable(false);
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
	
	
	/**
	 * Método que actualiza el formulario.
	 * @param itemToShow
	 */
	private void updateForm(JSONObject jsonRow){
    	objectToShow = comboBoxMostrar.getSelectedItem().toString().toLowerCase();
    	String tableName = objectToShow;
    	if (tableName.equals("cliente"))
    		tableName = "clientes";
    	
    	// Getting columns ot the table...
		tableColumns = conexion.getTableColumns(tableName);

		// Update form
		ArrayList<String> formLabels = conexion.getTableColumns(tableName);
		for (int i=0; i < formLabels.size(); i++){
			String propertie = "null";
			try {
				propertie = jsonRow.getString(formLabels.get(i));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nllenar el formulario..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);							
				return;
			}
			listFormFields.get("textField" + formLabels.get(i)).setText(propertie);
		}
		table.setRowSelectionInterval(controlClientes.getPosActual(), controlClientes.getPosActual());

	}
}
