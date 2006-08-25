// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.andromda.timetracker.service;

import org.andromda.timetracker.domain.User;
import org.andromda.timetracker.vo.UserDetailsVO;

/**
 * @see org.andromda.timetracker.service.AuthenticationService
 */
public class AuthenticationServiceImpl
    extends org.andromda.timetracker.service.AuthenticationServiceBase
{
    /**
     * @see org.andromda.timetracker.service.AuthenticationService#getUserDetails(java.lang.String)
     */
    protected UserDetailsVO handleGetUserDetails(String username)
        throws java.lang.Exception
    {
        UserDetailsVO userDetailsVO = null;
        User user = getUserDao().getUserDetails(username);
        if (user != null)
        {
            userDetailsVO = getUserDao().toUserDetailsVO(user);
        }
        return userDetailsVO;
    }
}