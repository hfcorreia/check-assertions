package ist.meic.pa.assertions;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Assertion {
	
	String value() default "";
	String before() default ""; 
}
