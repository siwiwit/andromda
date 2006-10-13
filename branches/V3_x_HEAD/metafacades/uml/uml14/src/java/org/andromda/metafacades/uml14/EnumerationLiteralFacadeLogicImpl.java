package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.EnumerationLiteralFacade.
 *
 * @see org.andromda.metafacades.uml.EnumerationLiteralFacade
 */
public class EnumerationLiteralFacadeLogicImpl
    extends EnumerationLiteralFacadeLogic
{
    public EnumerationLiteralFacadeLogicImpl(
        org.omg.uml.foundation.core.EnumerationLiteral metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacadeLogic#getName()
     */
    protected String handleGetName()
    {
        return this.getName(false);
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationLiteralFacade#getValue()
     */
    protected String handleGetValue()
    {
        return this.getValue(false);
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationLiteralFacade#getName(boolean)
     */
    protected String handleGetName(boolean modelName)
    {
        String name = super.handleGetName();
        final String mask = String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ENUMERATION_LITERAL_NAME_MASK));
        if (!modelName && StringUtils.isNotBlank(mask))
        {
            name = NameMasker.mask(name, mask);
        }
        return name;
    }
    
    /**
     * @see org.andromda.metafacades.uml.EnumerationLiteralFacade#getValue(boolean)
     */
    protected String handleGetValue(boolean modelValue)
    {
        return StringUtils.trimToEmpty(this.getName(modelValue));
    }
}