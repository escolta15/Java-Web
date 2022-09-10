/*********************************************************************
* Class Name: PlatoPedido.java
* Author/s name: Pablo Mora
* Release/Creation date: 30-06-2018
* Class version: 1.0
* Class description: Clase del objeto PlatoPedido
*********************************************************************/
package diseno.telecomandas.dominio;

import org.json.JSONObject;

import diseno.telecomandas.etiquetas.BSONable;
import diseno.telecomandas.etiquetas.JSONable;

@BSONable
public class PlatoPedido{
	@JSONable(campo = "_id", nombre = "idPlato")//tenemos que poner que son campos opcionales
	@BSONable(campo = "_id", nombre = "idPlato", OnDeleteCascade = true)
	private Plato plato;
	@JSONable
	private int unidades;
	
	public PlatoPedido(){
		
	}
	
	//Constructor
	public PlatoPedido(Plato plato, int unidades) {
		this.plato=plato;
		this.unidades=unidades;
	}
	
	//Convierte en JSON la platopedido
	public JSONObject toJSONObject() {
		JSONObject jso=this.plato.toJSONObject();
		jso.put("unidades", this.unidades);
		return jso;
	}

	public Plato getPlato(){
		return this.plato;
	}
	
	public int getUnidades(){
		return this.unidades;
	}
	
	public void setPlato(Plato plato){
		this.plato=plato;
	}
	
	public void setUnidades(int unidades){
		this.unidades=unidades;
	}
	
}
