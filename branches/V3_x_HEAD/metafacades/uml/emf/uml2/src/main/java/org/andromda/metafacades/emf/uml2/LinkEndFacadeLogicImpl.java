package org.andromda.metafacades.emf.uml2;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.LinkEndFacade.
 *
 * @see org.andromda.metafacades.uml.LinkEndFacade
 */
public class LinkEndFacadeLogicImpl
    extends LinkEndFacadeLogic
{

    public LinkEndFacadeLogicImpl(org.eclipse.uml2.InstanceValue metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.LinkEndFacade#getInstance()
     */
    protected java.lang.Object handleGetInstance()
    {
        return this.metaObject.getInstance();
    }

    /**
     * @see org.andromda.metafacades.uml.LinkEndFacade#getAssociationEnd()
     */
    protected java.lang.Object handleGetAssociationEnd()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * Although UML2 does not have the concept of a LinkEnd (unlike UML1.4+) this method returns the owner.
     *
     * @see org.andromda.metafacades.uml.LinkEndFacade#getLink()
     */
    protected java.lang.Object handleGetLink()
    {
        return this.metaObject.getOwner();
    }

}