package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
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
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;


/**
 * Controller class for the "Other Profile" screen.
 * This screen allows users to search for a player's profile by username.
 * Displays player's online/offline status.
 * Displays two tables:
 * 1. Player overall stats for games like Connect4, Checkers, and TicTacToe.
 * 2. Player's recent matches for any game.
 */
public class OtherProfileController {

    // Upper table for displaying overall statistics
    @FXML
    private TableView<PlayerProfile> playerTableView;
    @FXML
    private TableColumn<PlayerProfile, String> gameColumn;
    @FXML
    private TableColumn<PlayerProfile, String> currentEloColumn;
    @FXML
    private TableColumn<PlayerProfile, String> totalWinsColumn;

    // Lower table for displaying recent matches
    @FXML
    private TableView<JSONObject> playerRecentTableView;
    @FXML
    private TableColumn<JSONObject, String> playerRecentgameColumn;
    @FXML
    private TableColumn<JSONObject, String> playerRecentoutcomeColumn;
    @FXML
    private TableColumn<JSONObject, Number> playerRecentcurrentEloColumn;
    @FXML
    private TableColumn<JSONObject, Number> playerRecenttotalWinsColumn;
    @FXML
    private TableColumn<JSONObject, Number> playerRecentgamesPlayedColumn;

    // TextField and labels for input and messages
    @FXML
    private TextField usernameTextField;
    @FXML
    private Text statusMessage; // Displays messages to the user
    @FXML
    private Text playerStatus; // Displays the online/offline status of the player
    @FXML
    private Button challengeButton;

    // Data models for the tables
    private final ObservableList<PlayerProfile> overallStatsData = FXCollections.observableArrayList();
    private final ObservableList<PlayerMatch> recentMatchesData = FXCollections.observableArrayList();
    private Leaderboard leaderboard; // Singleton instance of the leaderboard

