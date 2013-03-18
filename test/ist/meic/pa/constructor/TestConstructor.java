package ist.meic.pa.constructor;

import ist.meic.pa.Assertion;
import ist.meic.pa.TestAssertion;
import ist.meic.pa.methodReturn.TestMethodReturn;

public class TestConstructor extends TestAssertion {

	int field = 4;

	@Assertion("$1 > 0")
	public TestConstructor(int field) {
		this.field = field;
	}

	public TestConstructor() {}

	public static void main(String[] args) {

		try{
			new TestConstructor(-10);
			printTestResult(System.err, FAIL, "constructor" , "-10");
		} catch(RuntimeException e){
			printTestResult(System.err, PASS, "constructor" , "-10");
		}
		
		try{
			new TestConstructor(10);
			printTestResult(System.err, PASS, "constructor" , "10");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "constructor" , "10");
		}
	}	
}
