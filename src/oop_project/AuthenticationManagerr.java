package oop_project;

public class AuthenticationManagerr {
    public static Userrr login(Store store, String username, String password) {
        if (store == null || store.getUsers() == null) return null;
        for (Userrr u : store.getUsers()) {//uses polymorphism by using the parent class to check the login because it has a 2 different sub class. admin and user
            if (u != null && u.getUsername() != null && u.getUsername().equalsIgnoreCase(username)) {
                if (u.isBlocked()) {
                    return null;
                }
                
                if (u.getPassword() != null && u.getPassword().equals(password)) {
                    u.setLoginAttempts(0);
                    return u;
                } else {
                    u.setLoginAttempts(u.getLoginAttempts() + 1);
                    if (u.getLoginAttempts() >= 3) {
                        u.setBlocked(true);
                    }
                    return null;
                }
            }
        }
        return null;
    }
}