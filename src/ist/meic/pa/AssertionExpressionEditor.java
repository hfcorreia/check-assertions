package ist.meic.pa;

import ist.meic.pa.assertions.CastAssertion;
import ist.meic.pa.assertions.ExceptionAssertion;
import ist.meic.pa.interceptors.CastInterceptor;
import ist.meic.pa.interceptors.ExceptionInterceptor;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.Cast;
import javassist.expr.ExprEditor;
import javassist.expr.Handler;

public class AssertionExpressionEditor extends ExprEditor {

	/**
	 * 
	 * Garantees only desired casts are made
	 * 
	 */
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

	/**
	 * 
	 * Calls desired method on catch clauses for specified Exceptions
	 * 
	 */
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
