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

	public String recursiveAssert(CtClass ctClass, MethodCall methodCall) {
		String currentAssert = getCurrentAssert(ctClass, methodCall);
		String superAssert = getSuperClassAssert(ctClass, methodCall);

		String interfaceAssert = getInterfaceAssert(ctClass, methodCall);

		String noInterf = getTotalAssert(currentAssert, superAssert);
		String debug = getTotalAssert(interfaceAssert, noInterf);
//		System.out.println("#DEBUG # returned assert: " + methodCall.getMethodName() +  " " + noInterf +  " " + debug);
		
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
