package main.java.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.database.SQLHelper;

public class Patient extends User {

    // Constructor
    public Patient(String username, String password) {
        super(username, password, "patient");
    }

    // Hasta randevularını görüntüler
    public List<Appointment> getAppointments() {
        String query = "SELECT * FROM Appointments WHERE patient = ?";
        List<Appointment> appointments = new ArrayList<>();
        try {
            PreparedStatement statement = SQLHelper.getConnection().prepareStatement(query);
            statement.setString(1, getUsername());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Appointment appointment = new Appointment(
                    resultSet.getInt("id"),
                    resultSet.getString("patient"),
                    resultSet.getString("doctor"),
                    resultSet.getString("dateTime")
                );
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            System.err.println("Hastanın randevularını çekme hatası: " + e.getMessage());
        }
        return appointments;
    }

    // Hasta randevu alır
    public void makeAppointment(String doctor, String date) {
        String query = "INSERT INTO Appointments (patient, doctor, dateTime) VALUES (?, ?, ?)";
        try {
            PreparedStatement statement = SQLHelper.getConnection().prepareStatement(query);
            statement.setString(1, getUsername());
            statement.setString(2, doctor);
            statement.setString(3, date);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Randevu ekleme hatası: " + e.getMessage());
        }
    }

    // Hasta randevusunu iptal eder
    public void cancelAppointment(String doctor, String date) {
        String query = "DELETE FROM Appointments WHERE doctor = ? AND patient = ? AND dateTime = ?";
        try {
            PreparedStatement statement = SQLHelper.getConnection().prepareStatement(query);
            statement.setString(1, doctor);
            statement.setString(2, getUsername());
            statement.setString(3, date);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Randevu silme hatası: " + e.getMessage());
        }
    }
}
