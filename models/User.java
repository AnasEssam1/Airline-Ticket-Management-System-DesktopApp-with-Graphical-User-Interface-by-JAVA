package models;

public class User {
    private int userId;
    private String username;
    private String password;
    private String fullName;
    private String phone_no;


    public User(int userId, String username, String password, String fullName, String phone_no) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone_no = phone_no;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNo() {
        return phone_no;
    }
}
