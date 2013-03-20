package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.Translator;

public class AssertionsTranslator implements Translator {

	@Override
	public void onLoad(ClassPool pool, String className) throws NotFoundException, CannotCompileException {
		CtClass ctClass = pool.get(className);
		//		ctClass.instrument(new AssertionExpressionEditor(ctClass));

		for(CtMethod ctMethod : ctClass.getDeclaredMethods()) {
			assertionVerifier(ctClass, ctMethod);
		}

		for(CtField ctField : ctClass.getFields()) {
			assertionVerifier(ctField);
		}

		for(CtConstructor ctConstructor : ctClass.getConstructors()) {
			assertionVerifier(ctConstructor);
		}

	}

	@Override
	public void start(ClassPool arg0) throws NotFoundException,CannotCompileException {
		// do nothing
	}

	private void assertionVerifier(CtClass ctClass, CtMethod ctMethod) {
		System.out.println("a tratar de: " + ctMethod.getName() + " na classe " + ctClass.getName());
	
		String assertion = recursiveAssert(ctClass, ctMethod);
		System.out.println("ASSERT: " + assertion);

		if(assertion != null) {
			try {
				CtMethod auxiliarMethod = CtNewMethod.copy(ctMethod, ctMethod.getName() + "$auxiliar", ctClass, null);
				ctClass.addMethod(auxiliarMethod);
				ctMethod.setBody(createMethodBody(ctMethod.getName(), assertion));
				
//				ctMethod.setBody("return ($r)" + ctMethod.getName() + "$auxiliar($$); ");
//				ctMethod.insertAfter(
//						"if(!("+ assertion + ")) {"
//								+ "throw new java.lang.RuntimeException(\"The assertion " + assertion + " is false\");"
//							+ "}" +
//						"}");
			} catch (CannotCompileException e1) {
				e1.printStackTrace();
			}
		}
	}


	private void assertionVerifier(CtField ctField) {
		// TODO Auto-generated method stub

	}

	private void assertionVerifier(CtConstructor ctConstructor) {
		// TODO Auto-generated method stub
	}

	public String recursiveAssert(CtClass ctClass, CtMethod ctMethod) {
		String currentAssert = getCurrentAssert(ctClass, ctMethod);
		String superAssert = getSuperClassAssert(ctClass, ctMethod);

		String interfaceAssert = getInterfaceAssert(ctClass, ctMethod);
		
		return getTotalAssert(getTotalAssert(currentAssert, superAssert),interfaceAssert);
	}

	private String getInterfaceAssert(CtClass ctClass, CtMethod ctMethod) {
		String result = null;
		CtClass[] interfaces = null;

		try {
			interfaces  = ctClass.getInterfaces();
			for(CtClass interf : interfaces) {
				CtMethod interfMethod = interf.getMethod(ctMethod.getName(), ctMethod.getSignature());
				String interfaceAssert = recursiveAssert(interf, interfMethod);
				result = getTotalAssert(result, interfaceAssert);
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
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

	private String getSuperClassAssert(CtClass ctClass, CtMethod ctMethod) {
		String superAssert = null;
		CtClass superclass = null;
		try {
			superclass = ctClass.getSuperclass();
			if(superclass != null) {
				CtMethod superMethod = superclass.getMethod(ctMethod.getName(), ctMethod.getSignature());
				superAssert = recursiveAssert(superclass, superMethod);
			}
		} catch (NotFoundException e) {
			superclass = null;
		}
		return superAssert;
	}

	private String getCurrentAssert(CtClass ctClass, CtMethod ctMethod) {
		String currentAssert = null;
		CtMethod classMethod = null;
		try {
			classMethod  = ctClass.getMethod(ctMethod.getName(), ctMethod.getSignature());
			if(classMethod.hasAnnotation(Assertion.class)) {
				currentAssert = ((Assertion) ctMethod.getAnnotation(Assertion.class)).value();
			}
		} catch (NotFoundException e) {
			classMethod = null;
		} catch (ClassNotFoundException e) {
			currentAssert = null;
		}
		return currentAssert;
	}
	
	public String createMethodBody(String methodName, String assertionExpr) {
		//verificacao esta a ser feita antes.....
		
		return "{ "+ 
					"if(!("+ assertionExpr + ")) {"
					+ 	"throw new java.lang.RuntimeException(\"The assertion " + assertionExpr + " is false\");"
					+ "}" +
					"else {" +
							"return " + methodName + "$auxiliar($$); " +
					"}" +
				"}";
		
//		return "{" + 
//					"Object return$value = " + methodName + "$auxiliar($$);" + 
//					"if(!("+ assertionExpr.replaceAll("$_", "($r)return$value") + ")) {"
//						+ "throw new java.lang.RuntimeException(\"The assertion " + "" + " is false\");" + 
//					"}" + 
//					"else {" +
//						"return ($r)return$value; " +
//					"}" + 
//				"}";
	}
}
