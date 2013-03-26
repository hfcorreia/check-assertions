package ist.meic.pa.interceptors;

import java.util.Collection;
import java.util.HashSet;

import ist.meic.pa.assertions.Assertion;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

/** 
 * * 
 * class with some util methods for intercepting field accesses 
 *
 */
public class FieldInterceptor {

	public static String createReadAssertionMethodTemplate(String fieldName, String methodType, String className) {
		String methodName = fieldName + "$assertion";
		String isInitialized = fieldName + "$isInitialized";
		String template =
				"public static " + methodType + " " + methodName +" (Object target) { " +
					" if( "+ isInitialized +" ){" +
					"	return " + "( ( " + className + " )   target  )." + fieldName + ";" +
					" } " +
					" else { " +
					"	throw new java.lang.RuntimeException(\"Error: " + fieldName + " was not initialized\"); " +
					" } "+
				" } ";
		return template;
	}
	
	public static String createWriteAssertionMethodTemplate(String fieldName, String fieldType, String originalAssertExpression, String className, boolean isStaticMethod) {
		String methodName = fieldName + "$writeAssertion";
		String originalField = "((" + className + ") target )." + fieldName;
		String isInitializedTemp = fieldName + "$isInitializedTemp";
		String isInitialized = fieldName + "$isInitialized";
		String originalValueCopy = fieldName + "$tmpValue";
		String isRunningField = fieldName + "$isRunning";
		String prefix = !isStaticMethod ? "((" + className + ") target )." : "";
		String assertExpression = prefix + fieldName+"$assertExpression()";
		
		String template =
				"public static public void " + methodName + " (Object target, " + fieldType + " newValue) { " +
						isRunningField + " = true;" +
						"boolean " + isInitializedTemp + " = " + isInitialized + ";" + 
						
						isInitialized + " = true;" +
						
						fieldType + " " + originalValueCopy + " = " + originalField +  ";" + 
						
						originalField + " =  newValue ;" +
						
						"if( ! " + assertExpression + " ) { " + 
							originalField + " = " + originalValueCopy + ";" + 
							isInitialized + " = " + isInitializedTemp + ";" + 
							"throw new java.lang.RuntimeException( " + createErrorMessage(originalAssertExpression) + " );" + 
						" } " +
				" } ";
		return template;
	}

	public static String createErrorMessage(String assertExpression) {
		return "\"The assertion " + assertExpression +" is false\"";
	}
	
	
	/**
	 *
	 * method responsible for creating auxiliar fields;
	 * this auxiliars fields help to verify if a field is initialized
	 * 
	 */
	public static void createAuxiliaryFields(CtClass ctClass, CtField ctField) throws CannotCompileException, NotFoundException {
		String fieldName = ctField.getName();

		if (! existsFields(ctClass, fieldName + "$isInitialized") ) {
			CtField isInitializedField = new CtField(CtClass.booleanType, fieldName + "$isInitialized", ctClass);
			isInitializedField.setModifiers(Modifier.STATIC);
			
			CtField isRunning = new CtField(CtClass.booleanType, fieldName + "$isRunning", ctClass);
			isRunning.setModifiers(Modifier.STATIC);
			
			ctClass.addField(isInitializedField, "false");
			ctClass.addField(isRunning, "false");
			
		}
	}


    public static Collection<CtField> findClassFields(CtClass ctClass) {
		HashSet<CtField> result = new HashSet<CtField>();
		HashSet<CtField> superFields = new HashSet<CtField>();
		
		if (MethodInterceptor.hasValidSuperclass(ctClass)) {
			try {
				superFields.addAll(findClassFields(ctClass.getSuperclass()));
			} catch (NotFoundException e) { 	/* DO NOTHING */ 	}
		
			//add if does not allready exist in a superclass
			for(CtField myField : ctClass.getFields()){
				if(!existsField(superFields, myField)){
					result.add(myField);
				}
			}
			
			result.addAll(superFields);
		}
		
		return result;
	}
    
    private static boolean existsField(Collection<CtField> allFields, CtField field){
		for(CtField superField : allFields){
			if(field.getName().equals(superField.getName())){
				return true;
			}
		}
		return false;
    }
	
	private static boolean existsFields(CtClass ctClass, String fieldName) {
		try {
			ctClass.getField(fieldName);
			return true;
		} catch(NotFoundException nfe) {
			return false;
		}
	}

	public static void createAuxiliaryMethods(CtClass ctClass, CtField ctField, Assertion assertion) {
		 String template = createBooleanMethod(ctField.getName() + "$assertExpression" , assertion.value());
		 CtMethod evaluateBooleanExpressionMethod;
		try {
			evaluateBooleanExpressionMethod = CtNewMethod.make(template, ctClass);
			if(Modifier.isStatic(ctField.getModifiers())){
				evaluateBooleanExpressionMethod.setModifiers(Modifier.STATIC);
			}
			ctClass.addMethod(evaluateBooleanExpressionMethod);
		} catch (CannotCompileException e) {
			e.printStackTrace();
		}
	}
	
	private static String createBooleanMethod(String methodName, String assertExpression) {
		return "boolean " + methodName + "(){ return " + assertExpression + "; }";
	}

}
