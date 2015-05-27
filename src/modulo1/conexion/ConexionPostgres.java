package modulo1.conexion;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConexionPostgres 
{
	private static ConexionPostgres instancia;
	public static ConexionPostgres getInstancia()
	{
		if(instancia==null)
			instancia = new ConexionPostgres();
		return instancia;
	}
	
	Connection connection;
	
	public ConexionPostgres()
	{
		conectar();
	}	
		
	// public boolean conectar() throws ClassNotFoundException, SQLException{
	public boolean conectar()
	{
		System.out.println("--- PostgreSQL " + " JDBC Connection Testing ---");
		try
		{
			Class.forName("org.postgresql.Driver");
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include it in your library path!");
			e.printStackTrace();
			return false;
		}		
		
		System.out.println("PostgreSQL JDBC Driver Registered!");
		connection = null;
		try
		{
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ProyectoCRM", "postgres", "cesarlui93");
			//connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ProyectoCRM", "postgres", "henzer");
			//connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/CRM-Clientes", "postgres", "ESPE125rare");
		}
		catch (SQLException e)
		{
			System.out.println("Connection Failed! Check output console..!");
			e.printStackTrace();
			return false;
		}
		
		if (connection != null) 
		{
			System.out.println("You made it, take control your database now!");	
			return true;
		}
		return false;
	}	
	
	
	/**
	 * A operation on the database without result. Executes the given SQL statement,
	 * which may be an INSERT, UPDATE, or DELETE statement or an SQL statement that
	 * returns nothing, such as an SQL DDL statement.
	 * @param query Query to execute.
	 * @return True if database was updated successfully. False, otherwise.
	 */
	public boolean executeUpdate(String query)
	{
		Statement st;
		ResultSet rs;
		try {
			st = connection.createStatement();
			st.executeUpdate(query);			
			st.close();
			System.out.println("Database updated successfully..!");			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * A operation on the database with result. Returns a Result Set.
	 * @param query Query to execute.
	 * @return
	 */
	public JSONArray executeQuery(String query){
		Statement statement;
		ResultSet resultSet;
		JSONArray jsonResult;
		try {
			statement = connection.createStatement();		// Create a statement.
			resultSet = statement.executeQuery(query);		// Execute query.			
			jsonResult = new JSONArray();					// Instance JSON result.
			// Iterate result set.
			while (resultSet.next()){
				JSONObject jsonRow = new JSONObject();
				int columnCount =  resultSet.getMetaData().getColumnCount();
				for (int i=1; i<columnCount+1; i++){
					String key = resultSet.getMetaData().getColumnLabel(i);
					String value = resultSet.getString(i);
					try {
						jsonRow.put(key, value);						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
					//System.out.println("columnLabel: " + resultSet.getMetaData().getColumnLabel(i));					
					//System.out.println("content: " + resultSet.getString(i));
					//System.out.println("type: " + resultSet.getMetaData().getColumnTypeName(i));
				}
				jsonResult.put(jsonRow);
			}
			resultSet.close();
			statement.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			// e.printStackTrace();
			return null;
		}
		
		return jsonResult;
	}
	
	/**
	 * Close Connection.
	 */
	public void closeDatabase(){
		try {
			connection.close();
			System.out.println("Database closed successfully..!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block			
			System.out.println("Unexpected Exception while trying to close conection..!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Get columns name for a specific table.
	 * @param tableName Table name to inspect.
	 * @return ArrayList with the columns name.
	 */
	public ArrayList<String> getTableColumns(String tableName){		
		ArrayList<String> columns = null;
	    DatabaseMetaData metadata;
		try {
			metadata = connection.getMetaData();
		    ResultSet resultSet = metadata.getColumns(null, null, tableName, null);
		    columns = new ArrayList<String>();
		    while (resultSet.next()) {
		    	String name = resultSet.getString("COLUMN_NAME");
		    	//String type = resultSet.getString("TYPE_NAME");
		    	//int size = resultSet.getInt("COLUMN_SIZE");
		    	columns.add(name);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return columns;
	}
	
	//INSERT INTO clientes VALUES(0, 'Cesar Anibal', 'Luis Alvarado', 'Las Charcas, Z11', 'cesarluis93@gmail.com', '22/02/93', 'M', '29-365686', 0, 0, 0, 0);
	
}
