package edu.ucar.unidata.cloudcontrol.repository;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import edu.ucar.unidata.cloudcontrol.domain.User;

/**
 * The UserDao implementation.  Persistence mechanism is a database.
 */

public class JdbcUserDao extends JdbcDaoSupport implements UserDao {

    protected static Logger logger = Logger.getLogger(JdbcUserDao.class);


    private SimpleJdbcInsert insertActor;

    /**
     * Looks up and retrieves a User from the persistence mechanism using the userId.
     *
     * @param userId  The userId of the User to locate (will be unique for each User).
     * @return  The User.
     * @throws DataRetrievalFailureException  If unable to lookup User with the given userId.
     */
    public User lookupUser(int userId) {
        String sql = "SELECT * FROM users WHERE userId = ?";
        List<User> users = getJdbcTemplate().query(sql, new UserMapper(), userId);
        if (users.isEmpty()) {
            throw new DataRetrievalFailureException("Unable to find User with userId " + new Integer(userId).toString());
        }
        return users.get(0);
    }

    /**
     * Looks up and retrieves a User from the persistence mechanism using the userName.
     *
     * @param userName  The userName of the User to locate (will be unique for each User).
     * @return  The User.
     * @throws DataRetrievalFailureException  If unable to lookup User with the given userName.
     */
    public User lookupUser(String userName) {
        String sql = "SELECT * FROM users WHERE userName = ?";
        List<User> users = getJdbcTemplate().query(sql, new UserMapper(), userName);
        if (users.isEmpty()) {
            throw new DataRetrievalFailureException("Unable to find User with userName " + userName);
        }
        return users.get(0);
    }

    /**
     * Looks up and retrieves a User from the persistence mechanism using the emailAddress.
     *
     * @param emailAddress The emailAddress of the User to locate (will be unique for each User).
     * @return  The User.
     * @throws DataRetrievalFailureException  If unable to lookup User with the given emailAddress.
     */
    public User lookupUserByEmailAddress(String emailAddress) {
        String sql = "SELECT * FROM users WHERE emailAddress = ?";
        List<User> users = getJdbcTemplate().query(sql, new UserMapper(), emailAddress);
        if (users.isEmpty()) {
            throw new DataRetrievalFailureException("Unable to find User with emailAddress " + emailAddress);
        }
        return users.get(0);
    }

    /**
     * Requests a List of all Users from the persistence mechanism.
     *
     * @return  A List of users.
     */
    public List<User> getUserList() {
        String sql = "SELECT * FROM users ORDER BY dateCreated DESC";
        List<User> users = getJdbcTemplate().query(sql, new UserMapper());
        return users;
    }

    /**
     * Finds and removes the User from the persistence mechanism using the userId.
     *
     * @param userId   The userId of the User to locate (will be unique for each User).
     * @throws DataRetrievalFailureException  If unable to find and delete the User.
     */
    public void deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE userId = ?";
        int rowsAffected  = getJdbcTemplate().update(sql, userId);
        if (rowsAffected <= 0) {
            throw new DataRetrievalFailureException("Unable to delete User. No User found with userId " + new Integer(userId).toString());
        }
    }

    /**
     * Creates a new User in the persistence mechanism.
     *
     * @param user  The User to be created.
     * @throws DataRetrievalFailureException  If the User already exists.
     * @return  The peristed User.
     */
    public User createUser(User user) {
        String sql = "SELECT * FROM users WHERE userName = ?";
        List<User> users = getJdbcTemplate().query(sql, new UserMapper(), user.getUserName());
        if (!users.isEmpty()) {
            throw new DataRetrievalFailureException("User with userName \"" +  user.getUserName() + "\" already exists.");
        } else {
            this.insertActor = new SimpleJdbcInsert(getDataSource()).withTableName("users").usingGeneratedKeyColumns("userId");
            SqlParameterSource parameters = new BeanPropertySqlParameterSource(user);
            Number newUserId = insertActor.executeAndReturnKey(parameters);
            if (newUserId != null) {
                user.setUserId(newUserId.intValue());
            } else {
                throw new DataRetrievalFailureException("Unable to create new User " + user.toString());
            }
        }
        return user;
    }

    /**
     * Saves changes made to an existing User in the persistence mechanism.
     *
     * @param user   The existing User with changes that needs to be saved.
     * @throws DataRetrievalFailureException  If unable to find the User to update.
     * @return  The updated User.
     */
    public User updateUser(User user) {
        String sql = "UPDATE users SET userName = ?, emailAddress = ?, fullName = ?, accessLevel = ?, accountStatus = ?, dateModified = ? WHERE userId = ?";
        int rowsAffected  = getJdbcTemplate().update(sql, new Object[] {
            // order matters here
            user.getUserName(),
            user.getEmailAddress(),
            user.getFullName(),
            user.getAccessLevel(),
            user.getAccountStatus(),
            user.getDateModified(),
            user.getUserId()
        });
        if (rowsAffected  <= 0) {
            throw new DataRetrievalFailureException("Unable to update User.  No User with userName " + user.getUserName() + " found.");
        }
        return user;
    }

    /**
     * Updates the User's Password in the persistence mechanism.
     *
     * @param user  The User whose password needs to be update.
     * @throws DataRetrievalFailureException  If unable to find the User to update.
     */
    public void updatePassword(User user) {
        String sql = "UPDATE users SET password = ?, dateModified = ? WHERE userName = ?";
        int rowsAffected  = getJdbcTemplate().update(sql, new Object[] {
            // order matters here
            user.getPassword(),
            user.getDateModified(),
            user.getUserName()
        });
        if (rowsAffected  <= 0) {
            throw new DataRetrievalFailureException("Unable to update User's password.  User not found " + user.toString());
        }
    }


    /***
     * Maps each row of the ResultSet to a User object.
     */
    private static class UserMapper implements RowMapper<User> {
        /**
         * Maps each row of data in the ResultSet to the User object.
         *
         * @param rs  The ResultSet to be mapped.
         * @param rowNum  The number of the current row.
         * @return  The populated User object.
         * @throws SQLException  If a SQLException is encountered getting column values.
         */
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(rs.getInt("userId"));
            user.setUserName(rs.getString("userName"));
            user.setPassword(rs.getString("password"));
            user.setAccessLevel(rs.getInt("accessLevel"));
            user.setAccountStatus(rs.getInt("accountStatus"));
            user.setEmailAddress(rs.getString("emailAddress"));
            user.setFullName(rs.getString("fullName"));
            user.setDateCreated(rs.getTimestamp("dateCreated"));
            user.setDateModified(rs.getTimestamp("dateModified"));
            return user;
        }
    }

}
