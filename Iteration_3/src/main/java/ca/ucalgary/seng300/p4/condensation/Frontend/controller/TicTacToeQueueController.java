package ca.ucalgary.seng300.p4.condensation.Frontend.controller;

import ca.ucalgary.seng300.p4.condensation.HelloApplication;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javafx.scene.layout.Pane;

import java.io.File;
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

    @FXML
    private Stage stage;


    @FXML
    public void initialize() throws IOException, ParseException, InterruptedException {
        // Allows user to leave
        TimeUnit.SECONDS.sleep(3);
        sendToBackendTicTacToe();
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

        Object newplayerJSONFileObject = new JSONParser().parse(new FileReader("playerID.json"));
        JSONObject newplayerJSONFile = (JSONObject) newplayerJSONFileObject;

        playerInfoForGame.put("playerID", newplayerJSONFile.get("playerID "));
        playerInfoForGame.put("gameType", GameTypes.TICTACTOE);
        JSONObject startGameInfoPayload = NetworkManager.getNetworkInstance().handleQueuePlayer(playerInfoForGame);

        FileWriter fileWriter = new FileWriter("src/main/java/ca/ucalgary/seng300/p4/condensation/Networking/Database/Leaderboard/newgame.json");
        fileWriter.write(startGameInfoPayload.toJSONString());
        fileWriter.flush();
        fileWriter.close();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("connect4-board.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Condensation Application - Tic Tac Toe");
        stage.setScene(scene);
        stage.show();
    }
}