package ist.meic.pa;

import ist.meic.pa.assertions.ArrayInitializationAssertion;
import ist.meic.pa.assertions.Assertion;
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

            instrumentArrays(pool, ctClass);

			ctClass.instrument(new AssertionExpressionEditor());

			for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
				assertionVerifier(ctClass, ctMethod);
			}

			for (CtConstructor ctConstructor : ctClass.getConstructors()) {
				assertionVerifier(ctConstructor);
			}
		}
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
