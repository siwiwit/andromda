// license-header java merge-point
//
// Generated by: SessionBeanImpl.vsl in andromda-ejb3-cartridge.
//
package org.andromda.demo.ejb3.mobile;

/**
 * @see org.andromda.demo.ejb3.mobile.MobileServiceBean
 */
/**
 * Do not specify the javax.ejb.Stateless annotation
 * Instead, define the session bean in the ejb-jar.xml descriptor
 * @javax.ejb.Stateless
 */
/**
 * Uncomment to enable webservices for MobileServiceBean
 *@javax.jws.WebService(endpointInterface = "org.andromda.demo.ejb3.mobile.MobileServiceWSInterface")
 */
public class MobileServiceBean
    extends org.andromda.demo.ejb3.mobile.MobileServiceBase
{
    // --------------- Constructors ---------------

    public MobileServiceBean()
    {
        super();
    }

    // -------- Business Methods Impl --------------

    /**
     * @see org.andromda.demo.ejb3.mobile.MobileServiceBase#addMobile(org.andromda.demo.ejb3.mobile.Mobile)
     */
    protected void handleAddMobile(org.andromda.demo.ejb3.mobile.Mobile mobile)
        throws java.lang.Exception
    {
        System.out.println("min length = " + minMobileLength + " max length = " + maxMobileLength);

        if (mobile.getNumber().length() > minMobileLength && mobile.getNumber().length() < maxMobileLength)
        {
            getMobileDao().create(mobile);
            if (mobile.getNetwork().equalsIgnoreCase(defaultNetwork))
            {
                System.out.println("Got one on the default network");
            }
        }
        else
        {
            System.out.println("Mobile length is invalid");
        }
    }

    /**
     * @see org.andromda.demo.ejb3.mobile.MobileServiceBase#getMobile(java.lang.String)
     */
    protected org.andromda.demo.ejb3.mobile.Mobile handleGetMobile(java.lang.String number)
        throws java.lang.Exception
    {
        return getMobileDao().load(number);
    }


    // -------- Lifecycle Callback Impl --------------

}
