package ist.meic.pa;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Assertion {
	
//	static final String[] defaultExpr = new String[] {"chanan","tapan","Amar","santosh","deepak"};

	String value();
	
//	String[] expressions() default {"",""};
	
//	String expression() default "";
//	boolean before() default false;
} 
