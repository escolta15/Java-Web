/*********************************************************************
* Class Name: MongoBroker.java
* Author/s name: Pablo Mora
* Release/Creation date: 30-06-2018
* Class version: 1.0
* Class description: Conecta con la BD
*********************************************************************/
/*
 * PATRONES UTILIZADOS EN LA CLASE MONGOBROKER.JAVA
 * Singleton
 * 
 */
package diseno.telecomandas.dao;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bson.BsonDocument;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoBroker {
	private final String databaseName;
	private MongoClient client;
	private MongoDatabase database;
	
	private MongoBroker() {
		this.databaseName = "telecomandas";
		this.client=new MongoClient();
		if (!exists())
			createDatabase();
		this.database=this.client.getDatabase(databaseName);
	}
	
	private void createDatabase() {
		String urlScript="https://bitbucket.org/macariopolo/comandas/raw/edd9c56e6379c1c1bd28b040722d4b45c3ebcc47/comandas/src/main/webapp/recursos/creacionTelecomandas.js";
		final HttpGet get=new HttpGet(urlScript);
		HttpClient client= HttpClientBuilder.create().build();
		try {
			HttpResponse resultado=client.execute(get);
			InputStream is = resultado.getEntity().getContent();
			int read=0;
			String textoScript=new String();
			StringBuffer sb=new StringBuffer();
			while ((read=is.read())!=-1)
				sb.append((char) read);
			textoScript=sb.toString().trim();
			System.out.println(textoScript);
			this.database=this.client.getDatabase(databaseName);
			this.database.runCommand(new BasicDBObject("eval", textoScript));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean exists() {
		MongoCursor<String> dbsCursor = client.listDatabaseNames().iterator();
		while(dbsCursor.hasNext()) {
		    if (dbsCursor.next().equals(this.databaseName))
		    	return true;
		}
		return false;
	}

	private static class MongoBrokerHolder {
		static MongoBroker singleton=new MongoBroker();
	}
	
	public static MongoBroker get() {
		return MongoBrokerHolder.singleton;
	}
	
	public MongoCollection<BsonDocument> getCollection(String name) {
		return this.database.getCollection(name, BsonDocument.class);
	}
	
	public void createCollection(String name) {
		this.database.createCollection(name);
	}
}
