package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.*;

public class AssertionExpressionEditor extends ExprEditor {

	private CtClass ctClass;
	private MethodInterceptor methodInterceptor;
	private FieldInterceptor fieldInterceptor;

	public AssertionExpressionEditor(CtClass ctClass) {
		this.ctClass = ctClass;
		this.methodInterceptor = new MethodInterceptor();
		this.fieldInterceptor = new FieldInterceptor();
	}

	@Override
	public void edit(MethodCall methodCall) throws CannotCompileException {
		try {
//			System.out.println("MethodCall w/ method: " + methodCall.getMethodName());
			String assertionExpr = methodInterceptor.recursiveAssert(ctClass, methodCall);
			if(assertionExpr != null) {
				String postMethod = methodInterceptor.createMethodBody(assertionExpr);
				methodCall.getMethod().insertAfter(postMethod);
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void edit(FieldAccess fieldAccess) throws CannotCompileException {
//		System.out.println("FieldAcess w/ field: " + fieldAccess.getFieldName());
		try {
			if(fieldAccess.getField().hasAnnotation(Assertion.class)){
				fieldInterceptor.createAuxiliaryFields(ctClass, fieldAccess.getField());

				Assertion assertion = (Assertion) fieldAccess.getField().getAnnotation(Assertion.class);

				if(fieldAccess.isWriter()){
					fieldAccess.replace(fieldInterceptor.createWriteFieldBody(assertion.value(), fieldAccess.getFieldName()));
				} 
				else {
					if(fieldAccess.isReader()) {
						fieldAccess.replace(fieldInterceptor.createReadFieldBody(fieldAccess.getFieldName()));
					}
				}
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void edit(ConstructorCall constructorCall) {
//		System.out.println("ConstructorCall w/ cena: " + constructorCall.getSignature() + " " + constructorCall.getMethodName());
		try {
			Assertion anot = (Assertion) constructorCall.getConstructor().getAnnotation(Assertion.class);
			String assertionExpr = anot != null ? anot.value() : null;
			
//			System.out.println("const cal? " + constructorCall.getClassName());
//			System.out.println("constructor? " + constructorCall.getConstructor().getLongName());
//			System.out.println("anot null? " + anot);
//			System.out.println("ASSERTION: " + assertionExpr);
			
			if(assertionExpr != null) {
				String postMethod = "if(!("+ assertionExpr + ")) {"
						+ "throw new java.lang.RuntimeException(\"The assertion " + assertionExpr + " is false\");"
						+ "}";
				constructorCall.getMethod().insertBefore(postMethod);
			}
		} catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
//	
//	@Override
//	public void edit(NewArray newArray) {
//		
//	}
//	
//	@Override
//	public void edit(Cast c) {
//		
//	}
//	
//	@Override
//	public void edit(Handler handler) {
//		
//	}
//	
//	@Override
//	public void edit(NewExpr expr) {
//		
//	}
}
