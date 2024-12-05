package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
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
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

/**
 * Controller for the Profile Home screen in the application.
 * Manages the user's profile overview, recent matches, and navigation to other screens.
 */
public class ProfileHomeController {

    // FXML-bound UI elements
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

    /**
     * Initializes the controller and populates tables with player data.
     * This method is automatically called after the FXML elements are loaded.
     */
    @FXML
    public void initialize() {
        // Configure table columns for overall stats
        gameColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("gameType")));
        currentEloColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("elo").toString()));
        totalWinsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("totalWins").toString()));

        // Configure table columns for recent matches
        playerRecentgameColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("gameType")));
        playerRecentoutcomeColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("outcome")));
        playerRecentcurrentEloColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("currentElo").toString()));
        playerRecenttotalWinsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("totalWins").toString()));
        playerRecentgamesPlayedColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("gamesPlayed").toString()));

        // Set username and initialize leaderboard
        try {
            String usernameOfPlayer = Leaderboard.getInstance().fetchUserFromNewPlayerJson();
            usernameField.setText(usernameOfPlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize the leaderboard instance
        try {
            leaderboard = Leaderboard.getInstance();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        try {
            FileReader reader = new FileReader(NetworkManager.PLAYER_ID_FILE);
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(reader);
            String playerIDString = (String) object.get("playerID");
            UUID playerID = UUID.fromString(playerIDString);
            String username = (String) object.get("username");

            displayPlayerStats(playerID);
            displayRecentMatches(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the player's overall statistics in the stats table.
     * @param playerUUID UUID of the player to display stats for.
     */
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

    /**
     * Creates a JSON object representing a player's game statistics.
     * @param user JSON object containing the user's data.
     * @param gameType Name of the game.
     * @param eloKey Key for the ELO rating in the JSON.
     * @param winsKey Key for the total wins in the JSON.
     * @param gamesKey Key for the total games played in the JSON.
     * @return A JSON object containing the game statistics.
     */
    private JSONObject createGameStats(JSONObject user, String gameType, String eloKey, String winsKey, String gamesKey) {
        JSONObject stats = new JSONObject();
        stats.put("gameType", gameType);
        stats.put("elo", user.get(eloKey));
        stats.put("totalWins", user.get(winsKey));
        stats.put("gamesPlayed", user.get(gamesKey));
        stats.put("status", "Online"); // Add status logic if required
        return stats;
    }

    /**
     * Displays the player's recent matches in the recent matches table.
     * @param username Name of the player.
     */
    public void displayRecentMatches(String username) {
        try {
            Leaderboard leaderboard = Leaderboard.getInstance();
            JSONObject user = leaderboard.findUserByName(username);
            System.out.println("User gotten from profile home" + user);

            if (user != null) {
                JSONArray recentMatches = (JSONArray) user.get("recentMatches");
                playerRecentTableView.getItems().clear();

                for (Object match : recentMatches) {
                    JSONObject matchObject = (JSONObject) match;

                    GameTypes gameType =  GameTypes.valueOf((String) matchObject.get("gameType"));

                    int totalWins = 0;
                    int gamesPlayed = 0;

                    switch (gameType) {
                        case TICTACTOE:
                            totalWins = ((Number) user.get("winsInTicTacToe")).intValue();
                            gamesPlayed = ((Number) user.get("gamesPlayedTicTacToe")).intValue();
                            break;
                        case CHECKERS:
                            totalWins = ((Number) user.get("winsInCheckers")).intValue();
                            gamesPlayed = ((Number) user.get("gamesPlayedCheckers")).intValue();
                            break;
                        case CONNECT4:
                            totalWins = ((Number) user.get("winsInConnect4")).intValue();
                            gamesPlayed = ((Number) user.get("gamesPlayedConnect4")).intValue();
                            break;
                    }

                    matchObject.put("totalWins", totalWins);
                    matchObject.put("gamesPlayed", gamesPlayed);

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

    /**
     * Handles the leaderboard button click event and navigates to the leaderboard screen.
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @FXML
    void onLeaderboardButtonClicked(ActionEvent event) throws IOException {
        navigateToScreen(event, "ProfileLeaderB.fxml");
    }

    /**
     * Handles the games button click event and navigates to the games screen.
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @FXML
    void onGamesButtonClicked(ActionEvent event) throws IOException {
        navigateToScreen(event, "ProfileGame.fxml");
    }

    /**
     * Handles the player profiles button click event and navigates to the player profiles screen.
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @FXML
    void onPlayerProfilesButtonClicked(ActionEvent event) throws IOException {
        navigateToScreen(event, "otherprofile.fxml");
    }

    /**
     * Handles the logout button click event, logs the player out, and navigates to the welcome screen.
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    void onlogOutButtonClicked(ActionEvent event) {
        System.out.println("Logged out");
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(NetworkManager.PLAYER_ID_FILE));

            String playerID = (String) jsonObject.get("playerID");
            UUID playerUUID = UUID.fromString(playerID);

            NetworkManager.getNetworkInstance().logoutHandler(playerUUID);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/ucalgary/seng300/p4/condensation/WelcomeScreen.fxml"));
            Pane root = loader.load();

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            window.setScene(scene);
            window.setResizable(false);
            window.show();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onEditUserButtonClicked(ActionEvent event) throws IOException, ParseException {
        // Load the FXML for editing the username
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editUser.fxml"));
        Parent root = fxmlLoader.load();

        // Create a new Stage for the popup
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // Make it a modal dialog
        stage.setTitle("Edit Username");

        // Set the scene for the popup
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();


        FileReader reader = new FileReader(NetworkManager.PLAYER_ID_FILE);
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(reader);
        String playerIDString = (String) object.get("playerID");
        EditUserController.setPlayerUUID(UUID.fromString(playerIDString));

    }

    @FXML
    void onEditPassButtonClicked(ActionEvent event) throws IOException, ParseException {
        // Load the FXML for editing the username
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editPass.fxml"));
        Parent root = fxmlLoader.load();

        // Create a new Stage for the popup
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // Make it a modal dialog
        stage.setTitle("Edit Password");

        // Set the scene for the popup
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();


        FileReader reader = new FileReader(NetworkManager.PLAYER_ID_FILE);
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(reader);
        String playerIDString = (String) object.get("playerID");
        EditPassController.setPlayerUUID(UUID.fromString(playerIDString));
    }


    /**
     * Navigates to the specified screen.
     * @param event The ActionEvent triggered by the navigation.
     * @param fxml The FXML file to load for the new screen.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    private void navigateToScreen(ActionEvent event, String fxml) throws IOException {
        Parent parent = FXMLLoader.load(HelloApplication.class.getResource(fxml));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
//    public static void setUsername(String username) {
//        ProfileHomeController.usernameField.setText();
//    }
}
