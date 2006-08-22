package org.andromda.metafacades.emf.uml2;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.AssociationClass;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.AssociationFacade.
 *
 * @see org.andromda.metafacades.uml.AssociationFacade
 */
public class AssociationFacadeLogicImpl
    extends AssociationFacadeLogic
{
    public AssociationFacadeLogicImpl(
        final org.eclipse.uml2.Association metaObject,
        final String context)
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
     * Overriden: A name may not have a name, which
     * is problematic for getSqlName (for an EntityAssociation).
     * We use the relation name as default
     * @see org.andromda.metafacades.emf.uml2.ModelElementFacadeLogic#handleGetName()
     */
    public String handleGetName() {
		String name = super.handleGetName();

		// if the name isn't defined, use the relation name
		if (StringUtils.isEmpty(name)) {
			name = this.getRelationName();
		}
		return name;
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
        return (List)CollectionUtils.collect(
            this.metaObject.getMemberEnds(),
            UmlUtilities.ELEMENT_TRANSFORMER);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationFacade#isAssociationClass()
     */
    protected boolean handleIsAssociationClass()
    {
        // TODO: Test this if it works.
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