package ist.meic.pa;

public class TestMethodReturn {
	public int method(int x) {
		System.out.println("start method( " + x + " );" );
		return ++x;
	}
	
	public static void main(String[] args) {
		Integer methodArgument = /*Integer.parseInt(args[0]); */ 1;
		TestMethodReturn testClass = new TestMethodReturn();
		testClass.method(methodArgument.intValue());
	}
}
