package ist.meic.pa.interceptors;

import javassist.NotFoundException;
import javassist.expr.Cast;

/**
 * 
 * class with some util methods for intercepting casts 
 *
 */
public class CastInterceptor {

	/**
	 *
	 * creates injecting code for verifing cast validation
	 * 
	 */
	public static String createCastTemplate(Cast castExpression, String[] castingClasses) throws NotFoundException {
        return " if( " + generateCastAssertion(castExpression, castingClasses) + " ) {" + 
        		"	" + "$_ = $proceed($$);" + 
        		" } " + 
                "else { " + 
        		"	" + "throw new RuntimeException(" + createCastErrorMessage(castExpression) + ");" + 
                " } ";
	}
	
	private static String generateCastAssertion(Cast castExpression, String[] assertions) throws NotFoundException {
		String assertion = null;

		for(String s : assertions) {
			if(s.equals(castExpression.getType().getName())) {
				assertion = castExpression.getType().getName();
				break;
			}
		}
		
		return "(\"" + castExpression.getType().getName() + "\"" + ".equals(\"" + assertion + "\")) || " + getSelfCastExpression(castExpression);
	}

	private static String getSelfCastExpression(Cast castExpression) {
		try {
			String castingClass = castExpression.getEnclosingClass().getName();
			String castClass = castExpression.getType().getName();
			
			return "(\"" + castingClass + "\"" + ".equals(\"" + castClass + "\"))";  
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return "" + false;
	}

	private static String createCastErrorMessage(Cast castExpression) throws NotFoundException {
		return "\"cast not allowed from class " + "<" + castExpression.getEnclosingClass().getName() + "> to " + "<" + castExpression.getType().getName() + ">\"";
	}
	
}


