package modulo1.vistas;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;

import modulo1.controladores.ControladorClientes;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AgregarColumna extends JFrame {

	private JPanel contentPane;
	private JTextField txtTitulo;
	private JComboBox cmbTipo;
	private ControladorClientes control;

	//El constructor recibe como parametro un ControladorCliente para delgarle las opciones para poder agregar las columnas
	//en la base de datos.
	public AgregarColumna(ControladorClientes control) {
		this.control = control;
		setTitle("Agregar Columna");
		setBounds(100, 100, 349, 159);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTtulo = new JLabel("T\u00EDtulo:");
		lblTtulo.setBounds(26, 26, 46, 14);
		contentPane.add(lblTtulo);
		
		txtTitulo = new JTextField();
		txtTitulo.setBounds(82, 23, 226, 20);
		contentPane.add(txtTitulo);
		txtTitulo.setColumns(10);
		
		JLabel lblTipo = new JLabel("Tipo:");
		lblTipo.setBounds(26, 57, 46, 14);
		contentPane.add(lblTipo);
		
		cmbTipo = new JComboBox();
		cmbTipo.setModel(new DefaultComboBoxModel(new String[] {"integer", "text", "boolean", "date", "float"}));
		cmbTipo.setBounds(82, 54, 226, 20);
		contentPane.add(cmbTipo);
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					validar();
					String titulo = txtTitulo.getText();
					String tipo = cmbTipo.getSelectedItem().toString();
					control.insertNewRow(titulo, tipo);
					cerrarVentana();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();
				}
					
					
			}
		});
		addWindowListener(new WindowAdapter(){
			 public void windowClosing(WindowEvent we){
				 cerrarVentana();
			 }
		});
		
		btnAceptar.setMnemonic('c');
		btnAceptar.setBounds(64, 93, 89, 23);
		contentPane.add(btnAceptar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cerrarVentana();
			}
		});
		btnCancelar.setBounds(182, 93, 89, 23);
		contentPane.add(btnCancelar);
	}
	
	public boolean validar() throws Exception{
		if(txtTitulo.getText().equals(""))
			throw new Exception("ERROR.-Debe ingresar un titulo.");
		if(cmbTipo.getSelectedIndex()==-1)
			throw new Exception("ERROR.-Debe seleccionar un tipo de dato.");
		return true;
	}
	
	public void cerrarVentana(){
		this.dispose();
	}
}
