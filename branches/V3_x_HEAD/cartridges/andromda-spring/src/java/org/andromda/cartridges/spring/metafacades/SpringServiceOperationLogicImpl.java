package org.andromda.cartridges.spring.metafacades;

import org.andromda.cartridges.spring.SpringProfile;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.MetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringServiceOperation.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation
 */
public class SpringServiceOperationLogicImpl
        extends SpringServiceOperationLogic
{

    public SpringServiceOperationLogicImpl(Object metaObject, String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#isWebserviceExposed()
     */
    protected boolean handleIsWebserviceExposed()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_WEBSERVICE_OPERATION);
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getImplementationName()
     */
    protected String handleGetImplementationName()
    {
        return this.getImplementationOperationName(StringUtils.capitalize(this.getName()));
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getImplementationSignature()
     */
    protected String handleGetImplementationSignature()
    {
        String signature;
        if (this.isIncomingMessageOperation())
        {
            signature = this.getIncomingMessageImplementationSignature();
        }
        else if (this.isOutgoingMessageOperation())
        {
            signature = this.getOutgoingMessageImplementationSignature();
        }
        else
        {
            signature = this.getImplementationOperationName(StringUtils.capitalize(this.getSignature()));
        }
        return signature;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperationL#getImplementationCall()
     */
    protected String handleGetImplementationCall()
    {
        return this.getImplementationOperationName(StringUtils.capitalize(this.getCall()));
    }

    /**
     * Retrieves the implementationOperatName by replacing the <code>replacement</code> in the {@link
     * SpringGlobals#IMPLEMENTATION_OPERATION_NAME_PATTERN}
     *
     * @param replacement the replacement string for the pattern.
     * @return the operation name
     */
    private String getImplementationOperationName(String replacement)
    {
        return StringUtils.trimToEmpty(String.valueOf(this.getConfiguredProperty(
                SpringGlobals.IMPLEMENTATION_OPERATION_NAME_PATTERN))).replaceAll("\\{0\\}", replacement);
    }

    /**
     * The transation type for Spring service operations.
     */
    private static final String SERVICE_OPERATION_TRANSACTION_TYPE = "serviceOperationTransactionType";

    /**
     * @see org.andromda.metafacades.uml.ServiceOperationFacade#getTransactionType()
     */
    public String handleGetTransactionType()
    {
        String transactionType = (String)this.findTaggedValue(SpringProfile.TAGGEDVALUE_TRANSACTION_TYPE);
        if (StringUtils.isBlank(transactionType))
        {
            transactionType = (String)this.getOwner().findTaggedValue(SpringProfile.TAGGEDVALUE_TRANSACTION_TYPE);
        }
        if (StringUtils.isBlank(transactionType))
        {
            transactionType = String.valueOf(this.getConfiguredProperty(SERVICE_OPERATION_TRANSACTION_TYPE));
        }
        return transactionType;
    }

    /**
     * The transaction type for EJB wrapped service operations..
     */
    private static final String EJB_SERVICE_OPERATION_TRANSACTION_TYPE = "ejbServiceOperationTransactionType";

    /**
     * @see org.andromda.metafacades.uml.ServiceOperationFacade#getEjbTransactionType()
     */
    protected String handleGetEjbTransactionType()
    {
        String transactionType = (String)this.findTaggedValue(SpringProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE);
        if (StringUtils.isBlank(transactionType))
        {
            transactionType = (String)this.getOwner().findTaggedValue(SpringProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE);
        }
        if (StringUtils.isBlank(transactionType))
        {
            transactionType = String.valueOf(this.getConfiguredProperty(EJB_SERVICE_OPERATION_TRANSACTION_TYPE));
        }
        return transactionType;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getThrowsClause()
     */
    protected String handleGetThrowsClause()
    {
        StringBuffer throwsClause = null;
        if (this.isExceptionsPresent())
        {
            throwsClause = new StringBuffer(this.getExceptionList());
        }
        if (throwsClause != null)
        {
            throwsClause.insert(0, "throws ");
        }
        return throwsClause != null ? throwsClause.toString() : null;
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getThrowsClause(java.lang.String)
     */
    protected String handleGetThrowsClause(String initialExceptions)
    {
        final StringBuffer throwsClause = new StringBuffer(initialExceptions);
        if (this.getThrowsClause() != null)
        {
            throwsClause.insert(0, ", ");
            throwsClause.insert(0, this.getThrowsClause());
        }
        else
        {
            throwsClause.insert(0, "throws ");
        }
        return throwsClause.toString();
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getOutgoingMessageImplementationCall()
     */
    protected String handleGetOutgoingMessageImplementationCall()
    {
        return this.getOutgoingMessageImplementationCall("session");
    }
    
    private String getOutgoingMessageImplementationCall(String firstArgument)
    {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(StringUtils.capitalize(this.getName()));
        buffer.append("(" + firstArgument + ", ");
        buffer.append(this.getArgumentNames());
        buffer.append(")");
        return this.getImplementationOperationName(buffer.toString());  
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getOutgoingMessageImplementationSignature()
     */
    protected String handleGetOutgoingMessageImplementationSignature()
    {
        return this.getMessagingImplementationSignature("javax.jms.Session session");
    }
    
    private String getMessagingImplementationSignature(final String firstArgument)
    {
        final StringBuffer signature = new StringBuffer(this.getImplementationName());
        signature.append("(" + firstArgument + ", ");
        signature.append(MetafacadeUtils.getTypedArgumentList(
                this.getArguments(),
                true,
                null));
        signature.append(")");
        return signature.toString();
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getIncomingMessageImplementationCall()
     */
    protected String handleGetIncomingMessageImplementationCall()
    {
        return this.getOutgoingMessageImplementationCall("message");
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getIncomingMessageImplementationSignature()
     */
    protected String handleGetIncomingMessageImplementationSignature()
    {
        return this.getMessagingImplementationSignature("javax.jms.Message message");
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getImplementationReturnTypeName()
     */
    protected String handleGetImplementationReturnTypeName()
    {
        String returnTypeName;
        if (this.isOutgoingMessageOperation())
        {
            returnTypeName = "javax.jms.Message";
        }
        else
        {
            final ClassifierFacade returnType = this.getReturnType();
            returnTypeName = returnType != null ? returnType.getFullyQualifiedName() : null;
        }
        return returnTypeName;
    }
}