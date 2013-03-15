package ist.meic.pa;

public class TestFieldFromSuper extends TestAssertion {

    @Assertion(" x < 1000 ")
    protected int x;

    //blowing up
    @Assertion(" s.length() == 2 && ( s.charAt(0) == \'K\' || s.charAt(1) == \'K\' ) ")
    String s = new String("KO");
}
