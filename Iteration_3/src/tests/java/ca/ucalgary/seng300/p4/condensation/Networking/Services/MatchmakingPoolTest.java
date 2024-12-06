package ca.ucalgary.seng300.p4.condensation.Networking.Services;

/**
 * NOTE: for testing fetch4RandomPlayers
 *
 * this file only can test 87% line coverage and 75% branch coverage because of the while
 * loop in this method. This is untestable because if we were to test it in a way for it
 * to satisfy that condition, it will become a never ending loop while in the actual application,
 * this won't happen. That loop is there because it will fetch 4 players, add them to the pool, and stop there.
 */

import ca.ucalgary.seng300.p4.condensation.Networking.DTO.QueuedPlayerMemo;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;

import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.UUID;
import java.util.List;

import static org.junit.Assert.*;

public class MatchmakingPoolTest {

    private MatchmakingPool matchmakingPool;

    private Leaderboard mockLeaderboard;

    private NetworkManager networkManager;

    public static final String MOCK_LEADERBOARD_FILE = "mockLeaderboard.json";
    public static final String TEMP_PLAYER_FILE = "tempPlayers.json";

    @Before
    public void setUp() throws Exception {
        System.out.println("ah");

        // Create a mock JSON file to simulate leaderboard data
        JSONArray mockData = new JSONArray();

        // Add sample user data
        JSONObject user1 = new JSONObject();
        user1.put("playerUUID", UUID.randomUUID().toString());
        user1.put("username", "player1");
        user1.put("password", "password1");
        user1.put("checkersElo", 1200);
        user1.put("recentMatches", new JSONArray());
        user1.put("gamesPlayedCheckers", 1);
        user1.put("winsInCheckers", 1);
        user1.put("gamesPlayedTicTacToe", 1);
        user1.put("winsInTicTacToe", 1);
        user1.put("tictactoeElo", 1200);
        user1.put("gamesPlayedConnect4", 1);
        user1.put("winsInConnect4", 1);
        user1.put("connect4Elo", 1200);
        user1.put("elo", 1200);
        user1.put("lossInCheckers", 0);
        user1.put("lossInTicTacToe", 0);
        user1.put("lossInConnect4", 0);

        mockData.add(user1);

        JSONObject user2 = new JSONObject();
        user2.put("playerUUID", UUID.randomUUID().toString());
        user2.put("username", "player2");
        user2.put("password", "password2");
        user2.put("checkersElo", 1300);
        user2.put("recentMatches", new JSONArray());
        user2.put("elo", 1300);
        mockData.add(user2);

        JSONObject user3 = new JSONObject();
        user3.put("playerUUID", UUID.randomUUID().toString());
        user3.put("username", "player3");
        user3.put("password", "password3");
        user3.put("elo", 1400);
        mockData.add(user3);

        JSONObject user4 = new JSONObject();
        user4.put("playerUUID", UUID.randomUUID().toString());
        user4.put("username", "player4");
        user4.put("password", "password4");
        user4.put("elo", 1500);
        mockData.add(user4);


        JSONObject user5 = new JSONObject();
        user5.put("playerUUID", UUID.randomUUID().toString());
        user5.put("username", "player5");
        user5.put("password", "password5");
        user5.put("elo", 1600);
        mockData.add(user5);

        System.out.println("hi");

        mockLeaderboard = new Leaderboard();
        Leaderboard.setInstance(mockLeaderboard);

        try (FileWriter writer = new FileWriter(MOCK_LEADERBOARD_FILE)) {
            writer.write(mockData.toJSONString());
            System.out.println("a");
        }

        System.out.println("hello");
        // Initialize the leaderboard with the mock file
        networkManager.LEADERBOARD_FILE = MOCK_LEADERBOARD_FILE;
        mockLeaderboard = Leaderboard.getInstance();
        System.out.println("gr");

        // create a mock JSON file for fetchUserFromNewPLayerJSON test
        JSONObject playerData = new JSONObject();
        playerData.put("username", "testPlayer");

        try (FileWriter writer = new FileWriter(TEMP_PLAYER_FILE)) {
            writer.write(playerData.toJSONString());

        }
        System.out.println(mockLeaderboard.getAllUsers());
        networkManager.PLAYER_ID_FILE = TEMP_PLAYER_FILE;

        System.out.println("here");

        matchmakingPool = new MatchmakingPool();
        System.out.println("woo");

        System.out.println("hello");
    }

    @After
    public void tearDown() throws IOException, IllegalAccessException {
        // Clean up the mock file after tests

        // reset leaderboard instance
        resetLeaderboardInstance();


        File file = new File(MOCK_LEADERBOARD_FILE);
        if (file.exists()) {
            file.delete();
        }

        NetworkManager.LEADERBOARD_FILE = null;
        NetworkManager.PLAYER_ID_FILE = null;
    }

