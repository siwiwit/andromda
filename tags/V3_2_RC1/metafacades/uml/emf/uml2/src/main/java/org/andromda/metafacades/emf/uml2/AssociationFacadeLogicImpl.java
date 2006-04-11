package org.andromda.metafacades.emf.uml2;

import java.util.Collection;
import java.util.Iterator;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.eclipse.uml2.AssociationClass;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.AssociationFacade.
 *
 * @see org.andromda.metafacades.uml.AssociationFacade
 */
public class AssociationFacadeLogicImpl
    extends AssociationFacadeLogic
{
    public AssociationFacadeLogicImpl(
        org.eclipse.uml2.Association metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#getRelationName()
     */
    protected java.lang.String handleGetRelationName()
    {
        final Collection ends = this.getAssociationEnds();
        final Iterator endIt = ends.iterator();
        final AssociationEndFacade firstEnd = (AssociationEndFacade)endIt.next();
        final AssociationEndFacade secondEnd = (AssociationEndFacade)endIt.next();
        return MetafacadeUtils.toRelationName(
            firstEnd.getName(),
            secondEnd.getName(),
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.RELATION_NAME_SEPARATOR)));
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#isMany2Many()
     */
    protected boolean handleIsMany2Many()
    {
        return ((AssociationEndFacade)this.getAssociationEnds().iterator().next()).isMany2Many();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#getAssociationEnds()
     */
    protected java.util.List handleGetAssociationEnds()
    {
        return this.metaObject.getMemberEnds();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#isAssociationClass()
     */
    protected boolean handleIsAssociationClass()
    {
        return AssociationClass.class.isAssignableFrom(this.metaObject.getClass());
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#getAssociationEndA()
     */
    protected Object handleGetAssociationEndA()
    {
        return this.getAssociationEnds().get(0);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#getAssociationEndB()
     */
    protected Object handleGetAssociationEndB()
    {
        return this.getAssociationEnds().get(1);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#isAbstract()
     */
    protected boolean handleIsAbstract()
    {
        return this.metaObject.isAbstract();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#isLeaf
     */
    protected boolean handleIsLeaf()
    {
        return this.metaObject.isLeaf();
    }
}