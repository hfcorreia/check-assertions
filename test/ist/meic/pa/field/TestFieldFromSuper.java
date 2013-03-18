package ist.meic.pa.field;

import ist.meic.pa.Assertion;
import ist.meic.pa.TestAssertion;

public class TestFieldFromSuper extends TestAssertion {

    @Assertion(" x < 1000 ")
    protected int x;

    //blowing up
    @Assertion(" s.length() == 2 && ( s.charAt(0) == \'K\' || s.charAt(1) == \'K\' ) ")
    String s = new String("KO");
    
    @Assertion(" sx.length() > 1")
    private String sx = new String("Creditos");
    
    
    public void changeSx() {
        sx = "bolas";
    }
}
