package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ProfileHomeController {

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button playerProfilesButton;

    @FXML
    private Button gamesButton;

    @FXML
    private Button logoutButton;

    @FXML
    private TextField usernameField;

    @FXML
    public void initialize() {
        // Initialize any required logic
        usernameField.setText("Guest User");
    }

    @FXML
    private void handleLeaderboardButton() {
        System.out.println("Leaderboard button clicked!");
        // Add navigation or functionality here
    }

    @FXML
    private void handlePlayerProfilesButton() {
        System.out.println("Player Profiles button clicked!");
        // Add navigation or functionality here
    }

    @FXML
    private void handleGamesButton() {
        System.out.println("Games button clicked!");
        // Add navigation or functionality here
    }

    @FXML
    private void handleLogoutButton() {
        System.out.println("Log out button clicked!");
        // Add logout logic here, e.g., close the stage
        System.exit(0);
    }
}

