package org.andromda.core.metadecorators.uml14;

import org.omg.uml.foundation.core.ModelElement;

public class DecoratorValidationException extends Exception
{
    public DecoratorValidationException(ModelElement modelElement, String message)
    {
        super(modelElement.getClass().getName() + " [" + modelElement.getName() + "] : " + message);
    }
}
