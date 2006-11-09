package org.andromda.metafacades.emf.uml2;

import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.Activity;
import org.eclipse.uml2.Element;
import org.eclipse.uml2.Operation;
import org.eclipse.uml2.ParameterDirectionKind;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ParameterFacade.
 *
 * @see org.andromda.metafacades.uml.ParameterFacade
 */
public class ParameterFacadeLogicImpl
    extends ParameterFacadeLogic
{
    public ParameterFacadeLogicImpl(
        final org.eclipse.uml2.Parameter metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getDefaultValue()
     */
    protected java.lang.String handleGetDefaultValue()
    {
        return this.metaObject.getDefault();
    }
    
    /**
     * Overridden to provide name masking.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    protected String handleGetName()
    {
        final String nameMask = String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.PARAMETER_NAME_MASK));
        return NameMasker.mask(
            super.handleGetName(),
            nameMask);
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isReturn()
     */
    protected boolean handleIsReturn()
    {
        return this.metaObject.getDirection().equals(ParameterDirectionKind.RETURN_LITERAL);
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isRequired()
     */
    protected boolean handleIsRequired()
    {
        return !this.hasStereotype(UMLProfile.STEREOTYPE_NULLABLE);
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getGetterName()
     */
    protected java.lang.String handleGetGetterName()
    {
        return UMLMetafacadeUtils.getGetterPrefix(this.getType()) + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getSetterName()
     */
    protected java.lang.String handleGetSetterName()
    {
        return "set" + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isReadable()
     */
    protected boolean handleIsReadable()
    {
        return this.isInParameter() || this.isInoutParameter();
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isWritable()
     */
    protected boolean handleIsWritable()
    {
        return this.isOutParameter() || this.isInoutParameter();
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isDefaultValuePresent()
     */
    protected boolean handleIsDefaultValuePresent()
    {
        return StringUtils.isNotBlank(this.getDefaultValue());
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isInParameter()
     */
    protected boolean handleIsInParameter()
    {
        return this.metaObject.getDirection().equals(ParameterDirectionKind.IN_LITERAL);
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isOutParameter()
     */
    protected boolean handleIsOutParameter()
    {
        return this.metaObject.getDirection().equals(ParameterDirectionKind.OUT_LITERAL);
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#isInoutParameter()
     */
    protected boolean handleIsInoutParameter()
    {
        return this.metaObject.getDirection().equals(ParameterDirectionKind.INOUT_LITERAL);
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getOperation()
     */
    protected java.lang.Object handleGetOperation()
    {
        Object owner = this.metaObject.getOwner();
        if (owner instanceof Operation)
        {
            return owner;
        }
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getEvent()
     */
    protected java.lang.Object handleGetEvent()
    {
        Element owner = this.metaObject.getOwner();
        if (owner instanceof Activity)
        {
            return owner;
        }
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.ParameterFacade#getType()
     */
    protected java.lang.Object handleGetType()
    {
        return this.metaObject.getType();
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        Object owner = this.getOperation();
        if (owner == null)
        {
            owner = this.getEvent();
        }
        return owner;
    }

    /**
     * Get the UML upper multiplicity Not implemented for UML1.4
     */
    protected int handleGetUpper()
    {
        return UmlUtilities.parseMultiplicity(this.metaObject.getUpperValue());
    }

    /**
     * Get the UML lower multiplicity Not implemented for UML1.4
     */
    protected int handleGetLower()
    {
        return UmlUtilities.parseMultiplicity(this.metaObject.getLowerValue());
    }
}