package ist.meic.pa.methodBefore;

import ist.meic.pa.Assertion;

public interface TestMethodReturnInterface1 {

	@Assertion(before="$1<100") 
	public int mBothInterfaces(int x);
	
	@Assertion(before="$1 > 0")
	public int mOnlyInterface1(int x);
	
}
