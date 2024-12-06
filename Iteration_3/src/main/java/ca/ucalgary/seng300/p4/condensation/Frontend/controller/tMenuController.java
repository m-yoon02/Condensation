package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class tMenuController {

    @FXML
    private Button playButton;

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button howToPlayButton;

    @FXML
    private Button goBack;

    @FXML
    public void initialize() {
        playButton.setOnAction(event -> queueTicTacToe());
        leaderboardButton.setOnAction(event -> navigateTo("tLeaderboard.fxml"));
        howToPlayButton.setOnAction(event -> navigateTo("tHowToPlay.fxml"));
        goBack.setOnAction(event -> navigateTo("ProfileGame.fxml"));
    }

    private void navigateTo(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load());
            HelloApplication.setActiveScene(scene, "Games Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void queueTicTacToe() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tictactoequeue.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TicTacToeQueueController ticTacToeQueueController = fxmlLoader.getController();
        HelloApplication.setActiveScene(scene, "TicTacToe");
        ticTacToeQueueController.start();
    }
}
