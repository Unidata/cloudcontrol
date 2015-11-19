package edu.ucar.unidata.cloudcontrol.service;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;
import java.util.List;

import edu.ucar.unidata.cloudcontrol.domain.User;
import edu.ucar.unidata.cloudcontrol.repository.UserDao;

/**
 * Service for processing User objects. 
 */
public class UserManagerImpl implements UserManager { 

    private UserDao userDao;

    /**
     * Sets the data access object which will acquire and persist the data 
     * passed to it via the methods of this UserManager. 
     * 
     * @param userDao  The service mechanism data access object representing a User. 
     */
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Looks up and retrieves a User using the userId.
     * 
     * @param userId  The userId of the User to locate (will be unique for each User). 
     * @return  The User.   
     */
    public User lookupUser(int userId) {
        return userDao.lookupUser(userId);
    }

    /**
     * Looks up and retrieves a User using the userName.
     * 
     * @param userName  The userName of the User to locate (will be unique for each User). 
     * @return  The User.   
     */
    public User lookupUser(String userName){
        return userDao.lookupUser(userName);
    }
   
    /**
     * Requests a List of all Users.
     * 
     * @return  A List of Users.   
     */
    public List<User> getUserList() {
        return userDao.getUserList();
    }

    /**
     * Queries and returns the number of Users.
     * 
     * @return  The total number of Users.   
     */
    public int getUserCount(){
        return userDao.getUserCount();
    }

    /**
     * Finds and removes the User using the userId.
     * 
     * @param userId  The userId of the User to locate (will be unique for each User). 
     */
    public void deleteUser(int userId) {
        userDao.deleteUser(userId);
    }

    /**
     * Finds and removes the User using the userName.
     * 
     * @param userName  The userName of the User to locate (will be unique for each User). 
     */
    public void deleteUser(String userName) {
        userDao.deleteUser(userName);
    }

    /**
     * Finds and toggles the User's accountStatus.
     * 
     * @param user  The User whose accountStatus needs to be toggled. 
     */
    public void toggleAccountStatus(User user) {
        Date now = new Date(System.currentTimeMillis());
        user.setDateModified(now);
        userDao.updateUser(user);
    }

    /**
     * Creates a new User.
     * 
     * @param user  The User to be created. 
     */
    public void createUser(User user) {
        String password = DigestUtils.md5Hex(user.getPassword());
        user.setPassword(password);
        user.setAccessLevel(1);
        Date now = new Date(System.currentTimeMillis());
        user.setDateCreated(now);
        user.setDateModified(now);
        userDao.createUser(user);
    }

    /**
     * Saves changes made to an existing User. 
     * 
     * @param user   The existing User with changes that needs to be saved. 
     */
    public void updateUser(User user) {
        Date now = new Date(System.currentTimeMillis());
        user.setDateModified(now);
        userDao.updateUser(user);
    }

    /**
     * Updates the User's Password.
     * 
     * @param user  The User to whose password we need to update. 
     */
    public void updatePassword(User user) {
        String password = DigestUtils.md5Hex(user.getPassword());
        user.setPassword(password);
        Date now = new Date(System.currentTimeMillis());
        user.setDateModified(now);
        userDao.updatePassword(user);
    }
	
	
	
}