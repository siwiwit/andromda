package org.andromda.core.common;

import org.apache.commons.lang.StringUtils;

/**
 * Contains Exception handling utilities.  
 * 
 * @author Chad Brandon
 */
public class ExceptionUtils {
    
	/**
	 * Checks if the argument is null, and if so, 
	 * throws an IllegalArgumentException, does nothing if not.
	 * 
	 * @param methodExecuteName the name of the method we are currently executing
	 * @param argumentName the name of the argument we are checking for null
	 * @param argument the argument we are checking
	 */
    public static void checkNull(
    	String methodExecuteName, 
    	String argumentName,
    	Object argument) {
    	String methodName = "checkNull";
    	if (StringUtils.isEmpty(methodExecuteName)) {
    		throw new IllegalArgumentException(methodName + 
    			" - methodExecuteName can not be null or an empty String");
    	}
		if (StringUtils.isEmpty(argumentName)) {
			throw new IllegalArgumentException("methodName: '" + methodName + 
				"' - argumentName can not be null or an empty String");
		}
		
		//this is what the method is actually for
		if (argument == null) {
			throw new IllegalArgumentException("methodName: " + methodExecuteName + 
				" - '" + argumentName + "' can not be null");
		}
    }
    
	/**
	 * Checks if the argument is null or an empty String
	 * throws an IllegalArgumentException if it is, does nothing if not.
	 * 
	 * @param methodExecuteName the name of the method we are currently executing
	 * @param argumentName the name of the argument we are checking for null
	 * @param argument the argument we are checking
	 */
	public static void checkEmpty(
		String methodExecuteName, 
		String argumentName,
		String argument) {
		String methodName = "checkEmpty";
		if (StringUtils.isEmpty(methodExecuteName)) {
			throw new IllegalArgumentException(methodName + 
				" - methodExecuteName can not be null or an empty String");
		}
		if (StringUtils.isEmpty(argumentName)) {
			throw new IllegalArgumentException(methodName + 
				" - argumentName can not be null or an empty String");
		}
		
		//this is what the method is actually for
		if (StringUtils.isEmpty(argument)) {
			throw new IllegalArgumentException("methodName: " + methodExecuteName + 
				" - '" + argumentName + "' can not be null or an empty String");
		}
	}
   
}
