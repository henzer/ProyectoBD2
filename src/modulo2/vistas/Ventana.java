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

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;

import java.awt.Button;

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
	
	
	
	public Ventana() {
		controlador = Controlador.getInstancia();
		setTitle("Analizar informaci\u00F3n de: ");
		setPreferredSize(new Dimension(800, 400));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		JComboBox cmbID = new JComboBox();
		cmbID.setModel(new DefaultComboBoxModel(new String[] {"", ">", "<", "="}));
		cmbID.setBounds(10, 44, 33, 20);
		filtros.add(cmbID);
		
		txtId = new JTextField();
		txtId.setBounds(56, 44, 97, 20);
		filtros.add(txtId);
		txtId.setColumns(10);
		
		txtTexto = new JTextField();
		txtTexto.setColumns(10);
		txtTexto.setBounds(56, 88, 97, 20);
		filtros.add(txtTexto);
		
		JComboBox cmbTexto = new JComboBox();
		cmbTexto.setModel(new DefaultComboBoxModel(new String[] {"", ">", "<", "="}));
		cmbTexto.setBounds(10, 88, 33, 20);
		filtros.add(cmbTexto);
		
		JLabel lblTexto = new JLabel("TEXTO");
		lblTexto.setBounds(10, 75, 46, 14);
		filtros.add(lblTexto);
		
		txtRetweets = new JTextField();
		txtRetweets.setColumns(10);
		txtRetweets.setBounds(56, 135, 97, 20);
		filtros.add(txtRetweets);
		
		JComboBox cmbRetweets = new JComboBox();
		cmbRetweets.setModel(new DefaultComboBoxModel(new String[] {"", ">", "<", "="}));
		cmbRetweets.setBounds(10, 135, 33, 20);
		filtros.add(cmbRetweets);
		
		JLabel lblCantidadDeRetweet = new JLabel("CANTIDAD DE RETWEETS");
		lblCantidadDeRetweet.setBounds(10, 119, 130, 14);
		filtros.add(lblCantidadDeRetweet);
		
		txtMencionados = new JTextField();
		txtMencionados.setColumns(10);
		txtMencionados.setBounds(56, 185, 97, 20);
		filtros.add(txtMencionados);
		
		JComboBox cmbMencionados = new JComboBox();
		cmbMencionados.setModel(new DefaultComboBoxModel(new String[] {"", ">", "<", "="}));
		cmbMencionados.setBounds(10, 185, 33, 20);
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
		
		JComboBox cmbFecha = new JComboBox();
		cmbFecha.setModel(new DefaultComboBoxModel(new String[] {"", ">", "<", "="}));
		cmbFecha.setBounds(10, 227, 33, 20);
		filtros.add(cmbFecha);
		
		txtFecha = new JTextField();
		txtFecha.setColumns(10);
		txtFecha.setBounds(56, 227, 97, 20);
		filtros.add(txtFecha);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		marcarTodos();
		
		tabla = new JTable();
		tabla.setRowHeight(25);
		tabla.setFillsViewportHeight(true);
		loadData();
		scrollPane.setViewportView(tabla);
		
	}
	
	public void loadData(){
		tabla.setModel(controlador.getInfoTable(camposSeleccionados()));
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
	
	public void marcarTodos(){
		chkId.setSelected(true);
		chkTexto.setSelected(true);
		chkRetweets.setSelected(true);
		chkMencionados.setSelected(true);
		chkFecha.setSelected(true);
	}
	
	
	
	
	
}
