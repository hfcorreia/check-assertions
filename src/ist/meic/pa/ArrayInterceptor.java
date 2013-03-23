package ist.meic.pa;

import java.util.ArrayList;
import java.util.HashMap;

public class ArrayInterceptor {
    public static HashMap<Object, ArrayList<Boolean>> arrays = new HashMap<Object, ArrayList<Boolean>>();

    public static void arrayWriteInt(Object obj, int index, int newValue) {
        int[] array = (int[]) obj;
        array[index] = newValue;
        
        ArrayList<Boolean> isInitialized = new ArrayList<Boolean>();
        
        for(int i = 0; i < array.length ; i++) {
            if(i == index)
                isInitialized.add(true);
            else
                isInitialized.add(false);
        }
        
        arrays.put(obj, isInitialized);
    }

    public static int arrayReadInt(Object obj, int index) {
       
        if( !arrays.containsKey(obj) || !arrays.get(obj).get(index) )  {
            throw new RuntimeException("Array is not initialized at " + index);
        }            
        int[] array = (int[]) obj;
        return array[index];
    }

    public static Object arrayReadObject(Object obj, int index) {
        if(obj.getClass().isArray())
            System.out.println("sou Array");
        return obj;
    }
    public static void arrayWriteObject(Object obj, int index, Object newValue) {
     
    }
}
