package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ConstructorCall;
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
            String assertionExpr = methodInterceptor.recursiveAssert(ctClass, methodCall);
            if (assertionExpr != null) {
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
        try {
            if (fieldAccess.getField().hasAnnotation(Assertion.class)) {
                fieldInterceptor.createAuxiliaryFields(ctClass, fieldAccess.getField());

                Assertion assertion = (Assertion) fieldAccess.getField().getAnnotation(Assertion.class);

                if (fieldAccess.isWriter()) {
                    System.out.println(fieldAccess.getField().getType().isArray());
                    fieldAccess.replace(fieldInterceptor.createWriteFieldBody(assertion.value(), fieldAccess.getFieldName()));
                } else {
                    if (fieldAccess.isReader()) {
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
        try {
            CtBehavior constructor = constructorCall.where();
            Assertion annotation = (Assertion) constructor.getAnnotation(Assertion.class);
            String assertionExpression = annotation != null ? annotation.value() : null;

            if (assertionExpression != null) {
                String constructorVerification =
                        "if(!(" + assertionExpression + ")) {" + "throw new java.lang.RuntimeException(\"The assertion "
                                + assertionExpression + " is false\");" + "}";
                constructor.insertBefore(constructorVerification);
            }
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
