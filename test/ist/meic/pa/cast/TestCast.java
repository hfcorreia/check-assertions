package ist.meic.pa.cast;

import ist.meic.pa.TestAssertion;
import ist.meic.pa.assertions.CastAssertion;

@CastAssertion({"ist.meic.pa.TestAssertion", "ist.meic.pa.cast.TestCastSuper"})
public class TestCast extends TestCastSuper {

	public static Object m1(Object obj) {
		return obj;
	}

	public static void main(String[] args) {

		//testing a cast to an indirect super class
		try{
			TestCast testCast = new TestCast();
			TestAssertion t = (TestAssertion) m1(testCast);
			printTestResult(System.err, PASS, "cast" , "TestAssertion");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "cast" , "TestAssertion");
		}
		
		//testing a cast to its own class
		try{
			TestCast testCast = new TestCast();
			TestAssertion t = (TestCast) m1(testCast);
			printTestResult(System.err, PASS, "cast" , "TestCast");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "cast" , "TestCast");
		}
		
		//testing a cast to direct super class
		try{
			TestCast testCast = new TestCast();
			Object t = (TestCastSuper) m1(testCast);
			printTestResult(System.err, PASS, "cast" , "TestCastSuper");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "cast" , "TestCastSuper");
		}
		
		//testing a cast to an especified class
		try{
			TestCast testCast = new TestCast();
			Object t = (TestCastSuperSuper) m1(testCast);
			printTestResult(System.err, FAIL, "cast" , "TestCastSuperSuper");
		} catch(RuntimeException e){
			printTestResult(System.err, PASS, "cast" , "TestCastSuperSuper");
		}
	}	
}
