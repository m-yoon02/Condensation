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
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CheckersQueueController {

    private Button leaveQueue;

    @FXML
    private Parent origin;

    @FXML
    private Scene createScene;


    public void start(boolean fromChallenge) {
        // Allows user to leave
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> {
            try {
                if (fromChallenge)
                    switchToGameScene();
                else
                    sendToBackendCheckers();
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        });
        delay.play();
    }

    public void start() {
        start(false);
    }

    // get player id

    // based of game -> enumeration (json payload) key->game type

    // https://dzone.com/articles/how-can-we-read-a-json-file-in-java
    public void sendToBackendCheckers() throws IOException, ParseException {
        JSONObject playerInfoForGame = new JSONObject();

        Object newplayerJSONFileObject = new JSONParser().parse(new FileReader(NetworkManager.PLAYER_ID_FILE));
        JSONObject newplayerJSONFile = (JSONObject) newplayerJSONFileObject;

        playerInfoForGame.put("playerID", newplayerJSONFile.get("playerID"));
        playerInfoForGame.put("gameType", GameTypes.CHECKERS);
        String startGameInfoPayload = NetworkManager.getNetworkInstance().handleQueuePlayer(playerInfoForGame);
        GameSessionRecord.getGameSessionRecord().fromJSON(startGameInfoPayload);
        switchToGameScene();
    }

    public void switchToGameScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("checkersInstance.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HelloApplication.setActiveScene(scene, "Checkers");
    }
}
