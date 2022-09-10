/*********************************************************************
* Class Name: Auxi.java
* Author/s name: Pablo Mora
* Release/Creation date: 30-06-2018
* Class version: 1.0
* Class description: Coge la hora
*********************************************************************/
package diseno.telecomandas.dominio;

import java.util.Date;

public class Auxi {

	public static Date getHora(long horaApertura) {
		return new Date(horaApertura);
	}

}