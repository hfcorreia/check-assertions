package ist.meic.pa;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface CastAssertion {
	String value();
} 
