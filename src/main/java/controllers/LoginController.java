package main.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.java.application.Main;
import main.java.database.SQLHelper;
import main.java.models.Admin;
import main.java.models.Doctor;
import main.java.models.Patient;
import main.java.models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    @FXML
    public void handleLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (SQLHelper.isAccountValid(username, password)) {
            String role = SQLHelper.getRole(username);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            if (role.equals("admin")) {
                User.setCurrentUser(new Admin(username, password));
                stage.setScene(new Scene(FXMLLoader.load(Main.class.getResource("admin.fxml"))));
            } else if(role.equals("doctor")) {
                User.setCurrentUser(new Doctor(username, password));
                stage.setScene(new Scene(FXMLLoader.load(Main.class.getResource("doctor.fxml"))));
            } else if(role.equals("patient")) {
                User.setCurrentUser(new Patient(username, password));
                stage.setScene(new Scene(FXMLLoader.load(Main.class.getResource("patient.fxml"))));
            }
            stage.show();
        } else {
            System.out.println("Invalid login credentials!");
        }
    }
}
