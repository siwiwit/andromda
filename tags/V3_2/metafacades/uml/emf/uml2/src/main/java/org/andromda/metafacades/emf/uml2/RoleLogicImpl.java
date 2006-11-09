package org.andromda.metafacades.emf.uml2;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.Service;
import org.andromda.metafacades.uml.ServiceOperation;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.Classifier;
import org.eclipse.uml2.UseCase;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.Role.
 *
 * @see org.andromda.metafacades.uml.Role
 */
public class RoleLogicImpl
    extends RoleLogic
{
    public RoleLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.emf.uml2.ModelElementFacadeLogic#handleGetName()
     */
    public String handleGetName()
    {
        String name;
        Object value = this.findTaggedValue(UMLProfile.TAGGEDVALUE_ROLE_NAME);
        if (value != null)
        {
            name = StringUtils.trimToEmpty(String.valueOf(value));
        }
        else
        {
            name = super.handleGetName();
            String mask =
                StringUtils.trimToEmpty(
                    String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ROLE_NAME_MASK)));
            name = NameMasker.mask(
                    name,
                    mask);
        }
        return name;
    }

    /**
     * @see org.andromda.metafacades.uml.Role#isReferencesPresent()
     */
    protected boolean handleIsReferencesPresent()
    {
        final Collection allSourceDependencies = new LinkedHashSet(this.getSourceDependencies());
        for (GeneralizableElementFacade parent = this.getGeneralization(); parent != null;
            parent = parent.getGeneralization())
        {
            allSourceDependencies.addAll(parent.getSourceDependencies());
        }
        boolean present =
            CollectionUtils.find(
                allSourceDependencies,
                new Predicate()
                {
                    public boolean evaluate(Object object)
                    {
                        DependencyFacade dependency = (DependencyFacade)object;
                        Object target = dependency.getTargetElement();
                        return target instanceof Service || target instanceof ServiceOperation;
                    }
                }) != null;

        // - if no references on any services, try the FrontEndUseCases
        // used like in uml1.4, throws a stack overflow error
        if (!present)
        {
            final Collection associationEnds = UmlUtilities.getAssociationEnds(
                    (Classifier)this.metaObject,
                    false);
            for (final Iterator iterator = associationEnds.iterator(); iterator.hasNext() && !present;)
            {
                final AssociationEnd associationEnd = (AssociationEnd)iterator.next();
                final Object otherEnd = UmlUtilities.getOppositeAssociationEnd(associationEnd).getType();
                if (otherEnd instanceof UseCase)
                {
                    UseCase uc = (UseCase)otherEnd;
                    if (UmlUtilities.containsStereotype(uc, UMLProfile.STEREOTYPE_FRONT_END_USECASE) ||
                    	    UmlUtilities.containsStereotype(uc, UMLProfile.STEREOTYPE_FRONT_END_APPLICATION))
                    {
                        present = true;
                    }
                }
            }

            // - a generalized role is still a role, and therefore is associated
            // with the FrontEndUseCase
            // TODO: The generalized actors have to be Role too, isn't it ?
            if (!present)
            {
                present = !this.getGeneralizedActors().isEmpty();
            }
        }

        return present;
    }
}