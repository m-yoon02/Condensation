package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

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
    private void goBack(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ca/ucalgary/seng300/p4/condensation/ConnectFourHomeMenu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("Connect 4 - Menu");
        stage.setScene(scene);
        stage.show();
    }
}
