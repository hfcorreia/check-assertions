package ist.meic.pa;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.expr.MethodCall;

public class MethodInterceptor {

	public String createMethodBody(final String assertionExpr) {
		return "if(!("+ assertionExpr + ")) {"
				+ "throw new java.lang.RuntimeException(\"The assertion " + assertionExpr + " is false\");"
				+ "}";
	}

	public String createErrorMessage(String assertExpression) {
		return "\"The assertion " + assertExpression + " is false\"";
	}

	public String recursiveAssert(CtClass ctClass, MethodCall methodCall) {
		String currentAssert = getCurrentAssert(ctClass, methodCall);
		String superAssert = getSuperClassAssert(ctClass, methodCall);

		String interfaceAssert = getInterfaceAssert(ctClass, methodCall);

		String noInterf = getTotalAssert(currentAssert, superAssert);
		String debug = getTotalAssert(interfaceAssert, noInterf);

		return debug;
	}

	private String getInterfaceAssert(CtClass ctClass, MethodCall methodCall) {
		String result = null;
		methodCall.where().getClass();
		CtClass[] interfaces = null;
		try {
			interfaces  = ctClass.getInterfaces();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		for(CtClass interf : interfaces) {
			String interfaceAssert = recursiveAssert(interf, methodCall);
			result = getTotalAssert(result, interfaceAssert);
		}

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

	private String getSuperClassAssert(CtClass ctClass, MethodCall methodCall) {
		String superAssert = null;
		CtClass superclass = null;
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

	private String getCurrentAssert(CtClass ctClass, MethodCall methodCall) {
		String currentAssert = null;
		CtMethod ctMethod = null;
		try {
			ctMethod  = ctClass.getMethod(methodCall.getMethodName(), methodCall.getSignature());
			if(ctMethod.hasAnnotation(Assertion.class)) {
				currentAssert = ((Assertion) ctMethod.getAnnotation(Assertion.class)).value();
			}
		} catch (NotFoundException e) {
			ctMethod = null;
		} catch (ClassNotFoundException e) {
			currentAssert = null;
		}
		return currentAssert;
	}

}
