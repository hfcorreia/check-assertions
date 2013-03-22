package ist.meic.pa.methodBefore;

import ist.meic.pa.Assertion;

public interface TestMethodReturnInterface2 {

	@Assertion(before="$1>0") 
	public int mBothInterfaces(int x);
	
}
