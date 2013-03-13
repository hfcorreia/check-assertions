package ist.meic.pa;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.Translator;

public class AssertionsTranslator implements Translator {

	@Override
	public void onLoad(ClassPool pool, String className) throws NotFoundException,
	CannotCompileException {
		CtClass ctClass = pool.get(className);
		ctClass.instrument(new AssertionExpressionEditor(ctClass));
	}

	@Override
	public void start(ClassPool arg0) throws NotFoundException,
	CannotCompileException {
	}

}
