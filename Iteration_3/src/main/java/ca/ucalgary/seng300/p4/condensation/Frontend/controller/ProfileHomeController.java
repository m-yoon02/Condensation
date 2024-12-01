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

public class ProfileHomeController {

    @FXML
    private TableView<JSONObject> tableView;

    @FXML
    private TableColumn<JSONObject, String> gameColumn;

    @FXML
    private TableColumn<JSONObject, String> outcomeColumn;

    @FXML
    private TableColumn<JSONObject, Number> currentEloColumn;

    @FXML
    private TableColumn<JSONObject, Number> totalWinsColumn;

    @FXML
    private TableColumn<JSONObject, Number> gamesPlayedColumn;


    @FXML
    private TextField profileTitle;

    @FXML
    private Label usernameField;

    @FXML
    private Button logoutButton;

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button playerProfilesButton;

    @FXML
    private Button gamesButton;

    @FXML
    public void initialize() {
        // Set the column resize policy for the TableView
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        usernameField.setText("Player 1");

        gameColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("gameType")));
        outcomeColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("outcome")));
        currentEloColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(((Long) cellData.getValue().get("currentElo")).intValue()));
        totalWinsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(0)); // Placeholder if not in matches
        gamesPlayedColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(0)); // Placeholder if not in matches

        UUID playerUUID = UUID.fromString("345e6789-e89d-45d5-a678-627836285538"); // Replace with real UUID logic

        // Display recent matches for this player
        displayRecentMatches(playerUUID);
    }

    /**When this method is called, it will change the scene back to log in page.*/
    @FXML
    public void onlogOutButtonClicked(ActionEvent event) {
        System.out.println("Logging out...");
        // Add logout functionality here
    }

    //When this method is called, it will change the scene to leaderboard page.
    @FXML
    void onLeaderboardButtonClicked (ActionEvent event) throws IOException{
        System.out.println("Opening Leaderboard...");
        // Add functionality to open the leaderboard page
        Parent gamesParent = FXMLLoader.load(HelloApplication.class.getResource("ProfileLeaderB.fxml"));
        Scene gamesScene = new Scene(gamesParent);

        //This line gets the Stage information
        Stage window= (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(gamesScene);
        window.show();

    }

    //When this method is called, it will change the scene back to player profile page.
    @FXML
    void onPlayerProfilesButtonClicked(ActionEvent event) {

    }

    //When this method is called, it will change the scene back to game selection page.
    @FXML
    void onGamesButtonClicked(ActionEvent event) throws IOException {
        System.out.println("Opening Games...");
        // Add functionality to open the games page
        Parent gamesParent = FXMLLoader.load(HelloApplication.class.getResource("ProfileGame.fxml"));
        Scene gamesScene = new Scene(gamesParent);

        //This line gets the Stage information
        Stage window= (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(gamesScene);
        window.show();

    }

    public void displayRecentMatches(UUID playerUUID) {
        try {
            // Fetch the user from the Leaderboard
            Leaderboard leaderboard = Leaderboard.getInstance("/Users/mortezafaraji/Desktop/school/SENG300/seng300-f24-project/Iteration_3/src/main/java/ca/ucalgary/seng300/p4/condensation/Networking/Database/Leaderboard/Leaderboard.json");
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


