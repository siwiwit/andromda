package org.andromda.cartridges.spring.metafacades;

import org.andromda.cartridges.spring.SpringProfile;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.spring.metafacades.SpringServiceOperation.
 * 
 * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation
 */
public class SpringServiceOperationLogicImpl
    extends SpringServiceOperationLogic
{
    // ---------------- constructor -------------------------------

    public SpringServiceOperationLogicImpl(
        Object metaObject,
        String context)
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
        return this.getImplementationNamePrefix()
            + StringUtils.capitalize(this.getName());
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperation#getImplementationSignature()
     */
    protected String handleGetImplementationSignature()
    {
        return this.getImplementationNamePrefix()
            + StringUtils.capitalize(this.getSignature());
    }

    /**
     * @see org.andromda.cartridges.spring.metafacades.SpringServiceOperationL#getImplementationCall()
     */
    protected String handleGetImplementationCall()
    {
        return this.getImplementationNamePrefix()
            + StringUtils.capitalize(this.getCall());
    }

    /**
     * Retrieves the implementationNamePrefix property from the namespace.
     * 
     * @return the implementation name prefix
     */
    private String getImplementationNamePrefix()
    {
        return StringUtils
            .trimToEmpty(String
                .valueOf(this
                    .getConfiguredProperty(SpringGlobals.PROPERTY_IMPLEMENTATION_OPERATION_NAME_PREFIX)));
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
        String transactionType = (String)this
            .findTaggedValue(SpringProfile.TAGGEDVALUE_TRANSACTION_TYPE);
        if (StringUtils.isBlank(transactionType))
        {
            transactionType = String.valueOf(this
                .getConfiguredProperty(SERVICE_OPERATION_TRANSACTION_TYPE));
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
        String transactionType = (String)this
            .findTaggedValue(SpringProfile.TAGGEDVALUE_EJB_TRANSACTION_TYPE);
        if (StringUtils.isBlank(transactionType))
        {
            transactionType = String.valueOf(this
                .getConfiguredProperty(EJB_SERVICE_OPERATION_TRANSACTION_TYPE));
        }
        return transactionType;
    }

}