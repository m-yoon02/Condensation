package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.UUID;

/**
 * Controller for the Profile Home screen.
 * Displays recent matches of the player and allows navigation to other parts of the application.
 */
public class ProfileHomeController {

    // TableView to display recent matches
    @FXML
    private TableView<JSONObject> tableView;

    @FXML
    private TableColumn<JSONObject, String> gameColumn; // Column for game type
    @FXML
    private TableColumn<JSONObject, String> outcomeColumn; // Column for match outcome
    @FXML
    private TableColumn<JSONObject, Number> currentEloColumn; // Column for player's ELO after the match
    @FXML
    private TableColumn<JSONObject, Number> totalWinsColumn; // Placeholder column for total wins
    @FXML
    private TableColumn<JSONObject, Number> gamesPlayedColumn; // Placeholder column for games played

    // Text fields and buttons
    @FXML
    private TextField profileTitle; // Title or name of the profile (editable)
    @FXML
    private Label usernameField; // Label to display the player's username
    @FXML
    private Button logoutButton; // Button to log out
    @FXML
    private Button leaderboardButton; // Button to navigate to the leaderboard screen
    @FXML
    private Button playerProfilesButton; // Button to navigate to the player profiles screen
    @FXML
    private Button gamesButton; // Button to navigate to the games screen

    /**
     * Initializes the controller and sets up the TableView.
     */
    @FXML
    public void initialize() {
        // Set the column resize policy for the TableView
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        // Example username (replace this logic with actual user data)
        usernameField.setText("Player 1");

        // Bind columns to the JSON object fields
        gameColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("gameType")));
        outcomeColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("outcome")));
        currentEloColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(((Long) cellData.getValue().get("currentElo")).intValue()));
        totalWinsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(0)); // Placeholder if not in matches
        gamesPlayedColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(0)); // Placeholder if not in matches

        // Replace this UUID logic with actual logic for identifying the current player
        UUID playerUUID = UUID.fromString("345e6789-e89d-45d5-a678-627836285538");

        // Display recent matches for the player
        displayRecentMatches(playerUUID);
    }

    /**
     * Handles the logout button click.
     * Redirects to the login screen or performs logout functionality.
     *
     * @param event ActionEvent triggered by the logout button.
     */
    @FXML
    public void onlogOutButtonClicked(ActionEvent event) {
        System.out.println("Logging out...");
        // TODO: Add logout functionality here
    }

    /**
     * Handles the leaderboard button click.
     * Navigates to the leaderboard page.
     *
     * @param event ActionEvent triggered by the leaderboard button.
     * @throws IOException if the FXML file for the leaderboard screen cannot be loaded.
     */
    @FXML
    void onLeaderboardButtonClicked(ActionEvent event) throws IOException {
        System.out.println("Opening Leaderboard...");
        Parent gamesParent = FXMLLoader.load(HelloApplication.class.getResource("ProfileLeaderB.fxml"));
        Scene gamesScene = new Scene(gamesParent);

        // Get the current stage and set the new scene
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(gamesScene);
        window.show();
    }

    /**
     * Handles the player profiles button click.
     * Navigates to the player profiles lookup page.
     *
     * @param event ActionEvent triggered by the player profiles button.
     * @throws IOException if the FXML file for the player profiles screen cannot be loaded.
     */
    @FXML
    void onPlayerProfilesButtonClicked(ActionEvent event) throws IOException {
        System.out.println("Opening player profile lookup page...");
        Parent gamesParent = FXMLLoader.load(HelloApplication.class.getResource("otherprofile.fxml"));
        Scene gamesScene = new Scene(gamesParent);

        // Get the current stage and set the new scene
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(gamesScene);
        window.show();
    }

    /**
     * Handles the games button click.
     * Navigates to the game selection page.
     *
     * @param event ActionEvent triggered by the games button.
     * @throws IOException if the FXML file for the game selection screen cannot be loaded.
     */
    @FXML
    void onGamesButtonClicked(ActionEvent event) throws IOException {
        System.out.println("Opening Games...");
        Parent gamesParent = FXMLLoader.load(HelloApplication.class.getResource("ProfileGame.fxml"));
        Scene gamesScene = new Scene(gamesParent);

        // Get the current stage and set the new scene
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(gamesScene);
        window.show();
    }

    /**
     * Displays recent matches for the given player.
     *
     * @param playerUUID UUID of the player whose recent matches are to be displayed.
     */
    public void displayRecentMatches(UUID playerUUID) {
        try {
            // Fetch the user from the Leaderboard
            Leaderboard leaderboard = Leaderboard.getInstance();
            JSONObject user = leaderboard.findUserById(playerUUID);

            if (user != null) {
                // Extract recent matches and populate the TableView
                JSONArray recentMatches = (JSONArray) user.get("recentMatches");
                tableView.getItems().clear();
                for (Object match : recentMatches) {
                    tableView.getItems().add((JSONObject) match);
                }
            } else {
                System.out.println("User not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error displaying recent matches.");
        }
    }
}
