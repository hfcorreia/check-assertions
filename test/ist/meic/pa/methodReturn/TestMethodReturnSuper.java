package ist.meic.pa.methodReturn;

import ist.meic.pa.Assertion;

public class TestMethodReturnSuper extends TestMethodReturnSuperSuper {

	@Assertion("$1>2") 
	public int super1(int x) {
		return x;
	}
	
	@Assertion("$1<20") 
	public int superBoth1(int x) {
		return x;
	}
	
	@Assertion("$1<100") 
	public int superOnAll(int x) {
		return x;
	}
	
	@Assertion("$1 > 0")
	public int superAnotated(int x) {
		return x;
	}
	
}
