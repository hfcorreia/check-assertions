package ist.meic.pa.methodReturn;

import ist.meic.pa.Assertion;
import ist.meic.pa.field.TestAssertion;

public class TestMethodReturnSuperSuper extends TestAssertion {

	@Assertion("$1>0") 
	public int superOnAll(int x) {
		return x;
	}
	
}
