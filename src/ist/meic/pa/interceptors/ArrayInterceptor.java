package ist.meic.pa.interceptors;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * ArrayInterceptor - defines static methods to replace array access.
 *      Maintains a HashMap of arrays with already initialized indexes of an array.
 *
 */
public class ArrayInterceptor {
    public static HashMap<Object, ArrayList<Boolean>> arrays = new HashMap<Object, ArrayList<Boolean>>();

    public static void arrayWriteInt(Object obj, int index, int newValue) {
        int[] array = (int[]) obj;
        array[index] = newValue;

        initialize(obj, index, array.length);
    }

    public static int arrayReadInt(Object obj, int index) throws RuntimeException {
        testAccess(obj, index);
        int[] array = (int[]) obj;
        return array[index];
    }

    public static void arrayWriteChar(Object obj, int index, char newValue) {
        char[] array = (char[]) obj;
        array[index] = newValue;

        initialize(obj, index, array.length);
    }

    public static char arrayReadChar(Object obj, int index) throws RuntimeException {
        testAccess(obj, index);
        char[] array = (char[]) obj;
        return array[index];
    }

    public static void arrayWriteLong(Object obj, int index, long newValue) {
        long[] array = (long[]) obj;
        array[index] = newValue;

        initialize(obj, index, array.length);
    }

    public static long arrayReadLong(Object obj, int index) throws RuntimeException {
        testAccess(obj, index);
        long[] array = (long[]) obj;
        return array[index];
    }

    public static void arrayWriteShort(Object obj, int index, short newValue) {
        short[] array = (short[]) obj;
        array[index] = newValue;

        initialize(obj, index, array.length);
    }

    public static short arrayReadShort(Object obj, int index) throws RuntimeException {
        testAccess(obj, index);
        short[] array = (short[]) obj;
        return array[index];
    }

    public static void arrayWriteDouble(Object obj, int index, double newValue) {
        double[] array = (double[]) obj;
        array[index] = newValue;

        initialize(obj, index, array.length);
    }

    public static double arrayReadDouble(Object obj, int index) throws RuntimeException {
        testAccess(obj, index);
        double[] array = (double[]) obj;
        return array[index];
    }

    public static void arrayWriteFloat(Object obj, int index, float newValue) {
        float[] array = (float[]) obj;
        array[index] = newValue;

        initialize(obj, index, array.length);
    }

    public static float arrayReadFloat(Object obj, int index) throws RuntimeException {
        testAccess(obj, index);
        float[] array = (float[]) obj;
        return array[index];
    }

    public static void arrayWriteByteOrBoolean(Object obj, int index, byte newValue) {
        if (obj instanceof byte[]) {
            byte[] array = (byte[]) obj;
            array[index] = newValue;

            initialize(obj, index, array.length);

        } else if (obj instanceof boolean[]) {
            boolean[] array = (boolean[]) obj;
            array[index] = (newValue != 0);

            initialize(obj, index, array.length);
        }

    }

    public static byte arrayReadByteOrBoolean(Object obj, int index) throws RuntimeException {
        testAccess(obj, index);

        if (obj instanceof boolean[]) {
            boolean[] array = (boolean[]) obj;
            if (array[index])
                return (byte) 1;
            else
                return (byte) 0;
        }

        byte[] array = (byte[]) obj;
        return array[index];

    }

    public static Object arrayReadObject(Object obj, int index) {
        if (obj.getClass().isArray()) {
            Object[] objectArray = (Object[]) obj;

            if (objectArray[index] == null)
                throw new RuntimeException("Array not initialized!");

            return objectArray[index];
        } else {
            System.out.println("Sou " + obj.getClass().getName());
            testAccess(obj, index);
        }
        return obj;
    }

    public static void arrayWriteObject(Object obj, int index, Object newValue) {
        Object[] arrayObject = (Object[]) obj;
        initialize(obj, index, arrayObject.length);
        arrayObject[index] = newValue;
    }

    static void testAccess(Object obj, int index) throws RuntimeException {
        if (!arrays.containsKey(obj) || !arrays.get(obj).get(index)) {
            throw new RuntimeException("Array is not initialized at " + index);
        }
    }

    static void initialize(Object obj, int index, int length) {
        ArrayList<Boolean> isInitialized = new ArrayList<Boolean>();

        for (int i = 0; i < length; i++) {
            if (i == index)
                isInitialized.add(true);
            else
                isInitialized.add(false);
        }

        arrays.put(obj, isInitialized);
    }
}
