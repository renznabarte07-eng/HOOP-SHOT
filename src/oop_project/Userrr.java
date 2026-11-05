package oop_project;
//abstract(parent class)
public abstract class Userrr {
    private String fullName;
    private String username;
    private String contact;
    private String password;
    private int loginAttempts = 0;
    private boolean isBlocked = false;
    //constructor
    public Userrr(String fullName, String username, String contact, String password) {
        this.fullName = fullName;
        this.username = username;
        this.contact = contact;
        this.password = password;
    }

    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getContact() { return contact; }
    public String getPassword() { return password; }

    public int getLoginAttempts() { return loginAttempts; }
    public void setLoginAttempts(int loginAttempts) { this.loginAttempts = loginAttempts; }

    public boolean isBlocked() { return isBlocked; }
    public void setBlocked(boolean blocked) { isBlocked = blocked; }

    public abstract boolean isAdmin();
}