/*********************************************************************
* Class Name: Comanda.java
* Author/s name: Pablo Mora
* Release/Creation date: 30-06-2018
* Class version: 1.0
* Class description: Clase del objeto comanda
*********************************************************************/
package diseno.telecomandas.dominio;

import org.json.JSONObject;
import java.util.Vector;
import org.json.JSONArray;
import diseno.telecomandas.etiquetas.BSONable;
import diseno.telecomandas.etiquetas.JSONable;

@BSONable
public class Comanda{
	@JSONable
	private Vector<PlatoPedido> platopedido;
	@JSONable
	private long horaApertura;
	@JSONable
	private long horaCierre;
	
	//Constructor
	public Comanda(){
		this.horaApertura=System.currentTimeMillis();
		this.platopedido=new Vector<>();
	}
	
	public void cerrar() {
		this.horaCierre=System.currentTimeMillis();
	}

	public Vector<PlatoPedido> getPlatopedido(){
		return this.platopedido;
	}
	public long getHoraApertura(){
		return this.horaApertura;
	}
	public long getHoraCierre(){
		return this.horaCierre;
	}
	
	public void add(Plato plato, int unidades) {
		PlatoPedido platoPedido=new PlatoPedido(plato, unidades);
		this.platopedido.add(platoPedido);
	}
	
	public void setHoraApertura(long horaApertura){
		this.horaApertura=horaApertura;
	}
	public void setHoraCierre(long horaCierre){
		this.horaCierre=horaCierre;
	}
	
	//Convierte en JSON la comanda
	public JSONObject toJSONObject() {
		JSONObject jso=new JSONObject();
		jso.put("horaApertura", Auxi.getHora(this.horaApertura));
		JSONArray platosPedidos=new JSONArray();
		for (PlatoPedido pp : this.platopedido)
			platosPedidos.put(pp.toJSONObject());
		jso.put("platos", platosPedidos);
		return jso;
	}
	
}