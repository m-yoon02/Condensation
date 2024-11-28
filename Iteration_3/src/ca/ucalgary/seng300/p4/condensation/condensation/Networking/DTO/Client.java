package ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO;

import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Enums.GameEventType;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Listeners.GameDataListener;
import org.json.simple.JSONObject;

import java.util.UUID;

public class Client implements GameDataListener {

    private final UUID playerId;
    private final String username;

    public Client(UUID playerId, String username) {
        this.playerId = playerId;
        this.username = username;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void onGameEvent(JSONObject gameData, GameEventType gameEventType) {

        // we can call a frontend recieveMethod(JSONObject gameData);

    }
}
