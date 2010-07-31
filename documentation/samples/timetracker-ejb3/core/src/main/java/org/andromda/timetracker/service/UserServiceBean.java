// license-header java merge-point
//
// Generated by: SessionBeanImpl.vsl in andromda-ejb3-cartridge.
//
package org.andromda.timetracker.service;

import java.util.Collection;

import org.andromda.timetracker.domain.User;
import org.andromda.timetracker.domain.UserDao;
import org.andromda.timetracker.domain.UserDaoException;
import org.andromda.timetracker.vo.UserDetailsVO;
import org.andromda.timetracker.vo.UserVO;

/**
 * @see org.andromda.timetracker.service.UserServiceBean
 */
/**
 * Do not specify the javax.ejb.Stateless annotation
 * Instead, define the session bean in the ejb-jar.xml descriptor
 * @javax.ejb.Stateless
 */
/**
 * Uncomment to enable webservices for UserServiceBean
 *@javax.jws.WebService(endpointInterface = "org.andromda.timetracker.service.UserServiceWSInterface")
 */
public class UserServiceBean 
    extends org.andromda.timetracker.service.UserServiceBase 
{
    // --------------- Constructors ---------------
    
    public UserServiceBean()
    {
        super();
    }

    // -------- Business Methods Impl --------------
    
    /**
     * @see org.andromda.timetracker.service.UserServiceBase#getAllUsers()
     */
    protected org.andromda.timetracker.vo.UserVO[] handleGetAllUsers()
        throws java.lang.Exception
    {
        Collection userVOs = this.getUserDao().loadAll(UserDao.TRANSFORM_USERVO);
        return (UserVO[])userVOs.toArray(new UserVO[0]);
    }

    /**
     * @see org.andromda.timetracker.service.UserServiceBase#handleRegisterUser(org.andromda.timetracker.vo.UserDetailsVO)
     */
    protected UserDetailsVO handleRegisterUser(UserDetailsVO userDetailVO) throws Exception
    {
        User user = this.getUserDao().userDetailsVOToEntity(userDetailVO);
        user = this.getUserDao().create(user);
        return this.getUserDao().toUserDetailsVO(user);
    }

    /**
     * @see org.andromda.timetracker.service.UserServiceBase#handleGetUser(java.lang.String)
     */
    protected UserVO handleGetUser(String username) throws Exception
    {
        try
        {
            User user = this.getUserDao().findByUsername(username);
            return getUserDao().toUserVO(user);
        }
        catch (UserDaoException ex)
        {
            throw new UserDoesNotExistException("User does not exist " + username);
        }
    }

    /**
     * @see org.andromda.timetracker.service.UserServiceBase#handleRemoveUser(org.andromda.timetracker.vo.UserVO)
     */
    protected void handleRemoveUser(UserVO userVO) throws Exception
    {
        getUserDao().remove(userVO.getId());
    }


    // -------- Lifecycle Callback Implementation --------------
    
}
