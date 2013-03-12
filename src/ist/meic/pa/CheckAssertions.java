package ist.meic.pa;

import java.lang.reflect.Method;
import java.util.Arrays;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;

public class CheckAssertions {
 
	public static void main(String[] args) throws Throwable {
		if (args.length < 2) {
			System.err.println("Usage: java Memoize <class> <method>");
			System.exit(1);
		}

		String className = args[0];
		String methodName = args[1];

		ClassPool pool = ClassPool.getDefault();
		CtClass ctClass = pool.get(className);

		// Annotated methods

		for (CtMethod method : ctClass.getMethods()) {
			if (method.hasAnnotation(Assertion.class)) {
				MethodInterceptor methodInterceptor = new MethodInterceptor();
				Assertion assertAnnotation = (Assertion) method
						.getAnnotation(Assertion.class);
				methodInterceptor.interceptMethod(ctClass, methodName,
						assertAnnotation.value());
			}
		}

		// Annotated fields

		for (CtField field : ctClass.getFields()) {
			if (field.hasAnnotation(Assertion.class)) {
				FieldInterceptor fieldInterceptor = new FieldInterceptor();
				Assertion assertAnnotation = (Assertion) field
						.getAnnotation(Assertion.class);
				fieldInterceptor.interceptField(ctClass, methodName,
						assertAnnotation.value());
			}
		}

		// execute main of the class given as argument

		Class<?> rtClass = ctClass.toClass();
		Method main = rtClass.getMethod("main", args.getClass());
		String[] methodArgs = Arrays.copyOfRange(args, 2, args.length);
		main.invoke(null, new Object[] { methodArgs });

	}

}
