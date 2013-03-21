package ist.meic.pa.array;

import ist.meic.pa.Assertion;
import ist.meic.pa.TestAssertion;

public class TestArrayInitializer extends TestAssertion {

    public TestArrayInitializer() {
    }

    @Assertion("strings.length == 2")
    String[] strings ;

    public void testArray() {
        strings = new String[2];
    }

    public static void main(String[] args) {
        try{
            TestArrayInitializer test = new TestArrayInitializer();
            test.testArray();
            printTestResult(System.out, PASS, "testArray", "");
        } catch(RuntimeException e){
            printTestResult(System.err, FAIL, "testArray", "");
        }

 
    }
}
