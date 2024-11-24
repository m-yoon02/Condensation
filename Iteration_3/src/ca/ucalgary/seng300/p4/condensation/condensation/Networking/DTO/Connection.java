package ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO;

import java.util.UUID;

public class Connection {

    // Do we need the following :
    // GameSessionManager? and sessions hashmap?

    private final UUID playerID;
    private boolean isConnected;

    /**
     * Constructor that takes in a playerID
     * @param playerID unique identifier for the player
     */
    public Connection(UUID playerID) {
        this.playerID = playerID;
        this.isConnected = true;  // Default to connected when created
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public boolean isConnected() {
        return isConnected;
    }


    public void disconnect() {
        isConnected = false;
        System.out.println("Player " + playerID + " is disconnected.");
    }
}
