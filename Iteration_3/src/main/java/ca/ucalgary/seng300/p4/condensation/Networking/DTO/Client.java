package ca.ucalgary.seng300.p4.condensation.Networking.DTO;

import ca.ucalgary.seng300.p4.condensation.Frontend.controller.CheckersController;
import ca.ucalgary.seng300.p4.condensation.Frontend.controller.ConnectFourController;
import ca.ucalgary.seng300.p4.condensation.Frontend.controller.TicTacToeController;
import ca.ucalgary.seng300.p4.condensation.Frontend.game.ConnectFour;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameEventType;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Listeners.GameDataListener;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.UUID;

public class Client implements GameDataListener {

    private final UUID playerId;

    public Client(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }


    @Override
    public void onChatEvent(GameEventType gameEventType, JSONArray clientResponsePayload){
        
    }



    @Override
    public void onGameEvent(GameEventType eventType, JSONObject payload, GameTypes gameType) {

        // we can call a frontend recieveMethod(JSONObject gameData) that client will directly call

        switch(gameType){
            case CHECKERS -> {
                CheckersController.receiveCheckers(payload);
            }
            case TICTACTOE -> {
                TicTacToeController.receiveFromBackendTicTacToe(payload);
            }
            case CONNECT4 -> {
                ConnectFourController.receiveConnectFour(payload);
            }
        }



    }
}
