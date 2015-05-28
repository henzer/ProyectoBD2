package modulo1.controladores;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		System.out.println(dataActual.toString());
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
		return ConexionPostgres.getInstancia().executeUpdate("ALTER TABLE cliente ADD COLUMN " + titulo + " " + type + ";");		
	}

	public int getPosActual() {
		return posActual;
	}

	public void setPosActual(int posActual) {
		this.posActual = posActual;
	}
	
	//Sirve para obtener la metadata de la tabla Clientes
	public ArrayList<String> getClientesLabels(){
		ArrayList<String> labels = new ArrayList<String>();
		JSONArray result = ConexionPostgres.getInstancia().executeQuery("SELECT * FROM metadata_clientes;");
		for (int i=0; i<result.length(); i++){
			try {
				JSONObject object = result.getJSONObject(i);
				labels.add(object.getString("texto"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block				
				e.printStackTrace();
				return null;
			}
			
		}
		
		return labels;
	}
	
	
	public boolean updateCliente(String query){		
		return ConexionPostgres.getInstancia().executeUpdate(query);
	}
	
	
	
	/**
	 * Obtiene el ID siguiente después del máximo ID existente en la tabla.
	 * @param tableName	Tabla a evaluar.
	 * @return	ID siguiente.
	 * @throws Exception Cuando no encuentra el máximo en la tabla.
	 */
	public int getNextID(String tableName) throws Exception{
		String query = "SELECT max(id" + tableName + ") FROM " + tableName + ";";
		JSONArray jsonResult = ConexionPostgres.getInstancia().executeQuery(query);		
		if (jsonResult == null)
			throw new Exception("ERROR.-Error al ejecutar query.");
		String idString = jsonResult.getJSONObject(0).getString("max");
		int idInt = Integer.parseInt(idString);
		return idInt+1;
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
	


	/**
	 * Método para parsear una fecha dada como string.
	 * @param dateString
	 * @return
	 */
	public boolean validateDate(String dateString){		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date date = null;
        try {
            date = formatter.parse(dateString);
            return true;
        } catch (ParseException e) {        	
			return false;			            
        }
	}
	
	
	/**
	 * Método para parsear un correo dado como string.
	 * @param emailString
	 * @return
	 */
	public boolean validateEmail(String emailString){
		Pattern pattern;
		Matcher matcher;
		
		String EMAIL_PATTERN =  "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";	 	
		pattern = Pattern.compile(EMAIL_PATTERN);		
		matcher = pattern.matcher(emailString);
		return matcher.matches();
		
	}
	
	public boolean validateGenre(String genre){
		Pattern pattern;
		Matcher matcher;		
		String EMAIL_PATTERN =  "m|M|f|M";	 	
		pattern = Pattern.compile(EMAIL_PATTERN);		
		matcher = pattern.matcher(genre);
		return matcher.matches();
	}
	
	
	

	

	
	
}

