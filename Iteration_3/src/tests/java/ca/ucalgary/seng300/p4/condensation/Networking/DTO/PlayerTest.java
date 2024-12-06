package ca.ucalgary.seng300.p4.condensation.Networking.DTO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class PlayerTest {
    private Player player;

    @Before
    public void setUp() throws Exception {
        // Initialize a new Player object before each test
        player = new Player("testUser", 1, 1500);
    }

    @After
    public void tearDown() throws Exception {
        // Clean up resources after each test
        player = null;
    }

    @Test
    public void testConstructor() {
        // Ensure the Player object is created with correct values
        assertNotNull("Player object should not be null", player);
        assertEquals("Username should be initialized correctly", "testUser", player.getUserName());
        assertEquals("Leaderboard position should be initialized correctly", 1, player.getLeaderBoardPosition());
        assertEquals("Elo should be initialized correctly", 1500, player.getElo());
        assertNotNull("PlayerID should not be null", player.getPlayerID());
    }

    @Test
    public void testSetUserName() {
        // Change the username and verify the update
        player.setUserName("newUser");
        assertEquals("Username should be updated correctly", "newUser", player.getUserName());
    }

    @Test
    public void testSetLeaderBoardPosition() {
        // Change the leaderboard position and verify the update
        player.setLeaderBoardPosition(2);
        assertEquals("Leaderboard position should be updated correctly", 2, player.getLeaderBoardPosition());
    }

    @Test
    public void testSetElo() {
        // Change the Elo rating and verify the update
        player.setElo(1600);
        assertEquals("Elo should be updated correctly", 1600, player.getElo());
    }

    @Test
    public void testUniquePlayerID() {
        // Create another Player and verify IDs are unique
        Player anotherPlayer = new Player("anotherUser", 5, 1200);
        assertFalse("Each Player should have a unique ID", player.getPlayerID().equals(anotherPlayer.getPlayerID()));
    }
}
