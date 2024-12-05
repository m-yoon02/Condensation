package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CheckersHowToPlayController {

    @FXML
    public void onHowToPlayBackBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersHomeMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HelloApplication.setActiveScene(scene, "Checkers Home Menu");
    }
}
