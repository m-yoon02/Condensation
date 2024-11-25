package ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO;

import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Listeners.GameDataListener;

import java.util.UUID;

public class Client implements GameDataListener {

    private final UUID userId;
    private final String username;
    private final QueuedPlayerMemo playerMemo;

    // Constructor with QueuedPlayerMemo
    public Client(UUID userId, String username, GameTypes gameType, int elo) {
        this.userId = userId;
        this.username = username;
        this.playerMemo = new QueuedPlayerMemo(userId, gameType, elo); // Creates a player memo which we will use when referencing the object
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public QueuedPlayerMemo getPlayerMemo() {
        return playerMemo;
    }

    @Override
    public void onGameDataReceived(String gameData) {
        System.out.println("Client " + username + " received game data: " + gameData);
        // Handle game data (e.g., forward to frontend)
    }
}
