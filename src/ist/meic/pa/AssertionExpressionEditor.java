package ist.meic.pa;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.reflect.Modifier;

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
				/*
				 * o que se passa? 
				 * estamos a alterar o methodCall em vez do metodo. como quem diz que em vez de no teste haver testReturn.m2(10) existe algo tipo:
				 * testReturn.m2(10); if($_ < testeField) ....
				 * ou seja, no main do test estamos a tentar aceder ao campo, dai o erro
				 * solucao:
				 * obter metodo e alterar
				 * como?
				 * o prof disse que havia um CtMethod.replace mas o meu eclipse diz q nao. existe tambem um insertAfter/Before
				 */
				String ass = methodInterceptor.recursiveAssertExpression(ctClass, methodCall.getMethodName(), methodCall.getSignature());
				
				String postMethod = 
						"if(!("+ ass + ")) {"
								+ "throw new java.lang.RuntimeException(\"The assertion " + ass + " is false\");"
						+ "}";
				
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
