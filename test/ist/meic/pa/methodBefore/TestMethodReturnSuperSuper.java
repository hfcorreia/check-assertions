package ist.meic.pa.methodBefore;

import ist.meic.pa.TestAssertion;
import ist.meic.pa.assertions.Assertion;

public class TestMethodReturnSuperSuper extends TestAssertion {

	@Assertion(before="$1>0") 
	public int superOnAll(int x) {
		return x;
	}
	
}
