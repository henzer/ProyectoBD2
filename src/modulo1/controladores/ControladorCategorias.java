package modulo1.controladores;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import modulo1.conexion.ConexionPostgres;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ControladorCategorias 
{

	private JSONArray data;
	
	public ControladorCategorias()
	{
		data = new JSONArray();
	}
	
	public DefaultTableModel getData()
	{
		DefaultTableModel model = new DefaultTableModel();
		data = ConexionPostgres.getInstancia().executeQuery("SELECT * FROM categorias;");
		List<String> columnas = ConexionPostgres.getInstancia().getTableColumns("categorias");
		for(String header: columnas)
		{
			model.addColumn(header);
		}
		try 
		{
			for(int i=0; i<data.length(); i++)
			{
				JSONObject elemento = (JSONObject)data.get(i);
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
	
	public JSONObject getElement(int i) throws JSONException{
		return data.getJSONObject(i);
	}

}
