package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class ConnectFourHomeMenuController {

    @FXML
    private Button playButton;

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button howToPlayButton;

    @FXML
    private Button backButton;

    @FXML
    private ImageView connect4Image;

    @FXML
    public void initialize() {
        // Set the Connect 4 image
        connect4Image.setImage(new Image(getClass().getResource("connect4.png").toExternalForm()));

        // Button navigation
        playButton.setOnAction(event -> navigateTo("connect4-board.fxml"));
        leaderboardButton.setOnAction(event -> navigateTo("ConnectFourLeaderboard.fxml"));
        howToPlayButton.setOnAction(event -> navigateTo("connect4-howtoplay.fxml"));
        backButton.setOnAction(event -> navigateTo("previous-page.fxml")); // Update with the actual previous page
    }

    private void navigateTo(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Stage stage = (Stage) playButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            stage.setTitle("Games Menu");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
