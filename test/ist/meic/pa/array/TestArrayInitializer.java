package ist.meic.pa.array;

import ist.meic.pa.TestAssertion;
import ist.meic.pa.assertions.ArrayInitializationAssertion;

@ArrayInitializationAssertion
public class TestArrayInitializer extends TestAssertion {


    public TestArrayInitializer() {
    }

    public void intAccess() {
        int[][] ints = new int[2][2];
        ints[1][1] = 1;
        int x = ints[1][1];
    }

    public void intAccess2() {
        int[][] ints = new int[2][2];
        ints[0][1] = 1;
        int x = ints[1][1];
    }

    public void IntegerAccess() {
        Integer[][] integer = new Integer[2][2];
        integer[0][1] = 2;
        Integer x = integer[0][1];
    }

    public void IntegerAccess2() {
        Integer[][] integer = new Integer[3][3];
        integer[1][2] = 1;
        integer[0][0] = 0;
        Integer x = integer[1][2];
        x = integer[0][1];
    }

    public void charAccess() {
        char[][] chars = new char[2][2];
        chars[1][1] = 'a';
        char x = chars[1][1];
    }

    public void charAccess2() {
        char[][] chars = new char[2][2];
        chars[0][1] = 'b';
        char x = chars[1][1];
    }

    public void longAccess() {
        long[][] longs = new long[2][2];
        longs[1][1] = 3;
        long x = longs[1][1];
    }

    public void longAccess2() {
        long[][] longs = new long[2][2];
        longs[0][1] = 1;
        long x = longs[1][1];
    }

    public void shortAccess() {
        short[][] shorts = new short[2][2];
        shorts[1][1] = 42;
        short x = shorts[1][1];
    }

    public void shortAccess2() {
        short[][] shorts = new short[2][2];
        shorts[0][1] = 32;
        short x = shorts[1][1];
    }

    public void doubleAccess() {
        double[][] doubles = new double[2][2];
        doubles[1][1] = 4.2;
        double x = doubles[1][1];
    }

    public void doubleAccess2() {
        double[][] doubles = new double[2][2];
        doubles[0][1] = 3.2;
        double x = doubles[1][1];
    }

    public void floatAccess() {
        float[][] floats = new float[2][2];
        floats[1][1] = 4.2f;
        float x = floats[1][1];
    }

    public void floatAccess2() {
        float[][] floats = new float[2][2];
        floats[0][1] = 3.2f;
        float x = floats[1][1];
    }

    public void byteAccess() {
        byte[][] bytes = new byte[2][2];
        bytes[1][1] = 1;
        byte x = bytes[1][1];
    }

    public void byteAccess2() {
        byte[][] bytes = new byte[2][2];
        bytes[0][1] = 0;
        byte x = bytes[1][1];
    }

    public void booleanAccess() {
        boolean[][] booleans = new boolean[2][2];
        booleans[1][1] = false;
        boolean x = booleans[1][1];
    }

    public void booleanAccess2() {
        boolean[][] booleans = new boolean[2][2];
        booleans[0][1] = true;
        boolean x = booleans[1][1];
    }

