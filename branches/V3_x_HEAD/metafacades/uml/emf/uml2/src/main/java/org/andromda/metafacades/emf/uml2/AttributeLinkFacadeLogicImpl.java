package org.andromda.metafacades.emf.uml2;

import java.util.List;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.AttributeLinkFacade.
 *
 * @see org.andromda.metafacades.uml.AttributeLinkFacade
 */
public class AttributeLinkFacadeLogicImpl
    extends AttributeLinkFacadeLogic
{

    public AttributeLinkFacadeLogicImpl(org.eclipse.uml2.Slot metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getAttribute()
     */
    protected java.lang.Object handleGetAttribute()
    {
        return this.metaObject.getDefiningFeature();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getLinkEnd()
     */
    protected java.lang.Object handleGetLinkEnd()
    {
        return null; // todo ?
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getInstance()
     */
    protected java.lang.Object handleGetInstance()
    {
        return this.metaObject.getOwningInstance();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeLinkFacade#getValue()
     */
    protected java.lang.Object handleGetValue()
    {
        final List values = this.metaObject.getValues();
        return values == null || values.isEmpty() ? null : values.get(0);
    }

}