package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.NewArray;

public class AssertionExpressionEditor extends ExprEditor {

    private CtClass ctClass;
    private FieldInterceptor fieldInterceptor;

    public AssertionExpressionEditor(CtClass ctClass) {
        this.ctClass = ctClass;
        this.fieldInterceptor = new FieldInterceptor();
    }

    @Override
    public void edit(FieldAccess fieldAccess) throws CannotCompileException {
        try {
            if (fieldAccess.getField().hasAnnotation(Assertion.class) ) {
                fieldInterceptor.createAuxiliaryFields(ctClass, fieldAccess.getField());

                Assertion assertion = (Assertion) fieldAccess.getField().getAnnotation(Assertion.class);

                if (fieldAccess.isWriter()) {
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
    public void edit(NewArray newArray) {
        int arrayDimension = newArray.getDimension();
        for (int i = 0; i < arrayDimension; i++) {

        }
    }
}
