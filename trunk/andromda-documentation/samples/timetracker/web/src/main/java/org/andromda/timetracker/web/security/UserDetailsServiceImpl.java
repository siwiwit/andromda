package org.andromda.timetracker.web.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.andromda.timetracker.service.SecurityService;
import org.andromda.timetracker.ServiceLocator;
import org.andromda.timetracker.vo.UserDetailsVO;
import org.springframework.dao.DataAccessException;

public class UserDetailsServiceImpl implements UserDetailsService {

    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, DataAccessException {

        SecurityService service = ServiceLocator.instance().getSecurityService();
        UserDetailsVO userDetailsVO = service.getUserDetails(username);
        if (userDetailsVO == null) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        return new UserDetailsImpl(userDetailsVO);
    }
}