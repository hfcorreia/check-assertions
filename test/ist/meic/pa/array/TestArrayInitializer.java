package ist.meic.pa.array;

import ist.meic.pa.ArrayInitializationAssertion;
import ist.meic.pa.TestAssertion;

@ArrayInitializationAssertion
public class TestArrayInitializer extends TestAssertion {

    public TestArrayInitializer() {
    }

    int[][] strings ;

    public void testArray() {
        strings = new int[2][2];
       strings[1][1] = 1;
//     int x = strings[1][1];
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
