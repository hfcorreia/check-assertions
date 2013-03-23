package ist.meic.pa.handler;

import ist.meic.pa.TestAssertion;
import ist.meic.pa.assertions.ExceptionAssertion;

import java.io.FileNotFoundException;
import java.nio.BufferOverflowException;

@ExceptionAssertion(exception = {"java.nio.BufferOverflowException", "java.lang.NullPointerException"}, method = "catchMethod")
public class TestExceptionHandler extends TestAssertion{

	public void m2() throws FileNotFoundException {
		throw new FileNotFoundException();
	}

	public void m1() throws NullPointerException {
		throw new NullPointerException();
	}
	
	
	public static void main(String[] args) {

		try{
			TestExceptionHandler t = new TestExceptionHandler();
			t.m1();
		} catch (NullPointerException e) {
			//do nothing
		} catch (BufferOverflowException e) {
			//do nothing
		}
		
		try{
			TestExceptionHandler t = new TestExceptionHandler();
			t.m2();
		} catch (NullPointerException e) {
			//do nothing
		} catch (BufferOverflowException e) {
			//do nothing
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		}
	}

	
	
	public void catchMethod() {
		System.out.println("FOUND A ASSERTED EXCEPTION");
	}
	
}
