package ist.meic.pa;

import ist.meic.pa.assertions.Assertion;
import ist.meic.pa.assertions.CastAssertion;
import ist.meic.pa.assertions.ExceptionAssertion;
import ist.meic.pa.interceptors.CastInterceptor;
import ist.meic.pa.interceptors.ExceptionInterceptor;
import ist.meic.pa.interceptors.FieldInterceptor;
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
			if(castExpression.getEnclosingClass().hasAnnotation(CastAssertion.class)) {
				String[] castingClasses = ((CastAssertion) castExpression.getEnclosingClass().getAnnotation(CastAssertion.class)).value();
				String verifiedCastExpr = CastInterceptor.createCastTemplate(castExpression, castingClasses);
				castExpression.replace(verifiedCastExpr);
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
	public void edit(Handler handler) {
		try {
			CtClass ctClass = handler.where().getDeclaringClass();

			if(ctClass.hasAnnotation(ExceptionAssertion.class)) {
				ExceptionAssertion anotation = (ExceptionAssertion) ctClass.getAnnotation(ExceptionAssertion.class);

				if(ExceptionInterceptor.exceptionPresent(handler, anotation.exception())) {
					String invocatingMethod = ExceptionInterceptor.createCatchTemplate(ctClass, anotation);
					handler.insertBefore(invocatingMethod);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			e.printStackTrace();
		}
	}
}
