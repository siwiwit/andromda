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

    public StrutsExceptionHandlerLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsExceptionHandler ...

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler#getExceptionKey()()
     */
    public java.lang.String getExceptionKey()
    {
        return findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_EXCEPTION_KEY);
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler#getExceptionType()()
     */
    public java.lang.String getExceptionType()
    {
        String type = findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_EXCEPTION_TYPE);
        if (type == null)
        {
            type = Bpm4StrutsProfile.TAGGED_VALUE_EXCEPTION_DEFAULT_TYPE;
        }
        return type;
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsExceptionHandler#getExceptionPath()()
     */
    public java.lang.String getExceptionPath()
    {
        return findTaggedValue(Bpm4StrutsProfile.TAGGED_VALUE_EXCEPTION_PATH);
    }

    // ------------- relations ------------------

}
