package ca.ucalgary.seng300.p4.condensation.Networking.DTO;

import ca.ucalgary.seng300.p4.condensation.Networking.Enums.PlayerColour;

import java.util.ArrayList;
import java.util.UUID;


public class GameSession {

    /**
     * Handles the notifying of the system of game state updates that occur in backend logic as the game progresses
     * Handles the Listerner lists which is mainly GameSession Manager
     *
     */
    private final String sessionId;
    private final PlayerColour player1;
    private final PlayerColour player2;
    private final List<TurnEndListener> turnEndListeners = new ArrayList<>();
    private final List<GameEndListener> gameEndListeners = new ArrayList<>();


    // if we do this by color

    public GameSession(Player player1, Player player2) {
        this.sessionId = UUID.randomUUID().toString();
        this.player1 = PlayerColour.RED;
        this.player2 = PlayerColour.BLUE;

    }
    /**
     * Listener pattern implementation
     */

    // we need logic for onTurnEnd and onGameEND
    // admin for Listeners lists
    // notify methods





    /**
     * Getters for player colour and session ID
     */

    public PlayerColour getPlayer1() {
        return player1;
    }

    public PlayerColour getPlayer2() {
        return player2;
    }

    public String getSessionId() {
        return sessionId;

    }

}