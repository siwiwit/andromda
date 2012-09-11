package org.andromda.metafacades.emf.uml2;

import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.ValueObject;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ValueObjectAssociationEnd.
 *
 * @see org.andromda.metafacades.uml.ValueObjectAssociationEnd
 */
public class ValueObjectAssociationEndLogicImpl
    extends ValueObjectAssociationEndLogic
{
    private static final long serialVersionUID = -8876529450826502636L;

    /**
     * @param metaObject
     * @param context
     */
    public ValueObjectAssociationEndLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * Overridden to provide handling of array names within many type
     * multiplicities.
     * @return getterSetterTypeName
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getGetterSetterTypeName()
     */
    protected String handleGetGetterSetterTypeName()
    {
        String name = super.handleGetGetterSetterTypeName();
        if (this.isMany())
        {
            boolean useArrays =
                    Boolean.valueOf(
                            String.valueOf(
                                    this.getConfiguredProperty(UMLMetafacadeProperties.USE_ARRAYS_FOR_MULTIPLICITIES_OF_TYPE_MANY)));
            if (useArrays)
            {
                if (this.getType() != null)
                {
                    name = this.getType().getFullyQualifiedArrayName();
                }
            }
        }
        return name;
    }

    /**
     * @return getType() instanceof ValueObject
     * @see org.andromda.metafacades.uml.ValueObjectAssociationEnd#isValueObjectType()
     */
    protected boolean handleIsValueObjectType()
    {
        return this.getType() instanceof ValueObject;
    }
}
