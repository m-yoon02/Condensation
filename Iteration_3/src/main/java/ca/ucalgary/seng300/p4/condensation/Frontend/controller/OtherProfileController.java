package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.UUID;

/**
 * Controller class for the "Other Profile" screen.
 * This screen allows users to search for a player's profile by username.
 * Displays two tables:
 * 1. Player overall stats for games like Connect4, Checkers, and TicTacToe.
 * 2. Player's recent matches for any game.
 */
public class OtherProfileController {

    // Upper Table for overall stats
    @FXML
    private TableView<PlayerProfile> playerTableView;
    @FXML
    private TableColumn<PlayerProfile, String> playerColumn;
    @FXML
    private TableColumn<PlayerProfile, String> gameColumn;
//    @FXML
//    private TableColumn<PlayerProfile, String> outcomeColumn;
    @FXML
    private TableColumn<PlayerProfile, String> currentEloColumn;
    @FXML
    private TableColumn<PlayerProfile, String> totalWinsColumn;
    @FXML
    private TableColumn<PlayerProfile, String> gamesPlayedColumn;
    @FXML
    private TableColumn<PlayerProfile, String> statusColumn;

    // Lower Table for recent matches
    @FXML
    private TableView<JSONObject> playerRecentTableView;
    @FXML
    private TableColumn<JSONObject, String> playerRecentgameColumn; // Column for game type
    @FXML
    private TableColumn<JSONObject, String> playerRecentoutcomeColumn; // Column for match outcome
    @FXML
    private TableColumn<JSONObject, Number> playerRecentcurrentEloColumn; // Column for player's ELO after the match
    @FXML
    private TableColumn<JSONObject, Number> playerRecenttotalWinsColumn; // Placeholder column for total wins
    @FXML
    private TableColumn<JSONObject, Number> playerRecentgamesPlayedColumn; // Placeholder column for games played

    @FXML
    private TextField usernameTextField; // Input field for username
    @FXML
    private Text statusMessage; // Text field to display search status

    private final ObservableList<PlayerProfile> overallStatsData = FXCollections.observableArrayList(); // Data for overall stats table
    private final ObservableList<PlayerMatch> recentMatchesData = FXCollections.observableArrayList(); // Data for recent matches table
    private Leaderboard leaderboard; // Leaderboard instance for fetching data

    /**
     * Initializes the controller. Sets up column bindings and loads the leaderboard data.
     */
    @FXML
    public void initialize() {
        // Bind overall stats columns to PlayerProfile properties
        gameColumn.setCellValueFactory(data -> data.getValue().gameNameProperty());
        currentEloColumn.setCellValueFactory(data -> data.getValue().currentEloProperty());
        totalWinsColumn.setCellValueFactory(data -> data.getValue().totalWinsProperty());
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());

