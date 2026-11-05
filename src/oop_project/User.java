package oop_project;

public abstract class User {
    private String fullName;
    private String username;
    private String contact;
    private String password;

    public User(String fullName, String username, String contact, String password) {
        fullName = fullName;
        username = username;
        contact = contact;
        password = password;
    }

    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getContact() { return contact; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public abstract boolean isAdmin();
}