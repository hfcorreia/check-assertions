package ist.meic.pa.field;

import ist.meic.pa.TestAssertion;
import ist.meic.pa.assertions.Assertion;

public class TestFieldFromSuper extends TestAssertion {

    @Assertion(" x < 1000 ")
    protected int x;

    @Assertion(" sx.length() > 1")
    private String sx = new String("Creditos");
    
    
    public void changeSx() {
        sx = "bolas";
    }
}
