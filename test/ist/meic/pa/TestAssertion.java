package ist.meic.pa;

import java.io.PrintStream;

public class TestAssertion {

	protected static final String PASS = "pass";
	protected static final String FAIL = "fail";
	
	public static void printTestResult(PrintStream stream, String result, String method, String args) {
		stream.println("Test: " + method + "(" + args + ")" + " w/ result: " + result);
	}
}
