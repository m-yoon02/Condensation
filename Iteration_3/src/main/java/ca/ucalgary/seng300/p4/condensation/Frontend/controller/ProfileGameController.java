package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the Profile Game selection screen.
 * This screen allows the user to select a game to play (Tic Tac Toe, Connect4, or Checkers)
 * or navigate back to the home screen.
 */
public class ProfileGameController {

    @FXML
    private Label ticTacToeArea; // Description or status area for Tic Tac Toe

    @FXML
    private Label connect4Area; // Description or status area for Connect 4

    @FXML
    private Label checkersArea; // Description or status area for Checkers

    @FXML
    private Button ticTacToePlayButton; // Button to play Tic Tac Toe

    @FXML
    private Button connect4PlayButton; // Button to play Connect 4

    @FXML
    private Button checkersPlayButton; // Button to play Checkers

    @FXML
    private Button homeButton; // Button to navigate back to the home screen

    /**
     * Initializes the controller.
     * This method is called automatically after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        System.out.println("Game selection page initialized");
        // Example: Additional initialization logic can go here
    }

    /**
     * Handles the event when the "Play Tic Tac Toe" button is clicked.
     *
     * @param event ActionEvent triggered by the "Play Tic Tac Toe" button.
     */
    @FXML
    void onTicTacToePlayClicked(ActionEvent event) throws IOException {
        System.out.println("Tic Tac Toe Play button clicked");
        Parent homeParent = FXMLLoader.load(HelloApplication.class.getResource("tMenu.fxml"));
        Scene homeScene = new Scene(homeParent);
        HelloApplication.setActiveScene(homeScene);
    }

    /**
     * Handles the event when the "Play Connect 4" button is clicked.
     *
     * @param event ActionEvent triggered by the "Play Connect 4" button.
     */
    @FXML
    void onConnect4PlayClicked(ActionEvent event) throws IOException {
        System.out.println("Connect 4 Play button clicked");
        Parent homeParent = FXMLLoader.load(HelloApplication.class.getResource("ConnectFourHomeMenu.fxml"));
        Scene homeScene = new Scene(homeParent);
        HelloApplication.setActiveScene(homeScene);
    }

    /**
     * Handles the event when the "Play Checkers" button is clicked.
     *
     * @param event ActionEvent triggered by the "Play Checkers" button.
     */
    @FXML
    void onCheckersPlayClicked(ActionEvent event) throws IOException {
        System.out.println("Checkers Play button clicked");
        Parent homeParent = FXMLLoader.load(HelloApplication.class.getResource("checkersHomeMenu.fxml"));
        Scene homeScene = new Scene(homeParent);
        HelloApplication.setActiveScene(homeScene);
    }

    /**
     * Handles the event when the "Home" button is clicked.
     * Navigates back to the Profile Home screen.
     *
     * @param event ActionEvent triggered by the "Home" button.
     * @throws IOException if the FXML file for the Profile Home screen cannot be loaded.
     */
    @FXML
    void onHomeButtonClicked(ActionEvent event) throws IOException {
        System.out.println("Home button clicked...redirecting to home");
        // Load the Profile Home FXML file
        Parent homeParent = FXMLLoader.load(HelloApplication.class.getResource("ProfileHome.fxml"));
        Scene homeScene = new Scene(homeParent);
        HelloApplication.setActiveScene(homeScene);
    }
}