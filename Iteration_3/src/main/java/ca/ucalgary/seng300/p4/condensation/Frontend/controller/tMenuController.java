package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.simple.parser.ParseException;

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
            Stage stage = (Stage) playButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            stage.setTitle("Games Menu");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void queueTicTacToe() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tictactoequeue.fxml"));
        Stage stage = (Stage) playButton.getScene().getWindow();
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 700, 500);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TicTacToeQueueController ticTacToeQueueController = fxmlLoader.getController();
        stage.setTitle("TicTacToe");
        stage.setScene(scene);
        stage.show();
        ticTacToeQueueController.start(stage);
    }
}
