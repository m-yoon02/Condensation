package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ProfileGameController {

    @FXML
    private TextArea ticTacToeArea;

    @FXML
    private TextArea connect4Area;

    @FXML
    private TextArea checkersArea;

    @FXML
    private Button ticTacToePlayButton;

    @FXML
    private Button connect4PlayButton;

    @FXML
    private Button checkersPlayButton;

    @FXML
    private Button homeButton;

    // Initialize method for setting up the controller
    @FXML
    public void initialize() {
        System.out.println("Game selection page initialized");
        // Example: Initialize game descriptions or other logic
    }

    @FXML
    void onTicTacToePlayClicked(ActionEvent event) {
        System.out.println("Tic Tac Toe Play button clicked");
        // Navigate to the Tic Tac Toe game
    }

    @FXML
    void onConnect4PlayClicked(ActionEvent event) {
        System.out.println("Connect 4 Play button clicked");
        // Navigate to the Connect 4 game
    }

    @FXML
    void onCheckersPlayClicked(ActionEvent event) {
        System.out.println("Checkers Play button clicked");
        // Navigate to the Checkers game
    }

    @FXML
    void onHomeButtonClicked(ActionEvent event) {
        System.out.println("Home button clicked");
        // Navigate back to the Home screen
    }
}

