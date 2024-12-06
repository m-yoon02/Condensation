package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class tLeaderboardController {

    @FXML
    private TableView<Player> leaderboardTable;

    @FXML
    private TableColumn<Player, Integer> rankColumn;

    @FXML
    private TableColumn<Player, String> nameColumn;

    @FXML
    private TableColumn<Player, Integer> gamesPlayedColumn;

    @FXML
    private TableColumn<Player, Integer> winsColumn;

    @FXML
    private TableColumn<Player, Integer> lossesColumn;

    @FXML
    private TableColumn<Player, Integer> drawsColumn;

    @FXML
    private TableColumn<Player, Double> winPercentageColumn;

    private ObservableList<Player> playerList;



    @FXML
    public void initialize() throws IOException, ParseException {
        // Bind each column's preferred width to a percentage of the table's total width
        rankColumn.prefWidthProperty().bind(leaderboardTable.widthProperty().multiply(0.05));
        nameColumn.prefWidthProperty().bind(leaderboardTable.widthProperty().multiply(0.4));
        gamesPlayedColumn.prefWidthProperty().bind(leaderboardTable.widthProperty().multiply(0.1));
        winsColumn.prefWidthProperty().bind(leaderboardTable.widthProperty().multiply(0.1));
        lossesColumn.prefWidthProperty().bind(leaderboardTable.widthProperty().multiply(0.1));
        drawsColumn.prefWidthProperty().bind(leaderboardTable.widthProperty().multiply(0.1));
        winPercentageColumn.prefWidthProperty().bind(leaderboardTable.widthProperty().multiply(0.15));

        // Initialize the columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        gamesPlayedColumn.setCellValueFactory(new PropertyValueFactory<>("gamesPlayedTicTacToe"));
        winsColumn.setCellValueFactory(new PropertyValueFactory<>("winsInTicTacToe"));
        lossesColumn.setCellValueFactory(new PropertyValueFactory<>("lossInTicTacToe"));

        // Center-align all numeric columns
        TableColumn<?, ?>[] numericColumns = {
                rankColumn, gamesPlayedColumn, winsColumn, lossesColumn, drawsColumn, winPercentageColumn
        };

        for (TableColumn<?, ?> column : numericColumns) {
            column.setStyle("-fx-alignment: CENTER;"); // Center-align the whole column
        }


        // Calculate draws
        drawsColumn.setCellValueFactory(data -> {
            Player player = data.getValue();
            int totalGames = player.getGamesPlayedTicTacToe();
            int totalOutcomes = player.getWinsInTicTacToe() + player.getLossInTicTacToe();
            int draws = totalGames - totalOutcomes;
            return new ReadOnlyObjectWrapper<>(Math.max(draws, 0));
        });

        // Calculate win percentage
        winPercentageColumn.setCellValueFactory(data -> {
            Player player = data.getValue();
            int totalGames = player.getGamesPlayedTicTacToe();
            double percentage = totalGames > 0 ? (player.getWinsInTicTacToe() * 100.0) / totalGames : 0;
            return new ReadOnlyObjectWrapper<>(Math.round(percentage * 100.0) / 100.0);
        });

        // Populate the player list
        playerList = FXCollections.observableArrayList(tLeaderboardController.mainTeam.getPlayers());

        // Sort the player list by win percentage (descending)
        ObservableList<Player> sortedList = FXCollections.observableArrayList(playerList);
        sortedList.sort((p1, p2) -> {
            double p1Percentage = p1.getGamesPlayedTicTacToe() > 0
                    ? (p1.getWinsInTicTacToe() * 100.0) / p1.getGamesPlayedTicTacToe()
                    : 0;
            double p2Percentage = p2.getGamesPlayedTicTacToe() > 0
                    ? (p2.getWinsInTicTacToe() * 100.0) / p2.getGamesPlayedTicTacToe()
                    : 0;
            return Double.compare(p2Percentage, p1Percentage); // Descending order
        });

        // Dynamically assign ranks based on sorted order
        rankColumn.setCellValueFactory(data -> {
            Player player = data.getValue();
            int rank = sortedList.indexOf(player) + 1;
            return new ReadOnlyObjectWrapper<>(rank);
        });

        // Set the sorted list to the table
        leaderboardTable.setItems(sortedList);
    }

    private Scene scene;

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/ca/ucalgary/seng300/p4/condensation/tMenu.fxml"));
        scene = new Scene(fxmlLoader.load());
        HelloApplication.setActiveScene(scene, "Tic Tac Toe - Menu");
    }


    public static Team mainTeam;

    static {
//        try {
//            Leaderboard leaderboard = Leaderboard.getInstance();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
        File file = new File(NetworkManager.LEADERBOARD_FILE);
        mainTeam = load(file);

        if (mainTeam == null) {
            System.err.println("Failed to load the team data from JSON.");
        }
    }

    public static Team load(File file) {
        Team team = new Team(new ArrayList<>());
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Read the JSON file and map it to a list of Player objects
            List<Player> players = objectMapper.readValue(file, new TypeReference<>() {
            });
            for (Player player : players) {
                team.storePlayer(player); // Store each player in the Team object
            }
        } catch (IOException e) {
            System.err.println("Error reading the JSON file: " + e.getMessage());
            return null;
        }

        return team;
    }

    /**
     * This class deals with all the process meant for the entire team and the storage of players and win-loss record
     */
    public static class Team {
        private ArrayList<Player> players;

        public Team(ArrayList<Player> players) {
            this.players = players;
        }

        /**
         * @return the current players and their stats stored as the Player object
         */
        public ArrayList<Player> getPlayers() {
            return players;
        }

        /**
         * @return the current players and their stats stored as the Player object
         */
        public ArrayList<Player> getAllRawPlayers() {
            return players;
        }

        public boolean playerExists(Player p1) { //Made public for testing
            for (Player player : players) {
                if (player.getId() == (p1.getId())) {
                    return true;
                }
            }
            return false;
        }

        public boolean storePlayer(Player player) {
            // first checks if the player exists (jersey number already exists) and if so returns false (not successfully saved player)
            if (!playerExists(player)) {
                players.add(player);
                return true;
            } else {
                return false;
            }
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Player {
        private String name;
        private String id;
        private int gamesPlayed_checkers;
        private int win_checkers;
        private int loss_checkers;

        // No-argument constructor for Jackson
        public Player() {
        }

        public Player(
                @JsonProperty("playerUUID") String id,
                @JsonProperty("username") String name,
                @JsonProperty("gamesPlayedTicTacToe") int gamesPlayed_checkers,
                @JsonProperty("winsInTicTacToe") int win_checkers,
                @JsonProperty("lossInTicTacToe") int loss_checkers
        ) {
            this.id = id;
            this.name = name;
            this.gamesPlayed_checkers = gamesPlayed_checkers;
            this.win_checkers = win_checkers;
            this.loss_checkers = loss_checkers;
        }

        // Getters and setters
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getGamesPlayedTicTacToe() {
            return gamesPlayed_checkers;
        }
        public int getWinsInTicTacToe() {
            return win_checkers;
        }
        public int getLossInTicTacToe() {
            return loss_checkers;
        }


        @Override
        public String toString() {
            return String.format("Player{id=%d, username='%s', gamesPlayedTicTacToe=%d, winsInTicTacToe=%d, lossInTicTacToe=%d}",
                    id, name, gamesPlayed_checkers, win_checkers, loss_checkers);
        }
    }
}
