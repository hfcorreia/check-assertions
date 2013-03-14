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

//	public String recursiveAssertExpression(CtClass ctClass, String methodName, String methodDesc) throws NotFoundException, ClassNotFoundException {
//		if (ctClass.getSuperclass() != null) {
//			String superClassExpression = recursiveAssertExpression(ctClass.getSuperclass(), methodName, methodDesc);
//
//			CtMethod ctMethod;
//			try {
//				ctMethod = ctClass.getMethod(methodName, methodDesc);
//			} catch (NotFoundException e) {
//				ctMethod = null;
//			}
//			String r = superClassExpression + " && " + (ctMethod != null ? getAssertExpression(ctClass,ctMethod) : "true");
//
//			return r;
//		} else {
//			return "true";
//		}
//	}

	public String recursiveAssert(CtClass ctClass, MethodCall methodCall) throws NotFoundException, ClassNotFoundException{
		CtMethod ctMethod = null;
		CtClass superclass = null;
		String currentAssert = null;
		String superAssert = null;

		currentAssert = getCurrentAssert(ctClass, methodCall, ctMethod, currentAssert);
		superAssert = getSuperClassAssert(ctClass, methodCall, superclass, superAssert);

		String result = getTotalAssert(currentAssert, superAssert);

		return result;
	}

	private String getTotalAssert(String currentAssert, String superAssert) {
		String r = null;
		if(superAssert != null) {
			r = superAssert;
			if (currentAssert != null) {
				r += " && " + currentAssert;
			}
		}
		else {
			if(currentAssert != null) {
				r = currentAssert;
			}
		}
		return r;
	}

	private String getSuperClassAssert(CtClass ctClass, MethodCall methodCall, CtClass superclass, String superAssert)
			throws ClassNotFoundException {
		try {
			superclass = ctClass.getSuperclass();
			if(superclass != null) {
				superAssert = recursiveAssert(superclass, methodCall);
			}
		} catch (NotFoundException e) {
			superclass = null;
		}
		return superAssert;
	}

	private String getCurrentAssert(CtClass ctClass, MethodCall methodCall, CtMethod ctMethod, String currentAssert) {
		try {
			ctMethod = ctClass.getMethod(methodCall.getMethodName(), methodCall.getSignature());
			currentAssert = ((Assertion) ctMethod.getAnnotation(Assertion.class)).value();
		} catch (NotFoundException e) {
			ctMethod = null;
		} catch (ClassNotFoundException e) {
			currentAssert = null;
		}
		return currentAssert;
	}

	//		private CtMethod getMethod(CtClass ctClass, String methodName, String methodDesc) {
	//			try {
	//				return ctClass.getMethod(methodName, methodDesc);
	//			} catch (NotFoundException e) {
	//				return null;
	//			}
	//		}

	private String getAssertExpression(CtClass ctClass,	CtMethod ctMethod) throws ClassNotFoundException {
		return ctMethod.hasAnnotation(Assertion.class) ? ((Assertion) ctMethod.getAnnotation(Assertion.class)).value() : "";
	}
}
