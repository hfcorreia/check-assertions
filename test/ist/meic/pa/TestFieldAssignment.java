package ist.meic.pa;

public class TestFieldAssignment {
	
	int field = 10;
	
	public static void main(String[] args) {
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
	
	public void validWrite(){
		field = 1234;
	}
	
	public void invalidWrite(){
		field = -1;
	}
	
}
