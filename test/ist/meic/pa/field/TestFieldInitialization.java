package ist.meic.pa.field;

import ist.meic.pa.TestAssertion;
import ist.meic.pa.assertions.Assertion;

public class TestFieldInitialization extends TestAssertion {
	
	@Assertion("testField > 2")
	int testField;
	
	public static void main(String[] args) {
		try {
		TestFieldInitialization test = new TestFieldInitialization();
		test.testField = 3;
//		TestFieldInitialization.testField = 3;

		System.out.println("TEST - " + test.testField);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
}
