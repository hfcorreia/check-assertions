package ist.meic.pa.methodReturn;

import ist.meic.pa.TestAssertion;
import ist.meic.pa.assertions.Assertion;

public class TestMethodReturnSuperSuper extends TestAssertion {

	@Assertion("$1>0") 
	public int superOnAll(int x) {
		return x;
	}
	
}
