package ist.meic.pa;

public class TestFieldAssignment extends TestFieldFromSuper{
	
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
			printTestResult(System.err, FAIL, "invalidWrite", "");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "invlaidWrite", "");
		}
		
		try {
            TestFieldAssignment fieldAssignment = new TestFieldAssignment();
            fieldAssignment.superInvalidReadWrite();
            printTestResult(System.err, FAIL, "superInvalidReadWrite", "");
        } catch(RuntimeException e){
            printTestResult(System.out, PASS, "superInvalidReadWrite", "");
        }
		
		try {
            TestFieldAssignment fieldAssignment = new TestFieldAssignment();
            fieldAssignment.superValidWrite();
            printTestResult(System.out, PASS, "superValidWrite", "");
        } catch(RuntimeException e){
            printTestResult(System.err, FAIL, "superValidWrite", "");
        }
		
	      try {
	            TestFieldAssignment fieldAssignment = new TestFieldAssignment();
	            fieldAssignment.superInvalidWrite();
	            printTestResult(System.err, FAIL, "superInvalidWrite", "");
	        } catch(RuntimeException e){
	            printTestResult(System.out, PASS, "superInvalidWrite", "");
	        }
	      
	      try {
              TestFieldAssignment fieldAssignment = new TestFieldAssignment();
              fieldAssignment.superInvalideWrite2();
              printTestResult(System.err, FAIL, "superInvalideWrite2", "");
          } catch(RuntimeException e){
              printTestResult(System.out, PASS, "superInvalideWrite2", "");
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
	
	public void superInvalidWrite() {
	    x = 10000;
	}
	
	public void superValidWrite() {
        x = 10;
    }
	
	public void superInvalidReadWrite() {
        x++;
    }
	
	public void superInvalideWrite2() {
	    s = "PO";
	}
}
