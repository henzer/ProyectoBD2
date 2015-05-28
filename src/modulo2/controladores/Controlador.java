package modulo2.controladores;

import java.util.Arrays;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.util.JSON;

import modulo2.conexion.Conexion;

public class Controlador {
	private Conexion conexion;
	public static Controlador instancia;
	public JSON clienteActual;
	
	//Patron singleton
	public static Controlador getInstancia(){
		if(instancia==null)
			instancia = new Controlador();
		return instancia;
	}
	
	//Constructor
	public Controlador(){
		conexion = Conexion.getInstancia();
	}

	//GETTERS Y SETTERS
	public JSON getClienteActual() {
		return clienteActual;
	}

	public void setClienteActual(JSON clienteActual) {
		this.clienteActual = clienteActual;
	}
	
	public DefaultTableModel getInfoTable(BasicDBObject query, boolean[] campVisibles){
		List<String> headers = Arrays.asList("ID", "TEXTO", "RETWEETS", "MENCIONADOS", "FECHA");
		List<String> nameColumns = Arrays.asList("tweet_ID", "tweet_text", "retweet_count", "tweet_mentioned_count", "tweet_date");
		
		DefaultTableModel model = new DefaultTableModel();
		BasicDBObject campos = new BasicDBObject();
		campos.append("_id", 0);
		int size = 0;
		for(int i=0; i<5;i++){
			if(campVisibles[i]){
				size++;
				model.addColumn(headers.get(i));
				campos.append(nameColumns.get(i), 1);
			}
		}
		System.out.println(query);
		System.out.println(campos);
		
		JSONArray result = conexion.search(query, campos);
		System.out.println("Resultado: " + result);
		
		
		for(int i= 0; i<result.length(); i++){
			try {
				JSONObject elemento = (JSONObject)result.get(i);
				Object [] row = new Object[size];
				int f = 0;
				for(int j=0; j<5;j++){
					if(campVisibles[j]){
						row[f] = elemento.get(nameColumns.get(j));
						f++;
					}
				}
				model.addRow(row);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return model;
		
	}
	
	

}
