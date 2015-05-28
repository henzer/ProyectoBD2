package modulo1.vistas;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.BoxLayout;

import modulo1.conexion.ConexionPostgres;
import modulo1.controladores.ControladorClientes;
import modulo2.vistas.Ventana;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Dimension;

import javax.swing.JCheckBox;

import java.awt.GridLayout;

import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.SpringLayout;

import java.awt.Component;
import java.nio.channels.FileChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
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
import org.omg.CORBA.CTX_RESTRICT_SCOPE;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ListSelectionModel;

public class GestionClientes extends JFrame
{
	ConexionPostgres conexion = null;
	private JPanel contentPane;
	private JTable table;
	private JTextField textField_1;	
	private JPanel panelGCForm;
	private JScrollPane scrollPaneGCForm;
	private JPanel panelGestionarCliente;
	private Map<String, JTextField> listFormFields;
	private Map<String, JCheckBox> listFieldsToShow;
	private ArrayList<JButton> actionButtons;		// Almacena los botones que son solo funcionales para la tabla clientes.
	private JTextField textFieldFiltrarID;
	private JTextField textFieldLike;
	private ArrayList<String> tableColumns;
	private ControladorClientes controlClientes;
	private JComboBox comboBoxMostrar;		// Selección de la tabla a mostrar.
	private JPanel panelUserImage;
	private JPanel panelFormObject;
	private JLabel labelPhotoImage;
	private String currentTable = "cliente";
	private File copia;
	private File path;
	private ImageIcon photo;
	
	
	private JTextField currentEditing;
	private String currentAction = "";
	
