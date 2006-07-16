package org.andromda.metafacades.emf.uml2;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.LinkEndFacade.
 *
 * @see org.andromda.metafacades.uml.LinkEndFacade
 */
public class LinkEndFacadeLogicImpl
    extends LinkEndFacadeLogic
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
        return UmlUtilities.ELEMENT_TRANSFORMER.transform(this.metaObject.getOwningInstance());
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