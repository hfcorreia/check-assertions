package ist.meic.pa.constructor;

import ist.meic.pa.Assertion;

public class TestConstructor extends TestConstructorSuper {

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
		
		try{
			new TestConstructor(200);
			printTestResult(System.err, FAIL, "constructor" , "200");
		} catch(RuntimeException e){
	         printTestResult(System.err, PASS, "constructor" , "200");

		}
	}	
}
