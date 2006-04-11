package org.andromda.samples.carrental.customers;

import javax.ejb.SessionContext;
//import org.apache.log4j.Logger;

/**
 * The CustomerService session bean.
 *
 * <p>
 *  Supports the workflow of customer tasks in the system. Can
 *  authenticate a user as a customer, can create customers, add
 *  drivers to them and search for customers. @author Matthias
 *  Bohlen
 * </p>
 *
 * <p>This class is intended to hold manual changes to the autogenerated
 * implementation in {@link CustomerServiceBean}.</p>
 */
public class CustomerServiceBeanImpl extends CustomerServiceBean {
    //private static Logger log = Logger.getLogger(CustomerServiceBeanImpl.class);
	private SessionContext context;
    
    public void setSessionContext(SessionContext ctx) {
        //if(log.isDebugEnabled()) log.debug("CustomerServiceBeanImpl.setSessionContext...");
        context = ctx;
    }

    public void ejbRemove() {
        //if(log.isDebugEnabled()) log.debug("CustomerServiceBeanImpl.ejbRemove...");
    }

    public void ejbPassivate() {
        ///if(log.isDebugEnabled()) log.debug("CustomerServiceBeanImpl.ejbPassivate...");
    }

    public void ejbActivate() {
        //if(log.isDebugEnabled()) log.debug("CustomerServiceBeanImpl.ejbActivate...");
    }

    /**
     * <p>
     *  Creates a new customer. @param name name of the new customer
     *  @param customerNo customer number of the new customer @return
     *  String with the customer id
     * </p>
     */
    public java.lang.String createCustomer(java.lang.String name, java.lang.String customerNo, java.lang.String password) throws org.andromda.samples.carrental.customers.CustomerException {
        //TODO: put your implementation here.
        // Dummy return value, just that the file compiles
        return null;
    }

    /**
     * <p>
     *  Adds a licensed driver to the drivers associated with a
     *  particular customer. @param customerId the id of the Customer
     *  object @param driverData the data for the Driver to be created
     *  @return a String with the Driver object's id
     * </p>
     */
    public java.lang.String addDriver(java.lang.String customerId, org.andromda.samples.carrental.customers.DriverData driverData) throws org.andromda.samples.carrental.customers.CustomerException {
        //TODO: put your implementation here.
        // Dummy return value, just that the file compiles
        return null;
    }

    /**
     * <p>
     *  Searches all customers. @return a Collection with CustomerData
     *  objects.
     * </p>
     */
    public java.util.Collection searchAllCustomers() throws org.andromda.samples.carrental.customers.CustomerException {
        //TODO: put your implementation here.
        // Dummy return value, just that the file compiles
        return null;
    }

    /**
     * <p>
     *  Authenticates a user as a customer. @return a String with the
     *  id of the Customer object. @param customerNo the customer
     *  number @param password the customer's password
     * </p>
     */
    public java.lang.String authenticateAsCustomer(java.lang.String customerNo, java.lang.String password) throws org.andromda.samples.carrental.customers.CustomerException {
        //TODO: put your implementation here.
        // Dummy return value, just that the file compiles
        return null;
    }

}