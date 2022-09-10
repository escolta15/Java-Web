/*********************************************************************
* Class Name: Categoria.java
* Author/s name: Pablo Mora
* Release/Creation date: 30-06-2018
* Class version: 1.0
* Class description: Clase del objeto categoria
*********************************************************************/
package diseno.telecomandas.dominio;

import org.json.JSONObject;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;

import diseno.telecomandas.dao.DAOPlato;
import diseno.telecomandas.dominio.Plato;
import diseno.telecomandas.etiquetas.BSONable;

@BSONable
public class Categoria{
	private String _id;
	private String nombre;
	private ConcurrentHashMap<String, Plato> platos;
	private JSONArray jsaplatos;
	
	public Categoria(){
		
	}
	
	//Constructor
	public Categoria(String _id, String nombre) {
		this._id=_id;
		this.nombre=nombre;
		this.jsaplatos=new JSONArray();
		this.platos=DAOPlato.load(_id);
		Enumeration<Plato> ePlatos = this.platos.elements();
		while (ePlatos.hasMoreElements()) {
			this.jsaplatos.put(ePlatos.nextElement().toJSONObject());
		}
	}

	public Plato find(String idPlato) {
		return this.platos.get(idPlato);
	}
	
	public String get_id(){
		return this._id;
	}
	
	public String getNombre(){
		return this.nombre;
	}
	
	public ConcurrentHashMap<String, Plato> getPlatos(){
		return this.platos;
	}
	
	public JSONArray getjsaplatos(){
		return jsaplatos;
	}
	
	public void set_id(String _id){
		this._id=_id;
	}
	
	public void setNombre(String nombre){
		this.nombre=nombre;
	}
	
	//Convierte en JSON la categoria
	public JSONObject toJSONObject() {
		JSONObject jso=new JSONObject();
		jso.put("_id", this._id);
		jso.put("nombre", this.nombre);
		jso.put("platos", this.jsaplatos);
		return jso;
	}
	
}
