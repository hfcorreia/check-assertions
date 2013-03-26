package ist.meic.pa;

import ist.meic.pa.assertions.ArrayInitializationAssertion;
import ist.meic.pa.assertions.Assertion;
import ist.meic.pa.interceptors.ConstructorInterceptor;
import ist.meic.pa.interceptors.FieldInterceptor;
import ist.meic.pa.interceptors.MethodInterceptor;

import java.util.Arrays;
import java.util.HashSet;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CodeConverter;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.Translator;

/**
 *
 * translator responsible for computing given class bytecodes
 *
 */
public class AssertionsTranslator implements Translator {
	private static final String ARRAY_INTERCEPTOR = "ist.meic.pa.interceptors.ArrayInterceptor";

    @Override
    public void onLoad(ClassPool pool, String className) throws NotFoundException, CannotCompileException {
        CtClass ctClass = pool.get(className);
        if (!className.equals(ARRAY_INTERCEPTOR)) {

            instrumentArrays(pool, ctClass);

			ctClass.instrument(new AssertionExpressionEditor());
			
			HashSet<CtField> allClassFields = new HashSet<CtField>();
			allClassFields.addAll( Arrays.asList( ctClass.getFields() ) );
			allClassFields.addAll( Arrays.asList( ctClass.getDeclaredFields() ) );
			for (CtField ctField : allClassFields) {
				assertionVerifier(ctClass, ctField);
			}
			
			for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
				assertionVerifier(ctClass, ctMethod);
			}

			for (CtConstructor ctConstructor : ctClass.getConstructors()) {
				assertionVerifier(ctConstructor);
			}
		}
	}

    private void assertionVerifier(CtClass ctClass, CtField ctField) {
        if (ctField.hasAnnotation(Assertion.class))
			try {
					Assertion assertion = (Assertion) ctField.getAnnotation(Assertion.class);
					
					FieldInterceptor.createAuxiliaryFields(ctClass, ctField);
					FieldInterceptor.createAuxiliaryMethods(ctClass, ctField, assertion);
					
					CtMethod newReadMethod = CtNewMethod.make(createReadAssertionMethodTemplate(ctField.getName(), ctField.getType().getName(), ctClass.getName()), ctClass);
					
					CtMethod newWriteMethod = CtNewMethod.make(createWriteAssertionMethodTemplate(ctField.getName(), ctField.getType().getName(), assertion.value(), ctClass.getName(), Modifier.isStatic(ctField.getModifiers())),ctClass);
					
					CodeConverter codeConverter = new CodeConverter();
					codeConverter.replaceFieldRead(ctField, ctClass, newReadMethod.getName());
					codeConverter.replaceFieldWrite(ctField, ctClass, newWriteMethod.getName());
					ctClass.instrument(codeConverter);

					ctClass.addMethod(newReadMethod);
					ctClass.addMethod(newWriteMethod);
			} catch (CannotCompileException e) {
				e.printStackTrace();
			} catch (NotFoundException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	}


	private String createReadAssertionMethodTemplate(String fieldName, String methodType, String className) {
		String methodName = fieldName + "_assertion";
		String isInitialized = fieldName + "$isInitialized";
		String template =
				"public static " + methodType + " " + methodName +" (Object target) { " +
					" if( "+ isInitialized +" ){" +
					"	return " + "( ( " + className + " )   target  )." + fieldName + ";" +
					" } " +
					" else { " +
					"	throw new java.lang.RuntimeException(\"Error: " + fieldName + " was not initialized\"); " +
					" } "+
				" } ";
		return template;
	}
	
	private String createWriteAssertionMethodTemplate(String fieldName, String fieldType, String originalAssertExpression, String className, boolean isStaticMethod) {
		String methodName = fieldName + "$writeAssertion";
		String originalField = "((" + className + ") target )." + fieldName;
		String isInitializedTemp = fieldName + "$isInitializedTemp";
		String isInitialized = fieldName + "$isInitialized";
		String originalValueCopy = fieldName + "$tmpValue";
		String isRunningField = fieldName + "$isRunning";
		String prefix = !isStaticMethod ? "((" + className + ") target )." : "";
		String assertExpression = prefix + fieldName+"$assertExpression()";
		
		String template =
				"public static public void " + methodName + " (Object target, " + fieldType + " newValue) { " +
						isRunningField + " = true;" +
						"boolean " + isInitializedTemp + " = " + isInitialized + ";" + 
						
						isInitialized + " = true;" +
						
						fieldType + " " + originalValueCopy + " = " + originalField +  ";" + 
						
						originalField + " =  newValue ;" +
						
						"if( ! " + assertExpression + " ) {" + 
							originalField + " = " + originalValueCopy + ";" + 
							isInitialized + " = " + isInitializedTemp + ";" + 
							"throw new java.lang.RuntimeException(" + createErrorMessage(originalAssertExpression) + ");" + 
						"}" +
				"}";
		return template;
	}

	public static String createErrorMessage(String assertExpression) {
		return "\"The assertion " + assertExpression +" is false\"";
	}

	

	void instrumentArrays(ClassPool pool, CtClass ctClass) throws NotFoundException, CannotCompileException {
        if (ctClass.hasAnnotation(ArrayInitializationAssertion.class)) {
            CtClass arrayClass = pool.get(ARRAY_INTERCEPTOR);
            CodeConverter conv = new CodeConverter();
            conv.replaceArrayAccess(arrayClass, new CodeConverter.DefaultArrayAccessReplacementMethodNames());
            ctClass.instrument(conv);
        }
    }

	@Override
	public void start(ClassPool classPool) throws NotFoundException, CannotCompileException {
		// do nothing
	}

	/**
	 *
	 * method that modifies class methods in order to assert the given expression 
	 * 
	 */
	private void assertionVerifier(CtClass ctClass, CtMethod originalMethod) throws NotFoundException {
		String beforeMethodAssertion = MethodInterceptor.getBeforeMethodAssertionExpression(ctClass, originalMethod);
		String afterMethodAssertion = MethodInterceptor.getAfterMethodAssertionExpression(ctClass, originalMethod);

		if ((!afterMethodAssertion.isEmpty() || !beforeMethodAssertion.isEmpty()) && !Modifier.isAbstract(originalMethod.getModifiers())) {
			try {
				// save a copy of the original method but with a different name.
				String originalMethodName = originalMethod.getName();
				String auxiliarMethodName = originalMethodName + "$auxiliar";

				CtMethod auxiliarMethod = CtNewMethod.copy(originalMethod, auxiliarMethodName, ctClass, null);
				ctClass.addMethod(auxiliarMethod);

				//BEFORE
				String beforeMethodExecutionTemplate = beforeMethodAssertion.isEmpty() ? "" : MethodInterceptor.createBeforeTemplate(beforeMethodAssertion);

				//AFTER
				String afterMethodExecutionTemplate = afterMethodAssertion.isEmpty() ? MethodInterceptor.createOriginalTemplate(auxiliarMethodName) : MethodInterceptor.createAfterTemplate(originalMethod, afterMethodAssertion, auxiliarMethodName);

				String body = " { " + beforeMethodExecutionTemplate + afterMethodExecutionTemplate + " } ";
				originalMethod.setBody(body);

			} catch (CannotCompileException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *
	 * method that modifies class constructors in order to assert the given expression 
	 * 
	 */
	private void assertionVerifier(CtConstructor ctConstructor) {
		try {
			String currentAssertion = ctConstructor.hasAnnotation(Assertion.class) ? ((Assertion) ctConstructor.getAnnotation(Assertion.class)).value() : "";

			String superClassAssertion = ConstructorInterceptor.getSuperClassAssertion(ctConstructor);

			String assertionExpression = MethodInterceptor.unionAsserExpressions(currentAssertion, superClassAssertion);

			if(!assertionExpression.isEmpty()) {
				String constructorVerification = ConstructorInterceptor.createConstructorVerification(assertionExpression);
				ctConstructor.insertAfter(constructorVerification);
			}
		}catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}
}
