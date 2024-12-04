package ca.ucalgary.seng300.p4.condensation.Networking.Managers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.Assert.*;

public class CloudManagerTest {

    private CloudManager cloudManager;


    @Before
    public void setup() {
        cloudManager = CloudManager.getInstance();
    }

    @After
    public void teardown() {
        cloudManager.disconnect();
    }


    @Test
    public void testSaveGameStateWithoutConnection() {
        JSONObject gameState = new JSONObject();
        gameState.put("SessionID", UUID.randomUUID().toString());
        gameState.put("PlayerID", UUID.randomUUID().toString());

        assertFalse("Saving game state should fail as there is no connection", cloudManager.saveGameState(gameState));
    }


    @Test
    public void testSaveGameStateWithConnection() {
        cloudManager.initializeConncection();
        JSONObject gameState = new JSONObject();
        gameState.put("SessionID", UUID.randomUUID().toString());
        gameState.put("PlayerID", UUID.randomUUID().toString());

        // since we are connected, it should be able to save game state and retrieve it

        assertTrue("Saving game state should pass connection", cloudManager.saveGameState(gameState));


        // retrieve the game state to see if it saved correctly by comparing the game state with its retrieve game state
        JSONObject retrieveGameState = cloudManager.retrieveGameState();
        assertNotNull("Retrieved game state should not be null", retrieveGameState); // the game state should not be null as it was saved since it was connected to the cloud
        assertEquals("Retrieved game state should match the saved state", retrieveGameState, gameState);


    }



    @Test
    public void testRetrieveGameStateWithoutConnection() {
        JSONObject retrieveGameState = cloudManager.retrieveGameState();
        assertNotNull("Retrieved game state should not be null", retrieveGameState);

    }


    @Test
    public void testRetrieveGameStateWithConnection() {
        cloudManager.initializeConncection();
        JSONObject gameState = new JSONObject();
        gameState.put("SessionID", UUID.randomUUID().toString());
        gameState.put("PlayerID", UUID.randomUUID().toString());

        // save the current game state
        cloudManager.saveGameState(gameState);

        // now retrieving that saved game state we just saved
        JSONObject retrieveGameState = cloudManager.retrieveGameState();
        assertNotNull("Retrieved game state should not be null", retrieveGameState);
        assertEquals("Retrieved game state should match the saved state", retrieveGameState, gameState);

    }






}
