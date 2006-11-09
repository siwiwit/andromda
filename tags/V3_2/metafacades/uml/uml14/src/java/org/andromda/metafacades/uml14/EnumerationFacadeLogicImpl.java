package org.andromda.metafacades.uml14;

import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;

/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.EnumerationFacade.
 *
 * @see org.andromda.metafacades.uml.EnumerationFacade
 */
public class EnumerationFacadeLogicImpl
        extends EnumerationFacadeLogic
{
    
    public EnumerationFacadeLogicImpl(org.omg.uml.foundation.core.Classifier metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * Overridden to provide name masking.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    protected String handleGetName()
    {
        final String nameMask = String.valueOf(
                this.getConfiguredProperty(UMLMetafacadeProperties.ENUMERATION_NAME_MASK));
        return NameMasker.mask(super.handleGetName(), nameMask);
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getLiterals()
     */
    protected java.util.Collection handleGetLiterals()
    {
        Collection literals = this.getAttributes();
        CollectionUtils.filter(
            literals,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    boolean isLiteral = true;
                    final AttributeFacade attribute = (AttributeFacade)object; 
                    if (attribute.isEnumerationMember())
                    {
                        isLiteral = false;
                    }
                    return isLiteral;
                }
            }
        );
        return literals;
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getMemberVariables()
     */
    protected java.util.Collection handleGetMemberVariables()
    {
        Collection variables = super.getAttributes();
        CollectionUtils.filter(
            variables,
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    boolean isMember = false;
                    final AttributeFacade attribute = (AttributeFacade)object;
                    if (attribute.isEnumerationMember())
                    {
                        isMember = true;
                    }
                    return isMember;
                }
            }
        );
        return variables;
    }
    
    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getFromOperationSignature()
     */
    protected String handleGetFromOperationSignature()
    {
        final StringBuffer signature = new StringBuffer(this.getFromOperationName());
        final ClassifierFacade type = this.getLiteralType();
        if (type != null)
        {
            signature.append('(');
            signature.append(type.getFullyQualifiedName());
            signature.append(" value)");
        }
        return signature.toString();
    }
    
    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#isTypeSafe()
     */
    protected boolean handleIsTypeSafe() 
    {
        return BooleanUtils.toBoolean(
                String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.TYPE_SAFE_ENUMS_ENABLED)));
    }
    
    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getFromOperationName()
     */
    protected String handleGetFromOperationName()
    {
        final StringBuffer name = new StringBuffer("from");
        final ClassifierFacade type = this.getLiteralType();
        if (type != null)
        {
            name.append(StringUtils.capitalize(type.getName()));
        }
        return name.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getLiteralType()
     */
    protected Object handleGetLiteralType()
    {
        Object type = null;
        final Collection literals = this.getLiterals();
        if (literals != null && !literals.isEmpty())
        {
            type = ((AttributeFacade)literals.iterator().next()).getType();
        }
        return type;
    }
}