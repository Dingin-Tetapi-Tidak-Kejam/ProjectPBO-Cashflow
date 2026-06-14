package model;

public class User {

    private int userId;
    private String userName;
    private String userEmail;
    private String userPassword;

    public User(int userId,
                String userName,
                String userEmail,
                String userPassword) {

        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }
}