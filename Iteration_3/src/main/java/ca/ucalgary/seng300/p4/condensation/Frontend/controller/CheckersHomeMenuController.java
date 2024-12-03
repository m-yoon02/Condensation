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

    public CheckersHomeMenuController(Stage stage) {
        this.stage = stage;
    }


    // Checkers home menu
    @FXML
    public void onHomeMenuBackBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ProfileGame.fxml"));
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        window.setTitle("Select your game");
        window.setScene(scene);
        window.setMinWidth(700);
        window.setMinHeight(530);
        window.show();
    }


    // play button
    @FXML
    public void onPlayBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersqueue.fxml"));
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        window.setScene(scene);
        window.show();
    }

    // leaderboard
    @FXML
    public void onLeaderboardBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CheckersLeaderboard.fxml"));
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        window.setTitle("Checkers - Leaderboard");
        window.setScene(scene);
        window.show();
    }

    // how to play button
    @FXML
    void onHowToPlayBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersDescription.fxml"));
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        window.setTitle("Checkers Description");
        window.setScene(scene);
        window.setMinWidth(700);
        window.setMinHeight(530);
        window.show();
    }


}
