/*********************************************************************
* Class Name: Plato.java
* Author/s name: Pablo Mora
* Release/Creation date: 30-06-2018
* Class version: 1.0
* Class description: Clase del objeto plato
*********************************************************************/
package diseno.telecomandas.dominio;

import org.json.JSONObject;

import diseno.telecomandas.etiquetas.BSONable;
import diseno.telecomandas.etiquetas.JSONable;

@BSONable(claseDependiente = "PlatoPedido")
public class Plato{
	@JSONable
	private String _id;
	@JSONable
	private String nombre;
	@JSONable
	private double precio;
	
	public Plato(){
		
	}
	
	//Constructor
	public Plato(String _id, String nombre, double precio) {
		this._id=_id;
		this.nombre=nombre;
		this.precio=precio;
	}
	
	//Convierte en JSON el plato
	public JSONObject toJSONObject() {
		JSONObject jso=new JSONObject();
		jso.put("_id", _id);
		jso.put("nombre", nombre);
		jso.put("precio", precio);
		return jso;
	}

	public String getId(){
		return this._id;
	}
	
	public String getNombre(){
		return this.nombre;
	}
	
	public double getPrecio(){
		return this.precio;
	}
	
	public void set_id(String _id){
		this._id=_id;
	}
	
	public void setNombre(String nombre){
		this.nombre=nombre;
	}
	
	public void setPrecio(double precio){
		this.precio=precio;
	}
	
}
