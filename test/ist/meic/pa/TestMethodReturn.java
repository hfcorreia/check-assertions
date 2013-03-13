package ist.meic.pa;

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


public class TestMethodReturn {
	
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
			System.out.println("pass");
		} catch(RuntimeException e){
			System.err.println("fail - valid parameter and an error was thrown");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.m1(1);
			System.err.println("fail - invalid parameter and no error");
		} catch(RuntimeException e){
			System.out.println("pass");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.m2(50);
			System.err.println("fail - invalid parameter and no error");
		} catch(RuntimeException e){
			System.out.println("pass");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.m2(1);
			System.err.println("fail - invalid parameter and no error");
		} catch(RuntimeException e){
			System.out.println("pass");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.m3(3,5);
			System.out.println("pass");
		} catch(RuntimeException e){
			System.err.println("fail - invalid parameter and no error");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.m4("tested String");
			System.out.println("pass");
		} catch(RuntimeException e){
			System.err.println("fail - invalid parameter and no error");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.m4("t");
			System.err.println("fail - invalid parameter and no error");
		} catch(RuntimeException e){
			System.out.println("pass");
		}
		
		
		
	}
	
}
