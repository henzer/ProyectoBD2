package modulo1.controladores;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import modulo1.conexion.ConexionPostgres;
import modulo2.conexion.Conexion;

public class ControladorClientes {
	private JSONArray clientes;
	private JSONArray categorias;
	private JSONArray departamentos;
	private JSONArray estados;
	private JSONArray paises;
	private JSONArray telefonos;
	private String actual;
	private int posActual;
	
	private static ControladorClientes instancia;
	public static ControladorClientes getInstancia(){
		if(instancia==null)
			instancia = new ControladorClientes();
		return instancia;
	}
	
	public ControladorClientes(){
		clientes = new JSONArray();
		categorias = new JSONArray();
		departamentos = new JSONArray();
		estados = new JSONArray();
		paises = new JSONArray();
		telefonos = new JSONArray();
	}
	
	//
	public DefaultTableModel getDataClientes(List<String> columnas, String query){
		actual = "clientes";
		DefaultTableModel model = new DefaultTableModel();
		clientes = ConexionPostgres.getInstancia().executeQuery(query);
		for(String header: columnas){
			model.addColumn(header);
		}
		try {
			for(int i=0; i<clientes.length(); i++){
				JSONObject elemento = (JSONObject)clientes.get(i);
				Object [] row = new Object[columnas.size()];
				for(int j=0; j<columnas.size(); j++){
					row[j] = elemento.get(columnas.get(j));
				}
				model.addRow(row);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return model;
	}
	
	//Devuelve el primer elemento de la lista actual. Si esta vacia retorna NULL
	public JSONObject getFirst() throws Exception{
		switch(actual){
		case "clientes":
			if(clientes.length()>0){
				return (JSONObject) clientes.get(0);
			}
			break;
		case "categorias":
			break;
		case "departamentos":
			break;
		case "estados":
			break;
		case "paises":
			break;
		case "telefonos":
			break;
		}
		return null;
	}
	
	//Devuelve el ultimo elemento de la lista actual. Si esta vacia retorna NULL
	public JSONObject getLast() throws Exception{
		switch(actual){
		case "clientes":
			if(clientes.length()>0){
				return (JSONObject) clientes.get(clientes.length()-1);
			}
			break;
		case "categorias":
			break;
		case "departamentos":
			break;
		case "estados":
			break;
		case "paises":
			break;
		case "telefonos":
			break;
		}
		return null;
	}
	
	//Devuelve el elemento ubicado en la posición POS, si está vacia retorna null.
	public JSONObject getElement(int pos) throws Exception{
		switch(actual){
		case "clientes":
			if(clientes.length()>0){
				return (JSONObject) clientes.get(pos);
			}
			break;
		case "categorias":
			break;
		case "departamentos":
			break;
		case "estados":
			break;
		case "paises":
			break;
		case "telefonos":
			break;
		}
		return null;
	}
	
	//Inserta una nueva columna. Lanza una Excepción si se intenta crear una columna que ya existe.
	//Cualquier otro error no lo maneja, simplemente lanzaría false. (Se debe manejar desde donde se llame).
	public boolean insertNewRow(String name, String type, String _default) throws Exception{
		List<String> columns = ConexionPostgres.getInstancia().getTableColumns("clientes");
		if(columns.contains(name))
			throw new Exception("ERROR.-Ya existe una columna con ese nombre");
		return ConexionPostgres.getInstancia().executeUpdate("ALTER TABLE clientes ADD COLUMN "+name+" "+ type +" DEFAULT "+_default+";");		
	}

	public int getPosActual() {
		return posActual;
	}

	public void setPosActual(int posActual) {
		this.posActual = posActual;
	}
}
