package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Backend.Handlers.CheckersHandler;
import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class CheckersHomeMenuController {

    Stage stage;

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
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("Select your game");
        stage.setScene(scene);
        stage.setMinWidth(700);
        stage.setMinHeight(530);
        stage.show();
    }


    // play button
    @FXML
    public void onPlayBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersqueue.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Condensation Application - Queue");
        stage.setScene(scene);
        stage.show();
    }

    // leaderboard
    @FXML
    public void onLeaderboardBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CheckersLeaderboard.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Checkers - Leaderboard");
        stage.setScene(scene);
        stage.show();
    }

    // how to play button
    @FXML
    void onHowToPlayBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersDescription.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("Checkers Description");
        stage.setScene(scene);
        stage.setMinWidth(700);
        stage.setMinHeight(530);
        stage.show();
    }


}
