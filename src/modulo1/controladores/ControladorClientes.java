package modulo1.controladores;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import modulo1.conexion.ConexionPostgres;

public class ControladorClientes {
	private static ControladorClientes instancia;
	public static ControladorClientes getInstancia(){
		if(instancia==null)
			instancia = new ControladorClientes();
		return instancia;
	}
	
	public ControladorClientes(){
		
	}
	
	public DefaultTableModel getDataClientes(List<String> columnas, String query){
		DefaultTableModel model = new DefaultTableModel();
		JSONArray arreglo = ConexionPostgres.getInstancia().executeQuery("select * from clientes;");
		for(String header: columnas){
			model.addColumn(header);
		}
		try {
			for(int i=0; i<arreglo.length(); i++){
				JSONObject elemento = (JSONObject)arreglo.get(i);
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
}