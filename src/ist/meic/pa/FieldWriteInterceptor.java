package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class FieldWriteInterceptor {
	public static void interceptField(final CtClass ctClass, final String fieldName, final String assertExpression) throws Exception {
		System.out.println("intercepting field " + ctClass.getName() + "." + fieldName);

		ctClass.instrument(new ExprEditor() { 
			public void edit(FieldAccess fieldAccess) throws CannotCompileException {
				if(fieldAccess.isWriter() && fieldAccess.getFieldName().equals(fieldName)){
					System.out.println("Founded a valid write field access");
						fieldAccess.replace(createFieldBody(assertExpression));
				}
			}

			private String createFieldBody(final String assertExpression) {
				return 	"{ " +
							"if(!(" + assertExpression + ")) {" +
								"throw new java.lang.RuntimeException(" + createErrorMessage(assertExpression) + ");" + 
							"} else {"+
								//TODO - register this field in the set of initialized fields
								"$proceed($$); " + 
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
