package ist.meic.pa.interceptors;

import ist.meic.pa.assertions.ExceptionAssertion;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.Handler;

/**
 * 
 * class with some util methods for intercepting catch blocks 
 *
 */
public class ExceptionInterceptor {

	public static boolean exceptionPresent(Handler handler, String[] exceptions)  {
		for(String s : exceptions) {
			try {
				if(handler.getType().getName().equals(s)) {
					return true;
				}
			} catch (NotFoundException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 *
	 * creates template for injecting code on catch block
	 * 
	 */
	public static String createCatchTemplate(CtClass ctClass, ExceptionAssertion anotation) {
		return " { " + 
				"	" + "try { " +
				"	" + "	" + ctClass.getName() + ".class.getMethod(\"" + anotation.method() + "\", null).invoke(new " + ctClass.getName() + "(), null);" +
				"	" + " }  catch (Exception e) { " + 
				"	" + "/* do nothing */ " + 
				"	" + " } " +
				" } ";
	}
	
}
