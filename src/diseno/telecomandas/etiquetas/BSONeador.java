/*********************************************************************
* Class Name: BSONeador.java
* Author/s name: Pablo Mora
* Release/Creation date: 30-06-2018
* Class version: 1.0
* Class description: Convierte los objetos en BSON
*********************************************************************/
package diseno.telecomandas.etiquetas;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonString;
import org.bson.BsonValue;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import diseno.telecomandas.dao.MongoBroker;
import diseno.telecomandas.dominio.Mesa;
import diseno.telecomandas.dominio.Plato;
import diseno.telecomandas.dominio.PlatoPedido;

public class BSONeador {
	
	public static void insert(Object objeto) throws Exception{
		/*
		 * 1.Traducir objecto a bso
		 * 1.1 Por reflexion,leer cada campo del objeto, leer su valor y colocarlo en bso
		 * 2. Acceder a una coleccion que se llame igual que la clase del objeto
		 * 3. A traves del MongoBroker, insertar el bso que se acaba de construir en la coleccion
		 */
		Class<?> clase=objeto.getClass();
		Field[] campos=clase.getDeclaredFields();
		BsonDocument bso=new BsonDocument();
		for(int i=0; i<campos.length; i++) {
			Field campo=campos[i];
			campo.setAccessible(true);
			Object valor=campo.get(objeto);
			if(valor==null)
				continue;
			BSONable anotacion =campo.getAnnotation(BSONable.class);
			if (anotacion==null)
				bso.append(campo.getName(), getBsonValue(valor));
			else {
				String nombreCampoAsociado=anotacion.campo();
				String nombreNuevo=anotacion.nombre();
				Field campoAsociado=valor.getClass().getDeclaredField(nombreCampoAsociado);
				campoAsociado.setAccessible(true);
				Object valorCampoAsociado=campoAsociado.get(valor);
				bso.append(nombreNuevo, getBsonValue(valorCampoAsociado));
			}
		}
		
		MongoCollection<BsonDocument> coleccion=MongoBroker.get().getCollection(clase.getName());
		coleccion.insertOne(bso);
	}
	
	private static BsonValue getBsonValue(Object valorDelCampo)  throws Exception{
		Class <? extends Object> tipo = valorDelCampo.getClass();
		
		if(tipo==int.class || tipo==Integer.class)
			return new BsonInt32((int) valorDelCampo);
		if(tipo==long.class || tipo==Long.class)
			return new BsonInt64((long) valorDelCampo);
		if(tipo==double.class || tipo==Double.class)
			return new BsonDouble((double) valorDelCampo);
		if(tipo==String.class)
			return new BsonString(valorDelCampo.toString());
		if(tipo.isAnnotationPresent(BSONable.class)) {
			Field[] campos=valorDelCampo.getClass().getDeclaredFields();
			BsonDocument bso=new BsonDocument();
			for(int i=0;i<campos.length;i++) {
				Field campo=campos[i];
				campo.setAccessible(true);
				bso.put(campo.getName(), getBsonValue(campo.get(valorDelCampo)));
			}
			return bso;
		}
		return getBsonValue(valorDelCampo);
	}

	public static ConcurrentHashMap<Object, Object> load(Class<?> clase) throws Exception{
		ConcurrentHashMap<Object, Object> result = new ConcurrentHashMap<>();
		MongoCollection<BsonDocument> coleccion=MongoBroker.get().getCollection(clase.getName());
		MongoCursor<BsonDocument> fi=coleccion.find().iterator();
		while (fi.hasNext()) {
			BsonDocument bso = fi.next();
			Object objeto=getObject(clase,bso);
			result.put(getId(bso), objeto);
		}
		return result;
	}

	private static Object getId(BsonDocument bso) {
		if (bso.get("_id").isString())
			return bso.get("_id").asString().getValue();
		if (bso.get("_id").isDouble())
			return bso.get("_id").asDouble().getValue();
		return null;
	}

	private static Object getObject(Class<?> clase, BsonDocument bso) throws Exception{
		Object result=clase.newInstance();//aqui llamamos al constructor sin parametros
		Iterator<String> nombresDeLosCampos=bso.keySet().iterator();
		while (nombresDeLosCampos.hasNext()) {
			String nombreDeCampo=nombresDeLosCampos.next();
			Field campo=clase.getDeclaredField(nombreDeCampo);
			if (campo==null)
				continue;
			campo.setAccessible(true);
			BsonValue valorDelBson=bso.get(nombreDeCampo);
			set(campo, result, valorDelBson);
		}
		return null;
	}
	
