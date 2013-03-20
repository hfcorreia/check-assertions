package ist.meic.pa.constructor;

import ist.meic.pa.Assertion;
import ist.meic.pa.TestAssertion;

public class TestConstructorSuper extends TestAssertion{

	static int fieldSuper = 100;

	public TestConstructorSuper() {
	}
	
	@Assertion("$1 < fieldSuper")
	public TestConstructorSuper(int field) {
	}
}
