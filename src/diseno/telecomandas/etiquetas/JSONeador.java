/*********************************************************************
* Class Name: JSONeador.java
* Author/s name: Pablo Mora
* Release/Creation date: 30-06-2018
* Class version: 1.0
* Class description: Convierte los objetos en JSON
*********************************************************************/
package diseno.telecomandas.etiquetas;

import java.lang.reflect.Field;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;

import diseno.telecomandas.dominio.Comanda;
import diseno.telecomandas.dominio.Plato;

public class JSONeador {
	@SuppressWarnings("rawtypes")
	public static JSONObject toJSONObject(Object object) {
		JSONObject jso=new JSONObject();
		Class<?> clase=object.getClass();
		Field[] campos = clase.getDeclaredFields();
		for(Field campo : campos) {
			if(!campo.isAnnotationPresent(JSONable.class))
				continue;
			campo.setAccessible(true);
			try {
				String nombre=campo.getName();
				Object valor = campo.get(object);
				if(Collection.class.isAssignableFrom(campo.getType())) {
					Collection<?> coleccion=(Collection) campo.get(object);
					JSONArray jsa=new JSONArray();
					for (Object objetito : coleccion) {
						jsa.put(toJSONObject(objetito));
					}
					jso.put(nombre, jsa);
				}else if(isJSONeable(valor)) {
					JSONable anotacion = campo.getAnnotation(JSONable.class);
					if (anotacion.nombre().length()== 0 && anotacion.campo().length()==0)
						jso.put(nombre, toJSONObject(valor));
					else {
						String nombreCampoAsociado=anotacion.campo();// "_id"
						String nombreNuevo=anotacion.nombre();// "idPlato"
						Field campoAsociado = valor.getClass().getDeclaredField(nombreCampoAsociado);
						campoAsociado.setAccessible(true);
						Object valorCampoAsociado=campoAsociado.get(valor);
						jso.put(nombreNuevo, valorCampoAsociado);
					}
				}else {
					jso.put(nombre, valor);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jso;
	}
	
	private static boolean isJSONeable(Object object) {
		Class<?> clase=object.getClass();
		Field[] campos=clase.getDeclaredFields();
		for(Field campo : campos) {
			if(campo.isAnnotationPresent(JSONable.class))
				return true;
		}
		return false;
	}

	public static void main(String[] args) {
		Plato tortilla=new Plato("27","Tortilla",6.50);
		System.out.println(JSONeador.toJSONObject(tortilla));
		
		Comanda comanda=new Comanda();
		comanda.add(tortilla,3);
		
		System.out.println(JSONeador.toJSONObject(comanda));
	}
}
