package ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO;

import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Listeners.GameDataListener;

import java.util.UUID;

public class Client implements GameDataListener {

    private final UUID userId;
    private final String username;

    // Constructor with QueuedPlayerMemo
    public Client(UUID userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public UUID getUserId() {
        return userId;
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
