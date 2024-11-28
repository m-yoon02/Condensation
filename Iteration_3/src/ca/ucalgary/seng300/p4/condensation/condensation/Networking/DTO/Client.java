package ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO;

import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Enums.GameEventType;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Listeners.GameDataListener;
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
    public void onGameEvent(GameEventType eventType, JSONObject payload) {
        if(eventType == GameEventType.CHAT_MESSAGE_RECEIVED){
            // will recieve message information from the frontend

        }else{

            // we can call a frontend recieveMethod(JSONObject gameData);
        }

    }
}
