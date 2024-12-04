package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.Frontend.GameSessionClient.GameSessionRecord;
import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import javafx.animation.PauseTransition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TicTacToeQueueController {

    @FXML
    private Button leaveQueue;

    @FXML
    private Parent origin;

    @FXML
    private Stage createWindow;
    @FXML
    private Scene createScene;

    private Stage stage;

    public void start(Stage stage) {
        // Allows user to leave
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        this.stage = stage;
        delay.setOnFinished(e -> {
            try {
                sendToBackendTicTacToe();
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        });
        delay.play();
    }

    @FXML
    protected void returnToTicTacToeMenu(Event event) throws IOException
    {
        origin = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("tMenu.fxml")));
        createWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        createScene = new Scene(origin);
        createWindow.setScene(createScene);
        createWindow.show();
    }

    // get player id

    // based of game -> enumeration (json payload) key->game type

    // https://dzone.com/articles/how-can-we-read-a-json-file-in-java
    public void sendToBackendTicTacToe() throws IOException, ParseException {
        JSONObject playerInfoForGame = new JSONObject();

        Object newplayerJSONFileObject = new JSONParser().parse(new FileReader(NetworkManager.PLAYER_ID_FILE));
        JSONObject newplayerJSONFile = (JSONObject) newplayerJSONFileObject;

        playerInfoForGame.put("playerID", newplayerJSONFile.get("playerID"));
        playerInfoForGame.put("gameType", GameTypes.TICTACTOE);
        String startGameInfoPayload = NetworkManager.getNetworkInstance().handleQueuePlayer(playerInfoForGame);
        GameSessionRecord.getGameSessionRecord().fromJSON(startGameInfoPayload);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tGame.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Condensation Application - Tic Tac Toe");
        stage.setScene(scene);
        stage.show();
    }
}