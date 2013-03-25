package ist.meic.pa.interceptors;

import ist.meic.pa.assertions.Assertion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * 
 * class with some util methods for intercepting method 
 *
 */
public class MethodInterceptor {

	/**
	 *
	 * method for create new method body. New body calls a copy from original method
	 * 
	 */
	public static String createOriginalTemplate(String auxiliarMethodName) {
		return " { " +
				"	" + "return ($r) " + auxiliarMethodName + "($$);" + 
				" } ";
	}

	/**
	 *
	 * creates method template for asserting params on method exiting
	 * 
	 */
	public static String createAfterTemplate(CtMethod originalMethod, String afterMethodAssertion, String auxiliarMethodName) throws NotFoundException {
		return " { " +
				"	" + originalMethod.getReturnType().getName() + " $_ = " + auxiliarMethodName + "($$);" +
				"	" + "if( ! ( " + afterMethodAssertion + " ) ) { " +
				"	" + "	"  + "throw new java.lang.RuntimeException(\"\");" +
				"	" + "}" + "	" + "return ($r)$_;" + 
				" } ";
	}

	/**
	 *
	 * creates method template for asserting params on method entry
	 * 
	 */
	public static String createBeforeTemplate(String beforeMethodAssertion) {
		return " { " +  
				"	" + "if( ! ( " + beforeMethodAssertion + ") ) {" +
				"	" + "	" + "throw new java.lang.RuntimeException(\"\");" +
				"	" + " } " + 
				" } ";
	}
	
	/**
	 *
	 * get recursive assertion expression for exiting assert
	 * 
	 */
	public static String getAfterMethodAssertionExpression(CtClass ctClass, CtMethod ctMethod) {
		String assertionExpression = "";

		for(Assertion assertion : getHierarquicAssertionsForMethod(ctClass, ctMethod)) {
			assertionExpression = unionAsserExpressions(assertionExpression, assertion.value());
		}

		return assertionExpression;
	}

	/**
	 *
	 * get recursive assertion expression for method entry assert
	 * 
	 */
	public static String getBeforeMethodAssertionExpression(CtClass ctClass, CtMethod ctMethod) {
		String assertionExpression = "";

		for(Assertion assertion : getHierarquicAssertionsForMethod(ctClass, ctMethod)) {
			assertionExpression = unionAsserExpressions(assertionExpression, assertion.before());
		}

		return assertionExpression;
	}
	
	private static List<Assertion> getHierarquicAssertionsForMethod(CtClass ctClass, CtMethod myCtMethod) {
		List<Assertion> assertions = new ArrayList<Assertion>();

		for (CtClass hierarquicClass : getAllHierarquicClasses(ctClass)) {
			try {
				CtMethod ctMethod = hierarquicClass.getMethod(myCtMethod.getName(), myCtMethod.getSignature());
				if (ctMethod.hasAnnotation(Assertion.class)) {
					assertions.add((Assertion) ctMethod.getAnnotation(Assertion.class));
				}
			} catch (ClassNotFoundException e) {
				//Error getting annotation - do nothing
			} catch (NotFoundException e) {
				//do nothing
			}
		}
		return assertions;
	}

	private static List<CtClass> getAllHierarquicClasses(CtClass ctClass) {
		List<CtClass> result = new ArrayList<CtClass>();
		
		if (hasValidSuperclass(ctClass)) {
			for (CtClass hierarquicClass : getSuperclassAndInterfaces(ctClass)) {
				result.addAll(getAllHierarquicClasses(hierarquicClass));
			}

			result.add(ctClass);
		}
		return result;
	}

	private static boolean hasValidSuperclass(CtClass ctClass) {
			try {
				return ctClass!=null && ctClass.getSuperclass()!=null;
			} catch (NotFoundException e) {
				return false;
			}
	}
	

	private static ArrayList<CtClass> getSuperclassAndInterfaces(CtClass ctClass) {
		ArrayList<CtClass> result = new ArrayList<CtClass>();
		
		//get superclass
		try {
			result.add(ctClass.getSuperclass());
		} catch (NotFoundException e) {
			//do nothing
		}
		
		//get interfaces
		try {
			result.addAll(Arrays.asList(ctClass.getInterfaces()));
		} catch (NotFoundException e) {
			//do nothing
		}
		
		return result;
	}
	
	public static String unionAsserExpressions(String expression1, String expression2) {
		if( !expression1.isEmpty() && !expression2.isEmpty() ) {
			return expression1 + " && " + expression2;
		}
		if( !expression1.isEmpty() && expression2.isEmpty() ) {
			return expression1;
		}
		if( expression1.isEmpty() && !expression2.isEmpty() ){
			return expression2;
		}
		return "";
	}

}
