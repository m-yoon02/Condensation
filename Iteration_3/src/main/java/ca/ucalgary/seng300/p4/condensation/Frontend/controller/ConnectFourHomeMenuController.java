package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConnectFourHomeMenuController {

    @FXML
    private Button playButton;

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button howToPlayButton;

    @FXML
    private Button backButton;

    @FXML
    private ImageView connect4Image;

    @FXML
    public void initialize() {
        // Set the Connect 4 image
        connect4Image.setImage(new Image(getClass().getResource("/ca/ucalgary/seng300/p4/condensation/connect4.png").toExternalForm()));

        // Button navigation
        playButton.setOnAction(event -> navigateTo("/ca/ucalgary/seng300/p4/condensation/connect4-board.fxml"));
        leaderboardButton.setOnAction(event -> navigateTo("/ca/ucalgary/seng300/p4/condensation/ConnectFourLeaderboard.fxml"));
        howToPlayButton.setOnAction(event -> navigateTo("/ca/ucalgary/seng300/p4/condensation/howToPlayConnect4.fxml"));
        backButton.setOnAction(event -> navigateTo("previous-page.fxml")); // Update with the actual previous page
    }

    private void navigateTo(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Stage stage = (Stage) playButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            stage.setTitle("Games Menu");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * EVERYTHING ONWARDS IS FOR THE LEADERBOARD
     */

    public static Team mainTeam;

    static {
        File file = new File("src/main/java/ca/ucalgary/seng300/p4/condensation/Networking/Database/Leaderboard/Leaderboard.json");

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
            List<Player> players = objectMapper.readValue(file, new TypeReference<List<Player>>() {
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
                @JsonProperty("gamesPlayedCheckers") int gamesPlayed_checkers,
                @JsonProperty("winsInCheckers") int win_checkers,
                @JsonProperty("lossInCheckers") int loss_checkers
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
        public int getGamesPlayedCheckers() {
            return gamesPlayed_checkers;
        }
        public int getWinsInCheckers() {
            return win_checkers;
        }
        public int getLossInCheckers() {
            return loss_checkers;
        }


        @Override
        public String toString() {
            return String.format("Player{id=%d, username='%s', gamesPlayedCheckers=%d, winsInCheckers=%d, lossInCheckers=%d}",
                    id, name, gamesPlayed_checkers, win_checkers, loss_checkers);
        }
    }

}
