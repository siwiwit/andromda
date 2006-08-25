package org.andromda.metafacades.emf.uml2;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.uml2.MultiplicityElement;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.AttributeFacade.
 *
 * @see org.andromda.metafacades.uml.AttributeFacade
 */
public class AttributeFacadeLogicImpl
    extends AttributeFacadeLogic
{
    public AttributeFacadeLogicImpl(
        final Attribute metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getGetterName()
     */
    protected java.lang.String handleGetGetterName()
    {
        return UMLMetafacadeUtils.getGetterPrefix(this.getType()) + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getSetterName()
     */
    protected java.lang.String handleGetSetterName()
    {
        return "set" + StringUtils.capitalize(this.metaObject.getName());
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isReadOnly()
     */
    protected boolean handleIsReadOnly()
    {
        return this.metaObject.isReadOnly();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getDefaultValue()
     */
    protected java.lang.String handleGetDefaultValue()
    {
        String defaultValue = this.metaObject.getDefault();
        return defaultValue.equals("") ? null : defaultValue;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isStatic()
     */
    protected boolean handleIsStatic()
    {
        return this.metaObject.isStatic();
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
        return this.getLower() > 0;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isChangeable()
     */
    protected boolean handleIsChangeable()
    {
        return !this.metaObject.isReadOnly();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isAddOnly()
     */
    protected boolean handleIsAddOnly()
    {
        // TODO: really nothing to do here ?
        return false;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#isEnumerationLiteral()
     */
    protected boolean handleIsEnumerationLiteral()
    {
        final ClassifierFacade owner = this.getOwner();
        return (owner != null) && owner.isEnumeration();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getEnumerationValue()
     */
    protected String handleGetEnumerationValue()
    {
        String value = null;
        if (this.isEnumerationLiteral())
        {
            value = this.getDefaultValue();
            value = (value == null) ? this.getName() : String.valueOf(value);
        }
        if (this.getType().isStringType())
        {
            value = "\"" + value + "\"";
        }
        return value;
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getGetterSetterTypeName()
     */
    protected java.lang.String handleGetGetterSetterTypeName()
    {
        String name = null;
        if (this.isMany())
        {
            final TypeMappings mappings = this.getLanguageMappings();
            name =
                this.isOrdered() ? mappings.getTo(UMLProfile.LIST_TYPE_NAME)
                                 : mappings.getTo(UMLProfile.COLLECTION_TYPE_NAME);

            // set this attribute's type as a template parameter if required
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
     * @see org.andromda.metafacades.uml.AttributeFacade#isOrdered()
     */
    protected boolean handleIsOrdered()
    {
        return this.metaObject.isOrdered();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getOwner()
     */
    protected java.lang.Object handleGetOwner()
    {
        // This is sure for attribute
        return this.metaObject.getClass_();
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return this.getOwner();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getType()
     */
    protected java.lang.Object handleGetType()
    {
        return this.metaObject.getType();
    }

    /**
     * @see org.andromda.metafacades.uml.AttributeFacade#getEnumeration()
     */
    protected Object handleGetEnumeration()
    {
        return this.isEnumerationLiteral() ? this.getOwner() : null;
    }

    protected boolean handleIsDefaultValuePresent()
    {
        return !(this.getDefaultValue() == null || this.getDefaultValue().equals(""));
    }

    /*  protected boolean handleIsBindingDependenciesPresent()
      {
          // TODO: Implement this with Templates.
          // This method has been overriden. Why ?
          return false;
      }

      protected boolean handleIsTemplateParametersPresent()
      {
          // TODO: This method has been overriden. Why ?
          return false;
      }

      protected void handleCopyTaggedValues(final ModelElementFacade element)
      {
          // TODO: This method has been overriden. Why ?
      }

      protected Object handleGetTemplateParameter(final String parameterName)
      {
          // TODO: This method has been overriden. Why ?
          return null;
      }

      protected Collection handleGetTemplateParameters()
      {
          // TODO: This method has been overriden. Why ?
          return null;
      } */

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

    protected Object handleFindTaggedValue(
        String name,
        final boolean follow)
    {
        name = StringUtils.trimToEmpty(name);
        Object value = this.findTaggedValue(name);
        if (follow)
        {
            ClassifierFacade type = this.getType();
            while (value == null && type != null)
            {
                value = type.findTaggedValue(name);
                type = (ClassifierFacade)type.getGeneralization();
            }
        }
        return value;
    }
}