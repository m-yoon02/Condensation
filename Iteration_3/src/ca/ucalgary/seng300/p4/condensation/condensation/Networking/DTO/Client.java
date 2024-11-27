package ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO;

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
    public void onGameEvent(JSONObject gameData) {

        // I need to handle the different kinds of GameEventsGAME_START
        // else if , TURN_END,
        // else if GAME_END,
        // else if CHAT_MESSAGE_RECEIVED
        System.out.println("Client " + username + " received game data: " + gameData);
        // Handle game data (e.g., forward to frontend) which can be access via the client class by getClient(playerUID)
    }
}
