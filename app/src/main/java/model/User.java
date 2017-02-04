package model;

/**
 * Created by Faina0502 on 28/01/2017.
 */

public class User {

    private String email;
    private String password;
    private int userID;
    private UserType type;

    public User(String email, String password, int userID, UserType type) {
        this.email = email;
        this.password = password;
        this.userID = userID;
        this.type = type;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }
}
