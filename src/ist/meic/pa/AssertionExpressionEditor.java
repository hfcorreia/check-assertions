package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.Cast;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.Handler;

public class AssertionExpressionEditor extends ExprEditor {

	private CtClass ctClass;
	private FieldInterceptor fieldInterceptor;

	public AssertionExpressionEditor(CtClass ctClass) {
		this.ctClass = ctClass;
		this.fieldInterceptor = new FieldInterceptor();
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
	public void edit(Cast castExpression) {
		try {
			String[] assertions = ((CastAssertion) castExpression.getEnclosingClass().getAnnotation(CastAssertion.class)).value();
			
			String replacingCastExpr = "if(" + generateIfCastCondition(castExpression, assertions) + ") {" + "$_ = $proceed($$);" + "}" + "else {" + "throw new RuntimeException(" + createCastErrorMessage(castExpression) + ");" + "}";
			castExpression.replace(replacingCastExpr);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			e.printStackTrace();
		}
	}

	private String generateIfCastCondition(Cast castExpression, String[] assertions) throws NotFoundException {
		String assertion = null;
		
		for(String s : assertions) {
			if(s.equals(castExpression.getType().getName())) {
				assertion = castExpression.getType().getName();
				break;
			}
		}
		return "(\"" + castExpression.getType().getName() + "\"" + ".equals(\"" + assertion + "\")) || " + getOwnCastExpression(castExpression);
	}

	private String getOwnCastExpression(Cast castExpression) {
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
	
	@Override
	public void edit(Handler handler) {
		System.out.println("handling exception at " + handler.getEnclosingClass().getName());
	}
}
