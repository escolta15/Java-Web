/*********************************************************************
* Class Name: ServidorWS.java
* Author/s name: Pablo Mora
* Release/Creation date: 30-06-2018
* Class version: 1.0
* Class description: Funcionalidad del Web Socket del Servidor
*********************************************************************/
package diseno.telecomandas.ws;

import java.util.Enumeration;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONArray;
import org.json.JSONObject;

import diseno.telecomandas.dominio.Manager;
import diseno.telecomandas.dominio.PlatoPedido;
import diseno.telecomandas.etiquetas.JSONable;

@ServerEndpoint(value="/ServidorWS")
public class ServidorWS {
	private static ConcurrentHashMap<String, Session> sessions=new ConcurrentHashMap<>();

	@OnOpen
	public void onOpen(Session session) {
		String sessionID = session.getId();
		sessions.put(sessionID, session);
	}

	@OnMessage
	public void onMessage(Session session, String msg){
		JSONObject jso = new JSONObject(msg);
		String tipo = jso.getString("tipo");
		if(tipo.equals("platoListo")) {
			System.out.println(jso);
			platoListo(jso);
		}
	}
	
	@OnClose
	public void close(Session session){
		String sessionID = session.getId();
		sessions.remove(sessionID, session);
	}

	//Envia a cocina los platos para cocinar
	public static void solicitarPlatos(int id, JSONArray platos) {
		JSONObject mensaje = new JSONObject();
		mensaje.put("type", "SolicitudDePlatos");
		mensaje.put("id", id);
		mensaje.put("platos", platos);
		Enumeration<Session> sesiones = sessions.elements();
		while (sesiones.hasMoreElements()) {
			Session sesion=sesiones.nextElement();
			sesion.getAsyncRemote().sendText(mensaje.toString());//con el getAsyncRemote se envia el contenido
		}
	}
	
	//Se reciben los platos que han sido ya preparados
	public static void platoListo(JSONObject jso) {
		Manager.get().platoListo(jso);
	}

}