    /**
     * Initializes the controller.
     * Sets up bindings between the table columns and their corresponding data properties.
     */
    @FXML
    public void initialize() {
        // Bind overall stats columns to PlayerProfile properties
        gameColumn.setCellValueFactory(data -> data.getValue().gameNameProperty());
        currentEloColumn.setCellValueFactory(data -> data.getValue().currentEloProperty());
        totalWinsColumn.setCellValueFactory(data -> data.getValue().totalWinsProperty());

        // Bind recent matches columns to JSONObject properties
        playerRecentgameColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("gameType")));
        playerRecentoutcomeColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("outcome")));
        playerRecentcurrentEloColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(((Long) cellData.getValue().get("currentElo")).intValue()));
        playerRecenttotalWinsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(((Number) cellData.getValue().get("totalWins")).intValue()));
        playerRecentgamesPlayedColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(((Number) cellData.getValue().get("gamesPlayed")).intValue()));

        // Initialize the leaderboard singleton instance
        try {
            leaderboard = Leaderboard.getInstance();
        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
            statusMessage.setText("Error initializing leaderboard.");
        }
    }

    /**
     * Handles the event when the Search button is clicked.
     * Searches for a user by their username and updates the overall stats and recent matches tables.
     *
     * @param event The ActionEvent triggered by clicking the Search button.
     * @throws IOException    If there is an error during network operations.
     * @throws ParseException If there is an error parsing JSON data.
     */
    @FXML
    void onSearchButtonClicked(ActionEvent event) throws IOException, ParseException {
        // Get the entered username
        String username = usernameTextField.getText().trim();

        if (username.isEmpty()) {
            statusMessage.setText("Please enter a username to search.");
            return;
        }

        // Search for the user in the leaderboard
        JSONObject user = leaderboard.findUserByName(username);
        if (user == null) {
            statusMessage.setText("User not found.");
            return;
        }

        statusMessage.setText("User found!");
        overallStatsData.clear(); // Clear previous data
        recentMatchesData.clear();

        // Update player's online/offline status
        String status = getPlayerStatus(user);
        playerStatus.setText(status);

        // Populate the overall stats table
        overallStatsData.add(parsePlayer(user, "Connect4"));
        overallStatsData.add(parsePlayer(user, "Checkers"));
        overallStatsData.add(parsePlayer(user, "TicTacToe"));

        // Set data for the overall stats table view
        playerTableView.setItems(overallStatsData);

        // Populate the recent matches table
        displayRecentMatches(username);
    }


    private void navigateToScreen(ActionEvent event, String fxml) throws IOException {

        switch(fxml){
            case "checkersqueue.fxml" -> {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersqueue.fxml"));
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                CheckersQueueController checkersQueueController = fxmlLoader.getController();
                window.setScene(scene);
                window.show();
                checkersQueueController.start();
            }
            case "tictactoequeue.fxml" -> {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tictactoequeue.fxml"));
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                TicTacToeQueueController ticTacToeQueueController = fxmlLoader.getController();
                window.setScene(scene);
                window.show();
                ticTacToeQueueController.start();
            }
            case "connectfourqueue.fxml" -> {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("connectfourqueue.fxml"));
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                ConnectFourQueueController connectFourQueueController = fxmlLoader.getController();
                window.setScene(scene);
                window.show();
                connectFourQueueController.start();
            }
        }


    }

    private String getCorrectQueueFXMLPlayer(GameTypes gameType){

        String queueFXML = null;
        switch(gameType){
            case CHECKERS -> {
                queueFXML = "checkersqueue.fxml";
            }
            case TICTACTOE -> {
                queueFXML = "tictactoequeue.fxml";
            }
            case CONNECT4 -> {
                queueFXML = "connectfourqueue.fxml";
            }
        }

        return queueFXML;
    }


    // this

    public void onChallengeClick(ActionEvent event) throws IOException, ParseException {

        String playerUser = usernameTextField.getText().strip();
        FileReader reader = new FileReader(NetworkManager.PLAYER_ID_FILE);
        JSONParser jsonParser = new JSONParser();
        JSONObject loginUser = (JSONObject) jsonParser.parse(reader);
        String uuid  = (String) loginUser.get("playerID");
        UUID uuidToString = UUID.fromString(uuid);


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Challenging Player");
        alert.setContentText("Challenging " + playerUser + "...");
        alert.showAndWait();

        ButtonType connect4Button = new ButtonType("Connect4");
        ButtonType checkersButton = new ButtonType("Checkers");
        ButtonType ticTacToeButton = new ButtonType("TicTacToe");
        ButtonType cancelButton = ButtonType.CANCEL;

        // Add buttons to the alert
        alert.getButtonTypes().setAll(connect4Button, checkersButton, ticTacToeButton, cancelButton);


        // Wait for user selection
        alert.showAndWait().ifPresent(response -> {
            GameTypes gameTypes;
            try {
                if (response == connect4Button) {
                    gameTypes = GameTypes.CONNECT4;
                    JSONObject payload = makeChallengePayload(gameTypes, uuidToString, playerUser);
                    NetworkManager.getNetworkInstance().handleChallengedQueuePlayer(payload);

                    String fxml = getCorrectQueueFXMLPlayer(GameTypes.CONNECT4);
                    navigateToScreen(event, fxml); // Pass the original event

                } else if (response == checkersButton) {
                    gameTypes = GameTypes.CHECKERS;
                    JSONObject payload = makeChallengePayload(gameTypes, uuidToString, playerUser);
                    NetworkManager.getNetworkInstance().handleChallengedQueuePlayer(payload);

                    String fxml = getCorrectQueueFXMLPlayer(GameTypes.CHECKERS);
                    navigateToScreen(event, fxml); // Pass the original event

                } else if (response == ticTacToeButton) {
                    gameTypes = GameTypes.TICTACTOE;
                    JSONObject payload = makeChallengePayload(gameTypes, uuidToString, playerUser);
                    NetworkManager.getNetworkInstance().handleChallengedQueuePlayer(payload);

                    String fxml = getCorrectQueueFXMLPlayer(GameTypes.TICTACTOE);
                    navigateToScreen(event, fxml); // Pass the original event

                } else if (response == cancelButton) {
                    System.out.println("Challenge canceled.");
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                throw new RuntimeException("Error navigating to the queue screen.");
            }
        });


    }


    public JSONObject makeChallengePayload(GameTypes gameTypes, UUID uuid, String username) {
        JSONObject payload = new JSONObject();
        payload.put("Player1ID", uuid);
        payload.put("Player2Username", username);
        payload.put("gameType", gameTypes);

        return  payload;
    }

    /**
     * Retrieves the player's online or offline status.
     *
     * @param user The JSON object representing the player.
     * @return A string representing the player's status ("Online" or "Offline").
     * @throws IOException    If there is an error during network operations.
     * @throws ParseException If there is an error parsing JSON data.
     */
    private String getPlayerStatus(JSONObject user) throws IOException, ParseException {
        UUID playerID = UUID.fromString((String) user.get("playerUUID"));
        boolean isOnline = NetworkManager.getNetworkInstance().isPlayerOnline(playerID);
        return isOnline ? "Online" : "Offline";
    }

    /**
     * Parses a player's overall stats for a specific game.
     *
     * @param user     The JSON object representing the player.
     * @param gameName The name of the game to parse stats for.
     * @return A PlayerProfile object containing the parsed stats.
     */
    private PlayerProfile parsePlayer(JSONObject user, String gameName) {
        String playerName = (String) user.get("username");
        String currentElo = user.get(gameName.toLowerCase() + "Elo").toString();
        String totalWins = user.get("winsIn" + gameName).toString();
        String gamesPlayed = user.get("gamesPlayed" + gameName).toString();
        return new PlayerProfile(playerName, gameName, currentElo, totalWins, gamesPlayed);
    }

    /**
     * Populates the recent matches table for the specified player.
     *
     * @param username The username of the player.
     */
    public void displayRecentMatches(String username) {
        try {
            JSONObject user = leaderboard.findUserByName(username);

            if (user != null) {
                JSONArray recentMatches = (JSONArray) user.get("recentMatches");
                playerRecentTableView.getItems().clear();

                // Populate the recent matches table
                for (Object match : recentMatches) {
                    JSONObject matchObject = (JSONObject) match;
                    String gameType = (String) matchObject.get("gameType");

                    int totalWins = 0;
                    int gamesPlayed = 0;

                    // Update match stats based on the game type
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

                    // Add the match object to the table
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
     * Handles the Home button click event to navigate back to the Profile Home scene.
     *
     * @param event The ActionEvent triggered by clicking the Home button.
     * @throws IOException If there is an error loading the FXML file.
     */
    @FXML
    void onHomeButtonClicked(ActionEvent event) throws IOException {
        Parent homeParent = FXMLLoader.load(HelloApplication.class.getResource("ProfileHome.fxml"));
        Scene homeScene = new Scene(homeParent);
        HelloApplication.setActiveScene(homeScene);
    }

    /**
     * Represents a player's profile statistics for a specific game.
     */
    public static class PlayerProfile {
        private final SimpleStringProperty playerName;
        private final SimpleStringProperty gameName;
        private final SimpleStringProperty currentElo;
        private final SimpleStringProperty totalWins;
        private final SimpleStringProperty gamesPlayed;

        public PlayerProfile(String playerName, String gameName, String currentElo, String totalWins, String gamesPlayed) {
            this.playerName = new SimpleStringProperty(playerName);
            this.gameName = new SimpleStringProperty(gameName);
            this.currentElo = new SimpleStringProperty(currentElo);
            this.totalWins = new SimpleStringProperty(totalWins);
            this.gamesPlayed = new SimpleStringProperty(gamesPlayed);
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
    }

    /**
     * Represents a player's match details, including the game name, outcome, and current ELO.
     */
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