        // Bind recent matches columns to PlayerMatch properties
        playerRecentgameColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("gameType")));
        playerRecentoutcomeColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("outcome")));
        playerRecentcurrentEloColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(((Long) cellData.getValue().get("currentElo")).intValue()));
        playerRecenttotalWinsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(0)); // Placeholder if not in matches
        playerRecentgamesPlayedColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(0)); // Placeholder if not in matches

        // Initialize the leaderboard instance
        try {
            leaderboard = Leaderboard.getInstance("/Users/mortezafaraji/Desktop/school/SENG300/seng300-f24-project/Iteration_3/src/main/java/ca/ucalgary/seng300/p4/condensation/Networking/Database/Leaderboard/Leaderboard.json");
        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
            statusMessage.setText("Error initializing leaderboard.");
        }
    }

    /**
     * Handles the "Search" button click event.
     * Searches for a player by username and populates both tables with their data.
     *
     * @param event ActionEvent triggered by the "Search" button.
     */
    @FXML
    void onSearchButtonClicked(ActionEvent event) {
        String username = usernameTextField.getText().trim();
        JSONObject user = leaderboard.findUserByName(username);
        if (username.isEmpty()) {
            statusMessage.setText("Please enter a username to search.");
            return;
        }

        statusMessage.setText("User found!");
        overallStatsData.clear();
        recentMatchesData.clear();

        // Populate overall stats
        overallStatsData.add(parsePlayer(user, "Connect4"));

        overallStatsData.add(parsePlayer(user, "Checkers"));
        overallStatsData.add(parsePlayer(user, "TicTacToe"));

        playerTableView.setItems(overallStatsData);
        displayRecentMatches(username);

    }

    /**
     * Parses the player data for a specific game and creates a PlayerProfile object.
     *
     * @param user     JSONObject containing player data.
     * @param gameName Name of the game (e.g., Connect4, Checkers).
     * @return PlayerProfile object containing game-specific stats.
     */
    private PlayerProfile parsePlayer(JSONObject user, String gameName) {
        String playerName = (String) user.get("username");
        String currentElo = user.get(gameName.toLowerCase() + "Elo").toString();
        String totalWins = user.get("winsIn" + gameName).toString();
        String gamesPlayed = user.get("gamesPlayed" + gameName).toString();
        String status = Integer.parseInt(currentElo) > 1500 ? "Online" : "Offline";

        return new PlayerProfile(playerName, gameName, currentElo, totalWins, gamesPlayed, status);
    }

    public void displayRecentMatches(String username) {
        try {
            // Fetch the user from the Leaderboard
            Leaderboard leaderboard = Leaderboard.getInstance("/Users/mortezafaraji/Desktop/school/SENG300/seng300-f24-project/Iteration_3/src/main/java/ca/ucalgary/seng300/p4/condensation/Networking/Database/Leaderboard/Leaderboard.json");
            JSONObject user = leaderboard.findUserByName(username);

            if (user != null) {
                // Extract recent matches and populate the TableView
                JSONArray recentMatches = (JSONArray) user.get("recentMatches");
                playerRecentTableView.getItems().clear();
                for (Object match : recentMatches) {
                    playerRecentTableView.getItems().add((JSONObject) match);
                }
            } else {
                System.out.println("User not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error displaying recent matches.");
        }
    }




    /**
     * Parses recent match data and creates a PlayerMatch object.
     *
     * @param user  JSONObject containing player data.
     * @param match JSONObject representing a single match.
     * @return PlayerMatch object containing match details.
     */
    private PlayerMatch parseRecentMatch(JSONObject user, JSONObject match) {
        String playerName = (String) user.get("username");
        String gameName = (String) match.get("gameType");
        String outcome = (String) match.get("outcome");
        String currentElo = match.get("currentElo").toString();

        return new PlayerMatch(playerName, gameName, outcome, currentElo);
    }

    /**
     * Handles the "Home" button click event.
     * Navigates back to the home screen.
     *
     * @param event ActionEvent triggered by the "Home" button.
     * @throws IOException if the FXML file for the home screen cannot be loaded.
     */
    @FXML
    void onHomeButtonClicked(ActionEvent event) throws IOException {
        Parent homeParent = FXMLLoader.load(HelloApplication.class.getResource("ProfileHome.fxml"));
        Scene homeScene = new Scene(homeParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(homeScene);
        window.show();
    }

    // PlayerProfile class represents a player's overall stats for a specific game.
    public static class PlayerProfile {
        private final SimpleStringProperty playerName;
        private final SimpleStringProperty gameName;
        private final SimpleStringProperty currentElo;
        private final SimpleStringProperty totalWins;
        private final SimpleStringProperty gamesPlayed;
        private final SimpleStringProperty status;

        public PlayerProfile(String playerName, String gameName, String currentElo, String totalWins, String gamesPlayed, String status) {
            this.playerName = new SimpleStringProperty(playerName);
            this.gameName = new SimpleStringProperty(gameName);
            this.currentElo = new SimpleStringProperty(currentElo);
            this.totalWins = new SimpleStringProperty(totalWins);
            this.gamesPlayed = new SimpleStringProperty(gamesPlayed);
            this.status = new SimpleStringProperty(status);
        }

        public SimpleStringProperty playerNameProperty() {
            return playerName;
        }

        public SimpleStringProperty gameNameProperty() {
            return new SimpleStringProperty(gameName.get().toUpperCase());
        }

        public SimpleStringProperty currentEloProperty() {
            return currentElo;
        }

        public SimpleStringProperty totalWinsProperty() {
            return totalWins;
        }

        public SimpleStringProperty gamesPlayedProperty() {
            return gamesPlayed;
        }

        public SimpleStringProperty statusProperty() {
            return status;
        }
    }

    // PlayerMatch class represents a single recent match.
    public static class PlayerMatch {
        private final SimpleStringProperty playerName;
        private final SimpleStringProperty gameName;
        private final SimpleStringProperty outcome;
        private final SimpleStringProperty currentElo;

        public PlayerMatch(String playerName, String gameName, String outcome, String currentElo) {
            this.playerName = new SimpleStringProperty(playerName);
            this.gameName = new SimpleStringProperty(gameName);
            this.outcome = new SimpleStringProperty(outcome);
            this.currentElo = new SimpleStringProperty(currentElo);
        }

        public SimpleStringProperty playerNameProperty() {
            return playerName;
        }

        public SimpleStringProperty gameNameProperty() {
            return gameName;
        }

        public SimpleStringProperty outcomeProperty() {
            return outcome;
        }

        public SimpleStringProperty currentEloProperty() {
            return currentElo;
        }
    }
}