	private BufferedImage currentImage;
	private String nameCurrentImage = "default_user.png";

	
	/**
	 * Create the frame.
	 */
	public GestionClientes() {
		
		actionButtons = new ArrayList<JButton>();
		
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
		addWindowListener(new WindowAdapter() 
		{
		     public void windowClosing(WindowEvent ev) 
		     {
	        	int resp = JOptionPane.showConfirmDialog(null, "¿Salir del Programa?", "Alerta!", JOptionPane.YES_NO_OPTION);
	        	// Si se sale del programa, desconecta la base de datos.
	    		if (resp == JOptionPane.OK_OPTION)
	    		{	    			
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
    	// Getting columns ot the table...
		tableColumns = conexion.getTableColumns(currentTable);
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
		chckbxSelectAll.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (chckbxSelectAll.isSelected())
				{
					for (int i=0; i<listFieldsToShow.size(); i++)
						listFieldsToShow.get("checkBox" + tableColumns.get(i)).setSelected(true);
				}
				else
				{
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
		for (int i=0; i<tableColumns.size(); i++)
		{			
			JCheckBox chckbxField = new JCheckBox(tableColumns.get(i));
			chckbxField.setSelected(true);
			panelMostrarCampos.add(chckbxField);
			listFieldsToShow.put("checkBox" + tableColumns.get(i), chckbxField);
		}
		
		
		ArrayList<String> tableColumnsToShow = new ArrayList<String>();		
		for (int i=0; i<listFieldsToShow.size(); i++) {
			if (listFieldsToShow.get("checkBox" + tableColumns.get(i)).isSelected()){
				tableColumnsToShow.add(tableColumns.get(i));
			}			
		}
					
		
		// ********* TABLA DE DATOS ********* 
		controlClientes = ControladorClientes.getInstancia();
		String query = "SELECT * FROM cliente;";
		DefaultTableModel dataModel = controlClientes.getDataClientes(tableColumnsToShow, query);		
		table = new JTable(dataModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
						
		// ********* SCROLL PANE PARA LA TABLA *********
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		panelTabla.add(scrollPane, BorderLayout.CENTER);
		
		// ********** CONSTRUCCION DEL FORMULARIO POR DEFAULT **********
		panelFormObject = new JPanel();
		panelGestionarCliente.add(panelFormObject, BorderLayout.CENTER);
		panelFormObject.setLayout(new BorderLayout(0, 0));		
		
		// ************** USER PHOTO **************
		panelUserImage = new JPanel();
		labelPhotoImage = new JLabel();
		panelFormObject.add(panelUserImage, BorderLayout.NORTH);
		panelUserImage.setLayout(new GridLayout(0, 2, 0, 0));		
		JButton btnNewButton = new JButton("Seleccionar Foto");
		
		btnNewButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
//				path = AbrirFoto();
//				try 
//				{
//					copyFile(new File(path.getPath()), new File("C:/Users/fred__000/Desktop/Copy 1 of ProyectoBD2/user_photos/"+path.getName()));
//				} 
//				catch (IOException e) 
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				photo=new ImageIcon();
//				photo=getPhotoResized(path.getName());
//				labelPhotoImage.setPreferredSize(new Dimension(100, 100));
//				labelPhotoImage.setIcon(photo);
				int idActual;
				try {
					idActual = ((JSONObject)controlClientes.getElement(controlClientes.getPosActual())).getInt("idcliente");
					cargarImagen(""+idActual);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		panelUserImage.add(btnNewButton);
		
		
		panelUserImage.add(labelPhotoImage);
		
		
		// Getting first row of table 'Cliente'.
		JSONObject firstObject = null;
		try
		{
			firstObject = controlClientes.getFirst();
		} 
		catch (Exception e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
		
		// Creating form for the first row of the table.
    	createFormShow(firstObject);		    	
    	
		scrollPaneGCForm = new JScrollPane(panelGCForm);
		panelFormObject.add(scrollPaneGCForm);
		//Create and populate the panel.
		panelGCForm = new JPanel(new SpringLayout());
		JPanel panelGCGestionar = new JPanel();
		panelGCGestionar.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panelGCGestionarNavegar = new JPanel();
		panelGCGestionar.add(panelGCGestionarNavegar);
		
		
		// ************** NAVEGATION BUTTONS IN FORM ************** 
		// ************** Button 'GO FIRST' **************
		JButton buttonFirst = new JButton(new ImageIcon("system_images/first.png"));
		buttonFirst.setToolTipText("Primero");
		buttonFirst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!currentAction.equals("")){
					JOptionPane.showMessageDialog(null, "Hay una accion sin completar.", "CRM Clientes", JOptionPane.ERROR_MESSAGE);					
					return;
				}
				
				JSONObject jsonRow;
				try {
					jsonRow = controlClientes.getFirst();
					if (jsonRow == null) {
						JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nobtener el elemento de la tabla..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);							
						return;
					}
				} 
				catch (Exception e1) 
				{
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
		
		
		// ************** Button 'GO PREVIOUS' **************
		JButton buttonPrevious = new JButton(new ImageIcon("system_images/previous.png"));
		buttonPrevious.setToolTipText("Anterior");
		buttonPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!currentAction.equals("")){
					JOptionPane.showMessageDialog(null, "Hay una accion sin completar.", "CRM Clientes", JOptionPane.ERROR_MESSAGE);					
					return;
				}
				
				int indice = controlClientes.getPosActual() - 1;				
				JSONObject jsonRow;
				try 
				{
					jsonRow = controlClientes.getElement(indice);
					if (jsonRow == null)
					{
						JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nobtener el elemento de la tabla..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);							
						return;
					}
				} 
				catch (Exception e1)
				{
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
		
		
		// ************** Button 'GO NEXT' **************
		JButton buttonNext = new JButton(new ImageIcon("system_images/next.png"));
		buttonNext.setToolTipText("Siguiente");
		buttonNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!currentAction.equals("")){
					JOptionPane.showMessageDialog(null, "Hay una accion sin completar.", "CRM Clientes", JOptionPane.ERROR_MESSAGE);					
					return;
				}
				
				int indice = controlClientes.getPosActual() + 1;
				JSONObject jsonRow;
				try 
				{
					jsonRow = controlClientes.getElement(indice);
					if (jsonRow == null)
					{
						JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nobtener el elemento de la tabla..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);							
						return;
					}
				} 
				catch (Exception e1)
				{
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
		
		
		// ************** Button 'GO LAST' **************
		JButton buttonLast = new JButton(new ImageIcon("system_images/last.png"));
		buttonLast.setToolTipText("Ultimo");
		buttonLast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!currentAction.equals("")){
					JOptionPane.showMessageDialog(null, "Hay una accion sin completar.", "CRM Clientes", JOptionPane.ERROR_MESSAGE);					
					return;
				}
				
				JSONObject jsonRow;
				try 
				{
					jsonRow = controlClientes.getLast();
					if (jsonRow == null)
					{
						JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nobtener el elemento de la tabla..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);							
						return;
					}
				} 
				catch (Exception e1) 
				{
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

		//ImageIcon imageNewUser = new ImageIcon("system_images/add_user.png");
		JButton btnNuevo = new JButton(new ImageIcon("system_images/add_user.png"));
		actionButtons.add(btnNuevo);	// Ver declaración de 'actionButons'.
		btnNuevo.setToolTipText("Agregar nuevo cliente");
		btnNuevo.setPreferredSize(new Dimension(60,50));
		
		/**
		 * Evento del botón 'Nuevo Cliente': Hace que los campos sean editables, los vacía y 
		 * agrega los listeners a aquellos que requieren de una ventana emergente
		 * para realizar el input.
		 */
		btnNuevo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!currentAction.equals("")){
					JOptionPane.showMessageDialog(null, "Hay una accion sin completar.", "CRM Clientes", JOptionPane.ERROR_MESSAGE);					
					return;
				}
				
				currentAction = "addingNewUser";
				ArrayList<String> fields = conexion.getTableColumns(currentTable);				
				fields.remove("foto");
				for (int i=0; i<fields.size(); i++){
					listFormFields.get("textField" + fields.get(i)).setEditable(true);					
					listFormFields.get("textField" + fields.get(i)).setText("");
					if (fields.get(i).equals("idcategoria") || fields.get(i).equals("iddepartamento") || fields.get(i).equals("idestado") || fields.get(i).equals("idtelefono")){
						listFormFields.get("textField" + fields.get(i)).addMouseListener(textFieldsListener);
					}
				}
				listFormFields.get("textField" + fields.get(0)).requestFocus();
			}
		});

		panelGCGestionarGestionar.add(btnNuevo);		
		
		JButton btnEditar = new JButton(new ImageIcon("system_images/edit_user.png"));
		actionButtons.add(btnEditar);	// Ver declaración de 'actionButons'.
		btnEditar.setToolTipText("Editar cliente");
		btnEditar.setPreferredSize(new Dimension(60,50));
		
		/**
		 * Evento del botón 'Editar Cliente': Hace que los campos sean editables y
		 * agrega los listeners a aquellos que requieren de una ventana emergente
		 * para realizar el input.
		 */
		btnEditar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!currentAction.equals("")){
					JOptionPane.showMessageDialog(null, "Hay una accion sin completar.", "CRM Clientes", JOptionPane.ERROR_MESSAGE);					
					return;
				}
				
