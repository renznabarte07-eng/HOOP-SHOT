package oop_project;

// [OOP] Inheritance
public class Customerr extends User {
    private static final long serialVersionUID = 1L;

    public Customerr(String fullName, String username, String contact, String password) {
        super(fullName, username, contact, password, false);
    }

    @Override
    public String getRoleTitle() {
        return "Customer";
    }
}