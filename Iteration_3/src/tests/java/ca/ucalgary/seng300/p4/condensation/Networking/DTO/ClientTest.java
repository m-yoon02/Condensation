package ca.ucalgary.seng300.p4.condensation.Networking.DTO;

import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameEventType;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientTest {

    private Client client;
    private UUID playerID;


    @Before
    public void setUp() throws Exception {

        // initialize player ID
        playerID = UUID.randomUUID();
        client = new Client(playerID);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getPlayerId() {

        // verify that getPlayerID() returns the correct player ID
        assertEquals("The returned player ID should match", playerID, client.getPlayerId());
    }

    @Test
    public void onChatEvent() {
        // making a lock chat payload
        JSONArray chatPayload = new JSONArray();
        chatPayload.add("Player1: Hello!");
        chatPayload.add("Player2: World!");

        // call onChatEvent method
        client.onChatEvent(GameEventType.CHAT_MESSAGE_RECEIVED, chatPayload);
    }

    @Test
    public void onGameEvent() {
        // creating mock data
        // in this case we're testing for CHAT_MESSAGE_RECEIVED
        GameEventType eventType = GameEventType.CHAT_MESSAGE_RECEIVED;
        JSONObject payload = new JSONObject();
        payload.put("message", "Hello!");
        GameTypes gameTypes = GameTypes.CHECKERS;

        try {
            // calling onGameEvent
            client.onGameEvent(eventType,payload,gameTypes);
            // if there was no exception thrown, then it passed with valid inputs
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}