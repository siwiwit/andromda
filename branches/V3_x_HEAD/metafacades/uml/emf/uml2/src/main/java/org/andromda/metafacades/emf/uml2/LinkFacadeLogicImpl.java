package org.andromda.metafacades.emf.uml2;

import java.util.Collections;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.LinkFacade.
 *
 * @see org.andromda.metafacades.uml.LinkFacade
 */
public class LinkFacadeLogicImpl
    extends LinkFacadeLogic
{

    public LinkFacadeLogicImpl(LinkInstance metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * UML2 does not have the concept of LinkEnds (unlike UML1.4+) so this method always returns
     * an empty collection.
     *
     * @see org.andromda.metafacades.uml.LinkFacade#getLinkEnds()
     */
    protected java.util.Collection handleGetLinkEnds()
    {
        return Collections.EMPTY_LIST; // todo figure out how to handle this
    }

}