package main.java.controllers;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.models.Appointment;
import main.java.models.Doctor;
import main.java.models.User;

public class DoctorController {
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> colAppointmentPatient;
    @FXML
    private TableColumn<Appointment, String> colAppointmentDate;

    private Doctor doctor;

    @FXML
    public void handleDeleteAppointment() {
        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (appointment != null) {
            doctor.cancelAppointment(appointment.getPatient(), appointment.getDate());
        }
        loadAppointments();
    }

    @FXML
    public void initialize() {
        doctor = (Doctor) User.getCurrentUser();

        colAppointmentPatient.setCellValueFactory(new PropertyValueFactory<>("patient"));
        colAppointmentDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        loadAppointments();
    }

    private void loadAppointments() {
        List<Appointment> appointments = doctor.getAppointments();
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList(appointments);
        appointmentTable.setItems(appointmentList); //
    }
}
