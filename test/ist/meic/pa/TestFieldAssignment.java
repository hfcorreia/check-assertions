package ist.meic.pa;

public class TestFieldAssignment extends TestAssertion{
	
	int otherField = 10;
	
	@Assertion("field > otherField")
	int field;
	
	public static void main(String[] args) {
		
		try {
			TestFieldAssignment fieldAssignment = new TestFieldAssignment();
			fieldAssignment.validInit();
			printTestResult(System.out, PASS, "validInit", "");
		} catch(RuntimeException e){
			e.printStackTrace();
			printTestResult(System.err, FAIL, "validInit", "");
		}
		
		try {
			TestFieldAssignment fieldAssignment = new TestFieldAssignment();
			fieldAssignment.validWrite();
			printTestResult(System.out, PASS, "validWrite", "");
		} catch(RuntimeException e){
			e.printStackTrace();
			printTestResult(System.err, FAIL, "validWrite", "");
		}
		try {
			TestFieldAssignment fieldAssignment = new TestFieldAssignment();
			fieldAssignment.invalidWrite();
			System.err.println("invalid access: fail - invalid write as been made.");
			printTestResult(System.err, FAIL, "invalidWrite", "");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "invlaidWrite", "");
		}
	}
	
	public void validInit(){
		field = 11;
	}
	
	public void validWrite(){
		field = 1234;
	}
	
	public void invalidWrite(){
		field = -1;
	}
	
}
