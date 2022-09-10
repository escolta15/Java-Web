/*********************************************************************
* Class Name: TestJSONeador.java
* Author/s name: Pablo Mora
* Release/Creation date: 30-06-2018
* Class version: 1.0
* Class description: Solo para pruebas
*********************************************************************/
/*package diseno.telecomandas.etiquetas;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;

import diseno.telecomandas.dominio.Comanda;
import diseno.telecomandas.dominio.Mesa;
import diseno.telecomandas.dominio.Plato;
import diseno.telecomandas.dominio.PlatoPedido;

public class TestJSONeador {

	@Test
	public void test() {
		Plato plato = new Plato("26","Tortilla",6.50);
		PlatoPedido platoPedido=new PlatoPedido(plato,3);
		JSONObject jso = JSONeador.toJSONObject(platoPedido);
		System.out.println(jso.toString());
		String valorEsperado = "{\"unidades\":3,\"idPlato\":\"26\"}";
		System.out.println(valorEsperado);
		assertEquals(jso.toString(),valorEsperado);
		
		
		
		Plato plato = new Plato("1","Gazpacho",5);
		String a=plato.toJSONObject().toString();
		String b=JSONeador.toJSONObject(plato).toString();
		System.out.println(a);
		System.out.println(b);
		assertEquals(a,b);
		
		PlatoPedido platoPedido=new PlatoPedido(plato,4);
		a=platoPedido.toJSONObject().toString();
		b=JSONeador.toJSONObject(platoPedido).toString();
		System.out.println(a);
		System.out.println(b);
		assertEquals(a,b);
		
		Comanda comanda= new Comanda();
		comanda.add(plato, 2);
		Plato tortilla= new Plato("2","Tortilla",6.5);
		comanda.add(tortilla, 1);
		a=comanda.toJSONObject().toString();
		b=JSONeador.toJSONObject(comanda).toString();
		assertEquals(a,b);*/
		
		/*Mesa mesa = new Mesa(1);
		try {
			mesa.abrir();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mesa.addToComanda(plato, 2);
		System.out.println(mesa.toJSONObject());
		JSONObject jsoMesa=JSONeador.toJSONObject(mesa);
		jsoMesa.put("estado", mesa.estaLibre() ? "Libre" : "Ocupada");
		System.out.println(JSONeador.toJSONObject(mesa));
	}

}*/
