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

public class ConnectFourQueueController {
    @FXML
    private Button leaveQueue;

    public void start(boolean fromChallenge) {
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(_ -> {
            try {
                if (fromChallenge)
                    switchToGameScene();
                else
                    sendToBackendConnectFour();
            } catch (IOException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        });
        delay.play();
    }

    public void start() {
        start(false);
    }


    @FXML
    protected void returnToConnectFourPage(Event event) throws IOException
    {
        Parent origin = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ConnectFourHomeMenu.fxml")));
        Scene createScene = new Scene(origin);
        HelloApplication.setActiveScene(createScene);
    }

    // get player id

    // based of game -> enumeration (json payload) key->game type

    // https://dzone.com/articles/how-can-we-read-a-json-file-in-java
    public void sendToBackendConnectFour() throws IOException, ParseException {
        JSONObject playerInfoForGame = new JSONObject();
        Object newplayerJSONFileObject = new JSONParser().parse(new FileReader(NetworkManager.PLAYER_ID_FILE));
        JSONObject newplayerJSONFile = (JSONObject) newplayerJSONFileObject;
        playerInfoForGame.put("playerID", newplayerJSONFile.get("playerID"));
        playerInfoForGame.put("gameType", GameTypes.CONNECT4);
        String startGameInfoPayload = NetworkManager.getNetworkInstance().handleQueuePlayer(playerInfoForGame);
        GameSessionRecord.getGameSessionRecord().fromJSON(startGameInfoPayload);
        switchToGameScene();
    }

    public void switchToGameScene() throws IOException {
        // Directing to the board
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("connect4-board.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HelloApplication.setActiveScene(scene, "Condensation Appliance - Connect Four");
    }
}
