package ist.meic.pa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class AssertionsTranslator implements Translator {
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

	private void assertionVerifier(CtClass ctClass, CtMethod originalMethod)
			throws NotFoundException {
		String beforeMethodAssertion = getBeforeMethodAssertionExpression( ctClass, originalMethod );
		String afterMethodAssertion = getAfterMethodAssertionExpression( ctClass, originalMethod );

		if ( ( !afterMethodAssertion.isEmpty() || !beforeMethodAssertion.isEmpty() ) && 
				ctClass!=null && originalMethod!=null && !isAbstractMethod(originalMethod) ) {
			try {
				// save a copy of the original method but with a different name.
				String originalMethodName = originalMethod.getName();
				String auxiliarMethodName = originalMethodName + "$auxiliar";
				
				CtMethod auxiliarMethod = CtNewMethod.copy( originalMethod, auxiliarMethodName, ctClass, null );
				ctClass.addMethod( auxiliarMethod );
				
				//ORIGINAL
				String originalAfterMethodExecutionTemplate = 
						" { " +
						"	" + "return ($r) " + auxiliarMethodName + "($$);" + 
						" } ";
				
				
				//BEFORE
				String beforeMethodExecutionTemplate =
						" { " +  
						"	" + "if( ! ( " + beforeMethodAssertion + ") ) {" +
						"	" + "	" + "throw new java.lang.RuntimeException(\"\");" +
						"	" + " } " + 
						" } ";

				beforeMethodExecutionTemplate = beforeMethodAssertion.isEmpty() ? "" : beforeMethodExecutionTemplate;
				//AFTER
				
				String afterMethodExecutionTemplate = 
						" { " +
								"	" + originalMethod.getReturnType().getName() + " $_ = " + auxiliarMethodName + "($$);" +
								"	" + "if( ! ( " + afterMethodAssertion + " ) ) { " +
								"	" + "	"  + "throw new java.lang.RuntimeException(\"\");" +
								"	" + "}" + "	" + "return ($r)$_;" + 
						" } ";
				
				afterMethodExecutionTemplate = afterMethodAssertion.isEmpty() ? originalAfterMethodExecutionTemplate : afterMethodExecutionTemplate;

				String body = " { " + beforeMethodExecutionTemplate + afterMethodExecutionTemplate + " } ";
				originalMethod.setBody(body);

			} catch (CannotCompileException e1) {
				e1.printStackTrace();
			}
		}
	}

	private String getAfterMethodAssertionExpression( CtClass ctClass, CtMethod ctMethod ) {
		String assertionExpression = "";
		
		for( Assertion assertion : getHierarquicAssertionsForMethod( ctClass, ctMethod ) ) {
			assertionExpression = unionAsserExpressions( assertionExpression, assertion.value() );
		}
		
		return assertionExpression;
	}
	
	private String getBeforeMethodAssertionExpression( CtClass ctClass, CtMethod ctMethod ) {
		String assertionExpression = "";
		
		for( Assertion assertion : getHierarquicAssertionsForMethod( ctClass, ctMethod ) ) {
			assertionExpression = unionAsserExpressions( assertionExpression, assertion.before() );
		}
		
		return assertionExpression;
	}
	
	private String unionAsserExpressions( String expression1, String expression2 ) {
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
	
	private List<Assertion> getHierarquicAssertionsForMethod(CtClass myCtClass, CtMethod myCtMethod) {
	    List<Assertion> assertions = new ArrayList<Assertion>();

	    for (CtClass ctClass : getAllHierarquicClasses(myCtClass)) {
	        CtMethod ctMethod = getMethodForClass(ctClass, myCtMethod);
	        if (ctMethod != null && ctMethod.hasAnnotation(Assertion.class)) {
	            try {
	                assertions.add((Assertion) ctMethod.getAnnotation(Assertion.class));
	            } catch (ClassNotFoundException e) {
	                //Error getting annotation - do nothing
	            }
	        }
	    }
	    return assertions;
	}
	
	private CtMethod getMethodForClass(CtClass ctClass, CtMethod myCtMethod) {
		try {
			return ctClass.getMethod( myCtMethod.getName(), myCtMethod.getSignature() );
		} catch (NotFoundException e) {
			return null;
		} 
	}

    private List<CtClass> getAllHierarquicClasses(CtClass myCtClass) {
        List<CtClass> result = new ArrayList<CtClass>();
        if (hasValidSuperclass(myCtClass)) {

            for (CtClass hierarquicClass : getSuperclassAndInterfaces(myCtClass)) {
                result.addAll(getAllHierarquicClasses(hierarquicClass));
            }

            result.add(myCtClass);
        }
        return result;
    }

	private boolean hasValidSuperclass(CtClass myCtClass) {
		try {
			return myCtClass!=null && myCtClass.getSuperclass()!=null && !myCtClass.getSuperclass().getClass().equals(Object.class);
		} catch (Exception e) {
			return false;
		}
	}
	
	private ArrayList<CtClass> getSuperclassAndInterfaces(CtClass ctClass) {
		ArrayList<CtClass> result = new ArrayList<CtClass>();
		//superclass
		try {
			result.add( ctClass.getSuperclass() );
		} catch (NotFoundException e) {
			//do nothing
		}
		//interfaces
		try {
			result.addAll( Arrays.asList( ctClass.getInterfaces() ) );
		} catch (NotFoundException e) {
			//do nothing
		}
		return result;
	}


	private boolean isAbstractMethod(CtMethod ctMethod) {
		return Modifier.isAbstract(ctMethod.getModifiers());
	}

	private void assertionVerifier(CtConstructor ctConstructor) {
		try {
			Assertion annotation = (Assertion) ctConstructor.getAnnotation(Assertion.class);
			String currentAssertion = annotation != null ? annotation.value() : null;

			CtClass superClass;
			String superClassAssertion;
			try {
				superClass = ctConstructor.getDeclaringClass().getSuperclass();
				superClassAssertion = getSuperConstructorExpression(superClass.getConstructor(ctConstructor.getSignature()));
			} catch (NotFoundException e) {
				superClassAssertion = null;
			}
			
			String assertionExpression = getTotalAssert(currentAssertion, superClassAssertion);

			if(assertionExpression != null) {
				String constructorVerification = 
						" { " +
						"	" + "if(!("+ assertionExpression + ")) {" +
						"	" + "	" + "throw new java.lang.RuntimeException(\"The assertion " + assertionExpression + " is false\");" +
						"	" + " } " + 
						" } ";
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

	private String getTotalAssert(String currentAssert, String superAssert) {
		String assertion = null;
		if (superAssert != null) {
			assertion = superAssert;
			if (currentAssert != null) {
				assertion += " && " + currentAssert;
			}
		} else {
			if (currentAssert != null) {
				assertion = currentAssert;
			}
		}
		return assertion;
	}
}
