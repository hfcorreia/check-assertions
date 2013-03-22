package ist.meic.pa;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class TestAssertion {

	protected final static String PASS = "pass";
	protected final static String FAIL = " !!!! @@@@ ##### FAIL #### @@@@ !!!!";

	/*
	 * metodo para tratar de falhas (detectadas por excepccao por ex?) e passados => contabilizar passados/falhados e mostrar no fim
	 */

	public TestAssertion() {}

	public static void printTestResult(PrintStream stream, String result, String method, String args) {
		stream.println("Test: " + method + "(" + args + ")" + " w/ result: " + result);
	}
	
	public static void printCatch(String message) {
		System.err.println(message);
	}
	
	public void catchMethod() {
		System.out.println("FOUND A ASSERTED EXCEPTION");
	}
}
