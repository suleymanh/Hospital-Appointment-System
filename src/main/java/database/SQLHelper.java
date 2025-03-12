package main.java.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.java.models.User;

public class SQLHelper {
    private static final String DB_URL = "jdbc:sqlite:hospital.db";
    private static Connection connection;

    // Veritabanı bağlantısını kurar
    public static void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
                System.out.println("Veritabanına başarılı bir şekilde bağlandı!");
                createTables();
            }
        } catch (SQLException e) {
            System.err.println("Veritabanı bağlantı hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createTables() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS Users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "role TEXT NOT NULL" +
                ");";

        String createAppointmentsTable = "CREATE TABLE IF NOT EXISTS Appointments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "doctor TEXT NOT NULL, " +
                "patient TEXT NOT NULL, " +
                "dateTime TEXT NOT NULL" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createAppointmentsTable);
            System.out.println("Tablolar kontrol edildi ve gerektiğinde oluşturuldu.");
        } catch (SQLException e) {
            System.err.println("Tablo oluşturma hatası: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    // Veritabanı bağlantısını kapatır
    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Veritabanı bağlantısı kapatıldı.");
            }
        } catch (SQLException e) {
            System.err.println("Bağlantı kapatma hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Kullanıcı hesabının geçerli olup olmadığını sorgular
    public static boolean isAccountValid(String username, String password) {
        String query = "SELECT id FROM Users WHERE username = ? AND password = ? LIMIT 1";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Eğer bir kayıt dönerse, kullanıcı geçerlidir
        } catch (SQLException e) {
            System.err.println("Hesap doğrulama hatası: " + e.getMessage());
        }
        return false;
    }
    

    // Kullanıcının tipini öğrenir (admin, doctor, patient)
    public static String getRole(String username) {
        String query = "SELECT role FROM Users WHERE username = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("role");
            }
        } catch (SQLException e) {
            System.err.println("Kullanıcı tipi sorgulama hatası: " + e.getMessage());
        }
        return null;
    }

    // Mevcut hasta ve doktorları görüntüler
    public static List<User> viewUsers(String role) {
        String query = "SELECT * FROM Users WHERE role = ?";
        List<User> users = new ArrayList<User>();
        try {
            
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, role);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(new User(
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    role
                ));
            }
            return users;
        } catch (SQLException e) {
            System.err.println(role + " görüntüleme hatası: " + e.getMessage());
        }
        return null;
    }

    public static boolean checkAppointmentForDoctor(String doctor, String date) {
        String query = "SELECT 1 FROM Appointments WHERE doctor = ? AND dateTime = ? LIMIT 1";
        try {
            PreparedStatement statement = SQLHelper.getConnection().prepareStatement(query);
            statement.setString(1, doctor);
            statement.setString(2, date);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Eğer kayıt varsa true döner, yoksa false
        } catch (SQLException e) {
            System.err.println("Randevu kontrol hatası: " + e.getMessage());
        }
        return false;
    }

    public static boolean checkAppointmentForPatient(String patient, String date) {
        String query = "SELECT 1 FROM Appointments WHERE patient = ? AND dateTime = ? LIMIT 1";
        try {
            PreparedStatement statement = SQLHelper.getConnection().prepareStatement(query);
            statement.setString(1, patient);
            statement.setString(2, date);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Eğer kayıt varsa true döner, yoksa false
        } catch (SQLException e) {
            System.err.println("Randevu kontrol hatası: " + e.getMessage());
        }
        return false;
    }
}
