// license-header java merge-point
//
// Generated by: SessionBeanImpl.vsl in andromda-ejb3-cartridge.
//
package org.andromda.demo.ejb3.rental;

/**
 * @see org.andromda.demo.ejb3.rental.PaymentServiceBean
 */
/**
 * Do not specify the javax.ejb.Stateless annotation
 * Instead, define the session bean in the ejb-jar.xml descriptor
 * @javax.ejb.Stateless
 */
/**
 * Uncomment to enable webservices for PaymentServiceBean
 *@javax.jws.WebService(endpointInterface = "org.andromda.demo.ejb3.rental.PaymentServiceWSInterface")
 */
public class PaymentServiceBean
    extends org.andromda.demo.ejb3.rental.PaymentServiceBase
{
    // --------------- Constructors ---------------

    public PaymentServiceBean()
    {
        super();
    }

    // -------- Business Methods Impl --------------

    /**
     * @see org.andromda.demo.ejb3.rental.PaymentServiceBase#register(org.andromda.demo.ejb3.rental.RentalCar, int)
     */
    protected void handleRegister(org.andromda.demo.ejb3.rental.RentalCar car, int leasePeriod)
        throws Exception
    {
        System.out.println("Registering new car rental: " + car.getSerial() + ", " + car.getName() + ", " + leasePeriod);
    }


    // -------- Lifecycle Callback Impl --------------

}
