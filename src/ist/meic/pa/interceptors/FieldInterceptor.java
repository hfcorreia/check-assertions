package ist.meic.pa.interceptors;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

public class FieldInterceptor {

	public static void createAuxiliaryFields(CtClass ctClass, CtField ctField) throws CannotCompileException, NotFoundException {
		String fieldName = ctField.getName();

		if (! existsFields(ctClass, fieldName + "$isInitialized") ) {
			CtField isInitializedField = new CtField(CtClass.booleanType, fieldName + "$isInitialized", ctClass);
			CtField isInitializedTempField = new CtField(CtClass.booleanType, fieldName + "$isInitializedTemp", ctClass);
			CtField tmpValueField = new CtField(ctClass.getField(fieldName).getType(), fieldName + "$tmpValue", ctClass);

			ctClass.addField(isInitializedField, "false");
			ctClass.addField(isInitializedTempField);
			ctClass.addField(tmpValueField);
		}
	}

	private static boolean existsFields(CtClass ctClass, String fieldName) {
		try {
			ctClass.getField(fieldName);
			return true;
		} catch(NotFoundException nfe) {
			return false;
		}
	}

	public static String createReadFieldBody(String fieldName) {
		return 	"{ " +
				"   " + "if(! ( " + fieldName + "$isInitialized" + " ) ) {" + 
				"   " + "   " + "throw new java.lang.RuntimeException(\"Error: " + fieldName + "was not initialized\");" + 
				"   " + "} else {" + 
				"   " + "   " + " $_ = $proceed(); " + 
				"   " + "}" +
				"}";
	}

	public static String createWriteFieldBody(String assertExpression, String fieldName ) {
		return 	"{ " + 
					fieldName + "$isInitializedTemp = " + fieldName + "$isInitialized;" + 
					fieldName + "$isInitialized = true;" +
					fieldName + "$tmpValue = " + fieldName +  ";" + 
					"$proceed($$); " + 
					"if( ! (" + assertExpression + ") ) {" + 
						fieldName + " = " + fieldName + "$tmpValue;" + 
						fieldName + "$isInitialized = " + fieldName + "$isInitializedTemp;" + 
						"throw new java.lang.RuntimeException(" + createErrorMessage(assertExpression) + ");" + 
					"}" +
				"}";
	} 

	public static String createErrorMessage(String assertExpression) {
		return "\"The assertion " + assertExpression +" is false\"";
	}

}
