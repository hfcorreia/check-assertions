package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class FieldReadInterceptor {
	public static void interceptField(final CtClass ctClass, final String fieldName, final String assertExpression) throws Exception {
		System.out.println("intercepting field " + ctClass.getName() + "." + fieldName);

		//TODO
		//add a set with the name of the initialzed fields for that object
		//add a method that verifies if the field is initialzed
		
		ctClass.instrument(new ExprEditor() { 
			public void edit(FieldAccess fieldAccess) throws CannotCompileException {
				if(fieldAccess.isReader() && fieldAccess.getFieldName().equals(fieldName)){
					System.out.println("Founded a valid write field access");
						fieldAccess.replace(createFieldBody(assertExpression));
				}
			}

			private String createFieldBody(final String assertExpression) {
				return 	"{ " +
//							"if(!(isInitializedField("+ fieldName +"))) {" +
//								"throw new java.lang.RuntimeException(" + createErrorMessage(assertExpression) + ");" + 
//							"} else {"+
								"$_ = $proceed(); " + 
//							"}" +
						"}";
			}

			private String createErrorMessage(String assertExpression) {
				return "\"The assertion " + assertExpression +" is false\"";
			}
		});

		ctClass.writeFile();

	}
}
