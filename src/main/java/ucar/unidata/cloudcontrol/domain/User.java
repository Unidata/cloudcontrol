package edu.ucar.unidata.cloudcontrol.domain;

import java.util.Date;
import java.io.Serializable;

/**
 * Object representing a User.  
 *
 * A User is person with an account in the cloudcontrol web app. 
 * The User attributes correspond to database columns.
 */

public class User implements Serializable {

    private int userId;
    private String userName;
    private String password;
    private int accessLevel;
    private int accountStatus;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private Date dateCreated;
    private Date dateModified;

    /**
     * Returns the userId of the User (immutable/unique to each User).
     * 
     * @return  The userId. 
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the userId of the User (immutable/unique to each User).
     * 
     * @param userId  The userId. 
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Returns the userName of the User (immutable/unique to each User).
     * 
     * @return  The User's userName.  
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the userName of the User (immutable/unique to each User).
     * 
     * @param userName  The User's userName. 
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Returns the User's password.
     * 
     * @return  The User's password.  
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the User's password.
     * 
     * @param password  The User's password. 
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the access level of the User.
     * 
     * @return  The User's access level. 
     */
    public int getAccessLevel() {
        return accessLevel;
    }

    /**
     * Sets the access level of the User.
     * 
     * @param accessLevel  The User's access level. 
     */
    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    /**
     * Returns the status of the User's account.
     * 
     * @return  The status of the User's account.
     */
    public int getAccountStatus() {
        return accountStatus;
    }

    /**
     * Sets the status of the User's account.
     * 
     * @param accountStatus  The status of the User's account.
     */
    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }


    /**
     * Returns the email address of the User (mutable/unique to each User).
     * 
     * @return  The User's email address.  
     */ 
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the email address of the User.
     * 
     * @param emailAddress  The User's email address. 
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Returns the first name of the User.
     * 
     * @return  The User's first name.  
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the User.
     * 
     * @param firstName  The User's first name. 
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the User.
     * 
     * @return  The User's last name.  
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the User.
     * 
     * @param lastName  The User's last name. 
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns date the User was created.
     * 
     * @return  The User's creation date.
     */
    public Date getDateCreated() {
        return dateCreated;
    }
    
    /**
     * Sets the date the User was created.
     * 
     * @param dateCreated   The User's creation date.
     */    
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Returns date the User's account was last modified.
     * 
     * @return  The User account last modified date.
     */
    public Date getDateModified() {
        return dateModified;
    }
    
    /**
     * Sets the date the User's account was last modified.
     * 
     * @param dateModified   The User account last modified date.
     */    
    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    /**
     * String representation of a User object.
     * A User is person with an account in the cloudcontrol web app. 
     * The User attributes correspond to database columns.
     */ 
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("userId: " + userId + "\n ");
        buffer.append("userName: " + userName + "\n ");
        buffer.append("password: " + password + "\n ");
        buffer.append("accessLevel: " + String.valueOf(accessLevel) + "\n ");
        buffer.append("accountStatus: " + String.valueOf(accountStatus) + "\n ");
        buffer.append("emailAddress: " + emailAddress + "\n ");
        buffer.append("firstName: " + firstName + "\n ");
        buffer.append("lastName: " + lastName + "\n ");
        buffer.append("dateCreated: " + dateCreated + "\n ");
        buffer.append("dateModified: " + dateModified + "\n ");        
       return buffer.toString();
    }


}
