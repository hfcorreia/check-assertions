package ist.meic.pa;


public class ArrayInterceptor {
   
    public ArrayInterceptor() {
    }

    public static void arrayWriteInt(Object obj, int index, int newValue) {
       int[] array = (int[]) obj;
       array[index] = newValue;
    }

    public static int arrayReadInt(Object obj, int index) {
        int[] array = (int[]) obj;
        return array[index];
    }
    
    public static void arrayWriteObject(Object obj, int index, Object newValue) {
    }
}
