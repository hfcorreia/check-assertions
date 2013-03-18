package ist.meic.pa.constructor;

import ist.meic.pa.Assertion;
import ist.meic.pa.field.TestAssertion;
import ist.meic.pa.methodReturn.TestMethodReturn;

public class TestConstructor extends TestAssertion {

	int field = 4;

	@Assertion("$1 > 0")
	public TestConstructor(int field) {
		System.out.println("ENTREI");
		this.field = field;
		System.out.println("SAI");
	}

	public TestConstructor() {}

	public static void main(String[] args) {

		try{
			TestConstructor constructor = new TestConstructor(-10);
			printTestResult(System.err, FAIL, "constructor" , "-10");
		} catch(RuntimeException e){
			printTestResult(System.err, PASS, "constructor" , "-10");
		}
	}	
}
