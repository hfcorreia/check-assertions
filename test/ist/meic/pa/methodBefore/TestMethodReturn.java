package ist.meic.pa.methodBefore;

import ist.meic.pa.assertions.Assertion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * brincar com modificadores
 */

public class TestMethodReturn  extends TestMethodReturnSuper implements TestMethodReturnInterface1, TestMethodReturnInterface2 {

	@Assertion(before="$1.isEmpty()") 
	public int mWithObject(List<String> x) {
		return 1;
	}
	@Assertion(before="$1>0") 
	public int mSimpleArgument(int x) {
		return x;
	}
	@Assertion("$1>0") 
	public int mSimpleArgumentAfter(int x) {
		return x;
	}
	@Assertion(before="$1>0", value="$1>0") 
	public int mSimpleArgumentBeforeAndAfter(int x) {
		return x;
	}
	@Assertion(before="$1>0") 
	public int bottomAndSuper(int x) {
		return x;
	}
	public int superAnotated(int x) {
		return x;
	}
	@Assertion(before="$1!=50") 
	public int superOnAll(int x) {
		return x;
	}
	@Override
	public int mOnlyInterface1(int x) {
		return x;
	}
	@Override
	public int mBothInterfaces(int x) {
		return 0;
	}

	public static void main(String[] args) {
		//BEFORE # SIMPLE ARGUMENT $1>0
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.mSimpleArgument(10);
			printTestResult(System.out, PASS, "mSimpleArgument", "10");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "mSimpleArgument", "10");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.mSimpleArgument(-10);
			printTestResult(System.err, FAIL, "mSimpleArgument", "-10");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "mSimpleArgument", "-10");
		}
		
		//AFTER # SIMPLE ARGUMENT $1>0
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.mSimpleArgumentAfter(10);
			printTestResult(System.out, PASS, "mSimpleArgumentAfter", "10");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "mSimpleArgumentAfter", "10");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.mSimpleArgumentAfter(-10);
			printTestResult(System.err, FAIL, "mSimpleArgumentAfter", "-10");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "mSimpleArgumentAfter", "-10");
		}
		
		//BEFORE AND AFTER # SIMPLE ARGUMENT $1>0
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.mSimpleArgumentBeforeAndAfter(10);
			printTestResult(System.out, PASS, "mSimpleArgumentBeforeAndAfter", "10");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "mSimpleArgumentBeforeAndAfter", "10");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.mSimpleArgumentBeforeAndAfter(-10);
			printTestResult(System.err, FAIL, "mSimpleArgumentBeforeAndAfter", "-10");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "mSimpleArgumentBeforeAndAfter", "-10");
		}
		
		
		//BEFORE # OBJECT AS ARGUMENT $1.isEmpty
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.mWithObject(new ArrayList<String>());
			printTestResult(System.out, PASS, "mWithObject", "Empty");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "mWithObject", "Empty");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.mWithObject(Arrays.asList( new String[]{"ei"} ));
			printTestResult(System.err, FAIL, "mWithObject", "Not Empty");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "mWithObject", "Not Empty");
		}
		
		//BEFORE # METHOD WITH ANNOTATION ONLY IN SUPERCLASS $1 > 0 BUT DEFINED IN THE BOTOM CLASS
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.superAnotated(10);
			printTestResult(System.out, PASS, "superAnotated", "10");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "superAnotated", "10");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.superAnotated(-10);
			printTestResult(System.err, FAIL, "superAnotated", "-10");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "superAnotated", "-10");
		}
		
		//BEFORE # METHOD WITH ANNOTATION ONLY IN SUPERCLASS $1 > 0
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.onlyOnSuper(10);
			printTestResult(System.out, PASS, "onlyOnSuper", "10");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "onlyOnSuper", "10");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.onlyOnSuper(-10);
			printTestResult(System.err, FAIL, "onlyOnSuper", "-10");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "onlyOnSuper", "-10");
		}
		
		
		//BEFORE # BOTTOM AND SUPER  [0 < $1 < 10]
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.bottomAndSuper(5);
			printTestResult(System.out, PASS, "bottomAndSuper", "5");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "bottomAndSuper", "5");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.bottomAndSuper(20);
			printTestResult(System.err, FAIL, "bottomAndSuper", "20");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "bottomAndSuper", "20");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.bottomAndSuper(-5);
			printTestResult(System.err, FAIL, "bottomAndSuper", "-5");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "bottomAndSuper", "-5");
		}
		
		//BEFORE # BOTTOM AND ALL SUPER CLASSES [ 0 < $1 < 100 except 50]
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.superOnAll(5);
			printTestResult(System.out, PASS, "superOnAll", "5");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "superOnAll", "5");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.superOnAll(50);
			printTestResult(System.err, FAIL, "superOnAll", "50");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "superOnAll", "50");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.superOnAll(101);
			printTestResult(System.err, FAIL, "superOnAll", "101");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "superOnAll", "101");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.superOnAll(-1);
			printTestResult(System.err, FAIL, "superOnAll", "-1");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "superOnAll", "-1");
		}
		
		//BEFORE # METHOD ANNOTATED ONLY ON ONE INTERFACE [$1 > 0]
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.mOnlyInterface1(10);
			printTestResult(System.out, PASS, "mOnlyInterface1", "10");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "mOnlyInterface1", "10");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.mOnlyInterface1(0);
			printTestResult(System.err, FAIL, "mOnlyInterface1", "0");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "mOnlyInterface1", "0");
		}
		
		//BEFORE # METHOD ANNOTATED ON MORE THAN ONE INTERFACE [0 < $1 < 100]
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.mBothInterfaces(10);
			printTestResult(System.out, PASS, "mBothInterfaces", "10");
		} catch(RuntimeException e){
			printTestResult(System.err, FAIL, "mBothInterfaces", "10");
		}
		
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.mBothInterfaces(0);
			printTestResult(System.err, FAIL, "mBothInterfaces", "0");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "mBothInterfaces", "0");
		}
		try{
			TestMethodReturn methodReturn = new TestMethodReturn();
			methodReturn.mBothInterfaces(100);
			printTestResult(System.err, FAIL, "mBothInterfaces", "100");
		} catch(RuntimeException e){
			printTestResult(System.out, PASS, "mBothInterfaces", "100");
		}
	}

}

