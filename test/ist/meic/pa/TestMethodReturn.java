package ist.meic.pa;

import java.io.PrintStream;

/*
 * testes
 * 	heranca
 * 		metodo so na super
 *		invocar pela super
 * 		metodo em ambos
 * 		metodo so na subclass
 * 		super super class
 * 	tipos nao primitivos
 * 	condiccoes complicadas
 * 	interface
 */


public class TestMethodReturn extends TestAssertion{
	
	int testField = 30;
	
	@Assertion("$1>2") 
	public int m1(int x) {
		return x;
	}
	
	@Assertion("$1 > testField") 
	public int m2(int x) {
		return x;
	}
	
	@Assertion("$1 + $2 < $_") 
	public int m3(int x, int y) {
		return x + y + 1;
	}
	
	@Assertion("$_.length() > 1") 
	public String m4(String s) {
		return s;
	}
	
	public static void main(String[] args) {
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.m1(10);
			printTestResult(System.out, PASS, "m1", "10");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "m1", "10");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.m1(1);
			printTestResult(System.err, FAIL, "m1", "1");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "m1", "1");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.m2(50);
			printTestResult(System.out, PASS, "m2", "50");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "m2", "50");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.m2(1);
			printTestResult(System.err, FAIL, "m2", "1");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "m2", "1");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.m3(3,5);
			printTestResult(System.out, PASS, "m3","3,5");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "m3","3,5");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.m4("tested String");
			printTestResult(System.out, PASS, "m4" , "testedString");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "m4" , "testedString");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.m4("t");
			printTestResult(System.err, FAIL, "m4" , "t");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "m4" , "t");
		}
		
		TestMethodReturn methodReturn = new TestMethodReturn();
		methodReturn.testField = 20;
	}
	
}

