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
    private Button backButton;

    @FXML
    public void goBack(ActionEvent event) throws IOException {
        Scene scene;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ca/ucalgary/seng300/p4/condensation/ConnectFourHomeMenu.fxml"));
        scene = new Scene(fxmlLoader.load());
        HelloApplication.setActiveScene(scene, "Connect 4 - Menu");
    }
}
