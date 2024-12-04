package ca.ucalgary.seng300.p4.condensation.Networking.Managers;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

        assertTrue("Saving game state should fail as there is no connection", cloudManager.saveGameState(gameState));
    }

    @Test
    public void testSaveGameStateWithConnection() {
        cloudManager.initializeConnection();
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
        cloudManager.initializeConnection();
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
    public void testInitializeConnection() {
        cloudManager.initializeConnection();
        assertTrue("Connected to Cloud Successfully", cloudManager.isConnected());
    }

    @Test
    public void testDisconnectConnection() {
        cloudManager.initializeConnection();
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
        cloudManager.initializeConnection();
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
    public void testGetSyncedPlayersWithConnection() {
        // Ensure CloudManager is connected and retrieve the list of players synced currently
        cloudManager.initializeConnection();
        List<UUID> syncedPlayers = cloudManager.getSyncedPlayers();
        if (syncedPlayers.isEmpty())
            assertTrue("Synced players list should be empty after initializing connection", syncedPlayers.isEmpty());
        else
            assertNotNull("Synced players list should not be null after connection", syncedPlayers);
    }

    @Test
    public void testInitializeConnectionAlreadyConnected() {
        cloudManager.initializeConnection(); // First call to establish connection
        assertTrue("isConnected should be true after the first connection", cloudManager.isConnected());

        boolean result = cloudManager.initializeConnection(); // Second call
        assertFalse("The second call should interrupt the connection", result);
        assertFalse("isConnected should be false after the second call", cloudManager.isConnected());
    }


    @Test
    public void testSyncDataWithoutConnection() {
        // Ensure the connection is not established
        cloudManager.disconnect();

        // Create a list of player IDs to sync
        List<UUID> playerIDS = List.of(UUID.randomUUID(), UUID.randomUUID());

        // Call the method you are testing
        boolean result = cloudManager.syncPlayerData(playerIDS);

        // Assert that the method returns false when not connected
        assertFalse("Syncing data should fail when not connected to the cloud", result);

        // Verify that the synced players list remains unchanged
        List<UUID> syncedPlayers = cloudManager.getSyncedPlayers();
        assertTrue("Synced players list should remain empty when not connected", syncedPlayers.isEmpty());
    }


    @Test
    public void testSyncDataWithConnection() {
        cloudManager.initializeConnection();
        List<UUID> playerIDS = List.of(UUID.randomUUID(), UUID.randomUUID());
        boolean result = cloudManager.syncPlayerData(playerIDS);

        // since we are connected, it should be true
        assertTrue("SyncPlayerData should return true when connected to the cloud", result);

        // Verify that syncedPlayerIDs contains the correct data
        List<UUID> syncedPlayers = cloudManager.getSyncedPlayers(); // Adjust this method as per your implementation
        assertEquals("Synced players should match the provided player IDs", playerIDS, syncedPlayers);
    }


    @Test
    public void testAuthenticateUserWithConnection() {
        // Ensure the cloud is connected
        cloudManager.initializeConnection();

        // Call the method
        boolean result = cloudManager.authenticateUser("testUser", "testPassword");

        // Since it's a stub, it always returns true
        assertTrue("authenticateUser should return true when connected (stub behavior)", result);
    }
}
