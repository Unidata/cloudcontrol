package edu.ucar.unidata.cloudcontrol.service.user;

import org.apache.log4j.Logger;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import edu.ucar.unidata.cloudcontrol.domain.User;
import edu.ucar.unidata.cloudcontrol.repository.UserDao;


/**
 * Service for processing User objects with respect to authentication and access control. 
 */
public class UserDetailsServiceImpl implements UserDetailsService { 

    protected static Logger logger = Logger.getLogger(UserDetailsServiceImpl.class);

    public UserDao userDao;

    /**
     * Sets the data access object. 
     * 
     * @param userDao  The service mechanism data access object representing a User. 
     */
    public void setUserDao(UserDao userDao) {
        logger.debug("Setting user data access object.");
        this.userDao = userDao;
    }

    /**
     * Retrieves a User record containing the User's credentials and access. 
     * 
     * @param userName  The userName of the User authenticating.
     * @return  The userDetails used by Spring.
     * @thows UsernameNotFoundException  If unable to find the User in the database.
     * @thows AccountStatusException  If the User's account has been deactived.
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        logger.debug("Loading user by user name " + userName);
        User user = null;
        try {
            user = userDao.lookupUser(userName);
        } catch (DataAccessException e) {
            logger.error("No user found for userName '" + userName +"': " + e);
            throw new UsernameNotFoundException("No user found for userName '" + userName +"'.");
        }
        return user;
    }

}