    private void resetLeaderboardInstance() throws IOException, IllegalAccessException {
        // cited by ChatGPT: I didn't know how to reset Leaderboard instance without modifying Leaderboard to have some clear method
        Field instanceField = null;
        try {
            instanceField = Leaderboard.class.getDeclaredField("instance");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }

    @Test
    public void testAddPlayerAtEnd() {
        //Arrange
        UUID playerID = UUID.randomUUID();
        QueuedPlayerMemo player = new QueuedPlayerMemo(playerID, GameTypes.CHECKERS, 1500);

        List<QueuedPlayerMemo> prevPool = matchmakingPool.getPool();
        int prevPoolSize = prevPool.size();

        matchmakingPool.addPlayer(player);

        //Assert
        List<QueuedPlayerMemo> pool = matchmakingPool.getPool();

        assertEquals("Pool should have grown by one player after adding", prevPoolSize + 1, pool.size());
        assertEquals("The player ID should match", playerID, pool.get(pool.size()-1).PlayerID());
    }

    @Test
    public void testAddPlayerAtIndex() {
        UUID playerID = UUID.randomUUID();
        QueuedPlayerMemo player = new QueuedPlayerMemo(playerID, GameTypes.CHECKERS, 1500);

        matchmakingPool.addPlayer(player, 0);

        List<QueuedPlayerMemo> pool = matchmakingPool.getPool();
        assertEquals("Pool size should not change", pool.size(), pool.size());
        assertEquals("The player ID should match", playerID, pool.get(0).PlayerID());
    }

    @Test
    public void testRemovePlayerFromMatchmakingPool() {
        UUID playerID = UUID.randomUUID();
        QueuedPlayerMemo player = new QueuedPlayerMemo(playerID, GameTypes.CHECKERS, 1500);

        matchmakingPool.addPlayer(player);

        List<QueuedPlayerMemo> prevPool = matchmakingPool.getPool();
        int addedPoolSize = prevPool.size();

        String result = matchmakingPool.removePlayerFromMatchmakingPool(playerID);

        //assert
        assertEquals("Player should be removed", "You have successfully logged out see you later ", result);
        List<QueuedPlayerMemo> pool = matchmakingPool.getPool();
        assertEquals("Pool should have one less player after removal", addedPoolSize - 1,pool.size());
    }

    @Test
    public void testRemovePlayerFromMatchmakingPoolNotFound() {
        UUID playerID = UUID.randomUUID();
        String result = matchmakingPool.removePlayerFromMatchmakingPool(playerID);

        assertEquals("Player does not exist.", "Player not found in matchmaking pool: "+ playerID, result);
    }

    @Test
    public void testRemovePlayer() {
        UUID playerID = UUID.randomUUID();
        QueuedPlayerMemo player = new QueuedPlayerMemo(playerID, GameTypes.CHECKERS, 1500);

        matchmakingPool.addPlayer(player);

        List<QueuedPlayerMemo> prevPool = matchmakingPool.getPool();
        int addedPoolSize = prevPool.size();

        matchmakingPool.removePlayer(player);

        List<QueuedPlayerMemo> pool = matchmakingPool.getPool();
        assertEquals("Pool should have one less player after removal", addedPoolSize - 1,pool.size());
    }

    @Test
    public void testFetch4RandomPlayers() throws IOException, ParseException {
        matchmakingPool.clearPool();

        QueuedPlayerMemo playerMemo1 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.CHECKERS, 1500);
        QueuedPlayerMemo playerMemo2 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.CONNECT4, 1400);
        QueuedPlayerMemo playerMemo3 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.TICTACTOE, 1200);
        QueuedPlayerMemo playerMemo4 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.CHECKERS, 1600);

        matchmakingPool.addPlayer(playerMemo1);
        matchmakingPool.addPlayer(playerMemo2);
        matchmakingPool.addPlayer(playerMemo3);
        matchmakingPool.addPlayer(playerMemo4);

        //act: Fetch random players
        matchmakingPool.fetch4RandomPlayers();

        //Assert
        List<QueuedPlayerMemo> pool = matchmakingPool.getPool();

        // Check that the correct game types were assigned
        assertEquals("First player should be ", GameTypes.CHECKERS, pool.get(0).gameType());
        assertEquals("Second player should be CONNECT4", GameTypes.CONNECT4, pool.get(1).gameType());
        assertEquals("Third player should be TICTACTOE", GameTypes.TICTACTOE, pool.get(2).gameType());
        assertEquals("Fourth player should be CHECKERS", GameTypes.CHECKERS, pool.get(3).gameType());
    }
}