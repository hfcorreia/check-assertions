package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

public class FieldInterceptor {

	//criar variavel que verirfica se o campo ja foi inicializado
	public void createAuxiliaryFields(CtClass ctClass, CtField ctField) throws CannotCompileException, NotFoundException {
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

	private boolean existsFields(CtClass ctClass, String fieldName) {
		try {
			ctClass.getField(fieldName);
			
			return true;
		} catch(NotFoundException nfe) {
			return false;
		}
	}

	public String createReadFieldBody(String fieldName) {
		return 	"{ " +
				"if(! ( " + fieldName + "$isInitialized" + " ) ) {" + 
				"throw new java.lang.RuntimeException(\"Error: " + fieldName + "was not initialized\");" + 
				"} else {" + 
				"$_ = $proceed(); " + 
				"}" +
				"}";
	}

	public String createWriteFieldBody(String assertExpression, String fieldName ) {
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

	public String createErrorMessage(String assertExpression) {
		return "\"The assertion " + assertExpression +" is false\"";
	}

}
