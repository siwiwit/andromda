package org.andromda.metafacades.uml14;


/**
 * 
 *
 * Metaclass facade implementation.
 *
 */
public class ParameterFacadeLogicImpl
       extends ParameterFacadeLogic
       implements org.andromda.metafacades.uml.ParameterFacade
{
    // ---------------- constructor -------------------------------
    
    public ParameterFacadeLogicImpl (org.omg.uml.foundation.core.Parameter metaObject, String context)
    {
        super (metaObject, context);
    }
    
    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class ParameterDecorator ...

    // ------------- relations ------------------
    
    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ParameterDecorator#handleGetType()
     */
    public Object handleGetType()
    {
        return metaObject.getType();
    }

    // ------------------------------------------------------------

}