    public static void main(String[] args) {
        try {
            TestArrayInitializer test = new TestArrayInitializer();
            test.intAccess();
            printTestResult(System.out, PASS, "intAccess", "");
        } catch (RuntimeException e) {
            printTestResult(System.err, FAIL, "intAccess", "");
        }

        try {
            TestArrayInitializer test = new TestArrayInitializer();
            test.intAccess2();
            printTestResult(System.out, FAIL, "intAccess2", "");
        } catch (RuntimeException e) {
            printTestResult(System.err, PASS, "intAccess2", "");
        }

        try {
            TestArrayInitializer test = new TestArrayInitializer();
            test.IntegerAccess();
            printTestResult(System.out, PASS, "IntegerAccess", "");
        } catch (RuntimeException e) {
            e.printStackTrace();
            printTestResult(System.err, FAIL, "IntegerAccess", "");
        }

        try {
            TestArrayInitializer test = new TestArrayInitializer();
            test.IntegerAccess2();
            printTestResult(System.out, FAIL, "IntegerAccess2", "");
        } catch (RuntimeException e) {
            printTestResult(System.err, PASS, "IntegerAccess2", "");
        }

        try {
            TestArrayInitializer test = new TestArrayInitializer();
            test.charAccess();
            printTestResult(System.out, PASS, "charAccess", "");
        } catch (RuntimeException e) {
            printTestResult(System.err, FAIL, "charAccess", "");
        }

        try {
            TestArrayInitializer test = new TestArrayInitializer();
            test.charAccess2();
            printTestResult(System.out, FAIL, "charAccess2", "");
        } catch (RuntimeException e) {
            printTestResult(System.err, PASS, "charAccess2", "");
        }

        try {
            TestArrayInitializer test = new TestArrayInitializer();
            test.longAccess();
            printTestResult(System.out, PASS, "longAccess", "");
        } catch (RuntimeException e) {
            printTestResult(System.err, FAIL, "longAccess", "");
        }

        try {
            TestArrayInitializer test = new TestArrayInitializer();
            test.longAccess2();
            printTestResult(System.out, FAIL, "longAccess2", "");
        } catch (RuntimeException e) {
            printTestResult(System.err, PASS, "longAccess2", "");
        }

        try {
            TestArrayInitializer test = new TestArrayInitializer();
            test.shortAccess();
            printTestResult(System.out, PASS, "shortAccess", "");
        } catch (RuntimeException e) {
            printTestResult(System.err, FAIL, "shortAccess", "");
        }

        try {
            TestArrayInitializer test = new TestArrayInitializer();
            test.shortAccess2();
            printTestResult(System.out, FAIL, "shortAccess2", "");
        } catch (RuntimeException e) {
            printTestResult(System.err, PASS, "shortAccess2", "");
        }

        try {
            TestArrayInitializer test = new TestArrayInitializer();
            test.doubleAccess();
            printTestResult(System.out, PASS, "doubleAccess", "");
        } catch (RuntimeException e) {
            printTestResult(System.err, FAIL, "doubleAccess", "");
        }

        try {
            TestArrayInitializer test = new TestArrayInitializer();
            test.doubleAccess2();
            printTestResult(System.out, FAIL, "doubleAccess2", "");
        } catch (RuntimeException e) {
            printTestResult(System.err, PASS, "doubleAccess2", "");
        }

        try {
            TestArrayInitializer test = new TestArrayInitializer();
            test.floatAccess();
            printTestResult(System.out, PASS, "floatAccess", "");
        } catch (RuntimeException e) {
            printTestResult(System.err, FAIL, "floatAccess", "");
        }

        try {
            TestArrayInitializer test = new TestArrayInitializer();
            test.floatAccess2();
            printTestResult(System.out, FAIL, "floatAccess2", "");
        } catch (RuntimeException e) {
            printTestResult(System.err, PASS, "floatAccess2", "");
        }

        try {
            TestArrayInitializer test = new TestArrayInitializer();
            test.byteAccess();
            printTestResult(System.out, PASS, "byteAccess", "");
        } catch (RuntimeException e) {
            printTestResult(System.err, FAIL, "byteAccess", "");
        }

        try {
            TestArrayInitializer test = new TestArrayInitializer();
            test.byteAccess2();
            printTestResult(System.out, FAIL, "byteAccess2", "");
        } catch (RuntimeException e) {
            printTestResult(System.err, PASS, "byteAccess2", "");
        }

        try {
            TestArrayInitializer test = new TestArrayInitializer();
            test.booleanAccess();
            printTestResult(System.out, PASS, "booleanAccess", "");
        } catch (RuntimeException e) {
            printTestResult(System.err, FAIL, "booleanAccess", "");
        }

        try {
            TestArrayInitializer test = new TestArrayInitializer();
            test.booleanAccess2();
            printTestResult(System.out, FAIL, "booleanAccess2", "");
        } catch (RuntimeException e) {
            printTestResult(System.err, PASS, "booleanAccess2", "");
        }
    }
}
