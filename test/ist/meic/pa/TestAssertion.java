package ist.meic.pa;

import java.io.PrintStream;

public class TestAssertion {

	protected final static String PASS = "pass";
	protected final static String FAIL = "fail";
	
	/*
	 * metodo para tratar de falhas (detectadas por excepccao por ex?) e passados => contabilizar passados/falhados e mostrar no fim
	 */
	
	public TestAssertion() {}
	
	public static void printTestResult(PrintStream stream, String result, String method, String args) {
		stream.println("Test: " + method + "(" + args + ")" + " w/ result: " + result);
	}
}
