package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class FieldInterceptor {
	
	public void interceptField(final CtClass ctClass, final String fieldName, final String assertExpression) throws Exception {
  
		//criar variavel que verirfica se o campo ja foi inicializado
		CtField isInitializedField = new CtField(CtClass.booleanType, fieldName + "$isInitialized", ctClass);
		CtField isInitializedTempField = new CtField(CtClass.booleanType, fieldName + "$isInitializedTemp", ctClass);
		CtField tmpValueField = new CtField(ctClass.getField(fieldName).getType(), fieldName + "$tmpValue", ctClass);
		
		ctClass.addField(isInitializedField, "false");
		ctClass.addField(isInitializedTempField);
		ctClass.addField(tmpValueField);

		ctClass.instrument(new ExprEditor() { 
			public void edit(FieldAccess fieldAccess) throws CannotCompileException {
				if(fieldAccess.isWriter() && fieldAccess.getFieldName().equals(fieldName)){
					fieldAccess.replace(createWriteFieldBody(assertExpression));
				} else if(fieldAccess.isReader() && fieldAccess.getFieldName().equals(fieldName)) {
					fieldAccess.replace(createReadFieldBody(fieldName));
				}
			}

			private String createReadFieldBody(String fieldName) {
				return 	"{ " +
						"if(! ( " + fieldName + "$isInitialized" + " ) ) {" + 
							"throw new java.lang.RuntimeException(\"Error: " + fieldName + "was not initialized\");" + 
						"} else {" + 
							"$_ = $proceed(); " + 
						"}" +
					"}";
			}

			private String createWriteFieldBody(final String assertExpression) {
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

			private String createErrorMessage(String assertExpression) {
				return "\"The assertion " + assertExpression +" is false\"";
			}
		});

		ctClass.writeFile();

	}
}
