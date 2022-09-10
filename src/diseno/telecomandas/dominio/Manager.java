/*********************************************************************
* Class Name: Manager.java
* Author/s name: Pablo Mora
* Release/Creation date: 30-06-2018
* Class version: 1.0
* Class description: Clase del objeto manager
*********************************************************************/
/*
 * PATRONES UTILIZADOS EN LA CLASE MANAGER.JAVA
 * Singleton
 * Facade
 * 
 */
package diseno.telecomandas.dominio;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import diseno.telecomandas.dao.DAOMesa;
import diseno.telecomandas.etiquetas.JSONable;
import diseno.telecomandas.dao.DAOCategoria;
import diseno.telecomandas.ws.ServidorWS;

public class Manager {
	private ConcurrentHashMap<Integer, Mesa> mesas;
	private ConcurrentHashMap<String, Categoria> categorias;
	private JSONArray jsacategorias;
	@JSONable
	private JSONArray platosEntregar;
	
	//Constructor
	private Manager() {
		cargarMesas();
		cargarCategorias();
	}
	
	private void cargarMesas(){
		mesas=DAOMesa.load();
		platosEntregar = new JSONArray();
	}
	
	private void cargarCategorias() {
		categorias=DAOCategoria.load();
		this.jsacategorias=new JSONArray();
		Enumeration<String> keys=categorias.keys();
		while (keys.hasMoreElements()) {
			String key=keys.nextElement();
			Categoria categoria=categorias.get(key);
			this.jsacategorias.put(categoria.toJSONObject());
		}
	}
	
	private static class ManagerHolder {
		static Manager singleton=new Manager();
	}
	
	public static Manager get() {
		return ManagerHolder.singleton;
	}

	public void add(Mesa mesa) {
		this.mesas.put(mesa.get_id(), mesa);
	}
	
	public void add(Categoria categoria) {
		this.categorias.put(categoria.get_id(), categoria);
	}

	//Se ejecuta cuando recibe una comanda
	public void recibirComanda(int idMesa, JSONArray platos) throws Exception {
		Mesa mesa=mesas.get(idMesa);
		if (mesa.estaLibre())
			throw new Exception("La mesa " + idMesa + " esta libre. Abrela primero");
		for (int i=0; i<platos.length(); i++) {
			JSONObject jsoPlato=platos.getJSONObject(i);
			String idCategoria=jsoPlato.getString("idCategoria");
			String idPlato=jsoPlato.getString("idPlato");
			int unidades=jsoPlato.getInt("unidades");
			Categoria categoria=this.categorias.get(idCategoria);
			Plato plato=categoria.find(idPlato);//aqui encontramos el plato para sacar el precio
			mesa.addToComanda(plato, unidades);//aqui anadimos los platos a la comanda de la mesa
		}
		DAOMesa.insertComanda(mesa);
		ServidorWS.solicitarPlatos(idMesa,platos);
	}
	
	//Devuelve las categorias
	public JSONArray getCategorias() {
		return this.jsacategorias;
	}
	
	//Devuelve los platos de las categorias
	public JSONArray getPlatosDeCategoria(String idCategoria) {
		for (int i=0; i<this.jsacategorias.length(); i++) {
			JSONObject jso=this.jsacategorias.getJSONObject(i);
			if (jso.getString("_id").equals(idCategoria))
				return jso.getJSONArray("platos");
		}
		return null;
	}
	
	//Envia al cliente los platos que se pueden entregar
	public JSONArray getPlatosListos(){
		JSONArray result=platosEntregar;
		platosEntregar=new JSONArray();
		return result;
	}
	
	//Abre la mesa
	public void abrirMesa(int id) throws Exception {
		Mesa mesa=mesas.get(id);
		boolean flag=mesa.abrir();
		if (flag == true) {
			DAOMesa.update(id,"Ocupada");
		}
	}
	
	//Cierra la mesa
	public void cerrarMesa(int id) throws Exception {
		Mesa mesa=mesas.get(id);
		boolean flag=mesa.cerrar();
		if (flag == true) {
			DAOMesa.update(id,"Libre");
		}
	}
	
	//Envia al cliente las mesas.
	public JSONArray getMesas() {
		JSONArray result=new JSONArray();
		Enumeration<Mesa> eMesas = this.mesas.elements();
		Mesa mesa;
		while (eMesas.hasMoreElements()) {
			mesa=eMesas.nextElement();
			result.put(mesa.toJSONObject());
			//Por si quisieramos obtener el estado al cargar las mesas
			//String estado=DAOMesa.leerEstado(mesa.get_id());
			//result.put(mesa.toJSONObject(estado));
		}
		return result;
	}
	
	//Devuelve el estado actual de la mesa
	public JSONObject getEstadoMesa(int id) {
		return this.mesas.get(id).estado();
	}
	
	//Cuando un plato se realiza, se anade para cuando el cliente lo pida
	public void platoListo(JSONObject data){
		int idMesa = data.getInt("id");
		JSONObject plato = data.getJSONObject("plato");
		JSONObject jso=new JSONObject();
		jso.put("id", idMesa);
		jso.put("plato", plato);
		platosEntregar.put(jso);
		System.out.println(jso);
	}
	
	public void confirmarPlatos(JSONObject data) {
		System.out.println("Confirmada");
	}
	
}
