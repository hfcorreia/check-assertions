package ist.meic.pa.cast;

import ist.meic.pa.CastAssertion;
import ist.meic.pa.TestAssertion;

@CastAssertion("ist.meic.pa.TestAssertion")
public class TestCast extends TestCastSuper {

	public static Object m1(Object obj1) {
		return obj1;
	}
	
	public static void main(String[] args) {

		try{
			TestCast testCast = new TestCast();
			TestAssertion t = (TestAssertion) m1(testCast);
			printTestResult(System.err, PASS, "cast" , "TestAssertion");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "cast" , "TestAssertion");
		}
		
		try{
			TestCast testCast = new TestCast();
			TestAssertion t = (TestCast) m1(testCast);
			printTestResult(System.err, PASS, "cast" , "TestCast");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "cast" , "TestCast");
		}
		
		try{
			TestCast testCast = new TestCast();
			Object t = (TestCastSuper) m1(testCast);
			printTestResult(System.err, FAIL, "cast" , "Object");
		} catch(RuntimeException e){
			printTestResult(System.err, PASS, "cast" , "Object");
		}
	}	
}
