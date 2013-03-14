package ist.meic.pa;

public class TestFieldAssignment extends TestAssertion{
	
	int otherField = 10;
	
	@Assertion("field > otherField")
	int field;
	
	public static void main(String[] args) {
		
		try {
			TestFieldAssignment fieldAssignment = new TestFieldAssignment();
			fieldAssignment.validInit();
			System.out.println("valid access: pass");
		} catch(RuntimeException e){
			e.printStackTrace();
			System.err.println("valid access: fail - valid init.");
		}
		
		try {
			TestFieldAssignment fieldAssignment = new TestFieldAssignment();
			fieldAssignment.validWrite();
			System.out.println("valid access: pass");
		} catch(RuntimeException e){
			e.printStackTrace();
			System.err.println("valid access: fail - valid write.");
		}
		try {
			TestFieldAssignment fieldAssignme = new TestFieldAssignment();
			fieldAssignme.invalidWrite();
			System.err.println("invalid access: fail - invalid write as been made.");
		} catch(RuntimeException e){
			System.out.println("invalid access: pass");
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
