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
        playButton.setOnAction(event -> navigateTo("Condensation Appliance - Connect Four Queue", "/ca/ucalgary/seng300/p4/condensation/connectfourqueue.fxml"));
        leaderboardButton.setOnAction(event -> navigateTo("Condensation Appliance - Connect Four Leaderboard", "/ca/ucalgary/seng300/p4/condensation/ConnectFourLeaderboard.fxml"));
        howToPlayButton.setOnAction(event -> navigateTo("Condensation Appliance - How To Play Connect Four", "/ca/ucalgary/seng300/p4/condensation/howToPlayConnect4.fxml"));
        backButton.setOnAction(event -> navigateTo("Condensation Appliance - Games Menu", "ProfileGame.fxml")); // Update with the actual previous page
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
