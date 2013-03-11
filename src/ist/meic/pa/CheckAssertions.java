package ist.meic.pa;

import java.lang.reflect.Method;
import java.util.Arrays;

import javassist.ClassPool;
import javassist.CtClass;

public class CheckAssertions {

	public static void main(String[] args) throws Throwable {
		if (args.length < 2) {
			System.err.println("Usage: java Memoize <class> <method>");
			System.exit(1);
		} else {
			String className = args[0];
			String methodName = args[1];
			ClassPool pool = ClassPool.getDefault();
			CtClass ctClass = pool.get(className);

			Object classAnnotations[] = ctClass.getAnnotations();
			
			//this expression should be read from the annotation
			String assertExpression = "$1>5";
			
//			MethodInterceptor.interceptMethod(ctClass, methodName, assertExpression);

			FieldInterceptor.interceptField(ctClass, methodName, assertExpression);
			
			Class<?> rtClass = ctClass.toClass();

			Method main = rtClass.getMethod("main", args.getClass());
			String[] methodArgs = Arrays.copyOfRange(args, 2, args.length);
			main.invoke(null, new Object[] { methodArgs });

		}
	}

}
