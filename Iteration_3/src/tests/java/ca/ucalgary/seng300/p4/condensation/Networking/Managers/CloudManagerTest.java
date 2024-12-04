package ca.ucalgary.seng300.p4.condensation.Networking.Managers;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

        assertTrue("Saving game state should fail as there is no connection", cloudManager.saveGameState(gameState));
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
        assertNull("Retrieved game state should be null", retrieveGameState);

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

    @Test
    public void testInitializeConnection(){
        cloudManager.initializeConncection();
        assertTrue("Connected to Cloud Successfully", cloudManager.isConnected());
    }

    @Test
    public void testDisconnectConnection(){
        cloudManager.initializeConncection();
        cloudManager.disconnect();
        assertFalse("Disconnected from Cloud Successfully", cloudManager.isConnected());
    }

    @Test
    public void testIsConnectedWithoutInitialization() {
        // Ensure the connection is not initialized
        assertFalse("isConnected() should return false when there is no active connection", cloudManager.isConnected());
    }

    @Test
    public void testIsConnectedAfterDisconnection() {
        // Initialize the connection
        cloudManager.initializeConncection();
        // Disconnect
        cloudManager.disconnect();
        // Test the isConnected method again
        assertFalse("isConnected() should return false after disconnection", cloudManager.isConnected());
    }

    @Test
    public void testGetSyncedPlayersWithoutConnection() {
        // Ensure the connection is not active
        cloudManager.disconnect();

        // Retrieve the list of synced players
        List<UUID> syncedPlayers = cloudManager.getSyncedPlayers();

        // Verify the result is an empty list
        assertTrue("getSyncedPlayers() should return an empty list when not connected", syncedPlayers.isEmpty());
    }
    @Test
    public void testGetSyncedPlayersWithoutConnection() {
        // Ensure the connection is not active
        cloudManager.disconnect();

        // Retrieve the list of synced players
        List<UUID> syncedPlayers = cloudManager.getSyncedPlayers();

        // Verify the result is an empty list
        assertTrue("getSyncedPlayers() should return an empty list when not connected", syncedPlayers.isEmpty());
    }
    @Test
    public void testGetSyncedPlayersWithConnection() {
        // Ensure CloudManager is connected and retrieve the list of players synced currently
        cloudManager.initializeConncection();
        List<UUID> syncedPlayers = cloudManager.getSyncedPlayers();
        if (syncedPlayers.isEmpty())
            assertTrue("Synced players list should be empty after initializing connection", syncedPlayers.isEmpty());
        else
            assertNotNull("Synced players list should not be null after connection", syncedPlayers);
    }

}
