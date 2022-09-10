/*********************************************************************
* Class Name: JSONable.java
* Author/s name: Pablo Mora
* Release/Creation date: 30-06-2018
* Class version: 1.0
* Class description: Definicion de JSONable
*********************************************************************/
package diseno.telecomandas.etiquetas;

import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface JSONable {

	String campo() default "";

	String nombre() default "";

}
