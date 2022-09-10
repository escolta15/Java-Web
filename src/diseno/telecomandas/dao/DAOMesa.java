/*********************************************************************
* Class Name: DAOMesa.java
* Author/s name: Pablo Mora
* Release/Creation date: 30-06-2018
* Class version: 1.0
* Class description: Carga las mesas de la BD y las actualiza.
Tambien inserta las comandas
*********************************************************************/
package diseno.telecomandas.dao;

import java.util.concurrent.ConcurrentHashMap;

import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDateTime;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonString;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;

import diseno.telecomandas.dominio.Auxi;
import diseno.telecomandas.dominio.Comanda;
import diseno.telecomandas.dominio.Mesa;
import diseno.telecomandas.dao.MongoBroker;

public class DAOMesa {

	//Carga las mesas
	public static ConcurrentHashMap<Integer, Mesa> load() {
		MongoCollection<BsonDocument> mesas = MongoBroker.get().getCollection("mesas");
		ConcurrentHashMap<Integer, Mesa> result=new ConcurrentHashMap<>();
		MongoCursor<BsonDocument> fiMesas = mesas.find().iterator();
		while (fiMesas.hasNext()) {
			BsonDocument bsonMesa = fiMesas.next();
			int _id=(int) bsonMesa.getDouble("_id").getValue();
			Mesa mesa=new Mesa(_id);
			result.put(_id, mesa);
		}
		return result;
	}
	
	//Por si quisieramos obtener el estado al cargar las mesas
	/*public static String leerEstado(int idMesa) {
		MongoCollection<BsonDocument> mesas = MongoBroker.get().getCollection("mesas");
		BsonDocument mesa=new BsonDocument("_id", new BsonDouble(idMesa));
		BsonDocument bso = mesas.find(mesa).first();
		String estado = bso.get("estado").asString().getValue();
		return estado;
	}*/
	
	//Actualiza el estado de las mesas
	public static void update(int idMesa, String estado) {
		MongoCollection<BsonDocument> mesas = MongoBroker.get().getCollection("mesas");
		BsonDocument mesa=new BsonDocument("_id", new BsonDouble(idMesa));
		BsonDocument mesaActualizada = new BsonDocument("_id", new BsonDouble(idMesa));
		if (estado.contentEquals("Ocupada")){
			mesaActualizada.append("estado", new BsonString("Ocupada"));
		}else {
			mesaActualizada.append("estado", new BsonString("Libre"));
		}
		mesas.replaceOne(mesa, mesaActualizada);
	}
	
	//Inserta la comanda
	public static void insertComanda(Mesa mesaAtendida){
		if (MongoBroker.get().getCollection("comandas")==null) {
			MongoBroker.get().createCollection("comandas");
		}
		MongoCollection<BsonDocument> comandas = MongoBroker.get().getCollection("comandas");
		for (int i=0; i<mesaAtendida.getComanda().getPlatopedido().size();i++) {//para cada plato que se ha pedido en la mesa
			BsonDocument comanda=new BsonDocument();
			comanda.append("idMesa", new BsonDouble(mesaAtendida.get_id()));
			comanda.append("nombre", new BsonString(mesaAtendida.getComanda().getPlatopedido().get(i).getPlato().getNombre()));
			comanda.append("unidades", new BsonDouble(mesaAtendida.getComanda().getPlatopedido().get(i).getUnidades()));
			comanda.append("horaApertura", new BsonDateTime(mesaAtendida.getComanda().getHoraApertura()));
			comanda.append("hecho", new BsonBoolean(false));
			comandas.insertOne(comanda);
		}
	}

}
