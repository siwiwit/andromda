package org.andromda.core.metafacade;

public class ModelValidationException extends Exception
{
    public ModelValidationException(String modelElementClassName, String modelElementName, String message)
    {
        super(modelElementClassName + " [" + modelElementName + "] : " + message);
    }
}
