package ist.meic.pa;

import java.io.PrintStream;


public class TestAssertion {

	protected final static String PASS = "SUCCESS";
	protected final static String FAIL = "FAILED!";

	public TestAssertion() {}

	public static void printTestResult(PrintStream stream, String result, String method, String args) {
		stream.println("Test: " + method + "(" + args + ")" + ": " + result);
	}
	
	public static void printCatch(String message) {
		System.err.println(message);
	}
	
	public void catchMethod() {
		System.out.println("FOUND A ASSERTED EXCEPTION");
	}
}
