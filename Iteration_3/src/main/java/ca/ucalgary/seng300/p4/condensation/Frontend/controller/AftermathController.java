package ca.ucalgary.seng300.p4.condensation.Frontend.controller;
import ca.ucalgary.seng300.p4.condensation.Frontend.GameSessionClient.GameSessionRecord;
import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

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

    @FXML
    public Button playAgainButton;


    @FXML
    public void initialize() throws IOException, ParseException {
        JSONArray data = Leaderboard.getInstance().getAllUsers();
        System.out.println(data);
        JSONObject playerInfoForGame = new JSONObject();

        Object newplayerJSONFileObject = new JSONParser().parse(new FileReader(NetworkManager.PLAYER_ID_FILE));
        JSONObject newplayerJSONFile = (JSONObject) newplayerJSONFileObject;
        System.out.println("NEWPLAYER JSON" + newplayerJSONFile);

        String idOfPlayer = (String) newplayerJSONFile.get("username");
        System.out.println("In init func");

        for (Object user : data) {
            JSONObject userJSON = (JSONObject) user;
            String usernameOfPlayer = (String) userJSON.get("username");
            if (usernameOfPlayer.equals(idOfPlayer)){
                playerInfoForGame = userJSON;
                System.out.println("PlayerINfoGame: " + playerInfoForGame);
                break;
            }
        }

        JSONArray recentMatches = (JSONArray) playerInfoForGame.get("recentMatches");
        JSONObject lastMatch = (JSONObject) recentMatches.getLast();

        String resultOfMatch = (String) lastMatch.get("outcome");
        GameTypes typeOfMatch = (GameTypes) lastMatch.get("gameType");
//        GameTypes typeOfMatch = GameTypes.valueOf(gameTypeString);

        if (Objects.equals(resultOfMatch, "L")) {
            gameResultLabel.setText("You Lost.");
        } else if (Objects.equals(resultOfMatch, "W")) {
            gameResultLabel.setText("You Won!");
        } else if (Objects.equals(resultOfMatch, "T")){
            gameResultLabel.setText("You Tied.");
        }

        // Calculate overall win/loss ratio
        Number totalWinsForTTT = (Number) playerInfoForGame.get("winsInTicTacToe");
        Number totalWinsForConnect4 = (Number) playerInfoForGame.get("winsInConnect4");
        Number totalWinsForCheckers = (Number) playerInfoForGame.get("winsInCheckers");

        long totalWins = totalWinsForTTT.longValue() + totalWinsForConnect4.longValue() + totalWinsForCheckers.longValue();

        Number totalGamesPlayedForTTT = (Number) playerInfoForGame.get("gamesPlayedTicTacToe");
        Number totalGamesPlayedForConnect4 = (Number) playerInfoForGame.get("gamesPlayedConnect4");
        Number totalGamesPlayedForForCheckers = (Number) playerInfoForGame.get("gamesPlayedCheckers");

        long totalGames = totalGamesPlayedForTTT.longValue() + totalGamesPlayedForConnect4.longValue() + totalGamesPlayedForForCheckers.longValue();

        double ratio = (totalGames > 0) ? (double) totalWins / totalGames : 0.0;
        overallWLR.setText(String.format("%d\\%d (%.2f)%s", totalWins, totalGames, Math.round(ratio * 1000) / 1000.0, "%"));

        updateGameStats(typeOfMatch, playerInfoForGame);


    }

    private String calculateWinLossRatio(int wins, int gamesPlayed) {
        if (gamesPlayed == 0) {
            return "0\\0 (0.00)%"; // handling divide by zero
        }
        double ratio = (double) wins / gamesPlayed;
        double roundedRatio = Math.round(ratio * 1000) / 1000.0; // round to 3 decimal places
        return String.format("%d\\%d (%.2f)%s", wins, gamesPlayed, roundedRatio, "%");
    }

    private void updateGameStats(GameTypes typeOfMatch, JSONObject playerInfoForGame) {
        int totalWins;
        int totalGamesPlayed;
        String elo;

        switch (typeOfMatch) {
            case TICTACTOE -> {
                totalWins = ((Number) playerInfoForGame.get("winsInTicTacToe")).intValue();
                totalGamesPlayed = ((Number) playerInfoForGame.get("gamesPlayedTicTacToe")).intValue();
                elo = String.valueOf(playerInfoForGame.get("tictactoeElo"));
            }
            case CHECKERS -> {
                totalWins = ((Number) playerInfoForGame.get("winsInCheckers")).intValue();
                totalGamesPlayed = ((Number) playerInfoForGame.get("gamesPlayedCheckers")).intValue();
                elo = String.valueOf(playerInfoForGame.get("checkersElo"));
            }
            case CONNECT4 -> {
                totalWins = ((Number) playerInfoForGame.get("winsInConnect4")).intValue();
                totalGamesPlayed = ((Number) playerInfoForGame.get("gamesPlayedConnect4")).intValue();
                elo = String.valueOf(playerInfoForGame.get("connect4Elo"));
            }
            default -> throw new IllegalArgumentException("Invalid game type: " + typeOfMatch);
        }

        String winLossRatio = calculateWinLossRatio(totalWins, totalGamesPlayed);
        gameWLR.setText(winLossRatio);
        eloRanking.setText(elo);
    }

    private Stage getGlobalStage() {
        return (Stage) gameResultLabel.getScene().getWindow();
    }

    public void handlePlayAgain(ActionEvent event) throws IOException, ParseException, InterruptedException {
        Stage stage = getGlobalStage();
        GameTypes gameType = GameSessionRecord.getGameSessionRecord().getGameType();
        if (gameType == GameTypes.TICTACTOE){
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tictactoequeue.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            TicTacToeQueueController ticTacToeQueueController = fxmlLoader.getController();
            stage.setTitle("Condensation Application - Queue");
            stage.setScene(scene);
            stage.show();
            ticTacToeQueueController.start(stage);
        }
        else if (gameType == GameTypes.CONNECT4){
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("connectfourqueue.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            ConnectFourQueueController connectFourQueueController = fxmlLoader.getController();
            stage.setTitle("Condensation Application - Queue");
            stage.setScene(scene);
            stage.show();
            connectFourQueueController.start(stage);
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersqueue.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            CheckersQueueController checkersQueueController = fxmlLoader.getController();
            stage.setTitle("Condensation Application - Queue");
            stage.setScene(scene);
            stage.show();
            checkersQueueController.start(stage);
        }
    }

    public void handleReturnToProfile(ActionEvent event) throws IOException {
        Stage stage = getGlobalStage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ProfileHome.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Condensation Application - Profile");
        stage.setScene(scene);
        stage.show();
    }
}