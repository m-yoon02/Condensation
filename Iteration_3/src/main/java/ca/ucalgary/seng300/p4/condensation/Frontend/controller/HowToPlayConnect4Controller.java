package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HowToPlayConnect4Controller {

    @FXML
    public Button backButton;

    @FXML
    public void initialize() {
        backButton.setOnAction(event -> navigateTo("ConnectFourHomeMenu.fxml"));
    }

    private void navigateTo(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load());
            HelloApplication.setActiveScene(scene, "How to Play Connect 4!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
