package ist.meic.pa.methodBefore;

import ist.meic.pa.assertions.Assertion;

public class TestMethodReturnSuper extends TestMethodReturnSuperSuper {

	@Assertion(before="$1>0") 
	public int onlyOnSuper(int x) {
		return x;
	}
	
	@Assertion(before="$1<10") 
	public int bottomAndSuper(int x) {
		return x;
	}
	
	@Assertion(before="$1<100") 
	public int superOnAll(int x) {
		return x;
	}
	
	@Assertion(before="$1 > 0")
	public int superAnotated(int x) {
		return x;
	}
	
}
