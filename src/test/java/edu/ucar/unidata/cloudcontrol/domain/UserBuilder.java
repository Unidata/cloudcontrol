package edu.ucar.unidata.cloudcontrol.domain;

import java.util.Date;

public class UserBuilder {

    private User user;

    public UserBuilder() {
        user = new User();
    }

    public UserBuilder userId(int userId) {
        user.setUserId(userId);
        return this;
    }

    public UserBuilder userName(String userName) {
        user.setUserName(userName);
        return this;
    }

    public UserBuilder fullName(String fullName) {
        user.setFullName(fullName);
        return this;
    }

    public UserBuilder emailAddress(String emailAddress) {
        user.setEmailAddress(emailAddress);
        return this;
    }

    public UserBuilder password(String password) {
        user.setPassword(password);
        return this;
    }

    public UserBuilder confirmPassword(String confirmPassword) {
        user.setConfirmPassword(confirmPassword);
        return this;
    }

    public UserBuilder accessLevel(int accessLevel) {
        user.setAccessLevel(accessLevel);
        return this;
    }

    public UserBuilder accountStatus(int accountStatus) {
        user.setAccountStatus(accountStatus);
        return this;
    }

    public UserBuilder dateCreated(Date dateCreated) {
        user.setDateCreated(dateCreated);
        return this;
    }

    public UserBuilder dateModified(Date dateModified) {
        user.setDateModified(dateModified);
        return this;
    }

    public User build() {
        return user;
    }
}
