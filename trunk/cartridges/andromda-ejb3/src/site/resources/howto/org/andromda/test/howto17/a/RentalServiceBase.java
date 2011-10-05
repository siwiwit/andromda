// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: SessionBeanBase.vsl in andromda-ejb3-cartridge.
//
package org.andromda.howto2.rental;

/**
 * Autogenerated EJB session bean base class RentalServiceBean.
 *
 *
 */
@javax.ejb.TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
@javax.ejb.Remote({org.andromda.howto2.rental.RentalServiceRemote.class})
@org.jboss.annotation.ejb.Clustered
public abstract class RentalServiceBase
    implements org.andromda.howto2.rental.RentalServiceRemote
{
    // ------ Session Context Injection ------

    @javax.annotation.Resource
    protected javax.ejb.SessionContext context;

    // ------ Persistence Context Definitions --------

    /**
     * Inject persistence context howtomodelcaching
     */
    @javax.persistence.PersistenceContext(unitName = "howtomodelcaching")
    protected javax.persistence.EntityManager emanager;

    // ------ DAO Injection Definitions --------

    /**
     * Inject DAO CarDao
     */
    @javax.ejb.EJB
    private org.andromda.howto2.rental.CarDao carDao;

    /**
     * Inject DAO PersonDao
     */
    @javax.ejb.EJB
    private org.andromda.howto2.rental.PersonDao personDao;

    // --------------- Constructors ---------------

    public RentalServiceBase()
    {
        super();
    }

    // ------ DAO Getters --------

    /**
     * Get the injected DAO CarDao
     */
    protected org.andromda.howto2.rental.CarDao getCarDao()
    {
        return this.carDao;
    }

    /**
     * Get the injected DAO PersonDao
     */
    protected org.andromda.howto2.rental.PersonDao getPersonDao()
    {
        return this.personDao;
    }

    // -------- Business Methods  --------------

    /**
     *
     */
    @javax.ejb.TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
    public java.util.List getAllCars()
        throws org.andromda.howto2.rental.RentalException
    {
        try
        {
            return this.handleGetAllCars();
        }
        catch (org.andromda.howto2.rental.RentalException ex)
        {
            throw ex;
        }
        catch (Throwable th)
        {
            throw new org.andromda.howto2.rental.RentalServiceException(
                "Error performing 'org.andromda.howto2.rental.RentalService.getAllCars()' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getAllCars()}
     */
    protected abstract java.util.List handleGetAllCars()
        throws Exception;

    /**
     *
     */
    @javax.ejb.TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
    public java.util.List getCustomersByName(String name)
        throws org.andromda.howto2.rental.RentalException
    {
        if (name == null)
        {
            throw new IllegalArgumentException(
                "org.andromda.howto2.rental.RentalServiceBean.getCustomersByName(String name) - 'name' can not be null");
        }
        try
        {
            return this.handleGetCustomersByName(name);
        }
        catch (org.andromda.howto2.rental.RentalException ex)
        {
            throw ex;
        }
        catch (Throwable th)
        {
            throw new org.andromda.howto2.rental.RentalServiceException(
                "Error performing 'org.andromda.howto2.rental.RentalService.getCustomersByName(String name)' --> " + th,
                th);
        }
    }

    /**
     * Performs the core logic for {@link #getCustomersByName(String)}
     */
    protected abstract java.util.List handleGetCustomersByName(String name)
        throws Exception;


    // -------- Lifecycle Callbacks --------------

}