package ist.meic.pa.handler;

import ist.meic.pa.ExceptionAssertion;

@ExceptionAssertion("java.lang.RuntimeException.RuntimeException")
public class TestExceptionHandler {

	public void m1(String cause) throws Exception {
		throw new Exception(cause);
	}

	public static void main(String[] args) {

		try{
			TestExceptionHandler t = new TestExceptionHandler();
			t.m1("random");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
