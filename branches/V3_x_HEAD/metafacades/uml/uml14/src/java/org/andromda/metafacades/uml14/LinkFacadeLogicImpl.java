package org.andromda.metafacades.uml14;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.LinkFacade.
 *
 * @see org.andromda.metafacades.uml.LinkFacade
 */
public class LinkFacadeLogicImpl
    extends LinkFacadeLogic
{
    public LinkFacadeLogicImpl (org.omg.uml.behavioralelements.commonbehavior.Link metaObject, String context)
    {
        super (metaObject, context);
    }

    protected java.util.Collection handleGetLinkEnds()
    {
        return this.metaObject.getConnection();
    }
}