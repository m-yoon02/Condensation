package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
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
        connect4Image.setImage(new Image(getClass().getResource("/ca/ucalgary/seng300/p4/condensation/connect4.png").toExternalForm()));

        // Button navigation
        playButton.setOnAction(event -> navigateToQueue("/ca/ucalgary/seng300/p4/condensation/connectfourqueue.fxml"));
        leaderboardButton.setOnAction(event -> navigateTo("/ca/ucalgary/seng300/p4/condensation/ConnectFourLeaderboard.fxml"));
        howToPlayButton.setOnAction(event -> navigateTo("/ca/ucalgary/seng300/p4/condensation/howToPlayConnect4.fxml"));
        backButton.setOnAction(event -> navigateTo("ProfileGame.fxml")); // Update with the actual previous page
    }

    public void navigateToQueue(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ConnectFourQueueController connectFourQueueController = fxmlLoader.getController();
        HelloApplication.setActiveScene(scene, "Connect Four Queue");
        connectFourQueueController.start();
    }

    public void navigateTo(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load());
            HelloApplication.setActiveScene(scene, "Games Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
