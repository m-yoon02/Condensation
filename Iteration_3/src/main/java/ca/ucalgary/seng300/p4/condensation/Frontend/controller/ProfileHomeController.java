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
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.UUID;

public class ProfileHomeController {

    // Overall stats table
    @FXML
    private TableView<JSONObject> playerTableView;
    @FXML
    private TableColumn<JSONObject, String> gameColumn;
    @FXML
    private TableColumn<JSONObject, String> currentEloColumn;
    @FXML
    private TableColumn<JSONObject, String> totalWinsColumn;
    @FXML
    private TableColumn<JSONObject, String> statusColumn;

    // Recent matches table
    @FXML
    private TableView<JSONObject> playerRecentTableView;
    @FXML
    private TableColumn<JSONObject, String> playerRecentgameColumn;
    @FXML
    private TableColumn<JSONObject, String> playerRecentoutcomeColumn;
    @FXML
    private TableColumn<JSONObject, String> playerRecentcurrentEloColumn;
    @FXML
    private TableColumn<JSONObject, String> playerRecenttotalWinsColumn;
    @FXML
    private TableColumn<JSONObject, String> playerRecentgamesPlayedColumn;

    // Other UI components
    @FXML
    private Label usernameField;
    @FXML
    private Button leaderboardButton;
    @FXML
    private Button gamesButton;
    @FXML
    private Button playerProfilesButton;
    @FXML
    private Button logoutButton;

    private Leaderboard leaderboard;

    @FXML
    public void initialize() {
        // Set up overall stats table
        gameColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("gameType")));
        currentEloColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("elo").toString()));
        totalWinsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("totalWins").toString()));
       // statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("status")));

        // Set up recent matches table
        playerRecentgameColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("gameType")));
        playerRecentoutcomeColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("outcome")));
        playerRecentcurrentEloColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("currentElo").toString()));
        playerRecenttotalWinsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("totalWins").toString()));
        playerRecentgamesPlayedColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("gamesPlayed").toString()));

        // Set dummy username and initialize leaderboard
        try {
            String usernameOfPlayer = Leaderboard.getInstance().fetchUserFromNewPlayerJson();
            usernameField.setText(usernameOfPlayer);
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            leaderboard = Leaderboard.getInstance();
        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        try {
            // Load player data (example UUID)
            UUID playerUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174123");
            JSONObject poo = Leaderboard.getInstance().findUserById(playerUUID);
            String name = (String) poo.get("username");
            displayPlayerStats(playerUUID);
            displayRecentMatches(name);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void displayPlayerStats(UUID playerUUID) {
        JSONObject user = leaderboard.findUserById(playerUUID);
        if (user == null) {
            System.out.println("Player not found.");
            return;
        }

        ObservableList<JSONObject> statsData = FXCollections.observableArrayList();

        // Add data for each game
        statsData.add(createGameStats(user, "TicTacToe", "tictactoeElo", "winsInTicTacToe", "gamesPlayedTicTacToe"));
        statsData.add(createGameStats(user, "Checkers", "checkersElo", "winsInCheckers", "gamesPlayedCheckers"));
        statsData.add(createGameStats(user, "Connect4", "connect4Elo", "winsInConnect4", "gamesPlayedConnect4"));

        playerTableView.setItems(statsData);
    }

    private JSONObject createGameStats(JSONObject user, String gameType, String eloKey, String winsKey, String gamesKey) {
        JSONObject stats = new JSONObject();
        stats.put("gameType", gameType);
        stats.put("elo", user.get(eloKey));
        stats.put("totalWins", user.get(winsKey));
        stats.put("gamesPlayed", user.get(gamesKey));
        stats.put("status", "Online"); // Add status logic if required
        return stats;
    }

    public void displayRecentMatches(String username) {
        try {
            // Fetch the user from the Leaderboard
            Leaderboard leaderboard = Leaderboard.getInstance();
            JSONObject user = leaderboard.findUserByName(username);

            if (user != null) {
                // Extract recent matches
                JSONArray recentMatches = (JSONArray) user.get("recentMatches");
                playerRecentTableView.getItems().clear();

                for (Object match : recentMatches) {
                    JSONObject matchObject = (JSONObject) match;

                    // Determine the game type and add total wins and games played
                    String gameType = (String) matchObject.get("gameType");

                    int totalWins = 0;
                    int gamesPlayed = 0;

                    switch (gameType) {
                        case "TICTACTOE":
                            totalWins = ((Long) user.get("winsInTicTacToe")).intValue();
                            gamesPlayed = ((Long) user.get("gamesPlayedTicTacToe")).intValue();
                            break;
                        case "CHECKERS":
                            totalWins = ((Long) user.get("winsInCheckers")).intValue();
                            gamesPlayed = ((Long) user.get("gamesPlayedCheckers")).intValue();
                            break;
                        case "CONNECT4":
                            totalWins = ((Long) user.get("winsInConnect4")).intValue();
                            gamesPlayed = ((Long) user.get("gamesPlayedConnect4")).intValue();
                            break;
                    }

                    // Add these fields to the matchObject for display
                    matchObject.put("totalWins", totalWins);
                    matchObject.put("gamesPlayed", gamesPlayed);

                    // Add the updated matchObject to the TableView
                    playerRecentTableView.getItems().add(matchObject);
                }
            } else {
                System.out.println("User not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error displaying recent matches.");
        }
    }


    @FXML
    void onLeaderboardButtonClicked(ActionEvent event) throws IOException {
        navigateToScreen(event, "ProfileLeaderB.fxml");
    }

    @FXML
    void onGamesButtonClicked(ActionEvent event) throws IOException {
        navigateToScreen(event, "ProfileGame.fxml");
    }

    @FXML
    void onPlayerProfilesButtonClicked(ActionEvent event) throws IOException {
        navigateToScreen(event, "otherprofile.fxml");
    }

    @FXML
    void onlogOutButtonClicked(ActionEvent event) {
        System.out.println("Logging out...");
    }

    private void navigateToScreen(ActionEvent event, String fxml) throws IOException {
        Parent parent = FXMLLoader.load(HelloApplication.class.getResource(fxml));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
