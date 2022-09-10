/*********************************************************************
* Class Name: DAOCategoria.java
* Author/s name: Pablo Mora
* Release/Creation date: 30-06-2018
* Class version: 1.0
* Class description: Carga las categorias de la BD
*********************************************************************/
package diseno.telecomandas.dao;

import java.util.concurrent.ConcurrentHashMap;

import org.bson.BsonDocument;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import diseno.telecomandas.dominio.Categoria;

public class DAOCategoria {

	//Carga las categorias
	public static ConcurrentHashMap<String, Categoria> load() {
		MongoCollection<BsonDocument> categorias = MongoBroker.get().getCollection("categorias");
		ConcurrentHashMap<String, Categoria> result=new ConcurrentHashMap<>();
		MongoCursor<BsonDocument> fiCategorias = categorias.find().iterator();
		while (fiCategorias.hasNext()) {
			BsonDocument bsonCategoria = fiCategorias.next();
			String _id = bsonCategoria.getObjectId("_id").getValue().toHexString();
			String nombre=bsonCategoria.getString("nombre").getValue();
			Categoria categoria=new Categoria(_id, nombre);
			result.put(_id, categoria);
		}
		return result;
	}

}
