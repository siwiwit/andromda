package org.andromda.cartridges.meta.metafacades;
import java.util.Iterator;
/**
 */
    {
        super(metaObjectIn, context);
    }
    /**
     * @see org.andromda.cartridges.meta.metafacades.PSMmetaclass#isOperationsPresent()
     */
    @Override
    {
        return this.getOperations().size() > 0;
    }
    /** 
     * @see org.andromda.cartridges.meta.metafacades.PSMmetaclassLogic#handleIsImplMustBeAbstract()
     */
    @Override
    {
        boolean result = false;
        // if the class itself is abstract, make the impl abstract, too.
        if (this.isAbstract())
        {
            result = true;