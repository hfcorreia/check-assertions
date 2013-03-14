package ist.meic.pa;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.expr.MethodCall;

public class MethodInterceptor {

	public String createMethodBody(final String assertExpression) {
		return "{ " 
				+ "$_ = $proceed($$); " 
				+ "if(!(" + assertExpression + ")) {"
				+	 	"throw new java.lang.RuntimeException("+ createErrorMessage(assertExpression) + ");"
				+ "}" 
				+"}";
	}

	public String createErrorMessage(String assertExpression) {
		return "\"The assertion " + assertExpression + " is false\"";
	}

	public String recursiveAssertExpression(CtClass ctClass, MethodCall methodCall) throws Exception {
		if (ctClass.getSuperclass() != null) {
			String superClassExpression = recursiveAssertExpression(ctClass.getSuperclass(), methodCall);

			CtMethod ctMethod = ctClass.getMethod(methodCall.getMethodName(), methodCall.getSignature());
			
			String r = superClassExpression + " && " + (ctMethod != null ? getAssertExpression(ctClass,ctMethod) : "true");
			return r;
		} else {
			return "true";
		}
	}
	
//	private CtMethod getMethod(CtClass ctClass, String methodName, String methodDesc) {
//		try {
//			return ctClass.getMethod(methodName, methodDesc);
//		} catch (NotFoundException e) {
//			return null;
//		}
//	}

	private String getAssertExpression(CtClass ctClass,	CtMethod ctMethod) throws ClassNotFoundException {
		return ctMethod.hasAnnotation(Assertion.class) ? ((Assertion) ctMethod.getAnnotation(Assertion.class)).value() : "";
	}
}
