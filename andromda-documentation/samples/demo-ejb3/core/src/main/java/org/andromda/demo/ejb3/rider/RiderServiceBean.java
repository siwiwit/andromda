// license-header java merge-point
//
// Generated by SessionBeanImpl.vsl in andromda-ejb3-cartridge on 08/06/2014 10:56:22.
// Modify as necessary. If deleted it will be regenerated.
//
package org.andromda.demo.ejb3.rider;

import java.util.Collection;

/**
 * @see RiderServiceBase
 *
 * Remember to manually configure the local business interface this bean implements if originally you only
 * defined the remote business interface.  However, this change is automatically reflected in the ejb-jar.xml.
 *
 * Do not specify the javax.ejb.Stateless annotation
 * Instead, the session bean is defined in the ejb-jar.xml descriptor.
 */
// Uncomment to enable webservices for RiderServiceBean
// @javax.jws.WebService(endpointInterface = "org.andromda.demo.ejb3.rider.RiderServiceWSInterface", serviceName = "RiderService")
public class RiderServiceBean
    extends RiderServiceBase
    implements RiderServiceRemote
{
    // --------------- Constructors ---------------

    /**
     * Default constructor extending base class default constructor
     */
    public RiderServiceBean()
    {
        super();
    }

    // -------- Business Methods Impl --------------

    /**
     * @see RiderServiceBase#addRider(Rider)
     */
    @Override
    protected void handleAddRider(Rider rider)
        throws Exception
    {
        getRiderDao().create(rider);
    }

    /**
     * @see RiderServiceBase#getRider(long)
     */
    @Override
    protected Rider handleGetRider(long id)
        throws Exception
    {
        return getRiderDao().load(id);
    }

    /**
     * @see RiderServiceBase#getAllRiders()
     */
    @Override
    protected Collection handleGetAllRiders()
        throws Exception
    {
        return getRiderDao().loadAll();
    }

    // -------- Lifecycle Callback Implementation --------------
}
