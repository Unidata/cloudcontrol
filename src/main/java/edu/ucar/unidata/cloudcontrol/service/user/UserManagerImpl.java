package edu.ucar.unidata.cloudcontrol.service.user;

import edu.ucar.unidata.cloudcontrol.domain.User;
import edu.ucar.unidata.cloudcontrol.repository.UserDao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Service for processing User objects.
 */
public class UserManagerImpl implements UserManager {

    protected static Logger logger = Logger.getLogger(UserManagerImpl.class);

    private UserDao userDao;

    /**
     * Sets the data access object which will acquire and persist the data
     * passed to it via the methods of this UserManager.
     *
     * @param userDao  The service mechanism data access object representing a User.
     */
    public void setUserDao(UserDao userDao) {
        logger.debug("Setting user data access object.");
        this.userDao = userDao;
    }

    /**
     * Looks up and retrieves a User using the userId.
     *
     * @param userId  The userId of the User to locate (will be unique for each User).
     * @return  The User.
     */
    public User lookupUser(int userId) {
        logger.debug("Using DAO to look up user by userId " + new Integer(userId).toString());
        return userDao.lookupUser(userId);
    }

    /**
     * Looks up and retrieves a User using the userName.
     *
     * @param userName  The userName of the User to locate (will be unique for each User).
     * @return  The User.
     */
    public User lookupUser(String userName){
        logger.debug("Using DAO to look up user by username " + userName);
        return userDao.lookupUser(userName);
    }

    /**
     * Looks up and retrieves a User using the emailAddress.
     *
     * @param emailAddress The emailAddress of the User to locate (will be unique for each User).
     * @return  The User.
     */
    public User lookupUserByEmailAddress(String emailAddress) {
        logger.debug("Using DAO to look up user by email address " + emailAddress);
        return userDao.lookupUserByEmailAddress(emailAddress);
    }

    /**
     * Requests a List of all Users.
     *
     * @return  A List of Users.
     */
    public List<User> getUserList() {
        logger.debug("Using DAO to get a list of all users.");
        return userDao.getUserList();
    }

    /**
     * Finds and removes the User using the userId.
     *
     * @param userId  The userId of the User to locate (will be unique for each User).
     */
    public void deleteUser(int userId) {
        logger.debug("Using DAO to delete user with userId " + new Integer(userId).toString());
        userDao.deleteUser(userId);
    }

    /**
     * Creates a new User.
     *
     * @param user  The User to be created.
     * @return  The User.
     */
    public User createUser(User user) {
        logger.debug("Using DAO to create new user with username " + user.getUserName() + " and email address " + user.getEmailAddress());
        String password = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(password);
        Date now = new Date(System.currentTimeMillis());
        user.setDateCreated(now);
        user.setDateModified(now);
        return userDao.createUser(user);
    }

    /**
     * Saves changes made to an existing User.
     *
     * @param user   The existing User with changes that needs to be saved.
     * @return  The updated User.
     */
    public User updateUser(User user) {
        logger.debug("Using DAO to update user with username " + user.getUserName() + " and email address " + user.getEmailAddress());
        Date now = new Date(System.currentTimeMillis());
        user.setDateModified(now);
        return userDao.updateUser(user);
    }

    /**
     * Updates the User's Password.
     *
     * @param user  The User to whose password we need to update.
     */
    public void updatePassword(User user) {
        logger.debug("Using DAO to update password for user with username " + user.getUserName());
        String password = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(password);
        Date now = new Date(System.currentTimeMillis());
        user.setDateModified(now);
        userDao.updatePassword(user);
    }
}
