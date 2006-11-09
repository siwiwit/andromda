package org.andromda.cartridges.meta.metafacades;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.meta.metafacades.PSMmetaclass.
 *
 * @see org.andromda.cartridges.meta.metafacades.PSMmetaclass
 */
public class PSMmetaclassLogicImpl
    extends PSMmetaclassLogic
{

    public PSMmetaclassLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.meta.metafacades.PSMmetaclass#isOperationsPresent()
     */
    protected boolean handleIsOperationsPresent()
    {
        return this.getOperations().size() > 0;
    }

}