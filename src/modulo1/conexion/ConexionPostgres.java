package modulo1.conexion;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionPostgres {

	Connection connection;
	
	public ConexionPostgres(){
		conectar();
	}	
		
	// public boolean conectar() throws ClassNotFoundException, SQLException{
	public boolean conectar(){
		System.out.println("--- PostgreSQL " + " JDBC Connection Testing ---");
		try{
			Class.forName("org.postgresql.Driver");
		}
		catch (ClassNotFoundException e){
			System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
			e.printStackTrace();
			return false;
		}		
		
		System.out.println("PostgreSQL JDBC Driver Registered!");
		connection = null;
		try{
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ProyectoCRM", "postgres", "IngCienComp93");
		}
		catch (SQLException e){
			System.out.println("Connection Failed! Check output console..!");
			e.printStackTrace();
			return false;
		}
		
		if (connection != null) {
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
	public boolean executeUpdate(String query){
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
	public boolean executeQuery(String query){
		Statement st;
		ResultSet rs;
		try {
			st = connection.createStatement();
			rs = st.executeQuery(query);			
			while (rs.next()){
			   System.out.print("Column 1 returned ");
			   System.out.println(rs.getString(1));
			}
			rs.close();
			st.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			// e.printStackTrace();
			return false;
		}
		
		return true;
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
}
