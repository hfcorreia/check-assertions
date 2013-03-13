package ist.meic.pa;

import java.lang.reflect.Method;
import java.util.Arrays;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

public class CheckAssertions {

	public static void main(String[] args) throws Throwable {
		if (args.length < 2) {
			System.err.println("Usage: <class> <arguments>");
			System.exit(1);
		}

		String className = args[0];
		String methodName = args[1];

		ClassPool pool = ClassPool.getDefault();
		CtClass ctClass = pool.get(className);

		// Annotated methods

		for (CtMethod ctMethod : ctClass.getMethods()) {
			if (ctMethod.hasAnnotation(Assertion.class)) {
				MethodInterceptor methodInterceptor = new MethodInterceptor();
				methodInterceptor.interceptMethod(ctClass, methodName, recursiveAssertExpression(ctClass, ctMethod.getName(), ctMethod.getSignature()));
			}
		}
  
		// Annotated fields
		for (CtField field : ctClass.getFields()) {
			if (field.hasAnnotation(Assertion.class)) {
				FieldInterceptor fieldInterceptor = new FieldInterceptor();
				Assertion assertAnnotation = (Assertion) field.getAnnotation(Assertion.class);
				fieldInterceptor.interceptField(ctClass, methodName, assertAnnotation.value());
			}
		}

		// execute main of the class given as argument

		Class<?> rtClass = ctClass.toClass();
		Method main = rtClass.getMethod("main", args.getClass());
		String[] methodArgs = Arrays.copyOfRange(args, 2, args.length);
		main.invoke(null, new Object[] { methodArgs });

	}

//	private static String getHierarquicalAssertExpression(CtClass ctClass, CtMethod ctMethod) throws ClassNotFoundException, NotFoundException {
//		//TODO
//		Assertion assertion = ((Assertion) ctMethod.getAnnotation(Assertion.class));
//		return assertion != null ? assertion.value() : "";
//	}
	
	private static String recursiveAssertExpression(CtClass ctClass, String methodName, String methodDesc) throws Exception {
		if( ctClass.getSuperclass()!=null ){
			String superClassExpression = recursiveAssertExpression(ctClass.getSuperclass(), methodName, methodDesc);

			CtMethod ctMethod = getMethod(ctClass, methodName, methodDesc);
			return superClassExpression + ( ctMethod!=null ? getAssertExpression(ctClass, ctMethod) : "" );
		} else {
			return "";
		}
	}
	
	private static CtMethod getMethod(CtClass ctClass, String methodName, String methodDesc){
		try {
			return ctClass.getMethod(methodName, methodDesc);
		} catch (NotFoundException e) {
			return null;
		}
	}
	
	private static String getAssertExpression(CtClass ctClass, CtMethod ctMethod) throws ClassNotFoundException {
		return ctMethod.hasAnnotation(Assertion.class) ? ((Assertion) ctMethod.getAnnotation(Assertion.class)).value() : "";
	}
	
}
