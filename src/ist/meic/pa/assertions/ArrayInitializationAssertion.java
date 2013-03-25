package ist.meic.pa.assertions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
*
* Anotation used for handling arrays initialization
* 
*/
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public  @interface ArrayInitializationAssertion {
    boolean value() default true;
}
