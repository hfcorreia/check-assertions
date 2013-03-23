package ist.meic.pa;

import ist.meic.pa.assertions.Assertion;
import ist.meic.pa.assertions.CastAssertion;
import ist.meic.pa.assertions.ExceptionAssertion;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.Cast;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.Handler;

public class AssertionExpressionEditor extends ExprEditor {

	@Override
	public void edit(FieldAccess fieldAccess) {
		CtClass ctClass = fieldAccess.getEnclosingClass();
		
		try {
			if(fieldAccess.getField().hasAnnotation(Assertion.class)){
				FieldInterceptor.createAuxiliaryFields(ctClass, fieldAccess.getField());

				Assertion assertion = (Assertion) fieldAccess.getField().getAnnotation(Assertion.class);

				if(fieldAccess.isWriter()){
					fieldAccess.replace(FieldInterceptor.createWriteFieldBody(assertion.value(), fieldAccess.getFieldName()));
				} 
				else {
					if(fieldAccess.isReader()) {
						fieldAccess.replace(FieldInterceptor.createReadFieldBody(fieldAccess.getFieldName()));
					}
				}
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void edit(Cast castExpression) {
		try {
			String[] castingClasses = ((CastAssertion) castExpression.getEnclosingClass().getAnnotation(CastAssertion.class)).value();

			String verifiedCastExpr = 
					"if(" + generateCastAssertion(castExpression, castingClasses) + ") {" + 
							"$_ = $proceed($$);" + 
					"}" + 
					"else {" + 
						"throw new RuntimeException(" + createCastErrorMessage(castExpression) + ");" + 
					"}";

			castExpression.replace(verifiedCastExpr);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void edit(Handler handler) {
		try {
			CtClass ctClass = handler.where().getDeclaringClass();
			
			if(ctClass.hasAnnotation(ExceptionAssertion.class)) {
				ExceptionAssertion anotation = (ExceptionAssertion) ctClass.getAnnotation(ExceptionAssertion.class);
				
				if(exceptionPresent(handler, anotation.exception())) {
					String invocatingMethod = 
							"{" + 
									"try {" +
										ctClass.getName() + ".class.getMethod(\"" + anotation.method() + "\", null).invoke(new " + ctClass.getName() + "(), null);" +
									"} catch (Exception e) { /* do nothing */ }" +
							"}";
					handler.insertBefore(invocatingMethod);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			e.printStackTrace();
		}
	}
	
	private String generateCastAssertion(Cast castExpression, String[] assertions) throws NotFoundException {
		String assertion = null;

		for(String s : assertions) {
			if(s.equals(castExpression.getType().getName())) {
				assertion = castExpression.getType().getName();
				break;
			}
		}
		
		return "(\"" + castExpression.getType().getName() + "\"" + ".equals(\"" + assertion + "\")) || " + getSelfCastExpression(castExpression);
	}

	private String getSelfCastExpression(Cast castExpression) {
		try {
			String castingClass = castExpression.getEnclosingClass().getName();
			String castClass = castExpression.getType().getName();
			
			return "(\"" + castingClass + "\"" + ".equals(\"" + castClass + "\"))";  
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return "" + false;
	}

	private String createCastErrorMessage(Cast castExpression) throws NotFoundException {
		return "\"cast not allowed from class " + "<" + castExpression.getEnclosingClass().getName() + "> to " + "<" + castExpression.getType().getName() + ">\"";
	}
	
	private boolean exceptionPresent(Handler handler, String[] exceptions)  {
		for(String s : exceptions) {
			try {
				if(handler.getType().getName().equals(s)) {
					return true;
				}
			} catch (NotFoundException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
