package ca.ucalgary.seng300.p4.condensation;

import ca.ucalgary.seng300.p4.condensation.Frontend.GameSessionClient.GameSessionRecord;
import ca.ucalgary.seng300.p4.condensation.Frontend.controller.CheckersController;
import ca.ucalgary.seng300.p4.condensation.Frontend.controller.CheckersHomeMenuController;
import ca.ucalgary.seng300.p4.condensation.Frontend.controller.ProfileGameController;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.GameSession;
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

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, ParseException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("playerID", "234e5678-e89c-34d4-a567-526725175098");
        jsonObject.put("gameType", GameTypes.CONNECT4);
        String  response = NetworkManager.getNetworkInstance().handleQueuePlayer(jsonObject);


        GameSessionRecord.getGameSessionRecord().fromJSON(response);

        // we have a gameSession
        NetworkManager.getNetworkInstance().receiveGameDataFromClient();                // you click this button

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