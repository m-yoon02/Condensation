package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CheckersHomeMenuController {

    @FXML
    Button leaderboardBtn;

    @FXML
    Button leaderboardBackBtn;

    @FXML
    Button homeMenuBackBtn;

    @FXML
    Button playBtn;

    @FXML
    Button howToPlayBtn;

    @FXML
    Button howToPlayBackBtn;


    // Checkers home menu
    @FXML
    public void onHomeMenuBackBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ProfileGame.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HelloApplication.setActiveScene(scene, "Select your game");
    }


    // play button
    @FXML
    public void onPlayBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersqueue.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        CheckersQueueController checkersQueueController = fxmlLoader.getController();
        HelloApplication.setActiveScene(scene);
        checkersQueueController.start();
    }

    // leaderboard
    @FXML
    public void onLeaderboardBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CheckersLeaderboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HelloApplication.setActiveScene(scene, "Checkers - Leaderboard");
    }

    // how to play button
    @FXML
    void onHowToPlayBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersDescription.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HelloApplication.setActiveScene(scene, "Checkers Description");
    }


}
