package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler
 */
public class StrutsExceptionHandlerLogicImpl
        extends StrutsExceptionHandlerLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler
{
    // ---------------- constructor -------------------------------
    
    public StrutsExceptionHandlerLogicImpl(java.lang.Object metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsExceptionHandler ...

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler#getKey()()
     */
    public java.lang.String getKey()
    {
        return findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_EXCEPTION_KEY);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler#getType()()
     */
    public java.lang.String getType()
    {
        String type = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_EXCEPTION_TYPE);
        if (type == null)
        {
            type = Bpm4StrutsProfile.TAGGED_VALUE_EXCEPTION_DEFAULT_TYPE;
        }
        return type;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler#getPath()()
     */
    public java.lang.String getPath()
    {
        return findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_EXCEPTION_PATH);
    }

    // ------------- relations ------------------

}
