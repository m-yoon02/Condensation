package ca.ucalgary.seng300.p4.condensation;

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
        playButton.setOnAction(event -> navigateTo("tGame.fxml"));
        leaderboardButton.setOnAction(event -> navigateTo("tLeaderboard.fxml"));
        howToPlayButton.setOnAction(event -> navigateTo("tHowToPlay.fxml"));
        //goBack.setOnAction(event -> navigateTo("profilePage.fxml"));
    }

    private void navigateTo(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Stage stage = (Stage) playButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            stage.setTitle("Games Menu");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
