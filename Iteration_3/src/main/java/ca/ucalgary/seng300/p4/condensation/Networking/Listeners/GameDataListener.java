package ca.ucalgary.seng300.p4.condensation.Networking.Listeners;

import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameEventType;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;

/**
 * Interface for handling game-related events and chat events.
 * Implementations of this interface should define how to process
 * these events for clients in the system.
 */
public interface GameDataListener {

    /**
     * Handles a game event, such as game state updates or player actions.
     *
     * @param eventType The type of game event.
     * @param payload   The data associated with the game event.
     * @param gameTypes The type of game the event is related to.
     * @throws IOException If an error occurs while processing the event.
     */
    void onGameEvent(GameEventType eventType, JSONObject payload, GameTypes gameTypes) throws IOException;

    /**
     * Handles a chat event, such as sending or receiving chat messages between players.
     *
     * @param gameEventType         The type of event associated with the chat.
     * @param clientResponsePayload The data containing the chat messages.
     */
    void onChatEvent(GameEventType gameEventType, JSONArray clientResponsePayload);
}
