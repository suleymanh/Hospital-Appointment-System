package main.java.models;

public class Appointment {
    private int id;
    private String patient;
    private String doctor;
    private String date;

    // Constructor
    public Appointment(int id, String patient, String doctor, String date) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
    }

    // Getters ve Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Randevu bilgilerini string olarak döner
    @Override
    public String toString() {
        return "Appointment [id=" + id + ", patient=" + patient + ", doctor=" + doctor + ", date=" + date + "]";
    }
}

