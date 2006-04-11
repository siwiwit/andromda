package org.andromda.samples.carrental.inventory;

import javax.ejb.SessionContext;
//import org.apache.log4j.Logger;

/**
 * The InventoryService session bean.
 *
 * <p>
 *  Supports the workflow of inventory management for the car
 *  rental company. Can create types of cars, can create cars and
 *  search for all these objects. @author Matthias Bohlen
 * </p>
 *
 * <p>This class is intended to hold manual changes to the autogenerated
 * implementation in {@link InventoryServiceBean}.</p>
 */
public class InventoryServiceBeanImpl extends InventoryServiceBean {
    //private static Logger log = Logger.getLogger(InventoryServiceBeanImpl.class);
	private SessionContext context;
    
    public void setSessionContext(SessionContext ctx) {
        //if(log.isDebugEnabled()) log.debug("InventoryServiceBeanImpl.setSessionContext...");
        context = ctx;
    }

    public void ejbRemove() {
        //if(log.isDebugEnabled()) log.debug("InventoryServiceBeanImpl.ejbRemove...");
    }

    public void ejbPassivate() {
        ///if(log.isDebugEnabled()) log.debug("InventoryServiceBeanImpl.ejbPassivate...");
    }

    public void ejbActivate() {
        //if(log.isDebugEnabled()) log.debug("InventoryServiceBeanImpl.ejbActivate...");
    }

    /**
     * <p>
     *  Creates a car type (kind of catalog entry). @param typeData the
     *  data for the catalog entry @return a String with the id of the
     *  CarType object
     * </p>
     */
    public java.lang.String createCarType(org.andromda.samples.carrental.inventory.CarTypeData typeData) throws org.andromda.samples.carrental.inventory.InventoryException {
        //TODO: put your implementation here.
        // Dummy return value, just that the file compiles
        return null;
    }

    /**
     * <p>
     *  Creates a car with a certain type. The car type must already
     *  exist. @param carData data for the Car object @param carTypeId
     *  the id of the CarType object @return a String with the id of
     *  the Car object
     * </p>
     */
    public java.lang.String createCar(org.andromda.samples.carrental.inventory.CarData carData, java.lang.String carTypeId) throws org.andromda.samples.carrental.inventory.InventoryException {
        //TODO: put your implementation here.
        // Dummy return value, just that the file compiles
        return null;
    }

    /**
     * <p>
     *  Searches all cars with a certain comfort class. @param
     *  comfortClass the comfort class to search for @return a
     *  Collection with CarData objects
     * </p>
     */
    public java.util.Collection searchCarByComfortClass(java.lang.String comfortClass) throws org.andromda.samples.carrental.inventory.InventoryException {
        //TODO: put your implementation here.
        // Dummy return value, just that the file compiles
        return null;
    }

    /**
     * <p>
     *  Returns all registered car types from the catalog. @return a
     *  Collection with CarTypeData objects.
     * </p>
     */
    public java.util.Collection searchAllCarTypes() throws org.andromda.samples.carrental.inventory.InventoryException {
        //TODO: put your implementation here.
        // Dummy return value, just that the file compiles
        return null;
    }

    /**
     * <p>
     *  Returns all known cars. @return a Collection with CarData
     *  objects
     * </p>
     */
    public java.util.Collection searchAllCars() throws org.andromda.samples.carrental.inventory.InventoryException {
        //TODO: put your implementation here.
        // Dummy return value, just that the file compiles
        return null;
    }

}