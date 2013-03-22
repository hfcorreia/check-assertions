package ist.meic.pa.handler;

import java.lang.reflect.InvocationTargetException;

import ist.meic.pa.ExceptionAssertion;
import ist.meic.pa.TestAssertion;

@ExceptionAssertion(exception = "java.lang.RuntimeException", method = "catchMethod")
public class TestExceptionHandler extends TestAssertion{

	public void m1(String cause) throws RuntimeException {
		throw new RuntimeException(cause);
	}

	public static void main(String[] args) {

//		try {
//		     ist.meic.pa.TestAssertion.class.getMethod("catchMethod", null).invoke(new ist.meic.pa.TestAssertion(), null);
//		} catch (IllegalArgumentException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (SecurityException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IllegalAccessException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (InvocationTargetException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (NoSuchMethodException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		
		try{
			TestExceptionHandler t = new TestExceptionHandler();
			t.m1("random cause");
		} catch (RuntimeException e) {
			//do nothing
		}
	}
}
