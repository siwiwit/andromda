// license-header java merge-point
//
// Generated by: SessionBeanImpl.vsl in andromda-ejb3-cartridge.
//
package org.andromda.demo.ejb3.vehicle;

import org.andromda.demo.ejb3.account.Account;
import org.andromda.demo.ejb3.account.AccountException;

/**
 * @see org.andromda.demo.ejb3.vehicle.VehicleManagerBean
 */
/**
 * Do not specify the javax.ejb.Stateless annotation
 * Instead, define the session bean in the ejb-jar.xml descriptor
 * @javax.ejb.Stateless
 */
/**
 * Uncomment to enable webservices for VehicleManagerBean
 *@javax.jws.WebService(endpointInterface = "org.andromda.demo.ejb3.vehicle.VehicleManagerWSInterface")
 */
public class VehicleManagerBean 
    extends org.andromda.demo.ejb3.vehicle.VehicleManagerBase 
{
    // --------------- Constructors ---------------
    
    public VehicleManagerBean()
    {
        super();
    }

    // -------- Business Methods Impl --------------
    
    /**
     * @see org.andromda.demo.ejb3.vehicle.VehicleManagerBase#addMotorcycle(org.andromda.demo.ejb3.vehicle.Motocycle)
     */
    protected void handleAddMotorcycle(org.andromda.demo.ejb3.vehicle.Motocycle mc)
        throws java.lang.Exception
    {
        getMotocycleDao().create(mc);
        
        // Test session bean injection
        insertAccount();
    }

    private void insertAccount()
    {
        System.out.println("Inserting account...");
        
        Account account = new Account("test");

        try
        {
            accountManager.addAccount(account);
        } 
        catch (AccountException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println("Insert complete.");
    }
    
    /**
     * @see org.andromda.demo.ejb3.vehicle.VehicleManagerBase#addCar(org.andromda.demo.ejb3.vehicle.Car)
     */
    protected void handleAddCar(org.andromda.demo.ejb3.vehicle.Car car)
        throws java.lang.Exception
    {
        getCarDao().create(car);
    }

    /**
     * @see org.andromda.demo.ejb3.vehicle.VehicleManagerBase#addVehicle(org.andromda.demo.ejb3.vehicle.Vehicle)
     */
    protected void handleAddVehicle(org.andromda.demo.ejb3.vehicle.Vehicle vehicle)
        throws java.lang.Exception
    {
        getVehicleDao().create(vehicle);
    }


    // -------- Lifecycle Callback Impl --------------
    
}