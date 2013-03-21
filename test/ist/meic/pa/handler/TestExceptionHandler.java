package ist.meic.pa.handler;

import ist.meic.pa.ExceptionAssertion;

public class TestExceptionHandler {

	public void m1(String cause) throws ArrayIndexOutOfBoundsException {
		throw new RuntimeException(cause);
	}
	
	public static void main(String[] args) {

		TestExceptionHandler t = new TestExceptionHandler();
		
		t.m1("random");
		
	}
	
}
