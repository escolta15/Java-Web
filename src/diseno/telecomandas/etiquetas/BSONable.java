/*********************************************************************
* Class Name: BSONable.java
* Author/s name: Pablo Mora
* Release/Creation date: 30-06-2018
* Class version: 1.0
* Class description: Definicion de BSONable
*********************************************************************/
/*
 * PATRONES UTILIZADOS EN LA CLASE BSONABLE.JAVA
 * Inyección de dependencias
 * 
 */
package diseno.telecomandas.etiquetas;

import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.TYPE;//para coger la clase
import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target( { TYPE , FIELD } )
public @interface BSONable {
	String campo() default "";
	String nombre() default "";
	boolean OnDeleteCascade() default false;
	String claseDependiente() default "";
}
