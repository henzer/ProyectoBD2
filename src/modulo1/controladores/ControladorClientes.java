package modulo1.controladores;

import java.util.List;

import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import modulo1.conexion.ConexionPostgres;
import modulo1.vistas.Categoria;
import modulo1.vistas.Departamento;
import modulo1.vistas.Estado;
import modulo1.vistas.GestionClientes;
import modulo1.vistas.Telefono;
import modulo2.conexion.Conexion;

public class ControladorClientes
{
	private JSONArray dataActual;
	private int posActual;
	
	private static ControladorClientes instancia;
	public static ControladorClientes getInstancia()
	{
		if(instancia==null)
			instancia = new ControladorClientes();
		return instancia;
	}
	
	public ControladorClientes()
	{
		dataActual = new JSONArray();
	}
	
	//
	public DefaultTableModel getDataClientes(List<String> columnas, String query)
	{
		DefaultTableModel model = new DefaultTableModel();
		dataActual = ConexionPostgres.getInstancia().executeQuery(query);
		for(String header: columnas){
			model.addColumn(header);
		}
		try 
		{
			for(int i=0; i<dataActual.length(); i++)
			{
				JSONObject elemento = (JSONObject)dataActual.get(i);
				System.out.println(elemento);
				Object [] row = new Object[columnas.size()];
				for(int j=0; j<columnas.size(); j++)
				{
					row[j] = elemento.get(columnas.get(j));
				}
				model.addRow(row);
			}
		} 
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return model;
	}
	
	//Devuelve el primer elemento de la lista actual. Si esta vacia retorna NULL
	public JSONObject getFirst() throws Exception
	{
		if(dataActual.length()>0)
		{
			posActual = 0;
			return (JSONObject) dataActual.get(0);
		}
		return null;
	}
	
	//Devuelve el ultimo elemento de la lista actual. Si esta vacia retorna NULL
	public JSONObject getLast() throws Exception
	{
		if(dataActual.length()>0){
			posActual = dataActual.length()-1;
			return (JSONObject) dataActual.get(dataActual.length()-1);
		}
		return null;
	}
	
	//Devuelve el elemento ubicado en la posición POS, si está vacia retorna null.
	public JSONObject getElement(int pos) throws Exception{
		if(pos<0)
			pos = 0;
		if(pos>dataActual.length()-1)
			pos = dataActual.length()-1;
			
		if(dataActual.length()>0){
			posActual = pos;
			return (JSONObject) dataActual.get(pos);
		}
		return null;
	}
	
	//Inserta una nueva columna. Lanza una Excepción si se intenta crear una columna que ya existe.
	//Cualquier otro error no lo maneja, simplemente lanzaría false. (Se debe manejar desde donde se llame).
	public boolean insertNewRow(String titulo, String type) throws Exception{
		List<String> columns = ConexionPostgres.getInstancia().getTableColumns("cliente");
		String newName = "nueva" + columns.size();
		int next = columns.size() + 1;
		if(columns.contains(newName))
			throw new Exception("ERROR.-Ya existe una columna con ese nombre");
		return ConexionPostgres.getInstancia().executeUpdate("INSERT INTO metadata_clientes values ("+next+", '"+newName+"', '"+type+"', '"+titulo+"', 'F');");		
	}

	public int getPosActual() {
		return posActual;
	}

	public void setPosActual(int posActual) {
		this.posActual = posActual;
	}
	
	//Sirve para obtener la metadata de la tabla Clientes
	public JSONArray getMetadata(){
		JSONArray result = ConexionPostgres.getInstancia().executeQuery("SELECT * FROM metadata_clientes;");
		return result;
	}
	
	
	
	
	
	public boolean updateCliente(String query){		
		return ConexionPostgres.getInstancia().executeUpdate(query);
	}
	
	
	
	
	/**
	 * METODOS QUE ABREN LAS VENTANAS CON TODA LA DATA DISPONIBLE.
	 * CADA UNO, ABRE ESPECIFICAMENTE UNA VENTANA.
	 */
	
	
	// Metodo que visualiza la ventana Telefono.
	public void openWindowCategoria(GestionClientes parent){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Categoria ventanaEstado = new Categoria(parent);
			ventanaEstado.setVisible(true);			
		}
			catch (Exception e){
			e.printStackTrace();
		}		
	}
	
	// Metodo que visualiza la ventana Telefono.
	public void openWindowDepartamento(GestionClientes parent){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Departamento ventanaDepartamento = new Departamento(parent);
			ventanaDepartamento.setVisible(true);			
		}
			catch (Exception e){
			e.printStackTrace();
		}		
	}
	
	// Metodo que visualiza la ventana Telefono.
	public void openWindowEstado(GestionClientes parent){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Estado ventanaEstado = new Estado(parent);
			ventanaEstado.setVisible(true);			
		}
			catch (Exception e){
			e.printStackTrace();
		}		
	}	
	
	// Metodo que visualiza la ventana Telefono.
	public void openWindowTelefono(GestionClientes parent){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Telefono ventanaTelefono = new Telefono(parent);
			ventanaTelefono.setVisible(true);			
		}
			catch (Exception e){
			e.printStackTrace();
		}		
	}
	


	

	

	
	
}

