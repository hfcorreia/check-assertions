package ist.meic.pa.methodReturn;

import ist.meic.pa.assertions.Assertion;

public interface TestMethodReturnInterface1 {

	@Assertion("$1<100") 
	public int mInterfaceBoth(int x);
	
	@Assertion("$1 > 0")
	public int mInterface1(int x);
	
}
