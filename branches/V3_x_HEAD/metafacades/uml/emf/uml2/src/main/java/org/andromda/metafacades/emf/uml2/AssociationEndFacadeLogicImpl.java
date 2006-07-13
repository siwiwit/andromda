package org.andromda.metafacades.emf.uml2;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.AggregationKind;
import org.eclipse.uml2.MultiplicityElement;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.AssociationEndFacade.
 *
 * @see org.andromda.metafacades.uml.AssociationEndFacade
 */
public class AssociationEndFacadeLogicImpl
    extends AssociationEndFacadeLogic
{
    public AssociationEndFacadeLogicImpl(
        final org.eclipse.uml2.Property metaObject,
        final String context)
    {
        super((AssociationEnd)metaObject, context);
        if (!(metaObject instanceof AssociationEnd))
        {
            // This case occurs when a method return property instead of an
            // "AssociationEnd"
            throw new RuntimeException("AssociationEndFacade created with a " + metaObject);
        }
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isOne2One()
     */
    protected boolean handleIsOne2One()
    {
        return !this.isMany() && !this.getOtherEnd().isMany();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isOne2Many()
     */
    protected boolean handleIsOne2Many()
    {
        return !this.isMany() && this.getOtherEnd().isMany();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany2One()
     */
    protected boolean handleIsMany2One()
    {
        return this.isMany() && !this.getOtherEnd().isMany();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany2Many()
     */
    protected boolean handleIsMany2Many()
    {
        return this.isMany() && this.getOtherEnd().isMany();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isAggregation()
     */
    protected boolean handleIsAggregation()
    {
        return this.metaObject.getAggregation().equals(AggregationKind.SHARED_LITERAL);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isComposition()
     */
    protected boolean handleIsComposition()
    {
        return this.metaObject.getAggregation().equals(AggregationKind.COMPOSITE_LITERAL);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isOrdered()
     */
    protected boolean handleIsOrdered()
    {
        return this.metaObject.isOrdered();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isReadOnly()
     */
    protected boolean handleIsReadOnly()
    {
        return this.metaObject.isReadOnly();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isNavigable()
     */
    protected boolean handleIsNavigable()
    {
        return this.metaObject.isNavigable();
    }

    /**
     * @see org.andromda.metafacades.uml14.ModelElementFacadeLogic#handleGetName()
     */
    protected String handleGetName()
    {
        String name = super.handleGetName();

        // if name is empty, then get the name from the type
        if (StringUtils.isEmpty(name))
        {
            final ClassifierFacade type = this.getType();
            if (type != null)
            {
                name = StringUtils.uncapitalize(StringUtils.trimToEmpty(type.getName()));
            }
            if (this.isMany() && this.isPluralizeAssociationEndNames())
            {
                name = StringUtilsHelper.pluralize(name);
            }
        }
        final String nameMask =
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.CLASSIFIER_PROPERTY_NAME_MASK));
        return NameMasker.mask(
            name,
            nameMask);
    }

    /**
     * Indicates whether or not we should pluralize association end names.
     *
     * @return true/false
     */
    private boolean isPluralizeAssociationEndNames()
    {
        final Object value = this.getConfiguredProperty(UMLMetafacadeProperties.PLURALIZE_ASSOCIATION_END_NAMES);
        return value != null && Boolean.valueOf(String.valueOf(value)).booleanValue();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getGetterName()
     */
    protected java.lang.String handleGetGetterName()
    {
        return UMLMetafacadeUtils.getGetterPrefix(this.getType()) + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getSetterName()
     */
    protected java.lang.String handleGetSetterName()
    {
        return "set" + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getGetterSetterTypeName()
     */
    protected java.lang.String handleGetGetterSetterTypeName()
    {
        String name = null;
        if (this.isMany())
        {
            final TypeMappings mappings = this.getLanguageMappings();
            if (mappings != null)
            {
                name =
                    this.isOrdered() ? mappings.getTo(UMLProfile.LIST_TYPE_NAME)
                                     : mappings.getTo(UMLProfile.COLLECTION_TYPE_NAME);
            }

            // set this association end's type as a template parameter if
            // required
            if (BooleanUtils.toBoolean(
                    ObjectUtils.toString(this.getConfiguredProperty(UMLMetafacadeProperties.ENABLE_TEMPLATING))))
            {
                name = name + "<" + this.getType().getFullyQualifiedName() + ">";
            }
        }
        if (name == null && this.getType() != null)
        {
            name = this.getType().getFullyQualifiedName();
        }
        return name;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isMany()
     */
    protected boolean handleIsMany()
    {
        // Because of MD11.5 (their multiplicity are String), we cannot use
        // isMultiValued()
        return this.getUpper() > 1 || this.getUpper() == MultiplicityElement.UNLIMITED_UPPER_BOUND;
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isRequired()
     */
    protected boolean handleIsRequired()
    {
        return (this.getLower() > 0);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#isChild()
     */
    protected boolean handleIsChild()
    {
        return this.getOtherEnd() != null && this.getOtherEnd().isComposition();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getOtherEnd()
     */
    protected java.lang.Object handleGetOtherEnd()
    {
        return UmlUtilities.getOppositeAssociationEnd(this.metaObject);
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getAssociation()
     */
    protected java.lang.Object handleGetAssociation()
    {
        return this.metaObject.getAssociation();
    }

    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getType()
     */
    protected java.lang.Object handleGetType()
    {
        // In uml1.4 facade, it returns getParticipant
        return this.metaObject.getType();
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return this.getType();
    }

    /**
     * Get the UML upper multiplicity Not implemented for UML1.4
     */
    protected int handleGetUpper()
    {
        // MD11.5 Exports multiplicity as String
        return UmlUtilities.parseMultiplicity(this.metaObject.getUpperValue());
    }

    /**
     * Get the UML lower multiplicity Not implemented for UML1.4
     */
    protected int handleGetLower()
    {
        // MD11.5 Exports multiplicity as String
        return UmlUtilities.parseMultiplicity(this.metaObject.getLowerValue());
    }
}