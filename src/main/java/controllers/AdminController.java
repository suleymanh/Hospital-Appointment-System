package main.java.controllers;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import main.java.database.SQLHelper;
import main.java.models.Admin;
import main.java.models.Appointment;
import main.java.models.User;

public class AdminController {
    @FXML
    private TableView<User> patientTable;
    @FXML
    private TableColumn<User, String> colPatientUsername;
    @FXML
    private TableColumn<User, String> colPatientPassword;

    @FXML
    private TableView<User> doctorTable;
    @FXML
    private TableColumn<User, String> colDoctorUsername;
    @FXML
    private TableColumn<User, String> colDoctorPassword;

    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> colAppointmentDoctor;
    @FXML
    private TableColumn<Appointment, String> colAppointmentPatient;
    @FXML
    private TableColumn<Appointment, String> colAppointmentDate;
    
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button addDoctorButton;
    @FXML
    private Button addPatientButton;
    @FXML
    private Button deleteUserButton;

    private Admin admin;

    @FXML
    public void handleAddDoctor() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        admin.addDoctor(username, password);
        System.out.println("Doctor added: " + username);
        loadDoctors();
    }

    @FXML
    public void handleAddPatient() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        admin.addPatient(username, password);
        loadPatients();
        System.out.println("Patient added: " + username);
    }

    @FXML
    public void handleDeleteUser() {
        admin.deleteUser(usernameField.getText());
        loadDoctors();
        loadPatients();
        System.out.println("User deleted");

    }
    
    @FXML
    public void initialize() {
        admin = (Admin) User.getCurrentUser();
        // TableView içindeki kolonları model sınıfı ile eşleştir
        colDoctorUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colDoctorPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colPatientUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPatientPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colAppointmentDoctor.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        colAppointmentPatient.setCellValueFactory(new PropertyValueFactory<>("patient"));
        colAppointmentDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Doktorları yükle
        loadDoctors();
        loadPatients();
        loadAppointments();
    }

    private void loadDoctors() {
        List<User> doctors = SQLHelper.viewUsers("doctor"); 
        ObservableList<User> doctorList = FXCollections.observableArrayList(doctors);
        doctorTable.setItems(doctorList);
    }

    private void loadPatients() {
        List<User> patients = SQLHelper.viewUsers("patient"); 
        ObservableList<User> patientList = FXCollections.observableArrayList(patients);
        patientTable.setItems(patientList);
    }

    private void loadAppointments() {
        List<Appointment> appointments = admin.getAppointments();
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList(appointments);
        appointmentTable.setItems(appointmentList); //
    }

}
