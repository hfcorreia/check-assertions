package ist.meic.pa;

public class TestFieldInitialization {
	
	int field;
	
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
		return field;
	}
	
	public void initializeField(){
		field = 123;
	}
	
	public void accessUninitializedField(){
		readField();
	}
	
	public void accessInitializedField(){
		initializeField();
		readField();
	}
}
