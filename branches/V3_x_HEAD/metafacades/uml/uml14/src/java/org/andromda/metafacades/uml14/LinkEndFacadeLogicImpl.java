package org.andromda.metafacades.uml14;

import java.util.Collection;
import java.util.Collections;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.LinkEndFacade.
 *
 * @see org.andromda.metafacades.uml.LinkEndFacade
 */
public class LinkEndFacadeLogicImpl
    extends LinkEndFacadeLogic
{
    public LinkEndFacadeLogicImpl (org.omg.uml.behavioralelements.commonbehavior.LinkEnd metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.LinkEndFacade#getInstance()
     */
    protected java.lang.Object handleGetInstance()
    {
        return metaObject.getInstance();
    }

    /**
     * @see org.andromda.metafacades.uml.LinkEndFacade#getAssociationEnd()
     */
    protected java.lang.Object handleGetAssociationEnd()
    {
        return metaObject.getAssociationEnd();
    }

    /**
     * @see org.andromda.metafacades.uml.LinkEndFacade#getLink()
     */
    protected java.lang.Object handleGetLink()
    {
        return metaObject.getLink();
    }

    /**
     * @see org.andromda.metafacades.uml.LinkEndFacade#getInstances()
     */
    protected Collection handleGetInstances()
    {
        return Collections.singleton(this.getInstance());
    }
}