package org.andromda.metafacades.uml2;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.LinkFacade.
 *
 * @see org.andromda.metafacades.uml.LinkFacade
 */
public class LinkFacadeLogicImpl
    extends LinkFacadeLogic
{

    public LinkFacadeLogicImpl (org.omg.uml2.kernel.Element metaObject, String context)
    {
        super (metaObject, context);
    }

    protected java.util.Collection handleGetLinkEnds()
    {
        // Wouter: I have no idea on which feature this maps in UML2
        // the notion of a link-end exists but apparently the 'link' has a different name
        return null;
    }
}