package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;

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
			if(methodCall.getMethod().hasAnnotation(Assertion.class)){
				methodCall.replace(methodInterceptor.createMethodBody(methodInterceptor.recursiveAssertExpression(ctClass, methodCall.getMethodName(), methodCall.getSignature())));
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void edit(FieldAccess fieldAccess) throws CannotCompileException {
		try {
			if(fieldAccess.getField().hasAnnotation(Assertion.class)){
				fieldInterceptor.createAuxiliaryFields(ctClass, fieldAccess.getField());
				Assertion assertion = (Assertion) fieldAccess.getField().getAnnotation(Assertion.class);
				if(fieldAccess.isWriter()){
					fieldAccess.replace(fieldInterceptor.createWriteFieldBody(assertion.value(), fieldAccess.getFieldName()));
				} else if(fieldAccess.isReader()) {
					fieldAccess.replace(fieldInterceptor.createReadFieldBody(fieldAccess.getFieldName()));
				}
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}