	private static void set(Field campo, Object result, BsonValue valorDelBson) throws Exception{
		if (valorDelBson.isString()) {
			campo.set(result, valorDelBson.asString().getValue());//result.campo="..."
			return;
		}
		if (valorDelBson.isDouble()) {
			campo.setDouble(result, valorDelBson.asDouble().getValue());
			return;
		}
	}
	
	public static void update(Object objeto, Object... nombresValores) throws Exception{//con los puntos suspensivos seria igual que una array
		if(nombresValores.length==0 || nombresValores.length % 2!=0)
			throw new IllegalArgumentException("Esperaba un número par de parámetros");
		
		BsonDocument nuevosValores=new BsonDocument();
		for(int i=0; i<nombresValores.length;i++) {
			nuevosValores.put(nombresValores[i].toString(), getBsonValue(nombresValores[i+1]));
			i++;
		}
		Class<?> clase = objeto.getClass();
		Field campoId=clase.getDeclaredField("_id");
		campoId.setAccessible(true);
		Object valorId=campoId.get(objeto);
		BsonDocument criterio = new BsonDocument();
		criterio.put("_id", getBsonValue(valorId));
		System.out.println(criterio);
		System.out.println(nuevosValores);
		MongoCollection<BsonDocument> coleccion = MongoBroker.get().getCollection(clase.getName());
		coleccion.replaceOne(criterio, nuevosValores);
	}
	
	public static void delete(Object objeto) throws Exception{//con los puntos suspensivos seria igual que una array
		Class<?> clase = objeto.getClass();
		Field campoId=clase.getDeclaredField("_id");
		campoId.setAccessible(true);
		Object valorId=campoId.get(objeto);
		BsonDocument criterio = new BsonDocument();
		criterio.put("_id", getBsonValue(valorId));
		System.out.println(criterio);
		MongoCollection<BsonDocument> coleccion = MongoBroker.get().getCollection(clase.getName());
		coleccion.deleteOne(criterio);
		
		BSONable anotacion=clase.getAnnotation(BSONable.class);
		if (anotacion==null)
			return;
		String nombreClaseDependiente=anotacion.claseDependiente();
		Class<?> claseDependiente=Class.forName(nombreClaseDependiente);
		Vector<Field> camposDependientes=findCampo(claseDependiente, clase);
		if (camposDependientes==null)
			return;
		//@BSONable(campo = "_id", nombre = "idPlato", OnDeleteCascade = true)
		//private Plato plato;
		for (Field campoDependiente : camposDependientes) {
			//Coger la anotacion, ver si tiene OnDeleteCascada a true.
			//Si la tiene, leer valor "nombre" de la anotacion (dara "idPlato")
			//Ir a la coleccion PlatoPedido y hacer delete de todos los objetos
			//cuyo idPlato sea el _id del objeto principal (parametro objeto)
		}
	}
	
	private static Vector<Field> findCampo(Class<?> claseDependiente, Class<?> clase) {
		Vector<Field> resultado=null;
		Field[] camposClaseDependiente=claseDependiente.getDeclaredFields();
		for(int i=0; i<camposClaseDependiente.length; i++) {
			Field campo=camposClaseDependiente[i];
			if (campo.getType()==clase && campo.getAnnotation(BSONable.class)!=null) {
				if (resultado==null)
					resultado=new Vector<>();
				resultado.add(campo);
			}
		}
		return null;
	}

	public static void main(String[] args) {
		Plato plato=new Plato("25","Tortilla de gambas",6.5);
		PlatoPedido platoPedido=new PlatoPedido(plato,2);
		try {
			BSONeador.insert(plato);
			//BSONeador.insert(platoPedido);
			//BSONeador.update(plato,"nombre","Tortilla de pescao");
			// Comprobar que en plato pedido se ha cambiado a 30
			BSONeador.delete(plato);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*Plato plato=new Plato("27","Tortilla",6.50);
		try {
			BSONeador.insert(plato);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*try {
			Enumeration<Object> platos = BSONeador.load(Plato.class).elements();
			while(platos.hasMoreElements()) {
				Plato plato=(Plato) platos.nextElement();
				System.out.println(plato.getId());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
}
