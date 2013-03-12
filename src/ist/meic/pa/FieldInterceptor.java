package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class FieldInterceptor {
	
	public void interceptField(final CtClass ctClass, final String fieldName, final String assertExpression) throws Exception {

		//criar variavel que verirfica se o campo ja foi inicializado
		CtField newField = new CtField(CtClass.booleanType, fieldName+"$isInitialized", ctClass);
		ctClass.addField(newField, "false");
		
		
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
						"} else {"+ 
							"$_ = $proceed(); " + 
						"}" +
					"}";
			}

			private String createWriteFieldBody(final String assertExpression) {
				return 	"{ " +
							//TODO: analizar o nivel de martelanço desta linha
							"if( " + assertExpression.replace(fieldName, "$1") + " ) {" + 
								"$proceed($$); " + 
								fieldName + "$isInitialized = true;" +
							"} else {"+ 
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
