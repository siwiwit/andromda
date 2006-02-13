package org.andromda.cartridges.ejb3.metafacades;

import java.text.MessageFormat;

import org.andromda.cartridges.ejb3.EJB3Profile;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacade.
 *
 * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacade
 */
public class EJB3ManageableEntityFacadeLogicImpl
    extends EJB3ManageableEntityFacadeLogic
{
    /**
     * The property which stores the pattern defining the manageable create exception name.
     */
    public static final String CREATE_EXCEPTION_NAME_PATTERN = "manageableCreateExceptionNamePattern";
    
    /**
     * The property which stores the pattern defining the manageable read exception name.
     */
    public static final String READ_EXCEPTION_NAME_PATTERN = "manageableReadExceptionNamePattern";
    
    /**
     * The property which stores the pattern defining the manageable update exception name.
     */
    public static final String UPDATE_EXCEPTION_NAME_PATTERN = "manageableUpdateExceptionNamePattern";
    
    /**
     * The property which stores the pattern defining the manageable delete exception name.
     */
    public static final String DELETE_EXCEPTION_NAME_PATTERN = "manageableDeleteExceptionNamePattern";
    
    /**
     * The property which stores the persistence context unit name associated with the default
     * Entity Manager.
     */
    private static final String PERSISTENCE_CONTEXT_UNIT_NAME = "persistenceContextUnitName";
    
    /**
     * The property that stores the JNDI name prefix.
     */
    public static final String SERVICE_JNDI_NAME_PREFIX = "jndiNamePrefix";

    /**
     * The property that stores the manageable service base name pattern
     */
    public static final String MANAGEABLE_SERVICE_BASE_NAME_PATTERN = "manageableServiceBaseNamePattern";
    
    public EJB3ManageableEntityFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetManageableServiceCreateExceptionName()
     */
    protected String handleGetManageableServiceCreateExceptionName()
    {
        String exceptionNamePattern = (String)this.getConfiguredProperty(CREATE_EXCEPTION_NAME_PATTERN);

        return MessageFormat.format(
                exceptionNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetFullyQualifiedManageableServiceCreateExceptionName()
     */
    protected String handleGetFullyQualifiedManageableServiceCreateExceptionName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getManageablePackageName(),
                this.getManageableServiceCreateExceptionName(),
                null);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetManageableServiceReadExceptionName()
     */
    protected String handleGetManageableServiceReadExceptionName()
    {
        String exceptionNamePattern = (String)this.getConfiguredProperty(READ_EXCEPTION_NAME_PATTERN);

        return MessageFormat.format(
                exceptionNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetFullyQualifiedManageableServiceReadExceptionName()
     */
    protected String handleGetFullyQualifiedManageableServiceReadExceptionName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getManageablePackageName(),
                this.getManageableServiceReadExceptionName(),
                null);
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetManageableServiceUpdateExceptionName()
     */
    protected String handleGetManageableServiceUpdateExceptionName()
    {
        String exceptionNamePattern = (String)this.getConfiguredProperty(UPDATE_EXCEPTION_NAME_PATTERN);

        return MessageFormat.format(
                exceptionNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetFullyQualifiedManageableServiceUpdateExceptionName()
     */
    protected String handleGetFullyQualifiedManageableServiceUpdateExceptionName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getManageablePackageName(),
                this.getManageableServiceUpdateExceptionName(),
                null);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetManageableServiceDeleteExceptionName()
     */
    protected String handleGetManageableServiceDeleteExceptionName()
    {
        String exceptionNamePattern = (String)this.getConfiguredProperty(DELETE_EXCEPTION_NAME_PATTERN);

        return MessageFormat.format(
                exceptionNamePattern,
                new Object[] {StringUtils.trimToEmpty(this.getName())});
    }
    
    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetFullyQualifiedManageableServiceDeleteExceptionName()
     */
    protected String handleGetFullyQualifiedManageableServiceDeleteExceptionName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getManageablePackageName(),
                this.getManageableServiceDeleteExceptionName(),
                null);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetDefaultPersistenceContextUnitName()
     */
    protected String handleGetDefaultPersistenceContextUnitName()
    {
        return StringUtils.trimToEmpty(ObjectUtils.toString(this.getConfiguredProperty(PERSISTENCE_CONTEXT_UNIT_NAME)));
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#handleGetJndiNamePrefix()
     */
    protected String handleGetJndiNamePrefix()
    {
        return this.isConfiguredProperty(SERVICE_JNDI_NAME_PREFIX) ? 
                ObjectUtils.toString(this.getConfiguredProperty(SERVICE_JNDI_NAME_PREFIX)) : null;
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetManageableServiceBaseName()
     */
    protected String handleGetManageableServiceBaseName()
    {
        String exceptionNamePattern = (String)this.getConfiguredProperty(MANAGEABLE_SERVICE_BASE_NAME_PATTERN);

        return MessageFormat.format(
            exceptionNamePattern,
            new Object[] {StringUtils.trimToEmpty(this.getManageableServiceName())});
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetManageableServiceBaseFullPath()
     */
    protected String handleGetManageableServiceBaseFullPath()
    {
        return StringUtils.replace(this.getFullyQualifiedManageableServiceBaseName(), ".", "/");
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#
     *      handleGetFullyQualifiedManageableServiceBaseName()
     */
    protected String handleGetFullyQualifiedManageableServiceBaseName()
    {
        return EJB3MetafacadeUtils.getFullyQualifiedName(
                this.getManageablePackageName(),
                this.getManageableServiceBaseName(),
                null);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#isDelete()
     */
    public boolean isDelete()
    {
        return (this.getIdentifiers(true).iterator().next() != null ? true : false);
    }

    /**
     * @see org.andromda.cartridges.ejb3.metafacades.EJB3ManageableEntityFacadeLogic#isUpdate()
     */
    public boolean isUpdate()
    {
        return (this.getIdentifiers(true).iterator().next() != null ? true : false);
    }
    
}