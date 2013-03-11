package ist.meic.pa;

public class TestFieldAssignment {
	
	int field = 0;
	
	public static void main(String[] args) {
		TestFieldAssignment fieldAssignment = new TestFieldAssignment();
		try {
			fieldAssignment.setValidValue();
			System.out.println("valid value: pass");
		} catch(RuntimeException e){
			e.printStackTrace();
			System.err.println("valid valud: Field write a valid value but an exception was thrown.");
		}
		try {
			fieldAssignment.setInvalidValue();
			System.err.println("invalid value: Field write invalid value.");
		} catch(RuntimeException e){
			System.out.println("invalid value: pass");
		}
	}

	public void setValidValue(){
		field = 10;
	}
	public void setInvalidValue(){
		field = 2;
	}
}
