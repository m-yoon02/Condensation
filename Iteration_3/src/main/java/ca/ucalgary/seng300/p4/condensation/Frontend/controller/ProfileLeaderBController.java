package ca.ucalgary.seng300.p4.condensation.Frontend.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProfileLeaderBController {

    @FXML
    private TableView<PlayerScore> ticTacToeTable;

    @FXML
    private TableView<PlayerScore> connect4Table;

    @FXML
    private TableView<PlayerScore> checkersTable;

    @FXML
    private TableColumn<PlayerScore, Integer> ticTacToeRankColumn;

    @FXML
    private TableColumn<PlayerScore, String> ticTacToePlayerColumn;

    @FXML
    private TableColumn<PlayerScore, Integer> ticTacToeScoreColumn;

    @FXML
    private TableColumn<PlayerScore, Integer> connect4RankColumn;

    @FXML
    private TableColumn<PlayerScore, String> connect4PlayerColumn;

    @FXML
    private TableColumn<PlayerScore, Integer> connect4ScoreColumn;

    @FXML
    private TableColumn<PlayerScore, Integer> checkersRankColumn;

    @FXML
    private TableColumn<PlayerScore, String> checkersPlayerColumn;

    @FXML
    private TableColumn<PlayerScore, Integer> checkersScoreColumn;

    public void initialize() {
        // Set up columns for Tic Tac Toe
        ticTacToeRankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        ticTacToePlayerColumn.setCellValueFactory(new PropertyValueFactory<>("player"));
        ticTacToeScoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        // Set up columns for Connect 4
        connect4RankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        connect4PlayerColumn.setCellValueFactory(new PropertyValueFactory<>("player"));
        connect4ScoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        // Set up columns for Checkers
        checkersRankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        checkersPlayerColumn.setCellValueFactory(new PropertyValueFactory<>("player"));
        checkersScoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        // Populate tables with sample data
        /*ObservableList<PlayerScore> sampleData = FXCollections.observableArrayList(
                new PlayerScore(1, "Alice", 100),
                new PlayerScore(2, "Bob", 90),
                new PlayerScore(3, "Charlie", 80)
        );

        ticTacToeTable.setItems(sampleData);
        connect4Table.setItems(sampleData);
        checkersTable.setItems(sampleData);  */
    }

    // Define a simple data class for PlayerScore
    public static class PlayerScore {
        private final Integer rank;
        private final String player;
        private final Integer score;

        public PlayerScore(Integer rank, String player, Integer score) {
            this.rank = rank;
            this.player = player;
            this.score = score;
        }

        public Integer getRank() {
            return rank;
        }

        public String getPlayer() {
            return player;
        }

        public Integer getScore() {
            return score;
        }
    }
}
