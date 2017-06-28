package edu.ucar.unidata.cloudcontrol.repository;

import java.util.List;

import edu.ucar.unidata.cloudcontrol.domain.User;

/**
 * The data access object representing a User.
 */

public interface UserDao {

    /**
     * Looks up and retrieves a User from the persistence mechanism using the userId.
     *
     * @param userId  The userId of the User to locate (will be unique for each User).
     * @return  The User.
     */
    public User lookupUser(int userId);

    /**
     * Looks up and retrieves a User from the persistence mechanism using the userName.
     *
     * @param userName  The userName of the User to locate (will be unique for each User).
     * @return  The User.
     */
    public User lookupUser(String userName);
 
    /**
     * Looks up and retrieves a User from the persistence mechanism using the emailAddress.
     *
     * @param emailAddress The emailAddress of the User to locate (will be unique for each User).
     * @return  The User.
     */
    public User lookupUserByEmailAddress(String emailAddress);

    /**
     * Requests a List of all Users from the persistence mechanism.
     *
     * @return  A List of Users.
     */
    public List<User> getUserList();

    /**
     * Finds and removes the User from the persistence mechanism using the userId.
     *
     * @param userId  The userId of the User to locate (will be unique for each User).
     */
    public void deleteUser(int userId);

    /**
     * Finds and removes the User from the persistence mechanism using the userName.
     *
     * @param userName  The userName of the User to locate (will be unique for each User).
     */
    public void deleteUser(String userName);

    /**
     * Creates a new User in the persistence mechanism.
     *
     * @param user  The User to be created.
     * @return  The peristed User.
     */
    public User createUser(User user);

    /**
     * Saves changes made to an existing User in the persistence mechanism.
     *
     * @param user   The existing User with changes that needs to be saved.
     */
    public void updateUser(User user);

    /**
     * Updates the User's Password in the persistence mechanism.
     *
     * @param user  The User whose password needs to be update.
     */
    public void updatePassword(User user);

}
