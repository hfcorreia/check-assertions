package ist.meic.pa.array;

import ist.meic.pa.TestAssertion;
import ist.meic.pa.assertions.Assertion;

public class TestArrayInitializer extends TestAssertion {

    public TestArrayInitializer() {
    }

    @Assertion("strings.length == 2")
    int[] strings ;

    public void testArray() {
        strings = new int[2];
        strings[1] = 1;
        int x = strings[1];
    }

    public static void main(String[] args) {
        try{
            TestArrayInitializer test = new TestArrayInitializer();
            test.testArray();
            printTestResult(System.out, PASS, "testArray", "");
        } catch(RuntimeException e){
            e.printStackTrace();
            printTestResult(System.err, FAIL, "testArray", "");
        }

 
    }
}
