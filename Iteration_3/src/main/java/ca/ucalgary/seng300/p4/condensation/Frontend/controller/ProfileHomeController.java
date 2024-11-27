package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class ProfileHomeController {

    @FXML
    private TableView<?> tableView;// TODO: add actual data from networking in <?>

    @FXML
    private TableColumn<?, ?> dateColumn;

    @FXML
    private TableColumn<?, ?> gameColumn;

    @FXML
    private TableColumn<?, ?> scoreColumn;

    @FXML
    private TextField usernameField;

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button gamesButton;

    @FXML
    private Button playerProfileButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Label titleLabel;

    /*// Initialize method to handle setup logic
    @FXML
    public void initialize() {
        // Example: Initialize the table columns or other components
        System.out.println("Profile Home initialized");
    }*/

    @FXML
    void onLeaderboardButtonClicked(ActionEvent event) {
        System.out.println("Leaderboard button clicked");
        // Navigate to leaderboard view
    }

    @FXML
    void onGamesButtonClicked(ActionEvent event) {
        System.out.println("Games button clicked");  // TODO
        // Navigate to games view

    }

    @FXML
    void onPlayerProfileButtonClicked(ActionEvent event) {
        System.out.println("Player Profile button clicked");
        // Navigate to player profile view
    }

    @FXML
    void onLogoutButtonClicked(ActionEvent event) {
        System.out.println("Logout button clicked");
        // Perform logout and return to login view
    }
}
