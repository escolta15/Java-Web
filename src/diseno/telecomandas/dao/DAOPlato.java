/*********************************************************************
* Class Name: DAOPlato.java
* Author/s name: Pablo Mora
* Release/Creation date: 30-06-2018
* Class version: 1.0
* Class description: Carga los platos de la categoria de la BD
*********************************************************************/
package diseno.telecomandas.dao;

import java.util.concurrent.ConcurrentHashMap;

import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import diseno.telecomandas.dominio.Plato;

public class DAOPlato {

	//Carga los platos de la categoria
	public static ConcurrentHashMap<String, Plato> load(String idCategoria) {
		MongoCollection<BsonDocument> platos = MongoBroker.get().getCollection("platos");
		ConcurrentHashMap<String, Plato> result=new ConcurrentHashMap<>();
		BsonDocument criterio=new BsonDocument();
		criterio.put("idCategoria", new BsonObjectId(new ObjectId(idCategoria)));
		MongoCursor<BsonDocument> fiPlatos = platos.find(criterio).iterator();
		while (fiPlatos.hasNext()) {
			BsonDocument bsonPlato = fiPlatos.next();
			String _id = bsonPlato.getObjectId("_id").getValue().toHexString();
			String nombre=bsonPlato.getString("nombre").getValue();
			double precio=bsonPlato.getDouble("precio").getValue();
			Plato plato=new Plato(_id, nombre, precio);
			result.put(_id, plato);
		}
		return result;
	}

}
