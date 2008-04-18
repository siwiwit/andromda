package org.andromda.metafacades.uml14;

import java.util.Collection;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.InstanceFacade.
 *
 * @see org.andromda.metafacades.uml.InstanceFacade
 */
public class InstanceFacadeLogicImpl
    extends InstanceFacadeLogic
{
    public InstanceFacadeLogicImpl (org.omg.uml.behavioralelements.commonbehavior.Instance metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getClassifiers()
     */
    protected java.util.Collection handleGetClassifiers()
    {
        return metaObject.getClassifier();
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getLinkEnds()
     */
    protected java.util.Collection handleGetLinkEnds()
    {
        return metaObject.getLinkEnd();
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getOwnedInstances()
     */
    protected java.util.Collection handleGetOwnedInstances()
    {
        return metaObject.getOwnedInstance();
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getOwnedLinks()
     */
    protected java.util.Collection handleGetOwnedLinks()
    {
        return metaObject.getOwnedLink();
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getSlots()
     */
    protected java.util.Collection handleGetSlots()
    {
        return metaObject.getSlot();
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getAttributeLinks()
     */
    protected Collection handleGetAttributeLinks()
    {
        // wouter: in UML1.4 the slots only convey the attribute links (unless I'm mistaken this is different in UML2)
        return metaObject.getSlot();
    }
}