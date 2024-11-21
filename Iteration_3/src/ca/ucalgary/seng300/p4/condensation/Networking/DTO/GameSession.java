package ca.ucalgary.seng300.p4.condensation.Networking.DTO;

import java.util.ArrayList;
import java.util.UUID;


public class GameSession implements GameEndListener, TurnEndListeners {

    /**
     * We Created Turn END and Game END Listeners that subscribe to listener lists in GameHandler respectivly
     * Passes Player 1 UUID and Player 2 into GameHandler
     * Passes GameSession objects GameEnd and TurnEnd listners into GameHandler.
     */
    private final String sessionId;
    private final UUID player1;
    private final UUID player2;


    /**
     * When creating GameSession we initialize sessionID field, and player1 and player2 UUIDs which will be passed into some instance of GameHandler
     * @param player1
     * @param player2
     */

    public GameSession(Player player1, Player player2) {
        this.sessionId = UUID.randomUUID().toString();
        this.player1 =  player1.getPlayerID();
        this.player2 =  player2.getPlayerID();
    }
    /**
     * Listener pattern implementation
     * Instanitiate listeners for TurnEnd and GameEnd Listeners
     * pass this information to backend so that  they can subscribe them
     */


    // here we need to send something to network manager, or a send message possobly a JSON payload which backend can then parse.
    //


}