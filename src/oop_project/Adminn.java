package oop_project;
//inheritance
public class Adminn extends Userrr {
    public Adminn(String fullName, String username, String contact, String password) {
        super(fullName, username, contact, password);//calls the constructors of parent in user class
    }

    @Override
    public boolean isAdmin() {//polymorphism
        return true;// returns true to prooves its for admin acc
    }
}