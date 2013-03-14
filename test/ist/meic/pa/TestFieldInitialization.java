package ist.meic.pa;

public class TestFieldInitialization extends TestAssertion{
	
	@Assertion("true")
	int testField;
	
	public static void main(String[] args) {
		try {
			TestFieldInitialization fieldInitialization = new TestFieldInitialization();
			fieldInitialization.accessInitializedField();
			System.out.println("valid access: pass");
		} catch(RuntimeException e){
			e.printStackTrace();
			System.err.println("valid access: fail - Field is allready initialized.");
		}
		try {
			TestFieldInitialization fieldInitialization = new TestFieldInitialization();
			fieldInitialization.accessUninitializedField();
			System.err.println("invalid access: fail - field access to uninitialized field.");
		} catch(RuntimeException e){
			System.out.println("invalid access: pass");
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
