package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.IOException;

/**
 * Controller for the Leaderboard screen.
 * Displays leaderboards for Tic Tac Toe, Connect 4, and Checkers.
 * Each leaderboard includes player rankings, usernames, and scores (ELO).
 */
public class ProfileLeaderBController {

    // Leaderboards for each game
    @FXML
    private TableView<org.json.simple.JSONObject> ticTacToeTable;
    @FXML
    private TableView<org.json.simple.JSONObject> connect4Table;
    @FXML
    private TableView<org.json.simple.JSONObject> checkersTable;

    // Columns for Tic Tac Toe leaderboard
    @FXML
    private TableColumn<JSONObject, Integer> ticTacToeRankColumn;
    @FXML
    private TableColumn<JSONObject, String> ticTacToePlayerColumn;
    @FXML
    private TableColumn<JSONObject, Integer> ticTacToeScoreColumn;

    // Columns for Connect 4 leaderboard
    @FXML
    private TableColumn<JSONObject, Integer> connect4RankColumn;
    @FXML
    private TableColumn<JSONObject, String> connect4PlayerColumn;
    @FXML
    private TableColumn<JSONObject, Integer> connect4ScoreColumn;

    // Columns for Checkers leaderboard
    @FXML
    private TableColumn<JSONObject, Integer> checkersRankColumn;
    @FXML
    private TableColumn<JSONObject, String> checkersPlayerColumn;
    @FXML
    private TableColumn<JSONObject, Integer> checkersScoreColumn;

    /**
     * Initializes the controller and sets up leaderboard data.
     * Configures column bindings for all three leaderboards and loads data.
     */
    @FXML
    public void initialize() {
        // Configure Tic Tac Toe columns
        ticTacToeRankColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(((Integer) cellData.getValue().get("rank")).intValue()).asObject());
        ticTacToePlayerColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty((String) cellData.getValue().get("username")));
        ticTacToeScoreColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(((Long) cellData.getValue().get("tictactoeElo")).intValue()).asObject());

        // Configure Connect 4 columns
        connect4RankColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(((Integer) cellData.getValue().get("rank")).intValue()).asObject());
        connect4PlayerColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty((String) cellData.getValue().get("username")));
        connect4ScoreColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(((Long) cellData.getValue().get("connect4Elo")).intValue()).asObject());

        // Configure Checkers columns
        checkersRankColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(((Integer) cellData.getValue().get("rank")).intValue()).asObject());
        checkersPlayerColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty((String) cellData.getValue().get("username")));
        checkersScoreColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(((Long) cellData.getValue().get("checkersElo")).intValue()).asObject());

        // Load leaderboard data dynamically
        loadLeaderboardData();
    }

    /**
     * Loads and populates leaderboard data for Tic Tac Toe, Connect 4, and Checkers.
     * Data is fetched from the Leaderboard JSON file and sorted by ELO.
     * Ranks are assigned dynamically after sorting.
     */
    private void loadLeaderboardData() {
        try {
            // Get the leaderboard instance
            Leaderboard leaderboard = Leaderboard.getInstance("/Users/mortezafaraji/Desktop/school/SENG300/seng300-f24-project/Iteration_3/src/main/java/ca/ucalgary/seng300/p4/condensation/Networking/Database/Leaderboard/Leaderboard.json");

            // Create observable lists for each game
            ObservableList<org.json.simple.JSONObject> ticTacToeData = FXCollections.observableArrayList();
            ObservableList<org.json.simple.JSONObject> connect4Data = FXCollections.observableArrayList();
            ObservableList<org.json.simple.JSONObject> checkersData = FXCollections.observableArrayList();

            // Populate the data with deep copies
            leaderboard.getAllUsers().forEach(user -> {
                JSONObject userObject = (JSONObject) user;

                // Create deep copies for each list
                ticTacToeData.add(new JSONObject(userObject));
                connect4Data.add(new JSONObject(userObject));
                checkersData.add(new JSONObject(userObject));
            });

            // Sort the data by score for each game
            ticTacToeData.sort((o1, o2) -> Long.compare((Long) o2.get("tictactoeElo"), (Long) o1.get("tictactoeElo")));
            for (int i = 0; i < ticTacToeData.size(); i++) {
                ticTacToeData.get(i).put("rank", i + 1); // Rank starts at 1
            }

            connect4Data.sort((o1, o2) -> Long.compare((Long) o2.get("connect4Elo"), (Long) o1.get("connect4Elo")));
            for (int i = 0; i < connect4Data.size(); i++) {
                connect4Data.get(i).put("rank", i + 1); // Rank starts at 1
            }

            checkersData.sort((o1, o2) -> Long.compare((Long) o2.get("checkersElo"), (Long) o1.get("checkersElo")));
            for (int i = 0; i < checkersData.size(); i++) {
                checkersData.get(i).put("rank", i + 1); // Rank starts at 1
            }

            // Set the data to the tables
            ticTacToeTable.setItems(ticTacToeData);
            connect4Table.setItems(connect4Data);
            checkersTable.setItems(checkersData);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading leaderboard data.");
        }
    }


    /**
     * Handles the "Home" button click event.
     * Navigates back to the Profile Home screen.
     *
     * @param event ActionEvent triggered by the "Home" button.
     * @throws IOException if the FXML file for the Profile Home screen cannot be loaded.
     */
    @FXML
    void onHomeButtonClicked(ActionEvent event) throws IOException {
        System.out.println("Home button clicked...redirecting to home.");
        Parent homeParent = FXMLLoader.load(HelloApplication.class.getResource("ProfileHome.fxml"));
        Scene homeScene = new Scene(homeParent);

        // Get the current stage and set the new scene
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(homeScene);
        window.show();
    }
}
