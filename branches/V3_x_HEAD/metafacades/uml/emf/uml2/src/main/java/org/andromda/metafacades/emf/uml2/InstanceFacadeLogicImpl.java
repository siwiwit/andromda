package org.andromda.metafacades.emf.uml2;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.eclipse.uml2.InstanceSpecification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


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
        return Collections.EMPTY_LIST; // todo figure out how to handle this (figure out all links and collect their ends ?)
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
        return CollectionUtils.transformedCollection(ownedElements, UmlUtilities.ELEMENT_TRANSFORMER);
    }

    /**
     * Instances do not own Links in UML2 (unlike UML1.4+), this method always returns an empty collection.
     *
     * @see org.andromda.metafacades.uml.InstanceFacade#getOwnedLinks()
     */
    protected java.util.Collection handleGetOwnedLinks()
    {
        return Collections.EMPTY_LIST; // todo figure out how to handle this (don't forget to transform)
    }

    /**
     * @see org.andromda.metafacades.uml.InstanceFacade#getSlots()
     */
    protected java.util.Collection handleGetSlots()
    {
        return CollectionUtils.transformedCollection(this.metaObject.getSlots(), UmlUtilities.ELEMENT_TRANSFORMER);
    }
}