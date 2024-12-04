package ca.ucalgary.seng300.p4.condensation;

import ca.ucalgary.seng300.p4.condensation.Frontend.GameSessionClient.GameSessionRecord;
import ca.ucalgary.seng300.p4.condensation.Frontend.controller.CheckersController;
import ca.ucalgary.seng300.p4.condensation.Frontend.controller.CheckersHomeMenuController;
import ca.ucalgary.seng300.p4.condensation.Frontend.controller.ProfileGameController;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameEventType;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, ParseException {
        JSONObject jsonObject = new JSONObject();

        JSONObject savePlayer = new JSONObject();
        savePlayer.put("username", "Ronaldo_Goat");
        savePlayer.put("password", "1234");
        NetworkManager.getNetworkInstance().signUpHandler(savePlayer);




        jsonObject.put("playerID", "65d58586-e9e5-41f1-a002-318fb202ac94");
        jsonObject.put("gameType", GameTypes.CONNECT4);
        System.out.println("AFTER");
        String response = NetworkManager.getNetworkInstance().handleQueuePlayer(jsonObject);

        System.out.println(response);


        // test to see if chat works

        GameSessionRecord.getGameSessionRecord().fromJSON(response);

        JSONObject chatSendPayload = new JSONObject();
        chatSendPayload.put("SessionID", GameSessionRecord.getGameSessionRecord().getSessionID());
        UUID Player1D = GameSessionRecord.getGameSessionRecord().getPlayer1ID();
        System.out.println("Player1poo: " + Player1D);
        chatSendPayload.put("Player1ID", Player1D);
        UUID meow = (UUID) chatSendPayload.get("Player1ID");
        chatSendPayload.put("Message", "hello");

        NetworkManager.getNetworkInstance().recievePlayerMessage(chatSendPayload);


        GameSessionRecord.getGameSessionRecord().fromJSON(response);

        // we have a gameSession

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("connect4-board.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        Image appIcon = new Image(getClass().getResourceAsStream("logo.png"));
        stage.getIcons().add(appIcon);

        stage.setResizable(false);
        stage.setTitle("Condensation Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}