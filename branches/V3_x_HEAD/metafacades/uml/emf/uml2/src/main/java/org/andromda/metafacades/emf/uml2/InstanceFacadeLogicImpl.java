package org.andromda.metafacades.emf.uml2;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.eclipse.uml2.Association;
import org.eclipse.uml2.InstanceSpecification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.InstanceFacade.
 *
 * @see org.andromda.metafacades.uml.InstanceFacade
 */
public class InstanceFacadeLogicImpl
    extends InstanceFacadeLogic
{

    public InstanceFacadeLogicImpl(ObjectInstance metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getClassifiers()
     */
    protected java.util.Collection handleGetClassifiers()
    {
        return this.metaObject.getClassifiers();
    }

    /**
     * Since UML2 does not have the notion of LinkEnds as UML1.4+ does this method always returns an empty collection.
     *
     * @see org.andromda.metafacades.uml.InstanceFacade#getLinkEnds()
     */
    protected java.util.Collection handleGetLinkEnds()
    {
        return Collections.EMPTY_LIST; // todo figure out how to handle this
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getOwnedInstances()
     */
    protected java.util.Collection handleGetOwnedInstances()
    {
        final Collection ownedElements = new ArrayList(this.metaObject.getOwnedElements());
        CollectionUtils.filter(ownedElements, new Predicate()
        {
            public boolean evaluate(Object object)
            {
                return object instanceof InstanceSpecification;
            }
        });
        return ownedElements;
    }

    /**
     * Instances do not own Links in UML2 (unlike UML1.4+), this method always returns an empty collection.
     *
     * @see org.andromda.metafacades.uml.InstanceFacade#getOwnedLinks()
     */
    protected java.util.Collection handleGetOwnedLinks()
    {
        return Collections.EMPTY_LIST; // todo figure out how to handle this
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getSlots()
     */
    protected java.util.Collection handleGetSlots()
    {
        return this.metaObject.getSlots();
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