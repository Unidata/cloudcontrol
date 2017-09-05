package edu.ucar.unidata.cloudcontrol.service.user;

import edu.ucar.unidata.cloudcontrol.domain.User;

import java.util.List;

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
     * Looks up and retrieves a User using the emailAddress.
     *
     * @param emailAddress The emailAddress of the User to locate (will be unique for each User).
     * @return  The User.
     */
    public User lookupUserByEmailAddress(String emailAddress);

    /**
     * Requests a List of all Users.
     *
     * @return  A List of Users.
     */
    public List<User> getUserList();

    /**
     * Finds and removes the User using the userId.
     *
     * @param userId  The userId of the User to locate (will be unique for each User).
     */
    public void deleteUser(int userId);

    /**
     * Creates a new User.
     *
     * @param user  The User to be created.
     * @return  The User.
     */
    public User createUser(User user);

    /**
     * Saves changes made to an existing User.
     *
     * @param user   The existing User with changes that needs to be saved.
     * @return  The updated User.
     */
    public User updateUser(User user);

    /**
     * Updates the User's Password.
     *
     * @param user  The User whose password needs to be update.
     */
    public void updatePassword(User user);
}
