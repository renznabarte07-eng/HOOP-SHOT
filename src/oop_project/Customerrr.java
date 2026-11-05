package oop_project;

public class Customerrr extends Userrr {
    public Customerrr(String fullName, String username, String contact, String password) {
        super(fullName, username, contact, password);
    }

    @Override
    public boolean isAdmin() {//polymorphism
        return false;// returns false to prooves its for user acc
    }
}