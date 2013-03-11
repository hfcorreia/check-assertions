package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class FieldInterceptor {
	
	public static void interceptField(final CtClass ctClass, final String fieldName, final String assertExpression) throws Exception {
		System.out.println("intercepting field " + ctClass.getName() + "." + fieldName);

		//criar variavel que verirfica se o campo ja foi inicializado
		CtField newField = new CtField(CtClass.booleanType, fieldName+"$isInitialized", ctClass);
		ctClass.addField(newField, "false");
		
		
		ctClass.instrument(new ExprEditor() { 
			public void edit(FieldAccess fieldAccess) throws CannotCompileException {
				if(fieldAccess.isWriter() && fieldAccess.getFieldName().equals(fieldName)){
					//instrumenta acessos de escrita ao field
//					System.out.println("Founded a valid write field access");
					fieldAccess.replace(createWriteFieldBody(assertExpression));
				} else if(fieldAccess.isReader() && fieldAccess.getFieldName().equals(fieldName)) {
					//instrumenta acessos de leitura ao field
//					System.out.println("Founded a valid read field access");
					fieldAccess.replace(createReadFieldBody(assertExpression));
				}
			}

			private String createReadFieldBody(String assertExpression) {
				return 	"{ " +
						"if(! ( " + fieldName + "$isInitialized" + " ) ) {" + 
							//acesso de leitura INVALIDO
							"throw new java.lang.RuntimeException(\"not initialized field\");" + 
						"} else {"+ 
							//acesso de leitura VALIDO
							"$_ = $proceed(); " + 
						"}" +
					"}";
			}

			private String createWriteFieldBody(final String assertExpression) {
				return 	"{ " +
							"if(! ( " + assertExpression + " ) ) {" + 
								//acesso de escrita INVALIDO
								"throw new java.lang.RuntimeException(" + createErrorMessage(assertExpression) + ");" + 
							"} else {"+ 
								//acesso de escrita VALIDO
								"$proceed($$); " + 
								fieldName + "$isInitialized = true;" +
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
