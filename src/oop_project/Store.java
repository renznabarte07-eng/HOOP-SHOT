package oop_project;

import java.util.ArrayList;
//storage that stores the account and reservations made by the users and uses encapsulation to make the datas not accessible and accessible only  by using setters and getters
public class Store {
    private ArrayList<Userrr> users = new ArrayList<>();
    private ArrayList<Reservation> reservations = new ArrayList<>();
    private int nextReservationId = 1;

    public ArrayList<Userrr> getUsers() {//stores the registred accs inside the system
        return users;
    }

    public ArrayList<Reservation> getReservations() {//stores the reservations of the users inside the arraylist 
        return reservations;
    }

    public int getNextReservationId() {
        return nextReservationId;// for reservation id 
    }

    public void setNextReservationId(int nextReservationId) {
       this.nextReservationId = nextReservationId;
    }
}