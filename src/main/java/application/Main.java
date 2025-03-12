package main.java.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.database.SQLHelper;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println(getClass().getResource("login.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle("Hospital Appointment System");
        primaryStage.show();
        SQLHelper.connect();
    }

    public static void main(String[] args) {
        launch(args);
    }   
}
