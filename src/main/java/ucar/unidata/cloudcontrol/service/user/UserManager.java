package edu.ucar.unidata.cloudcontrol.service.user;

import java.util.List;

import edu.ucar.unidata.cloudcontrol.domain.User;


/**
 * Service for processing User objects. 
 */
public interface UserManager {

    /**
     * Looks up and retrieves a User using the userId.
     * 
     * @param userId  The userId of the User to locate (will be unique for each User). 
     * @return  The User.
     */
    public User lookupUser(int userId);
   
    /**
     * Looks up and retrieves a User using the userName.
     * 
     * @param userName  The userName of the User to locate (will be unique for each User). 
     * @return  The User.   
     */
    public User lookupUser(String userName);

    /**
     * Requests a List of all Users.
     * 
     * @return  A List of Users.   
     */
    public List<User> getUserList();

    /**
     * Queries and returns the number of Users.
     * 
     * @return  The total number of Users.   
     */
    public int getUserCount();

    /**
     * Finds and removes the User using the userId.
     * 
     * @param userId  The userId of the User to locate (will be unique for each User). 
     */
    public void deleteUser(int userId);

    /**
     * Finds and removes the User using the userName.
     * 
     * @param userName  The userName of the User to locate (will be unique for each User). 
     */
    public void deleteUser(String userName);

    /**
     * Finds and toggles the User's accountStatus.
     * 
     * @param user  The User whose accountStatus needs to be toggled. 
     */
    public void toggleAccountStatus(User user);

    /**
     * Creates a new User.
     * 
     * @param user  The User to be created. 
     */
    public void createUser(User user);

    /**
     * Saves changes made to an existing User. 
     * 
     * @param user   The existing User with changes that needs to be saved. 
     */
    public void updateUser(User user);

    /**
     * Updates the User's Password.
     * 
     * @param user  The User whose password needs to be update. 
     */
    public void updatePassword(User user);
}
