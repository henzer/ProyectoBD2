package modulo2.vistas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import modulo2.conexion.Conexion;
import modulo2.controladores.Controlador;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;

import twitter4j.JSONObject;

import com.mongodb.BasicDBObject;

import java.awt.Button;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class Ventana extends JFrame {

	private JPanel contentPane;
	private JTable tabla;
	private JTextField txtId;
	private JTextField txtTexto;
	private JTextField txtRetweets;
	private JTextField txtMencionados;
	private Controlador controlador;
	private JTextField txtFecha;
	private JCheckBox chkId;
	private JCheckBox chkTexto;
	private JCheckBox chkRetweets;
	private JCheckBox chkMencionados;
	private JCheckBox chkFecha;
	private JComboBox cmbId;
	private JComboBox cmbTexto;
	private JComboBox cmbRetweets;
	private JComboBox cmbMencionados;
	private JComboBox cmbFecha;
	private JLabel lblResultados;
	private JLabel lblCantidad;
	private String cliente;
	
	
	
	public Ventana(String nombre) {
		this.cliente = nombre;
		controlador = Controlador.getInstancia();
		setTitle("Analizar informaci\u00F3n de: ");
		setPreferredSize(new Dimension(800, 400));
		setBounds(100, 100, 926, 587);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JPanel filtros = new JPanel();
		filtros.setPreferredSize(new Dimension(160, 10));
		contentPane.add(filtros, BorderLayout.WEST);
		filtros.setLayout(null);
		
		JLabel lblFiltros = new JLabel("FILTROS");
		lblFiltros.setBounds(56, 11, 42, 14);
		filtros.add(lblFiltros);
		
		JLabel lblId = new JLabel("ID");
		lblId.setBounds(10, 30, 46, 14);
		filtros.add(lblId);
		
		cmbId = new JComboBox();
		cmbId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(cmbId.getSelectedIndex()==0){
					txtId.setEnabled(false);
					txtId.setText("");
				}else{
					txtId.setEnabled(true);
				}
			}
		});
		cmbId.setModel(new DefaultComboBoxModel(new String[] {"", ">", "<", "="}));
		cmbId.setBounds(10, 44, 43, 20);
		filtros.add(cmbId);
		
		txtId = new JTextField();
		txtId.setBounds(56, 44, 97, 20);
		filtros.add(txtId);
		txtId.setColumns(10);
		
		txtTexto = new JTextField();
		txtTexto.setColumns(10);
		txtTexto.setBounds(56, 88, 97, 20);
		filtros.add(txtTexto);
		
		cmbTexto = new JComboBox();
		cmbTexto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(cmbTexto.getSelectedIndex()==0){
					txtTexto.setEnabled(false);
					txtTexto.setText("");
				}else{
					txtTexto.setEnabled(true);
				}
			}
		});
		cmbTexto.setModel(new DefaultComboBoxModel(new String[] {"", "Like"}));
		cmbTexto.setBounds(10, 88, 43, 20);
		filtros.add(cmbTexto);
		
		JLabel lblTexto = new JLabel("TEXTO");
		lblTexto.setBounds(10, 75, 46, 14);
		filtros.add(lblTexto);
		
		txtRetweets = new JTextField();
		txtRetweets.setColumns(10);
		txtRetweets.setBounds(56, 135, 97, 20);
		filtros.add(txtRetweets);
		
		cmbRetweets = new JComboBox();
		cmbRetweets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(cmbRetweets.getSelectedIndex()==0){
					txtRetweets.setEnabled(false);
					txtRetweets.setText("");
				}else{
					txtRetweets.setEnabled(true);
				}
			}
		});
		cmbRetweets.setModel(new DefaultComboBoxModel(new String[] {"", ">", "<", "="}));
		cmbRetweets.setBounds(10, 135, 43, 20);
		filtros.add(cmbRetweets);
		
		JLabel lblCantidadDeRetweet = new JLabel("CANTIDAD DE RETWEETS");
		lblCantidadDeRetweet.setBounds(10, 119, 130, 14);
		filtros.add(lblCantidadDeRetweet);
		
		txtMencionados = new JTextField();
		txtMencionados.setColumns(10);
		txtMencionados.setBounds(56, 185, 97, 20);
		filtros.add(txtMencionados);
		
		cmbMencionados = new JComboBox();
		cmbMencionados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(cmbMencionados.getSelectedIndex()==0){
					txtMencionados.setEnabled(false);
					txtMencionados.setText("");
				}else{
					txtMencionados.setEnabled(true);
				}
			}
		});
		cmbMencionados.setModel(new DefaultComboBoxModel(new String[] {"", ">", "<", "="}));
		cmbMencionados.setBounds(10, 185, 43, 20);
		filtros.add(cmbMencionados);
		
		JLabel lblCantidadMencionados = new JLabel("CANTIDAD MENCIONADOS");
		lblCantidadMencionados.setBounds(10, 166, 130, 14);
		filtros.add(lblCantidadMencionados);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(13, 258, 140, 2);
		filtros.add(separator);
		
		JLabel lblSeleccionarCampos = new JLabel("SELECCIONAR CAMPOS");
		lblSeleccionarCampos.setHorizontalAlignment(SwingConstants.CENTER);
		lblSeleccionarCampos.setBounds(13, 271, 140, 14);
		filtros.add(lblSeleccionarCampos);
		
		chkId = new JCheckBox("ID");
		chkId.setBounds(13, 285, 140, 23);
		filtros.add(chkId);
		
		chkTexto = new JCheckBox("TEXTO");
		chkTexto.setBounds(13, 311, 140, 23);
		filtros.add(chkTexto);
		
		chkRetweets = new JCheckBox("CANT. RETWEETS");
		chkRetweets.setBounds(13, 337, 140, 23);
		filtros.add(chkRetweets);
		
		chkMencionados = new JCheckBox("CANT. MENCIONADOS");
		chkMencionados.setBounds(13, 363, 140, 23);
		filtros.add(chkMencionados);
		
		chkFecha = new JCheckBox("FECHA");
		chkFecha.setBounds(13, 389, 140, 23);
		filtros.add(chkFecha);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(13, 416, 140, 2);
		filtros.add(separator_1);
		
		JButton btnFiltrar = new JButton("FILTRAR");
		btnFiltrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadData();
			}
		});
		btnFiltrar.setBounds(35, 429, 91, 37);
		filtros.add(btnFiltrar);

		JLabel lblFechaDeCreacin = new JLabel("FECHA DE CREACI\u00D3N");
		lblFechaDeCreacin.setBounds(10, 210, 130, 14);
		filtros.add(lblFechaDeCreacin);
		
		cmbFecha = new JComboBox();
		cmbFecha.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(cmbFecha.getSelectedIndex()==0){
					txtFecha.setEnabled(false);
					txtFecha.setText("");
				}else{
					txtFecha.setEnabled(true);
				}
			}
		});
		cmbFecha.setModel(new DefaultComboBoxModel(new String[] {"", ">", "<", "="}));
		cmbFecha.setBounds(10, 227, 43, 20);
		filtros.add(cmbFecha);
		
		txtFecha = new JTextField();
		txtFecha.setColumns(10);
		txtFecha.setBounds(56, 227, 97, 20);
		filtros.add(txtFecha);
		
		lblResultados = new JLabel("Resultados:");
		lblResultados.setBounds(30, 504, 57, 14);
		filtros.add(lblResultados);
		
		lblCantidad = new JLabel("0");
		lblCantidad.setBounds(93, 504, 57, 14);
		filtros.add(lblCantidad);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		marcarTodos();
		
		tabla = new JTable();
		tabla.setRowHeight(25);
		tabla.setFillsViewportHeight(true);
		loadData();
		scrollPane.setViewportView(tabla);
		
		addWindowListener(new WindowAdapter(){
			 public void windowClosing(WindowEvent we){
				 cerrarVentana();
			 }
		});
		
		blockTextFields();
		
	}
	
	public void cerrarVentana(){
		this.dispose();
	}
	
	public void loadData(){
		try {
			tabla.setModel(controlador.getInfoTable(getFiltros(), camposSeleccionados()));
			lblCantidad.setText(""+tabla.getModel().getRowCount());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			//e.printStackTrace();
		}
		//tabla.getColumnModel().getColumn(1).setPreferredWidth(400);
	}
	
	public boolean[] camposSeleccionados(){
		boolean[] c = new boolean[5];
		c[0] = chkId.isSelected();
		c[1] = chkTexto.isSelected();
		c[2] = chkRetweets.isSelected();
		c[3] = chkMencionados.isSelected();
		c[4] = chkFecha.isSelected();
		return c;
	} 
	
	public void blockTextFields(){
		txtId.setEnabled(false);
		txtTexto.setEnabled(false);
		txtRetweets.setEnabled(false);
		txtMencionados.setEnabled(false);
		txtFecha.setEnabled(false);
	}
	public BasicDBObject getFiltros() throws Exception{
		BasicDBObject query = new BasicDBObject();
		if(cmbId.getSelectedIndex()!=0){
			try{
				long value = Long.parseLong(txtId.getText());
				switch(cmbId.getSelectedIndex()){
				case 1:
					query.put("tweet_ID", new BasicDBObject("$gt", value));
					break;
				case 2:
					query.put("tweet_ID", new BasicDBObject("$lt", value));
					break;
				case 3:
					query.put("tweet_ID", value);
					break;
				}
			}catch(Exception ex){
				throw new Exception("ERROR.-El ID ingresado es incorrecto.");
			}
		}
		
		if(cmbTexto.getSelectedIndex()!=0){
			try{
				String value = txtTexto.getText();
				switch(cmbTexto.getSelectedIndex()){
				case 1:
					query.put("tweet_text", Pattern.compile(value));
					break;
				}
			}catch(Exception ex){
				throw new Exception("ERROR.-El valor ingresado en Texto no es correcto.");
			}
		}
		
		if(cmbRetweets.getSelectedIndex()!=0){
			try{
				int value = Integer.parseInt(txtRetweets.getText());
				switch(cmbRetweets.getSelectedIndex()){
				case 1:
					query.put("retweet_count", new BasicDBObject("$gt", value));
					break;
				case 2:
					query.put("retweet_count", new BasicDBObject("$lt", value));
					break;
				case 3:
					query.put("retweet_count", value);
					break;
				}
			}catch(Exception ex){
				throw new Exception("ERROR.-El valor ingresado en el campo Retweets debe ser entero.");
			}
		}
		
		if(cmbMencionados.getSelectedIndex()!=0){
			try{
				int value = Integer.parseInt(txtMencionados.getText());
				switch(cmbMencionados.getSelectedIndex()){
				case 1:
					query.put("tweet_mentioned_count", new BasicDBObject("$gt", value));
					break;
				case 2:
					query.put("tweet_mentioned_count", new BasicDBObject("$lt", value));
					break;
				case 3:
					query.put("tweet_mentioned_count", value);
					break;
				}
			}catch(Exception ex){
				throw new Exception("ERROR.-El valor ingresado en el campo Mencionados debe ser entero.");
			}
		}
		
		if(cmbFecha.getSelectedIndex()!=0){
			try{
				String value = txtFecha.getText();
				DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
				formato.setLenient(false);
				formato.parse(value);
				
				switch(cmbFecha.getSelectedIndex()){
				case 1:
					query.put("tweet_date", new BasicDBObject("$gt", value));
					break;
				case 2:
					query.put("tweet_date", new BasicDBObject("$lt", value));
					break;
				case 3:
					query.put("tweet_date", value);
					break;
				}
			}catch(Exception ex){
				throw new Exception("ERROR.-El formato de la fecha es incorrecto. Debe ser dd/MM/yyyy.");
			}
		}
		query.put("user_name", cliente);
		return query;
	}
	
	public void marcarTodos(){
		chkId.setSelected(true);
		chkTexto.setSelected(true);
		chkRetweets.setSelected(true);
		chkMencionados.setSelected(true);
		chkFecha.setSelected(true);
	}
	
	
	
	
	
}
