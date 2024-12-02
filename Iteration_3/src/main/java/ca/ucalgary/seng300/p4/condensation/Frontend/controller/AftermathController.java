package ca.ucalgary.seng300.p4.condensation.Frontend.controller;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.GameResult;
import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javafx.fxml.FXMLLoader;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Objects;

import static ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes.*;

public class AftermathController {
    @FXML
    public Label gameResultLabel; // Match with fx:id in FXML

    @FXML
    public Label gameWLR;

    @FXML
    public Label overallWLR;

    @FXML
    public Button profileButton;

    @FXML
    public Label eloRanking;

    public static GameTypes gameType;

    @FXML
    public Button playAgainButton;

    @FXML
    public Stage stage;

    @FXML
    public void initialize() throws IOException, ParseException {
        JSONArray data = Leaderboard.getInstance().getAllUsers();
        JSONObject playerInfoForGame = new JSONObject();

        Object newplayerJSONFileObject = new JSONParser().parse(new FileReader("playerID.json"));
        JSONObject newplayerJSONFile = (JSONObject) newplayerJSONFileObject;

        String idOfPlayer = (String) newplayerJSONFile.get("playerID");

        for (Object user : data) {
            JSONObject userJSON = (JSONObject) user;
            String usernameOfPlayer = (String) userJSON.get("username");
            if (usernameOfPlayer.equals(idOfPlayer)){
                playerInfoForGame = userJSON;
            }
            break;
        }

        JSONArray recentMatches = (JSONArray) playerInfoForGame.get("recentMatches");
        JSONObject lastMatch = (JSONObject) recentMatches.getLast();
        String resultOfMatch = (String) lastMatch.get("outcome");
        GameTypes typeOfMatch = (GameTypes) lastMatch.get("gameType");

        int totalWinsForTTT = (int) playerInfoForGame.get("winsInTicTacToe");
        int totalWinsForConnect4 = (int) playerInfoForGame.get("winsInConnect4");
        int totalWinsForCheckers = (int) playerInfoForGame.get("winsInCheckers");
        int totalWins = totalWinsForTTT + totalWinsForConnect4 + totalWinsForCheckers;

        int totalGamesPlayedForTTT = (int) playerInfoForGame.get("gamesPlayedTicTacToe");
        int totalGamesPlayedForConnect4 = (int) playerInfoForGame.get("gamesPlayedConnect4");
        int totalGamesPlayedForForCheckers = (int) playerInfoForGame.get("gamesPlayedCheckers");
        int totalGames = totalGamesPlayedForTTT + totalGamesPlayedForConnect4 + totalGamesPlayedForForCheckers;

        double ratio = (double) totalWins / totalGames;

        if (Objects.equals(resultOfMatch, "L")) {
            gameResultLabel.setText("You Lost.");
        }
        else if (Objects.equals(resultOfMatch, "W")){
            gameResultLabel.setText("You Won!");
        }
        else{
            gameResultLabel.setText("You Tied.");
        }

        // https://stackoverflow.com/questions/153724/how-to-round-a-number-to-n-decimal-places-in-java
        overallWLR.setText(String.format("%d\\%d (%.2f)%s",totalWins, totalGames, (double)Math.round(ratio * 1000) / 1000, "%"));

        if (typeOfMatch == TICTACTOE){
            double specRatio = (double) totalWinsForTTT / totalGamesPlayedForTTT;
            double roundedRatio = (double) Math.round(specRatio * 1000) / 1000;
            gameWLR.setText(String.format("%d\\%d (%.2f)%s",totalWinsForTTT, totalGamesPlayedForTTT, roundedRatio, "%"));
            eloRanking.setText((String) playerInfoForGame.get("tictactoeElo"));
        }
        else if (typeOfMatch == CHECKERS){
            double specRatio = (double) totalWinsForCheckers / totalGamesPlayedForForCheckers;
            double roundedRatio = (double) Math.round(specRatio * 1000) / 1000;
            gameWLR.setText(String.format("%d\\%d (%.2f)%s",totalWinsForCheckers, totalGamesPlayedForForCheckers, roundedRatio, "%"));
            eloRanking.setText((String) playerInfoForGame.get("checkersElo"));
        }
        else{
            double specRatio = (double) totalWins / totalGamesPlayedForConnect4;
            double roundedRatio = (double) Math.round(specRatio * 1000) / 1000;
            gameWLR.setText(String.format("%d\\%d (%.2f)%s",totalWinsForConnect4, totalGamesPlayedForConnect4, roundedRatio, "%"));
            eloRanking.setText((String) playerInfoForGame.get("connect4Elo"));
        }

    }
    public void handlePlayAgain(ActionEvent event) throws IOException {
        if (gameType == TICTACTOE){
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tictactoequeue.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setTitle("Condensation Application - Queue");
            stage.setScene(scene);
            stage.show();
        }
        else if (gameType == CONNECT4){
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("connectfourqueue.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setTitle("Condensation Application - Queue");
            stage.setScene(scene);
            stage.show();
        }
        else{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersqueue.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setTitle("Condensation Application - Queue");
            stage.setScene(scene);
            stage.show();
        }
    }

    public void handleReturnToProfile(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ProfileHome.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Condensation Application - Profile");
        stage.setScene(scene);
        stage.show();
    }
}