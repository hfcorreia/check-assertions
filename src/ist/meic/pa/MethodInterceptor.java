package ist.meic.pa;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class MethodInterceptor {

	public String createMethodBody(final String assertExpression) {
		return "{ " + "$_ = $proceed($$); " + "if(!("
				+ assertExpression + ")) {"
				+ "throw new java.lang.RuntimeException("
				+ createErrorMessage(assertExpression) + ");" + "}"
				+ "}";
	}

	public String createErrorMessage(String assertExpression) {
		return "\"The assertion " + assertExpression + " is false\"";
	}

	public String recursiveAssertExpression(CtClass ctClass,
			String methodName, String methodDesc) throws Exception {
		if (ctClass.getSuperclass() != null) {
			String superClassExpression = recursiveAssertExpression(
					ctClass.getSuperclass(), methodName, methodDesc);

			CtMethod ctMethod = getMethod(ctClass, methodName,
					methodDesc);
			String r = superClassExpression
					+ " && "
					+ (ctMethod != null ? getAssertExpression(ctClass,
							ctMethod) : "true");
//			System.out.println(r);
			return r;
		} else {
			return "true";
		}
	}

	private CtMethod getMethod(CtClass ctClass, String methodName,
			String methodDesc) {
		try {
			return ctClass.getMethod(methodName, methodDesc);
		} catch (NotFoundException e) {
			return null;
		}
	}

	private String getAssertExpression(CtClass ctClass,
			CtMethod ctMethod) throws ClassNotFoundException {
		return ctMethod.hasAnnotation(Assertion.class) ? ((Assertion) ctMethod
				.getAnnotation(Assertion.class)).value() : "";
	}
}
