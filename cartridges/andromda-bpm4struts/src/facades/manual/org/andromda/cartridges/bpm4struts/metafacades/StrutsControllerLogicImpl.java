package org.andromda.cartridges.bpm4struts.metafacades;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController
 */
public class StrutsControllerLogicImpl
        extends StrutsControllerLogic
        implements org.andromda.cartridges.bpm4struts.metafacades.StrutsController
{
    // ---------------- constructor -------------------------------
    
    public StrutsControllerLogicImpl(java.lang.Object metaObject, java.lang.String context)
    {
        super(metaObject, context);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsController ...

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getControllerHelperClassName()()
     */
    public java.lang.String getControllerHelperClassName()
    {
        return getName();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getControllerHelperPackageName()()
     */
    public java.lang.String getControllerHelperPackageName()
    {
        return getPackageName();
    }

    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsController#getControllerHelperType()()
     */
    public java.lang.String getControllerHelperType()
    {
        return getFullyQualifiedName();
    }

    // ------------- relations ------------------

}
