package ist.meic.pa;

import javassist.CtClass;
import javassist.CtMethod;

public class ArrayInterceptor {
    private CtClass ctClass;
    private CtMethod ctMethod;
    
    public ArrayInterceptor(CtClass ctClass, CtMethod ctMethod) {
        this.ctClass = ctClass;
        this.ctMethod = ctMethod;
    }

    public static void arrayWriteInt(Object obj, int index, int newValue) {
       int[] array = (int[]) obj;
       array[index] = newValue;
    }

    public static int arrayReadInt(Object obj, int index) {
        int[] array = (int[]) obj;
        return array[index];
    }
}
