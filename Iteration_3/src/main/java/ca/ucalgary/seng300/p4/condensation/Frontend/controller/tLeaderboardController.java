package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class tLeaderboardController {
    @FXML
    public Button backToMenu;

    @FXML
    public void initialize() {
        backToMenu.setOnAction(event -> navigateTo("tMenu.fxml"));
    }

    private void navigateTo(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load());
            HelloApplication.setActiveScene(scene, "Leaderboard for Tic Tac Toe!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
