package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class MethodInterceptor {
	public static void interceptMethod(final CtClass ctClass, final String methodName, final String assertExpression) throws Exception {
		System.out.println("intercepting method " + ctClass.getName() + "."
				+ methodName + "()");

		ctClass.instrument(new ExprEditor() { 
			public void edit(MethodCall methodCall) throws CannotCompileException {
				if(methodCall.getMethodName().equals(methodName)){
					System.out.println("Founded a valid methodcall");
						methodCall.replace(createMethodBody(assertExpression));
				}
			}

			private String createMethodBody(final String assertExpression) {
				return 	"{ " +
							"$_ = $proceed($$); " + 
							"if(!(" + assertExpression + ")) {" +
								"throw new java.lang.RuntimeException(" + createErrorMessage(assertExpression) + ");" + 
							"}"+
						"}";
			}

			private String createErrorMessage(String assertExpression) {
				return "\"The assertion " + assertExpression +" is false\"";
			}
		});

		ctClass.writeFile();

	}
}
