package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CodeConverter;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.Translator;

public class LegacyCodeAssertionsTranslator implements Translator {
	private static final String ARRAY_INTERCEPTOR = "ist.meic.pa.ArrayInterceptor";

	@Override
	public void onLoad(ClassPool pool, String className) throws NotFoundException, CannotCompileException {
		CtClass ctClass = pool.get(className);
		if (!className.equals(ARRAY_INTERCEPTOR)) {
			CodeConverter conv = new CodeConverter();
			CtClass arrayClass = pool.get(ARRAY_INTERCEPTOR);

			conv.replaceArrayAccess(arrayClass, new CodeConverter.DefaultArrayAccessReplacementMethodNames());
			ctClass.instrument(conv);
			ctClass.instrument(new AssertionExpressionEditor(ctClass));
			
			for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
				assertionVerifier(ctClass, ctMethod);
			}

			for (CtConstructor ctConstructor : ctClass.getConstructors()) {
				assertionVerifier(ctConstructor);
			}
		}
	}

	@Override
	public void start(ClassPool arg0) throws NotFoundException, CannotCompileException {
		// do nothing
	}

	private void assertionVerifier(CtClass ctClass, CtMethod ctMethod)
			throws NotFoundException {
		String afterMethodAssertion = recursiveBeforeMethodAssertion(ctClass, ctMethod);
		String beforeMethodAssertion = beforeMethodAssertion(ctClass, ctMethod);

		if ( afterMethodAssertion != null && ctClass!=null && !isAbstractMethod(ctMethod) ) {
			try {
				// save a copy of the original method but with a diferent name.
				String name = ctMethod.getName();
				ctMethod.setName(ctMethod.getName() + "$auxiliar");
				CtMethod auxiliarMethod = CtNewMethod.copy(ctMethod, name, ctClass, null);
				//BEFORE
				String beforeMethodExecutionTemplate =
						" { " +  
						"	" + "if( ! ( " + beforeMethodAssertion + ") ) {" +
						"	" + "	" + "throw new java.lang.RuntimeException(\"\");" +
						"	" + " } " + 
						" } ";
				
				beforeMethodExecutionTemplate = beforeMethodAssertion == null || beforeMethodAssertion.isEmpty() ? "" : beforeMethodExecutionTemplate;
				//AFTER
				String afterMethodExecutionTemplate = 
						" { " +
								"	" + ctMethod.getReturnType().getName() + " $_ = " + ctMethod.getName() + "($$);" +
								"	" + "if( ! ( " + afterMethodAssertion + " ) ) { " +
								"	" + "	"  + "throw new java.lang.RuntimeException(\"\");" +
								"	" + "}" + "	" + "return ($r)$_;" + 
						" } ";
				auxiliarMethod.setBody( beforeMethodExecutionTemplate + afterMethodExecutionTemplate );
				ctClass.addMethod(auxiliarMethod);
			} catch (CannotCompileException e1) {
				e1.printStackTrace();
				System.out.println("ERROR compiling");
			}
		}
	}

	private String beforeMethodAssertion(CtClass ctClass, CtMethod ctMethod) {
		CtMethod classMethod;
		try {
			classMethod = ctClass.getMethod(ctMethod.getName(), ctMethod.getSignature());
		} catch (NotFoundException e1) {
			return null;
		}
		if (classMethod.hasAnnotation(Assertion.class)) {
			try {
				return((Assertion) ctMethod.getAnnotation(Assertion.class)).before();
			} catch (ClassNotFoundException e) {
				return null;
			}
		}
		return null;
	}

	private boolean isAbstractMethod(CtMethod ctMethod) {
		return Modifier.isAbstract(ctMethod.getModifiers());
	}

	private void assertionVerifier(CtConstructor ctConstructor) {
		try {
			Assertion annotation = (Assertion) ctConstructor.getAnnotation(Assertion.class);
			String currentAssertion = annotation != null ? annotation.value() : null;

			String superClassAssertion = getSuperConstructorExpression(ctConstructor);
			String assertionExpression = getTotalAssert(currentAssertion, superClassAssertion);

			if(assertionExpression != null) {
				String constructorVerification = "if(!("+ assertionExpression + ")) {"
						+ "throw new java.lang.RuntimeException(\"The assertion " + assertionExpression + " is false\");"
						+ "}";

				ctConstructor.insertAfter(constructorVerification);
			}
		}catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private String getSuperConstructorExpression(CtConstructor ctConstructor) {
		String currentAssert = null;
		String superAssert = null;

		try {
			Assertion anotation = (Assertion) ctConstructor.getAnnotation(Assertion.class);
			currentAssert = anotation != null ? anotation.value() : null;
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			CtClass superClass = ctConstructor.getDeclaringClass().getSuperclass();
			if(superClass != null) {
				superAssert = getSuperConstructorExpression(superClass.getConstructor(ctConstructor.getSignature()));
			}
		} catch (NotFoundException e) {
			//do nothing
		}

		return getTotalAssert(currentAssert, superAssert);
	}

	public String recursiveBeforeMethodAssertion(CtClass ctClass, CtMethod ctMethod) {		
		String currentAssert = getCurrentAssert(ctClass, ctMethod);

		String superAssert = getSuperClassAssert(ctClass, ctMethod);

		String interfaceAssert = getInterfaceAssert(ctClass, ctMethod);

		return getTotalAssert(getTotalAssert(currentAssert, superAssert),
				interfaceAssert);
	}
	private String getInterfaceAssert(CtClass ctClass, CtMethod ctMethod) {
		String result = null;
		CtClass[] interfaces = null;

		try {
			interfaces = ctClass.getInterfaces();
		} catch (NotFoundException e) {
			// DO NOTHING BECAUSE THE INTERFACES DON'T EXISTS
			return null;
		}
		for (CtClass interf : interfaces) {
			CtMethod interfMethod = null;
			try {
				interfMethod = interf.getMethod(ctMethod.getName(),
						ctMethod.getSignature());
				String interfaceAssert = recursiveBeforeMethodAssertion(interf, interfMethod);
				result = getTotalAssert(result, interfaceAssert);
			} catch (NotFoundException e) {
				// DO NOTHING BECAUSE THE METHOD DON'T EXISTS IN THIS INTERFACE
			}
		}
		return result ;
	}

	private String getTotalAssert(String currentAssert, String superAssert) {
		String r = null;
		//super has an expression
		if (superAssert != null) {
			r = superAssert;
			//current has an expression
			if (currentAssert != null) {
				r += " && " + currentAssert;
			}
		} else {//super does not have an expression
			if (currentAssert != null) { // current has an expression
				r = currentAssert;
			}
		}
		//none of them has an expression
		return r;
	}

	private String getSuperClassAssert(CtClass ctClass, CtMethod ctMethod) {
		String superAssert = null;
		CtClass superclass = null;
		try {
			superclass = ctClass.getSuperclass();
			if (superclass != null) {
				CtMethod superMethod = superclass.getMethod(ctMethod.getName(),
						ctMethod.getSignature());
				superAssert = recursiveBeforeMethodAssertion(superclass, superMethod);
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
			classMethod = ctClass.getMethod(ctMethod.getName(),
					ctMethod.getSignature());
			if (classMethod.hasAnnotation(Assertion.class)) {
				currentAssert = ((Assertion) ctMethod
						.getAnnotation(Assertion.class)).value();
			}
		} catch (NotFoundException e) {
			classMethod = null;
		} catch (ClassNotFoundException e) {
			currentAssert = null;
		}
		return currentAssert;
	}

}
