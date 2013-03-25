package ist.meic.pa.assertions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
*
* Anotation used for assert primary goals from project and constructor assertions
* 
*/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Assertion {
	
	String value() default "";
	String before() default ""; 
}
