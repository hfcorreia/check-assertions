package ist.meic.pa.field;

import ist.meic.pa.Assertion;
import ist.meic.pa.TestAssertion;

public class TestFieldInitialization extends TestAssertion{
	
	@Assertion("true")
	int testField;
	
	public static void main(String[] args) {
		try {
			TestFieldInitialization fieldInitialization = new TestFieldInitialization();
			fieldInitialization.accessInitializedField();
			printTestResult(System.out, PASS, "accessInitializedField", "");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "accessInitializedField", "");
		}
		
		try {
			TestFieldInitialization fieldInitialization = new TestFieldInitialization();
			fieldInitialization.accessUninitializedField();
			printTestResult(System.err, FAIL, "accessUnitializedField", "");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "accessInitializedField", "");

		}
	}
	
	public int readField(){
		return testField;
	}
	
	public void initializeField(){
		testField = 123;
	}
	
	public void accessUninitializedField(){
		readField();
	}
	
	public void accessInitializedField(){
		initializeField();
		readField();
	}
}
