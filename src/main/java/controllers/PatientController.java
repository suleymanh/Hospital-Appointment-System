package main.java.controllers;

import java.util.List;
import java.util.stream.IntStream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.database.SQLHelper;
import main.java.models.Appointment;
import main.java.models.Patient;
import main.java.models.User;

public class PatientController {

    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> colAppointmentDoctor;
    @FXML
    private TableColumn<Appointment, String> colAppointmentDate;

    @FXML
    private ComboBox<Integer> choiceDay;

    @FXML
    private ComboBox<Integer> choiceMonth;

    @FXML
    private ComboBox<String> choiceTime;

    @FXML
    private ComboBox<String> choiceDoctor;

    @FXML
    private Label infoLabel;

    private Patient patient;

    @FXML
    public void handleSubmit() {
        String doctor = choiceDoctor.getSelectionModel().getSelectedItem();
        int day = choiceDay.getSelectionModel().getSelectedItem();
        int month = choiceMonth.getSelectionModel().getSelectedItem();
        String time = choiceTime.getSelectionModel().getSelectedItem();
        String date = "%d/%d %s".formatted(day, month, time);
        if (SQLHelper.checkAppointmentForDoctor(doctor, date)) {
            infoLabel.setText("There is already an appointment for this doctor at this time.");
        } else if(SQLHelper.checkAppointmentForPatient(patient.getUsername(), date)) {
            infoLabel.setText("You already have an appointment for another doctor at this time.");
        } else {
            infoLabel.setText("Appointment has created");
            patient.makeAppointment(doctor, date);
            loadAppointments();
        }
    }

    @FXML
    public void handleCancel() {
        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (appointment != null) {
            patient.cancelAppointment(appointment.getDoctor(), appointment.getDate());
        }
        loadAppointments();
    }
    @FXML
    public void initialize() {
        patient = (Patient) User.getCurrentUser();

        colAppointmentDoctor.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        colAppointmentDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        loadAppointments();

        ObservableList<Integer> days = FXCollections.observableArrayList();
        IntStream.rangeClosed(1, 30).forEach(days::add);
        choiceDay.setItems(days);

        // Ayları (1-12) ekle
        ObservableList<Integer> months = FXCollections.observableArrayList();
        IntStream.rangeClosed(1, 12).forEach(months::add);
        choiceMonth.setItems(months);

        // Saatleri ekle
        ObservableList<String> times = FXCollections.observableArrayList(
            "09:00", "10:00", "11:00", "13:00", "14:00", "15:00", "16:00"
        );
        choiceTime.setItems(times);

        // Doktorları veritabanından çek ve ekle
        loadDoctors();

    }

    private void loadAppointments() {
        List<Appointment> appointments = patient.getAppointments();
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList(appointments);
        appointmentTable.setItems(appointmentList); //
    }
    
    private void loadDoctors() {
        ObservableList<String> doctorNames = FXCollections.observableArrayList();
        SQLHelper.viewUsers("doctor").forEach(doctor -> doctorNames.add(doctor.getUsername()));
        choiceDoctor.setItems(doctorNames);
    }

}
