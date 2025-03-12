package main.java.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.database.SQLHelper;

public class Admin extends User {

    // Constructor
    public Admin(String username, String password) {
        super(username, password, "admin");
    }

    // Kullanıcı ekler
    private void addUser(String username, String password, String role) {
        String query = "INSERT INTO Users (username, password, role) VALUES (?, ?, ?)";
        try {
            PreparedStatement statement = SQLHelper.getConnection().prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Kullanıcı ekleme hatası: " + e.getMessage());
        }
    }

    public void addPatient(String username, String password) {
        addUser(username, password, "patient");
    }

    public void addDoctor(String username, String password) {
        addUser(username, password, "doctor");
    }

    // Kullanıcı siler
    public void deleteUser(String username) {
        String query = "DELETE FROM Users WHERE username = ?";
        try {
            PreparedStatement statement = SQLHelper.getConnection().prepareStatement(query);
            statement.setString(1, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Kullanıcı silme hatası: " + e.getMessage());
        }
    }

    public List<Appointment> getAppointments() {
        String query = "SELECT * FROM Appointments";
        List<Appointment> appointments = new ArrayList<>();
        try {
            PreparedStatement statement = SQLHelper.getConnection().prepareStatement(query);
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
            System.err.println("Randevuları çekme hatası: " + e.getMessage());
        }
        return appointments;
    }
    
}
