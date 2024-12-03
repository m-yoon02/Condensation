package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class tHowToPlayController {
    @FXML
    public Button backbutton;

    @FXML
    public void initialize() {
        backbutton.setOnAction(event -> navigateTo("tMenu.fxml"));
    }

    private void navigateTo(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            Stage stage = (Stage) backbutton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            stage.setTitle("Condensation Appliance - How To Play Tic Tac Toe");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
