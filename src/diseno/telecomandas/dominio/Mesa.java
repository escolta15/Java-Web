/*********************************************************************
* Class Name: Auxi.java
* Author/s name: Pablo Mora
* Release/Creation date: 30-06-2018
* Class version: 1.0
* Class description: Coge la hora
*********************************************************************/
package diseno.telecomandas.dominio;

import org.json.JSONObject;

import diseno.telecomandas.etiquetas.BSONable;
import diseno.telecomandas.etiquetas.JSONable;

@BSONable
public class Mesa{
	private Comanda comanda;
	@JSONable
	private Integer _id;
	
	public Mesa(){
		
	}
	
	//Constructor
	public Mesa(int id) {
		this._id=id;
	}
	
	//Convierte en JSON la mesa
	public JSONObject toJSONObject() {
		JSONObject jso=new JSONObject();
		jso.put("_id", this._id);
		jso.put("estado", comanda==null ? "Libre" : "Ocupada");
		jso.put("comanda", this.comanda);
		return jso;
	}
	
	//Por si quisieramos obtener el estado al cargar las mesas
	/*public JSONObject toJSONObject(String estado) {
		JSONObject jso=new JSONObject();
		jso.put("_id", this._id);
		jso.put("estado", estado);
		jso.put("comanda", this.comanda);
		return jso;
	}*/
	
	//La comanda esta vacia si la mesa esta libre
	public boolean estaLibre() {
		return comanda==null;
	}

	//Abre la mesa y una comanda
	public boolean abrir() throws Exception {
		if (comanda!=null)
			throw new Exception("La mesa ya esta abierta. Elige otra");
		comanda=new Comanda();//al abrir es donde se crea la nueva comanda no en el constructor
		return true;
	}

	//Cierra la mesa y la comanda
	public boolean cerrar() throws Exception {
		if (comanda==null)
			throw new Exception("La mesa ya esta cerrada");
		comanda.cerrar();
		comanda=null;
		return true;
	}

	//Anade el plato a la comanda
	public void addToComanda(Plato plato, int unidades) {
		this.comanda.add(plato, unidades);
	}

	//Devuelve la mesa en JSONObject
	public JSONObject estado() {
		JSONObject jso=new JSONObject();
		jso.put("id", this._id);
		if (comanda==null) 
			jso.put("estado", "Libre");
		else {
			jso.put("estado", "Ocupada");
			jso.put("comanda", this.comanda.toJSONObject());
		}
		return jso;
	}

	public Comanda getComanda(){
		return this.comanda;
	}
	
	public Integer get_id(){
		return this._id;
	}
	
	public void setComanda(Comanda comanda){
		this.comanda=comanda;
	}
	
	public void set_id(Integer _id){
		this._id=_id;
	}
	
}