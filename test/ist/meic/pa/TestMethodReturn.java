package ist.meic.pa;

/*
 * testes
 * 	heranca
 * 		metodo so na super
 * 		metodo em ambos
 * 		metodo so na subclass
 * 		super super class
 * 	tipos nao primitivos
 * 	condiccoes complicadas
 * 	interface
 */


public class TestMethodReturn extends TestMethodReturnSuper implements TestMethodReturnInterface {

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


	@Assertion("$1>2") 
	public int superBoth1(int x) {
		return x;
	}

	@Assertion("$1!=50") 
	public int superOnAll(int x) {
		return x;
	}

	@Override
	public int mInterface1(int x) {
		return x;
	}

	@Assertion("$1 > 0") 
	public int mSet1(int x) {
		x = 0;
		return x;
	}

	@Assertion("$_ > $1") 
	public int inc1(int x) {
		x++;
		return x;
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

		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.super1(1);
			printTestResult(System.err, FAIL, "super1" , "1");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "super1" , "1");
		}

		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.super1(10);
			printTestResult(System.out, PASS, "super1" , "10");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "super1" , "10");
		}

		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.superBoth1(1);
			printTestResult(System.err, FAIL, "superBoth1" , "1");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "superBoth1" , "1");
		}

		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.superBoth1(10);
			printTestResult(System.err, PASS, "superBoth1" , "10");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "superBoth1" , "10");
		}

		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.superBoth1(30);
			printTestResult(System.err, FAIL, "superBoth1" , "30");
		} catch(RuntimeException e){
			printTestResult(System.err, PASS, "superBoth1" , "30");
		}

		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.superOnAll(10);
			printTestResult(System.err, PASS, "superOnAll" , "10");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "superOnAll" , "10");
		}

		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.superOnAll(-3);
			printTestResult(System.err, FAIL, "superOnAll" , "-3");
		} catch(RuntimeException e){
			printTestResult(System.err, PASS, "superOnAll" , "-3");
		}

		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.superOnAll(50);
			printTestResult(System.err, FAIL, "superOnAll" , "50");
		} catch(RuntimeException e){
			printTestResult(System.err, PASS, "superOnAll" , "50");
		}

		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.mInterface1(10);
			printTestResult(System.err, PASS, "mInterface1" , "10");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "mInterface1" , "10");
		}

		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.mInterface1(-4);
			printTestResult(System.err, FAIL, "mInterface1" , "-4");
		} catch(RuntimeException e){
			printTestResult(System.err, PASS, "mInterface1" , "-4");
		}

		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.mSet1(10);
			printTestResult(System.err, PASS, "mSet1" , "10");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "mSet1" , "10");
		}

		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.mSet1(-10);
			printTestResult(System.err, FAIL, "mSet1" , "-10");
		} catch(RuntimeException e){
			printTestResult(System.err, PASS, "mSet1" , "-10");
		}

		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.inc1(10);
			printTestResult(System.err, PASS, "inc1" , "10");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "inc1" , "10");
		}
	}
}

