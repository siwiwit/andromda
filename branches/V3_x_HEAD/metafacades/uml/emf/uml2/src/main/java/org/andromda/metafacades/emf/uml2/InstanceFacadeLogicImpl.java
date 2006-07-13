package org.andromda.metafacades.emf.uml2;

import org.eclipse.uml2.Association;

import java.util.List;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.InstanceFacade.
 *
 * @see org.andromda.metafacades.uml.InstanceFacade
 */
public class InstanceFacadeLogicImpl
    extends InstanceFacadeLogic
{

    public InstanceFacadeLogicImpl (org.eclipse.uml2.InstanceSpecification metaObject, String context)
    {
        super (metaObject, context);
    }
    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getClassifiers()
     */
    protected java.util.Collection handleGetClassifiers()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getLinkEnds()
     */
    protected java.util.Collection handleGetLinkEnds()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getOwnedInstances()
     */
    protected java.util.Collection handleGetOwnedInstances()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getOwnedLinks()
     */
    protected java.util.Collection handleGetOwnedLinks()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getSlots()
     */
    protected java.util.Collection handleGetSlots()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * This method has been written to provide exception support to the mapping declarations since both
     * UML2 instances and links share the same meta-class
     * <p/>
     * You can safely ignore this feature, in fact, it's best never to use it at all except if you really know
     * what you're doing.
     * <p/>
     * This method checks whether the first classifier is an association and returns <code>true</code> only in that
     * case.
     */
    public boolean isShouldMapToLinkFacade()
    {
        final List classifiers = this.metaObject.getClassifiers();
        return classifiers != null && !classifiers.isEmpty() && classifiers.get(0) instanceof Association;
    }
}