package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ProfileHomeController {

    @FXML
    private TableView<?> tableView;

    @FXML
    private TextField profileTitle;

    @FXML
    private TextField usernameField;

    @FXML
    private Button logoutButton;

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button playerProfilesButton;

    @FXML
    private Button gamesButton;

    @FXML
    void handleLogout(ActionEvent event) {
        System.out.println("Logging out...");
        // Add logout functionality here
    }

    @FXML
    void handleLeaderboard(ActionEvent event) {
        System.out.println("Opening Leaderboard...");
        // Add functionality to open the leaderboard page
    }

    @FXML
    void handlePlayerProfiles(ActionEvent event) {
        System.out.println("Opening Player Profiles...");
        // Add functionality to open the player profiles page
    }

    @FXML
    void handleGames(ActionEvent event) {
        System.out.println("Opening Games...");
        // Add functionality to open the games page
    }
}