				currentAction = "updatingUser";
				ArrayList<String> fields = conexion.getTableColumns(currentTable);				
				fields.remove("foto");
				for (int i=0; i<fields.size(); i++){
					listFormFields.get("textField" + fields.get(i)).setEditable(true);					
					if (fields.get(i).equals("idcategoria") || fields.get(i).equals("iddepartamento") || fields.get(i).equals("idestado") || fields.get(i).equals("idtelefono")){
						listFormFields.get("textField" + fields.get(i)).addMouseListener(textFieldsListener);
					}
				}
				listFormFields.get("textField" + fields.get(0)).requestFocus();
				
			}
		});
		
		panelGCGestionarGestionar.add(btnEditar);
		
		
		
		JButton btnGuardar = new JButton(new ImageIcon("system_images/store_user.png"));
		actionButtons.add(btnGuardar);	// Ver declaración de 'actionButons'.
		btnGuardar.setToolTipText("Guardar cliente");
		btnGuardar.setPreferredSize(new Dimension(60,50));
		btnGuardar.addActionListener(updateClienteListener);	// Ver definición de 'updateClienteListener'.
		panelGCGestionarGestionar.add(btnGuardar);		
		
		
		JButton btnEliminar = new JButton(new ImageIcon("system_images/delete_user.png"));
		actionButtons.add(btnEliminar);	// Ver declaración de 'actionButons'.
		btnEliminar.setToolTipText("Eliminar cliente");
		btnEliminar.setPreferredSize(new Dimension(60,50));
		btnEliminar.addActionListener(eliminarClienteListener);		// Ver definición de 'eliminarClienteListener'.
		panelGCGestionarGestionar.add(btnEliminar);
		
		
		
		

		
		// ****************** FALTAN POR PROGRAMAR ******************
		// Botones NuevoCampo y VerTweets.
		
		JButton btnNuevoCampo = new JButton(new ImageIcon("system_images/add_column.png"));
		actionButtons.add(btnNuevoCampo);	// Ver declaración de 'actionButons'.
		btnNuevoCampo.setToolTipText("Agregar nuevo campo");
		btnNuevoCampo.setPreferredSize(new Dimension(60,50));
		btnNuevoCampo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AgregarColumna a = new AgregarColumna(controlClientes);
				a.setVisible(true);
			}
		});
		panelGCGestionarGestionar.add(btnNuevoCampo);
		
		JButton btnVerTweets = new JButton(new ImageIcon("system_images/user_tweets.png"));
		actionButtons.add(btnVerTweets);	// Ver declaración de 'actionButons'.
		btnVerTweets.setToolTipText("Ver tweets de cliente");
		btnVerTweets.setPreferredSize(new Dimension(60,50));
		btnVerTweets.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(controlClientes.getPosActual()!=-1 && currentTable.equals("cliente")){
					String cliente="";
					try {
						cliente = ((JSONObject)controlClientes.getElement(controlClientes.getPosActual())).get("nombres").toString();
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					Ventana v = new Ventana(cliente);
					v.setVisible(true);
				}else{
					
				}
				
				
			}
			
		});
		panelGCGestionarGestionar.add(btnVerTweets);		
				
		panelGestionarCliente.add(panelGCGestionar, BorderLayout.SOUTH);
		panelGestionarCliente.updateUI();
		
		
		
		/**
		 * ***************** CONDIGO DE LOS EVENTOS PRINCIPALES *****************
		 */ 
		
		
		// ***************** EVENTO DEL BOTON DE BUSQUEDA DEL FORMULARIO *****************
		
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!currentAction.equals("")){
					JOptionPane.showMessageDialog(null, "Hay una accion sin completar.", "CRM Clientes", JOptionPane.ERROR_MESSAGE);					
					return;
				}
				// Getting id...
				int id;
				try {
					id = Integer.parseInt(textField_1.getText());
				}
				catch (Exception err) {
					JOptionPane.showMessageDialog(null, "ID inválido..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);
					textField_1.selectAll();
					textField_1.requestFocusInWindow();
					return;
				}    	

				// Getting properties for this id.
				String query = "SELECT * FROM " + currentTable + " WHERE id" + currentTable + " = " + id + ";" ;
				JSONArray jsonRow = conexion.executeQuery(query);
				if (jsonRow.length() == 0) {
					JOptionPane.showMessageDialog(null, "No se encontró ningún resultado\ncon el ID especificado..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);
					textField_1.selectAll();
					textField_1.requestFocusInWindow();
					return;
				}
				
				// Update form
		    	ArrayList<String> formLabels = conexion.getTableColumns(currentTable);		    	
				for (int i=0; i < formLabels.size(); i++) {
					
					/**
					 *  If table showing is 'cliente', field 'foto' exits.
					 *  So, we can get user photo url and update the view. 
					 */
					if (formLabels.get(i).equals("foto")){
						// *************** Getting user photo *************** 
					    String userPhotoUrl = "null";
					    try {
					    	userPhotoUrl = jsonRow.getJSONObject(0).getString("foto");
						} 
					    catch (JSONException error) {
							// TODO Auto-generated catch block
					    	error.printStackTrace();
							JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nobtener la foto del usuario..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);				
							return;
						}
				        ImageIcon icon = getPhotoResized(userPhotoUrl);
				        if (icon == null){
				        	icon = getPhotoResized("default_user.png");
				        }
				        labelPhotoImage.setIcon(icon);
						continue;
					}
					
					
					String propertie = "null";
					try {
						propertie = jsonRow.getJSONObject(0).getString(formLabels.get(i));
					} 
					catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						return;
					}
					listFormFields.get("textField" + formLabels.get(i)).setText(propertie);
				}
				
				controlClientes.setPosActual(id);
				table.setRowSelectionInterval(controlClientes.getPosActual(), controlClientes.getPosActual());
				
			}
		});	
		
		
		// ********** EVENTO DEL COMBO BOX AL SELECCIONAR UNA TABLA ********** 
		comboBoxMostrar.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {		    	
				if (!currentAction.equals("")){
					JOptionPane.showMessageDialog(null, "Hay una accion sin completar.", "CRM Clientes", JOptionPane.ERROR_MESSAGE);					
					return;
				}
				
		    	currentTable = comboBoxMostrar.getSelectedItem().toString().toLowerCase();
	    		
		    	// Enable or Disable checkboxes used by seraching with filters because this only works with tabla 'clientes'.
		    	Collection<JCheckBox> textFields = listFieldsToShow.values();
	    		Iterator<JCheckBox> iter = textFields.iterator();
		    	if (currentTable.equals("cliente")){
		    		chckbxSelectAll.setEnabled(true);
		    		while (iter.hasNext())
		    			iter.next().setEnabled(true);
		    		for (int i=0; i<actionButtons.size(); i++)
		    			actionButtons.get(i).setEnabled(true);
		    	}
		    	else{
		    		chckbxSelectAll.setEnabled(false);
		    		while (iter.hasNext())
		    			iter.next().setEnabled(false);		
		    		for (int i=0; i<actionButtons.size(); i++)
		    			actionButtons.get(i).setEnabled(false);
		    	}
		    	
		    	panelFormObject.removeAll();

		    	// ************** CONSTRUCCION DE LA TABLA **************
		    	// Getting columns ot the table...
				tableColumns = conexion.getTableColumns(currentTable);
				// Getting the model for the table
				String query = "SELECT * FROM " + currentTable + ";";				
				DefaultTableModel dataModel = controlClientes.getDataClientes(tableColumns, query);				
				table.setModel(dataModel);
				table.updateUI();
				
				// Cuando se seleccione la tabla clientes, se actualiza los checkboxes de seleccion.
				if (currentTable.equals("cliente")) {
					chckbxSelectAll.setSelected(true);
					for (int i=0; i<listFieldsToShow.size(); i++)
						listFieldsToShow.get("checkBox" + tableColumns.get(i)).setSelected(true);
				}
				
				
				// Getting first row of the table.
				JSONObject firstObject = null;
				try {
					firstObject = controlClientes.getFirst();
				}
				catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    	
				// Creating form for the first row of the table.
				panelFormObject.add(panelUserImage, BorderLayout.NORTH);
		    	createFormShow(firstObject);
				scrollPaneGCForm = new JScrollPane(panelGCForm);
				panelFormObject.add(scrollPaneGCForm);
				panelFormObject.updateUI();
		    }
		});

		
		
		// ***************** EVENTO DEL BOTON CON CONSULTA CON FILTROS *****************		
		
		btnBuscar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!currentAction.equals("")){
					JOptionPane.showMessageDialog(null, "Hay una accion sin completar.", "CRM Clientes", JOptionPane.ERROR_MESSAGE);					
					return;
				}
				
				// La búsqueda con filtros sólo funciona sobre la tabla clientes..!
				if (!currentTable.equals("cliente")) {
					JOptionPane.showMessageDialog(null, "La búsqueda con filtros sólo\nfunciona sobre la tabla clientes..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);					
					comboBoxMostrar.requestFocusInWindow();
					return;
				}
				ArrayList<String> tableColumnsToShow = new ArrayList<String>();				
				// Consult construction.
				String query = "SELECT ";
				// Columns for select...				
				if (!chckbxSelectAll.isSelected()) {									
					for (int i=0; i<listFieldsToShow.size(); i++)
						if (listFieldsToShow.get("checkBox" + tableColumns.get(i)).isSelected())							
							tableColumnsToShow.add(tableColumns.get(i));
				}
				else {
					tableColumnsToShow.addAll(tableColumns);					
				}
				
				/**
				 * En la query se le dice que seleccione todo porque el metodo que crea el modelo
				 * para la tabla filtra las columnas en base a la variable tableColumnsToShow.
				 */
				query += " * FROM cliente ";			
				
				
				// ID Filter
				boolean whereExist = false;
				if (!comboBoxRelOp.getSelectedItem().equals("default"))
				{
					// Validación de campo ID.
					try
					{
						int idFilter = Integer.parseInt(textFieldFiltrarID.getText());
					}
					catch (Exception error)
					{
						JOptionPane.showMessageDialog(null, "ID inválido..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);
						textFieldFiltrarID.selectAll();
						textFieldFiltrarID.requestFocusInWindow();
						return;
					}
					
					// ID condition
					if (!comboBoxRelOp.getSelectedItem().toString().equals("default")) {
						whereExist = true;
						query += " WHERE idcliente " + comboBoxRelOp.getSelectedItem().toString() + " " + textFieldFiltrarID.getText();
					}					
				}
					
				// LIKE filter
				if (!comboBoxLike.getSelectedItem().equals("default")) {
					if (!whereExist)
						query += " WHERE " + comboBoxLike.getSelectedItem().toString() + " ILIKE '%" + textFieldLike.getText() + "%'";
					else
						query += " AND " + comboBoxLike.getSelectedItem().toString() + " ILIKE '%" + textFieldLike.getText() + "%'";
				}
				
				// Order By Filter
				if (!comboBoxOrderBy.getSelectedItem().equals("default")) {					
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
				if (!currentAction.equals("")){
					JOptionPane.showMessageDialog(null, "Hay una accion sin completar.", "CRM Clientes", JOptionPane.ERROR_MESSAGE);					
					return;
				}
				
				int indice = table.getSelectedRow();
				JSONObject jsonRow;
				try {
					jsonRow = controlClientes.getElement(indice);
					if (jsonRow == null) {
						JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nobtener el elemento de la tabla..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);							
						return;
					}
				} 
				catch (Exception e1) {
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
	private void onClose()
	{
		System.out.println("Saliendo del programa...");
		if (conexion != null)
		{
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
	public void createFormShow(JSONObject object) {
		
		panelUserImage.setVisible(false);
		// Additional adjustments to the table 'clientes'.
		if (currentTable.equals("cliente")){
			
			// *************** Getting user photo *************** 
		    String userPhotoUrl = "null";
		    try {
		    	userPhotoUrl = object.getString("foto");
			} 
		    catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nobtener la foto del usuario..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);				
				return;
			}
					    
	        ImageIcon icon = getPhotoResized(userPhotoUrl);
	        if (icon == null){
	        	icon = getPhotoResized("default_user.png");
	        }
	        labelPhotoImage.setIcon(icon);
	        
	        // User Image only showed for 'clientes' table.
	        panelUserImage.setVisible(true);
		}
		
		// Getting labels to show in form. It is precisely the id columns of the table..
		ArrayList<String> formLabels = conexion.getTableColumns(currentTable);
		int numPairs = formLabels.size();		
		
		//Create and populate the panel.
		panelGCForm = new JPanel(new SpringLayout());
		listFormFields = new HashMap<String, JTextField>();
		
		for (int i = 0; i < numPairs; i++) {
			// Don't show photo's field.
			if (formLabels.get(i).equals("foto"))
				continue;
			
		    JLabel label = new JLabel(formLabels.get(i) + " : ", JLabel.TRAILING);		    
		    panelGCForm.add(label);		    
		    JTextField textField = new JTextField();		    
		    if (object != null) {
			    String rowLabel = "null";
			    try {
					rowLabel = object.getString(formLabels.get(i));
				} 
			    catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nllenar el formulario..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);				
					return;
				}
			    textField.setText(rowLabel);
		    }		 
		    textField.setName("textField" + formLabels.get(i));
		    textField.setEditable(false);		    
		    label.setLabelFor(textField);
		    panelGCForm.add(textField);
		    listFormFields.put("textField" + formLabels.get(i), textField);
		}
		
		// When the table is 'clientes', disregard the field 'foto'. 
		numPairs = currentTable.equals("cliente")? (numPairs-1): numPairs;
		
		//Lay out the panel.
		SpringUtilities.makeCompactGrid(panelGCForm,
		                                numPairs, 2, //rows, cols
		                                6, 6,        //initX, initY
		                                6, 6);       //xPad, yPad	
				
		if (object != null)
			table.setRowSelectionInterval(controlClientes.getPosActual(), controlClientes.getPosActual());
	} 
	
	
	/**
	 * Método que actualiza el formulario.
	 * @param itemToShow
	 */
	private void updateForm(JSONObject jsonRow) {
    	
    	// Getting columns ot the table...
		tableColumns = conexion.getTableColumns(currentTable);

		// Update form			
		ArrayList<String> formLabels = conexion.getTableColumns(currentTable);
		for (int i=0; i < formLabels.size(); i++) {
			// Don't show photo's field.
			if (formLabels.get(i).equals("foto")){
				// *************** Getting user photo *************** 
			    String userPhotoUrl = "null";
			    try {
			    	userPhotoUrl = jsonRow.getString("foto");
				}
			    catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nobtener la foto del usuario..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);				
					return;
				}
		        ImageIcon icon = getPhotoResized(userPhotoUrl);
		        if (icon == null){
		        	icon = getPhotoResized("default_user.png");
		        }
		        labelPhotoImage.setIcon(icon);
				continue;
			}
			String propertie = "null";
			try {
				propertie = jsonRow.getString(formLabels.get(i));
			} 
			catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar\nllenar el formulario..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);							
				return;
			}
			listFormFields.get("textField" + formLabels.get(i)).setText(propertie);
		}
		table.setRowSelectionInterval(controlClientes.getPosActual(), controlClientes.getPosActual());
	}
	
	/**
	 * Metodo que sirve para la creacion de nuevos clientes.
	 * @param valoresColumnas
	 * @return query con la ejecucion a realizar en Postgres.
	 */
	private String clienteNuevo(ArrayList<String> valoresColumnas, ArrayList<String> columnsType) {
		String query = "INSERT INTO " + currentTable + " VALUES (" + valoresColumnas.get(0);
		for (int i = 1; i<valoresColumnas.size();i++){
			if (columnsType.get(i).contains("text") || columnsType.get(i).contains("date") || columnsType.get(i).contains("char"))
				query +=", '" + valoresColumnas.get(i) + "'";
			else
				query +=", " + valoresColumnas.get(i);
		}
		query += ");";
		System.out.println(query);
		return query;
	}
	
	/**
	 * Metodo que sirve para la eliminacion de clientes.
	 * @param idCliente
	 * @return query con la ejecucion a realizar en Postgres.
	 */
	private String eliminarCliente(String idCliente) {
		String query = "DELETE FROM " + currentTable + " WHERE idcliente = " + idCliente + ";";
		System.out.println(query);
		return query;
	}
	
	/**
	 * Metodo que sirve para la hacer el update de campos de clientes.
	 * @param nombresColumnas, camposColumnas
	 * @return query con la ejecucion a realizar en Postgres.
	 */
	private String updateCliente(ArrayList<String> nombresColumnas, ArrayList<String> camposColumnas,  ArrayList<String> columnsType) {
		String query ="UPDATE " + currentTable + " SET ";
		for(int i=0; i<nombresColumnas.size(); i++){
			if (columnsType.get(i).contains("text") || columnsType.get(i).contains("date") || columnsType.get(i).contains("char"))
				query += nombresColumnas.get(i) + " = '" + camposColumnas.get(i) + "', ";
			else
				query += nombresColumnas.get(i) + " = " + camposColumnas.get(i) + ", ";
		}
		query = query.substring(0, query.length()-2);
		query += " WHERE " + nombresColumnas.get(0) + " = " + listFormFields.get("textField" + nombresColumnas.get(0)).getText() + ";";
		System.out.println(query);
		return query;
	}
	
	/**
	 * Metodo que sirve para la hacer el update de campos de clientes.
	 * @param nombresColumnas, camposColumnas
	 * @return query con la ejecucion a realizar en Postgres.
	 */
	private String agregarCampo(String nuevoCampo, String valorCampo) {
		String query = "Alter Table cliente Add Column"+nuevoCampo+" "+valorCampo+";";
		System.out.println(query);
		return query;
	}
	
	
	/**
	 * Resize an image.
	 * @param originalImage Original Image.
	 * @param type	
	 * @param width	Widht required.
	 * @param height Height required.
	 * @return Image resized.
	 */
	
    private static BufferedImage resizeImage(BufferedImage originalImage, int type, int width, int height){
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
    }
    
    /**
     * Resize an image to 100x100 pixeles.
     * @param userPhotoUrl
     * @return
     */
    public ImageIcon getPhotoResized(String userPhotoUrl){
    	BufferedImage originalImage = null;
		try {
			originalImage = ImageIO.read(new File("user_photos/" + userPhotoUrl));
		} catch (IOException e3) {
			JOptionPane.showMessageDialog(null, "No se encontró la foto del cliente..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);
			// TODO Auto-generated catch block
			e3.printStackTrace();
			return null;
		}			
		
		int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
		BufferedImage resizedImage = new BufferedImage(100, 100, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, 100, 100, null);
		g.dispose();
		
        ImageIcon icon = new ImageIcon(resizedImage);

        return icon;
    }
    
    
    
    
    
    /**
     * Listener para ya sea agregar nuevo cliente o actualizar datos de cliente. 
     */
    ActionListener updateClienteListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			if (currentAction.equals("")){
				JOptionPane.showMessageDialog(null, "Ninguna cambio para guardar..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// TODO Auto-generated method stub			
			ArrayList<String> fields = conexion.getTableColumns(currentTable);
			ArrayList<String> values = new ArrayList<String>();
			ArrayList<String> columnsType = conexion.getTableColumnsType(currentTable);
			fields.remove("foto");	// Este remove sirve solo para evitar el error cuando se recorre la lista de TextFields.
			for (int i=0; i<fields.size(); i++){				
				JTextField textField = listFormFields.get("textField" + fields.get(i));
				// Verificacion de que el campo no este vacío.
				if (textField.getText().equals("")){
					JOptionPane.showMessageDialog(null, "Complete todo los campos..!", "CRM Clientes", JOptionPane.ERROR_MESSAGE);
					listFormFields.get("textField" + fields.get(0)).requestFocus();
					return;
				}
				values.add(textField.getText());
			}
			
			fields.add("foto");		// Se regresa el campo foto.
			
			// ************************* Obtener la url de la imagen *************************
			String urlPhoto = nameCurrentImage;
			values.add(urlPhoto);
			
			
			
			// Si la ACCION es agregar nuevo cliente....
			if (currentAction.equals("addingNewUser")){
				String query = clienteNuevo(values, columnsType);
				boolean result = controlClientes.updateCliente(query);
				if (!result){
					JOptionPane.showMessageDialog(null, "No se pudo insertar cliente.\nAsegúrese de que todos los campos "
							+ "sean correctos.", "CRM Clientes", JOptionPane.ERROR_MESSAGE);
					listFormFields.get("textField" + fields.get(0)).requestFocus();
					return;
				}
				else
					JOptionPane.showMessageDialog(null, "Cliente insertado correctamente.", "CRM Clientes", JOptionPane.ERROR_MESSAGE);
				
			}
			
			// Si la ACCION es actualizar cliente....
			else if (currentAction.equals("updatingUser")){
				String query = updateCliente(fields, values, columnsType);
				boolean result = controlClientes.updateCliente(query);
				if (!result){
					JOptionPane.showMessageDialog(null, "No se pudo actualizar cliente.\nAsegúrese de que todos los campos "
							+ "sean correctos.", "CRM Clientes", JOptionPane.ERROR_MESSAGE);
					listFormFields.get("textField" + fields.get(0)).requestFocus();
					return;
				}
				else
					JOptionPane.showMessageDialog(null, "Cliente actualizado correctamente.", "CRM Clientes", JOptionPane.ERROR_MESSAGE);
			}
			
			//Se guarda la imagen seleccionada.
			if(!nameCurrentImage.equals("default_user.png")){
				File destino = new File("user_photos/" + nameCurrentImage);
	   		 	try {
					ImageIO.write(currentImage, "jpg", destino);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			

			// ********* ACTUALIZAR LA TABLA DE DATOS *********			
			ArrayList<String> tableColumnsToShow = new ArrayList<String>();		
			for (int i=0; i<listFieldsToShow.size(); i++) {
				if (listFieldsToShow.get("checkBox" + tableColumns.get(i)).isSelected()){
					tableColumnsToShow.add(tableColumns.get(i));
				}			
			}			
			controlClientes = ControladorClientes.getInstancia();
			String query = "SELECT * FROM cliente;";
			DefaultTableModel dataModel = controlClientes.getDataClientes(tableColumnsToShow, query);		
			table.setModel(dataModel);
			table.updateUI();
			
			
			
			// Dejar inactivos y no editables todos los campos.
			fields.remove("foto");		// De nuevo, para evitar el error removemos el campo 'foto'.
			for (int i=0; i<fields.size(); i++){				
				JTextField textField = listFormFields.get("textField" + fields.get(i));				
				textField.setEditable(false);
				if (fields.get(i).equals("idcategoria") || fields.get(i).equals("iddepartamento") || fields.get(i).equals("idestado") || fields.get(i).equals("idtelefono")){
					textField.removeMouseListener(textFieldsListener);
				}
			}
			currentAction = "";
			
		}
    	
    };
    
    
    /**
     * Listener para el botón eliminar cliennte. Elimina un cliente de la tabla.
     */
    ActionListener eliminarClienteListener = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (!currentAction.equals("")){
				JOptionPane.showMessageDialog(null, "Hay una accion sin completar.", "CRM Clientes", JOptionPane.ERROR_MESSAGE);					
				return;
			}
			String idCliente = listFormFields.get("textFieldidcliente").getText();
			String query = eliminarCliente(idCliente);
			
			boolean result = controlClientes.updateCliente(query);
			if (!result){
				JOptionPane.showMessageDialog(null, "No se pudo eliminar cliente.", "CRM Clientes", JOptionPane.ERROR_MESSAGE);				
				return;
			}
			else
				JOptionPane.showMessageDialog(null, "Cliente eliminado correctamente.", "CRM Clientes", JOptionPane.ERROR_MESSAGE);
			
			
			currentAction = "";
			
			// ********* ACTUALIZAR LA TABLA DE DATOS *********			
			ArrayList<String> tableColumnsToShow = new ArrayList<String>();		
			for (int i=0; i<listFieldsToShow.size(); i++) {
				if (listFieldsToShow.get("checkBox" + tableColumns.get(i)).isSelected()){
					tableColumnsToShow.add(tableColumns.get(i));
				}			
			}			
			controlClientes = ControladorClientes.getInstancia();
			query = "SELECT * FROM cliente;";
			DefaultTableModel dataModel = controlClientes.getDataClientes(tableColumnsToShow, query);		
			table.setModel(dataModel);
			table.updateUI();
			
		}
    	
    };
    
    
    
    
    /**
     * Listener para los campos de texto que requieren un id.
     * El evento abre la ventana con los datos requeridos por el campo.
     */
    MouseListener textFieldsListener = new MouseListener(){

		@Override
		public void mouseClicked(MouseEvent e) {
			// Obtener el origen del click.
			JTextField textFieldClicked = (JTextField)e.getSource();
			System.out.println("TextField cliqueado: " + textFieldClicked.getName());
			// Si el campo clickeado es el de idcategoria, abrir la ventana con todas las categorías...
			if (textFieldClicked.getName().equals("textFieldidcategoria")){
				currentEditing = listFormFields.get("textFieldidcategoria");
				controlClientes.openWindowCategoria(GestionClientes.this);
			}
			// Si el campo clickeado es el de iddepartamento, abrir la ventana con todos los departamentos...
			else if (textFieldClicked.getName().equals("textFieldiddepartamento")){
				currentEditing = listFormFields.get("textFieldiddepartamento");
				controlClientes.openWindowDepartamento(GestionClientes.this);
			}
			// Si el campo clickeado es el de idestado, abrir la ventana con todos los estados...
			else if (textFieldClicked.getName().equals("textFieldidestado")){
				currentEditing = listFormFields.get("textFieldidestado");
				controlClientes.openWindowEstado(GestionClientes.this);
			}
			// Si el campo clickeado es el de idtelefono, abrir la ventana con todos los telefonos...
			else {
				currentEditing = listFormFields.get("textFieldidtelefono");
				controlClientes.openWindowTelefono(GestionClientes.this);
			}			
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	};   
	
	
	/**
	 * Mediante este metodo es que un campo clickeado, obtenga el valor seleccionado
	 * desde la ventana emergente. Es modificado por la clase de dicha ventana. 
	 * @param value
	 */
    public void modificarCampo(String value){
    	currentEditing.setText(value);
    }
	
    public File AbrirFoto()
    {
    	try
    	{
    		/**llamamos el metodo que permite cargar la ventana*/
    		JFileChooser file=new JFileChooser();
    		file.showOpenDialog(this);
    		/**abrimos el archivo seleccionado*/
    		File abre=file.getSelectedFile();
    		
    		return abre;
    	}
    	catch(Exception e)
    	{
    		JOptionPane.showMessageDialog(null,"No se ha encontrado el archivo","ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
    		return null;
    	}
    }
    
    public void copiar(File original, File copia) 
    { 
	    FileInputStream archivoOriginal = null; 
	    FileOutputStream archivoCopia = null; 
	    if((original != null)&&(copia != null)) 
	    { 
	    	try 
	    	{ 
			    copia.createNewFile(); 
			    archivoOriginal = new FileInputStream(original); 
			    archivoCopia = new FileOutputStream(copia); 
			    //lectura por segmentos de 0.5MB 
			    byte buffer[] = new byte[512*1024]; 
			    int nbLectura; 
			    while((nbLectura=archivoOriginal.read(buffer)) != -1) 
			    { 
			    	archivoCopia.write(buffer,0,nbLectura); 
			    } 
	    	}
	    	catch(FileNotFoundException fnf)
	    	{ 
	    		
	    	}
		    catch(IOException io)
	    	{ 
		    }
	    	finally
	    	{ 
			    try 
			    { 
			    archivoOriginal.close(); 
			    }catch(Exception e){ 
			    } 
			    try 
			    { 
			    archivoCopia.close(); 
			    }catch(Exception e){ 
			    } 
		    } 
	    }
    }
    
 
    
    public static void copyFile(File sourceFile, File destFile) throws IOException { 

    	   if(!destFile.exists()) { 

    	    destFile.createNewFile(); 

    	   } 

    	    

    	   FileChannel source = null; 

    	   FileChannel destination = null; 

    	   try { 

    	    source = new FileInputStream(sourceFile).getChannel(); 

    	    destination = new FileOutputStream(destFile).getChannel(); 

    	    destination.transferFrom(source, 0, source.size()); 

    	   } 

    	   finally { 

    	    if(source != null) { 

    	     source.close(); 

    	    } 

    	    if(destination != null) { 

    	     destination.close(); 

    	    } 

    	  } 

    	 }
    
    
    
    public boolean cargarImagen(String idCliente){
    	JFileChooser fc=new JFileChooser();
    	//Creamos el filtro
    	FileNameExtensionFilter filtro = new FileNameExtensionFilter("*.JPG", "jpg", "*.PNG", "png");
    	//Le indicamos el filtro
    	fc.setFileFilter(filtro);
    	
    	int seleccion=fc.showOpenDialog(this);
    	if(seleccion==JFileChooser.APPROVE_OPTION){
    		 
			 try {
				 File imagen=fc.getSelectedFile();
	    		 BufferedImage original = ImageIO.read(imagen);
	    		 BufferedImage resizedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_BGR);
	    		 Graphics2D g = resizedImage.createGraphics();
	    		 g.drawImage(original, 0, 0, 100, 100, null);
	    		 g.dispose();
	    		 currentImage = resizedImage;
	    		 nameCurrentImage = idCliente+".jpg";
	    		 //ImageIO.write(resizedImage, ext, destino);
	    		 
	    		 labelPhotoImage.setIcon(new ImageIcon(resizedImage));
				} catch (IOException e) {
					System.out.println("Error de escritura");
					e.printStackTrace();
			 }
    		 
    	}
    	
    	return true;
    }
    
}


