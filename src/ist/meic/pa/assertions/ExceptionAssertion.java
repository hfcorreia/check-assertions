package ist.meic.pa.assertions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 *
 * Anotation used for handling specified exceptions with desired method
 * 
 */
@Target({ElementType.TYPE})
public @interface ExceptionAssertion {
	
	String[] exception();
	String method();
}
