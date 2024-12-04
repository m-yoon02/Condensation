package ca.ucalgary.seng300.p4.condensation.Networking.DTO;

import ca.ucalgary.seng300.p4.condensation.Frontend.Receiver.ClientReceiver;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameEventType;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Listeners.GameDataListener;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
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



    /**
     * Handles a game event by delegating the payload to the appropriate game controller
     * based on the game type.
     * @param eventType The type of game event being handled.
     * @param payload   The {@link JSONObject} containing the event data to process.
     * @param gameType  The type of game the event is associated with.
     * @throws IOException If an error occurs while processing the event.
     */
    @Override
    public void onGameEvent(GameEventType eventType, JSONObject payload, GameTypes gameType) throws IOException {
        ClientReceiver.receive(playerId, gameType, payload);
    }
}
