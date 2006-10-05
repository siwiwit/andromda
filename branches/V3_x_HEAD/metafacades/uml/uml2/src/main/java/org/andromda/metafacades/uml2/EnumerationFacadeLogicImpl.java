package org.andromda.metafacades.uml2;

import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang.BooleanUtils;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.EnumerationFacade.
 *
 * @see org.andromda.metafacades.uml.EnumerationFacade
 */
public class EnumerationFacadeLogicImpl
    extends EnumerationFacadeLogic
{

    public EnumerationFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getFromOperationSignature()
     */
    protected java.lang.String handleGetFromOperationSignature()
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getFromOperationName()
     */
    protected java.lang.String handleGetFromOperationName()
    {
        // TODO: put your implementation here.
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getLiterals()
     */
    protected java.util.Collection handleGetLiterals()
    {
        // TODO: add your implementation here!
        return null;
    }

    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getMemberVariables()
     */
    protected java.util.Collection handleGetMemberVariables()
    {
        // TODO: add your implementation here!
        return null;
    }
    
    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#getLiteralType()
     */
    protected java.lang.Object handleGetLiteralType()
    {
        // TODO: add your implementation here!
        return null;
    }
    
    /**
     * @see org.andromda.metafacades.uml.EnumerationFacade#isTypeSafe()
     */
    protected boolean handleIsTypeSafe() 
    {
        return BooleanUtils.toBoolean(
                String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.TYPE_SAFE_ENUMS_ENABLED)));
    }
}