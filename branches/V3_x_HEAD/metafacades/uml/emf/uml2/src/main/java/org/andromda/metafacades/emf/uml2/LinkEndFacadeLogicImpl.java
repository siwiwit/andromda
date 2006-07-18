package org.andromda.metafacades.emf.uml2;

import org.eclipse.uml2.InstanceValue;

import java.util.List;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.LinkEndFacade.
 *
 * @see org.andromda.metafacades.uml.LinkEndFacade
 */
public class LinkEndFacadeLogicImpl extends LinkEndFacadeLogic
{
    public LinkEndFacadeLogicImpl(LinkEnd metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.LinkEndFacade#getInstance()
     */
    protected java.lang.Object handleGetInstance()
    {
        final Object instance;

        final List values = this.metaObject.getValues();
        if (values != null && !values.isEmpty() && values.get(0) instanceof InstanceValue)
        {
            instance = UmlUtilities.ELEMENT_TRANSFORMER.transform(((InstanceValue)values.get(0)).getInstance());
        }
        else
        {
            instance = null;
        }

        return instance;
    }

    /**
     * @see org.andromda.metafacades.uml.LinkEndFacade#getAssociationEnd()
     */
    protected java.lang.Object handleGetAssociationEnd()
    {
        return UmlUtilities.ELEMENT_TRANSFORMER.transform(this.metaObject.getDefiningFeature());
    }

    /**
     * @see org.andromda.metafacades.uml.LinkEndFacade#getLink()
     */
    protected java.lang.Object handleGetLink()
    {
        return UmlUtilities.ELEMENT_TRANSFORMER.transform(this.metaObject.getOwner());
    }
}