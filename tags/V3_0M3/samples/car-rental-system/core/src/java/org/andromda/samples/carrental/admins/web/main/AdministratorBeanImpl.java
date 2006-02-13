package org.andromda.samples.carrental.admins.web.main;

import javax.ejb.EntityContext;
import javax.ejb.RemoveException;
//import org.apache.log4j.Logger;

/**
 * The Administrator entity bean.
 *
 * <p>
 *  Represents a user who has the right to create new customers,
 *  cars, etc. @author Matthias Bohlen
 * </p>
 *
 * <p>This class is intended to hold manual changes to the autogenerated
 * implementation in {@link AdministratorBean}.</p>
 */
public abstract class AdministratorBeanImpl extends AdministratorBean {
    //private static Logger log = Logger.getLogger(AdministratorBeanImpl.class);
	private EntityContext ctx;
	
	public void setEntityContext(EntityContext ctx) {
        //if(log.isDebugEnabled()) log.debug("AdministratorBean.setEntityContext...");
        this.ctx = ctx;
    }

    public void unsetEntityContext() {
        //if(log.isDebugEnabled()) log.debug("AdministratorBean.unsetEntityContext...");
        ctx = null;
    }

    public void ejbRemove() throws RemoveException {
        //if(log.isDebugEnabled()) log.debug("AdministratorBean.ejbRemove...");
    }

    public void ejbLoad() {
        //if(log.isDebugEnabled()) log.debug("AdministratorBean.ejbLoad...");
    }

    public void ejbStore() {
        //if(log.isDebugEnabled()) log.debug("AdministratorBean.ejbStore...");
    }

    public void ejbPassivate() {
        //if(log.isDebugEnabled()) log.debug("AdministratorBean.ejbPassivate...");
    }

    public void ejbActivate() {
        //if(log.isDebugEnabled()) log.debug("AdministratorBean.ejbActivate...");
    }

    /**
     * 
     */
    public org.andromda.samples.carrental.admins.web.main.Administrator create(long id, java.lang.String name, java.lang.String accountNo, java.lang.String password)
        throws org.andromda.samples.carrental.admins.web.main.AdminException {
        // TODO put your implementation here.
        Administrator result = null;
        return result;
    }

}