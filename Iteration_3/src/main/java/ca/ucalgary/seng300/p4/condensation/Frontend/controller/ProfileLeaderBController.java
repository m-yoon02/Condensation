package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Frontend.leaderboard.Leaderboard;
import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;

public class ProfileLeaderBController {

    @FXML
    private TableView<LeaderboardEntry> ticTacToeTable;

    @FXML
    private TableView<LeaderboardEntry> connect4Table;

    @FXML
    private TableView<LeaderboardEntry> checkersTable;

    @FXML
    private TableColumn<LeaderboardEntry, Integer> ticTacToeRankColumn;

    @FXML
    private TableColumn<LeaderboardEntry, String> ticTacToePlayerColumn;

    @FXML
    private TableColumn<LeaderboardEntry, Integer> ticTacToeScoreColumn;

    @FXML
    private TableColumn<LeaderboardEntry, Integer> connect4RankColumn;

    @FXML
    private TableColumn<LeaderboardEntry, String> connect4PlayerColumn;

    @FXML
    private TableColumn<LeaderboardEntry, Integer> connect4ScoreColumn;

    @FXML
    private TableColumn<LeaderboardEntry, Integer> checkersRankColumn;

    @FXML
    private TableColumn<LeaderboardEntry, String> checkersPlayerColumn;

    @FXML
    private TableColumn<LeaderboardEntry, Integer> checkersScoreColumn;


    // Initialize method for setting up the leaderboard data
    @FXML
    public void initialize() {
        // Set up table columns
        ticTacToeRankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        ticTacToePlayerColumn.setCellValueFactory(new PropertyValueFactory<>("player"));
        ticTacToeScoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        connect4RankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        connect4PlayerColumn.setCellValueFactory(new PropertyValueFactory<>("player"));
        connect4ScoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        checkersRankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        checkersPlayerColumn.setCellValueFactory(new PropertyValueFactory<>("player"));
        checkersScoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        // Load sample data
        loadSampleData();  //TODO: Should be connected with the networking team
    }

    //this is just the sample data to see how the table looks like  //TODO: connect with networking
    private void loadSampleData() {
        ObservableList<LeaderboardEntry> ticTacToeData = FXCollections.observableArrayList(
                new LeaderboardEntry(1, "A", 100),
                new LeaderboardEntry(2, "B", 80),
                new LeaderboardEntry(3, "C", 60),
                new LeaderboardEntry(4, "D", 95),
                new LeaderboardEntry(5, "E", 85)
        );

        ObservableList<LeaderboardEntry> connect4Data = FXCollections.observableArrayList(
                new LeaderboardEntry(1, "F", 85),
                new LeaderboardEntry(2, "G", 75),
                new LeaderboardEntry(3, "H", 100),
                new LeaderboardEntry(4, "I", 90),
                new LeaderboardEntry(5, "J", 85)
        );

        ObservableList<LeaderboardEntry> checkersData = FXCollections.observableArrayList(
                new LeaderboardEntry(1, "K", 100),
                new LeaderboardEntry(2, "L", 90),
                new LeaderboardEntry(3, "M", 70),
                new LeaderboardEntry(4, "N", 85),
                new LeaderboardEntry(5, "O", 85)
        );

        // Set data to tables
        ticTacToeTable.setItems(ticTacToeData);
        connect4Table.setItems(connect4Data);
        checkersTable.setItems(checkersData);
    }

    //back to home
    @FXML
    void onHomeButtonClicked(ActionEvent event) throws IOException {
        System.out.println("Home button clicked..redirecting to home.");
        // Navigate back to the Home screen
        // Add functionality to go back to home
        Parent gamesParent = FXMLLoader.load(HelloApplication.class.getResource("ProfileHome.fxml"));
        Scene gamesScene = new Scene(gamesParent);

        //This line gets the Stage information
        Stage window= (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(gamesScene);
        window.show();
    }

    // Helper class for leaderboard entries
    public static class LeaderboardEntry {
        private final int rank;
        private final String player;
        private final int score;

        public LeaderboardEntry(int rank, String player, int score) {
            this.rank = rank;
            this.player = player;
            this.score = score;
        }

        public int getRank() {
            return rank;
        }

        public String getPlayer() {
            return player;
        }

        public int getScore() {
            return score;
        }
    }
}

/*package ca.ucalgary.seng300.p4.condensation.Frontend.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ProfileLeaderBController {

    @FXML
    private Button btnHome;

    @FXML
    private TableView<?> tableView1;

    @FXML
    private TableView<?> tableView2;

    @FXML
    private TableView<?> tableView3;

    @FXML
    private TableColumn<?, ?> colRank1;

    @FXML
    private TableColumn<?, ?> colPlayer1;

    @FXML
    private TableColumn<?, ?> colScore1;

    @FXML
    private TableColumn<?, ?> colRank2;

    @FXML
    private TableColumn<?, ?> colPlayer2;

    @FXML
    private TableColumn<?, ?> colScore2;

    @FXML
    private TableColumn<?, ?> colRank3;

    @FXML
    private TableColumn<?, ?> colPlayer3;

    @FXML
    private TableColumn<?, ?> colScore3;

    @FXML
    private Label lblLeaderboard;

    @FXML
    private Label lblTicTacToe;

    @FXML
    private Label lblCheckers;

    @FXML
    private Label lblConnect4;

    @FXML
    private void initialize() {
        // Initialization logic here
        lblLeaderboard.setText("Leaderboard");
        lblTicTacToe.setText("TIC TAC TOE");
        lblCheckers.setText("CHECKERS");
        lblConnect4.setText("CONNECT 4");
    }

    @FXML
    private void handleHome() {
        System.out.println("Home button clicked!");
        // Add logic to handle the Home button click, like navigating to the home screen
    }
}
*/