package org.andromda.metafacades.uml2;

import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.NameMasker;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.EnumerationLiteralFacade.
 *
 * @see org.andromda.metafacades.uml.EnumerationLiteralFacade
 */
public class EnumerationLiteralFacadeLogicImpl
    extends EnumerationLiteralFacadeLogic
{
    public EnumerationLiteralFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml2.ModelElementFacadeLogic#handleGetName()
     */
    protected String handleGetName()
    {
        final String mask = String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ENUMERATION_LITERAL_NAME_MASK));
        return NameMasker.mask(super.handleGetName(), mask);
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationLiteralFacade#getValue()
     */
    protected java.lang.String handleGetValue()
    {
        return StringUtils.trimToEmpty(this.getName());
    }
}