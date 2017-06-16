package edu.ucar.unidata.cloudcontrol.domain;


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

    public User build() {
        return user;
    }
}
