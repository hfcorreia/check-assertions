package ist.meic.pa;

import ist.meic.pa.assertions.Assertion;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;

public class ConstructorInterceptor {

	public static String getSuperClassAssertion(CtConstructor ctConstructor) {
		String superClassAssertion = "";
		try {
			CtConstructor superClassConstructor = ctConstructor.getDeclaringClass().getSuperclass().getConstructor(ctConstructor.getSignature());
			superClassAssertion = getSuperClassConstructorExpression(superClassConstructor);
		} catch (NotFoundException e) {
			superClassAssertion = "";
		}
		return superClassAssertion;
	}

	/*
	 * gets superclasses assertions on constructors with same signature. Recursive implementation 
	 */
	private static String getSuperClassConstructorExpression(CtConstructor ctConstructor) {
		String currentAssert = "";
		String superAssert = "";

		try {
			currentAssert = ctConstructor.hasAnnotation(Assertion.class) ? ((Assertion) ctConstructor.getAnnotation(Assertion.class)).value() : "";
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			CtClass superClass = ctConstructor.getDeclaringClass().getSuperclass();
			if(superClass != null) {
				superAssert = getSuperClassConstructorExpression(superClass.getConstructor(ctConstructor.getSignature()));
			}
		} catch (NotFoundException e) {
			//do nothing
		}

		return MethodInterceptor.unionAsserExpressions(currentAssert, superAssert);
	}
	
	public static String createConstructorVerification(String assertionExpression) {
		return " { " +
				"	" + "if(!("+ assertionExpression + ")) {" +
				"	" + "	" + "throw new java.lang.RuntimeException(\"The assertion " + assertionExpression + " is false\");" +
				"	" + " } " + 
				" } ";
	}
}
