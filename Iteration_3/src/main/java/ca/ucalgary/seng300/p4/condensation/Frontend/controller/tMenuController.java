package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
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
        playButton.setOnAction(event -> navigateTo("Condensation Appliance - Tic Tac Toe Queue", "tictactoequeue.fxml"));
        leaderboardButton.setOnAction(event -> navigateTo("Condensation Appliance - Tic Tac Toe Leaderboard", "tLeaderboard.fxml"));
        howToPlayButton.setOnAction(event -> navigateTo("Condensation Appliance - How to Play Tic Tac Toe", "tHowToPlay.fxml"));
        goBack.setOnAction(event -> navigateTo("Condensation Appliance - Games Menu", "ProfileGame.fxml"));
    }

    private void navigateTo(String title, String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            Stage stage = (Stage) playButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
