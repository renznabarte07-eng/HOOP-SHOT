package oop_project;
//encapsulation
public class Reservation {//where all the details of reservation stored privately 
    private String id;
    private String username;
    private String fullName;
    private String contact;
    private String date;
    private String time;
    private int duration;
    private int amount;
    private String proofPath;
    private String status;

    public Reservation(String id, String username, String fullName, String contact, String date, String time, int duration, int amount, String proofPath, String status) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.contact = contact;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.amount = amount;
        this.proofPath = proofPath;
        this.status = status;
    }
    //setters getters (for getting the private data)
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getContact() { return contact; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public int getDuration() { return duration; }
    public int getAmount() { return amount; }
    public String getProofPath() { return proofPath; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}