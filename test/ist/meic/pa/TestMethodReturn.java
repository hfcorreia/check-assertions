package ist.meic.pa;

public class TestMethodReturn {
	
	@Assertion("$1>2") //valor de retorno > valor do argumento
	public int method(int x) {
		System.out.println("start method( " + x + " );" );
		return ++x;
	}
	
	public static void main(String[] args) {
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.validParameter();
			System.out.println("pass");
		} catch(RuntimeException e){
			System.err.println("fail - valid parameter and an error was thrown");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.invalidParameter();
			System.err.println("fail - invalid parameter and no error");
		} catch(RuntimeException e){
			System.out.println("pass");
		}
	}
	
	public void validParameter(){
		method(10);
	}
	
	public void invalidParameter(){
		method(1);
	}
}
