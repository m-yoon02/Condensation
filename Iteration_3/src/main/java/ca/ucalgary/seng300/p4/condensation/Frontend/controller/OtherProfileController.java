


//This doesn't run the program, but I tried to code the way you did.
//all the other profile scenes don't retrieve data from JSON file(leaderboard class). File directory problem. Tried to change them but didn't work.

/*package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;

public class OtherProfileController {

    @FXML
    private TableView<JSONObject> playerTableView;

    @FXML
    private TableColumn<JSONObject, String> usernameColumn;

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

    //@FXML
    //private TableColumn<JSONObject, String> statusColumn;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Text statusMessage;

    @FXML
    private Button searchButton;

    @FXML
    private Button homeButton;

    private Leaderboard leaderboard;

    @FXML
    public void initialize() {
        // Configure the table columns
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("username")));
        gameColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("gameType")));
        outcomeColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue().get("outcome")));
        currentEloColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(((Long) cellData.getValue().get("currentElo")).intValue()));
        totalWinsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(((Long) cellData.getValue().getOrDefault("totalWins", 0)).intValue()));
        gamesPlayedColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(((Long) cellData.getValue().getOrDefault("gamesPlayed", 0)).intValue()));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(getStatus((Long) cellData.getValue().get("currentElo"))));

        try {
            // Initialize the leaderboard instance
            leaderboard = Leaderboard.getInstance("src/main/java/ca/ucalgary/seng300/p4/condensation/Networking/Database/Leaderboard/Leaderboard.json");
        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
            statusMessage.setText("Error initializing leaderboard.");
        }
    }

    @FXML
    public void onSearchButtonClicked(ActionEvent event) {
        String username = usernameTextField.getText().trim();
        if (username.isEmpty()) {
            statusMessage.setText("Please enter a username.");
            return;
        }

        try {
            JSONObject user = leaderboard.findUserByName(username);
            if (user == null) {
                statusMessage.setText("User not found.");
                playerTableView.getItems().clear();
                return;
            }


        } catch (Exception e) {
            e.printStackTrace();
            statusMessage.setText("Error retrieving user data.");
        }
    }



    /*private String getStatus(Long elo) {
        TODO: connect with networking
    }*/

 /*   @FXML
    void onHomeButtonClicked(ActionEvent event) throws IOException {
        Parent homeParent = FXMLLoader.load(HelloApplication.class.getResource("ProfileHome.fxml"));
        Scene homeScene = new Scene(homeParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(homeScene);
        window.show();
    }
}
*/



//this one runs the application but cannot retrieve the data. error says file directory problem
//Some codes are hard coded and it isn't done so data won't be fully retrieved. But you can see the design of this scene by running the application.

package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
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
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class OtherProfileController {

    @FXML
    private TableView<PlayerProfile> playerTableView;

    @FXML
    private TableColumn<PlayerProfile, String> playerColumn;
    @FXML
    private TableColumn<PlayerProfile, String> gameColumn;
    @FXML
    private TableColumn<PlayerProfile, String> outcomeColumn;
    @FXML
    private TableColumn<PlayerProfile, String> currentEloColumn;
    @FXML
    private TableColumn<PlayerProfile, String> totalWinsColumn;
    @FXML
    private TableColumn<PlayerProfile, String> gamesPlayedColumn;
    @FXML
    private TableColumn<PlayerProfile, String> statusColumn;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Text statusMessage;

    private final ObservableList<PlayerProfile> playerData = FXCollections.observableArrayList();
    private Leaderboard leaderboard;

    @FXML
    public void initialize() {
        // Initialize columns with cell value factories
        playerColumn.setCellValueFactory(data -> data.getValue().playerNameProperty());
        gameColumn.setCellValueFactory(data -> data.getValue().gameNameProperty());
        outcomeColumn.setCellValueFactory(data -> data.getValue().outcomeProperty());
        currentEloColumn.setCellValueFactory(data -> data.getValue().currentEloProperty());
        totalWinsColumn.setCellValueFactory(data -> data.getValue().totalWinsProperty());
        gamesPlayedColumn.setCellValueFactory(data -> data.getValue().gamesPlayedProperty());
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());

        // Initialize the leaderboard
        try {
            leaderboard = Leaderboard.getInstance("src/main/java/ca.ucalgary.seng300.p4.condensation/Networking/Database.Leaderboard/Leaderboard.json");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            statusMessage.setText("Error initializing leaderboard.");
        }
    }

    @FXML
    void onSearchButtonClicked(ActionEvent event) {
        String username = usernameTextField.getText().trim();
        if (username.isEmpty()) {
            statusMessage.setText("Please enter a username to search.");
            return;
        }

        JSONObject user = leaderboard.findUserByName(username);
        if (user == null) {
            statusMessage.setText("User not found.");
            playerData.clear();
            playerTableView.setItems(playerData);
            return;
        }

        statusMessage.setText("User found!");
        playerData.clear();
        playerData.add(parsePlayer(user));
        playerTableView.setItems(playerData);
    }

    private PlayerProfile parsePlayer(JSONObject user) {
        String playerName = (String) user.get("username");
        String gameName = "Connect4"; // Hardcoded; can be dynamic based on data
        String outcome = "Win/Loss Placeholder"; // Placeholder; implement logic
        String currentElo = user.get("connect4Elo").toString();
        String totalWins = user.get("winsInConnect4").toString();
        String gamesPlayed = user.get("gamesPlayedConnect4").toString();
        String status = Integer.parseInt(currentElo) > 1500 ? "Online" : "Offline";

        return new PlayerProfile(playerName, gameName, outcome, currentElo, totalWins, gamesPlayed, status);
    }

    @FXML
    void onHomeButtonClicked(ActionEvent event) throws IOException {
        Parent gamesParent = FXMLLoader.load(HelloApplication.class.getResource("ProfileHome.fxml"));
        Scene gamesScene = new Scene(gamesParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(gamesScene);
        window.show();
    }

    public static class PlayerProfile {
        private final SimpleStringProperty playerName;
        private final SimpleStringProperty gameName;
        private final SimpleStringProperty outcome;
        private final SimpleStringProperty currentElo;
        private final SimpleStringProperty totalWins;
        private final SimpleStringProperty gamesPlayed;
        private final SimpleStringProperty status;

        public PlayerProfile(String playerName, String gameName, String outcome, String currentElo, String totalWins, String gamesPlayed, String status) {
            this.playerName = new SimpleStringProperty(playerName);
            this.gameName = new SimpleStringProperty(gameName);
            this.outcome = new SimpleStringProperty(outcome);
            this.currentElo = new SimpleStringProperty(currentElo);
            this.totalWins = new SimpleStringProperty(totalWins);
            this.gamesPlayed = new SimpleStringProperty(gamesPlayed);
            this.status = new SimpleStringProperty(status);
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
}
