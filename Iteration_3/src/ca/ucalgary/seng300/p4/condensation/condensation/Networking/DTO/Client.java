package ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO;

import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Listeners.GameDataListener;

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
    public void onGameDataReceived(String gameData) {
        System.out.println("Client " + username + " received game data: " + gameData);
        // Handle game data (e.g., forward to frontend)
    }
}
