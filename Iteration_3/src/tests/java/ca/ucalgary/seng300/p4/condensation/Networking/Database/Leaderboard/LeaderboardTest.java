package ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard;

import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import org.assertj.core.internal.bytebuddy.implementation.bind.MethodNameEqualityResolver;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.*;

        import java.io.*;
import java.lang.reflect.Field;
import java.util.UUID;

import static org.junit.Assert.*;

public class LeaderboardTest {

    private NetworkManager networkManager;
    private Leaderboard leaderboard;
    private static final String MOCK_LEADERBOARD_FILE = "mockLeaderboard.json";

    @Before
    public void setUp() throws Exception {
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

        try (FileWriter writer = new FileWriter(MOCK_LEADERBOARD_FILE)) {
            writer.write(mockData.toJSONString());
        }

        // Initialize the leaderboard with the mock file
        networkManager.LEADERBOARD_FILE = MOCK_LEADERBOARD_FILE;
        leaderboard = Leaderboard.getInstance();
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
    }


    /**
     * This helper method helps reset the instance of Leaderboard so that it tears down after each test case
     * @throws IOException
     * @throws IllegalAccessException
     */
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
    public void testGetInstanceLeaderboard() throws IOException, ParseException {
        // get the first instance
        Leaderboard firstInstance = Leaderboard.getInstance();
        // get the second instance
        Leaderboard secondInstance = Leaderboard.getInstance();

        // both instances should be the same

        assertNotNull("the first instance should not be null", firstInstance);
        assertNotNull("the second instance should not be null", secondInstance);
        assertEquals("Both instances should be the same", firstInstance, secondInstance);


    }

    @Test
    public void testLoadData() throws IOException, ParseException {
        leaderboard.loadData();
        JSONArray users = leaderboard.getAllUsers();
        assertEquals(5, users.size());
    }

    @Test
    public void testCreateUser() throws IOException {
        leaderboard.createUser("player3", "password3");
        JSONArray users = leaderboard.getAllUsers();
        assertEquals(6, users.size());
    }

    @Test
    public void testFindUserByName() {
        JSONObject user = leaderboard.findUserByName("player1");
        assertNotNull(user);
        assertEquals("player1", user.get("username"));
    }

    @Test
    public void testUpdateForTieGame() {
        UUID player1ID = UUID.randomUUID();
        UUID player2ID = UUID.randomUUID();
        UUID sessionID = UUID.randomUUID();

        leaderboard.updateForTieGame(player1ID, player2ID, sessionID, GameTypes.CHECKERS);

        JSONObject player1 = leaderboard.findUserById(player1ID);
        JSONObject player2 = leaderboard.findUserById(player2ID);

        assertNotNull(player1);
        assertNotNull(player2);

        JSONArray recentMatches1 = (JSONArray) player1.get("recentMatches");
        JSONArray recentMatches2 = (JSONArray) player2.get("recentMatches");

        assertEquals(1, recentMatches1.size());
        assertEquals(1, recentMatches2.size());
    }




    @Test
    public void testUpdateEloForWinningPlayerUsingMockDataForCheckers() throws IOException {
        // retrieve an existing player
        JSONObject user1 = leaderboard.findUserByName("player1");

        // verify that the existing player is not null
        assertNotNull("Player1 should exist in the leaderboard", user1);

        // extracting user 1's player UUID and elo
        UUID player1ID = UUID.fromString((String) user1.get("playerUUID"));
        Number initialElo = (Number) user1.get("checkersElo");
        Number initialWins = (Number) user1.get("winsInCheckers");
        Number initialGamesPlayed = (Number) user1.get("gamesPlayedCheckers");

        // create sessionID
        UUID sessionID = UUID.randomUUID();

        // update elo for the winnning player
        leaderboard.updateEloForWinningPlayer(player1ID, GameTypes.CHECKERS, sessionID);

        // reload user1 to verify elo update
        JSONObject updatedUser1 = leaderboard.findUserByName("player1");

        // verify that elo has increased by 100
        Number updatedElo = (Number) updatedUser1.get("checkersElo");
        assertEquals("the elo score should increase by 100", initialElo.intValue() + 100, updatedElo.intValue());

        // verify that wins have increased by 1
        Number updatedWins = (Number) updatedUser1.get("winsInCheckers");
        assertEquals("Wins should increase by 1", initialWins.intValue() + 1, updatedWins.intValue());

        // verify that games played have increased by 1
        Number updatedGamesPlayed = (Number) updatedUser1.get("gamesPlayedCheckers");
        assertEquals("Games played should increase by 1", initialGamesPlayed.intValue() + 1, updatedGamesPlayed.intValue());

        // verify the recent matches
        JSONArray recentMatches = (JSONArray) updatedUser1.get("recentMatches");
        assertEquals("Recent matches should contain one game", 1, recentMatches.size());
        JSONObject recentMatch = (JSONObject) recentMatches.get(0);
        assertEquals("The match ID should match the session ID.", sessionID.toString(), recentMatch.get("matchID"));
        assertEquals("The game type should be CHECKERS.", GameTypes.CHECKERS.toString(), recentMatch.get("gameType"));
        assertEquals("The outcome should be 'W' for win.", "W", recentMatch.get("outcome"));
        assertEquals("The Elo score should match the updated Elo.", updatedElo, recentMatch.get("currentElo"));




    }



    @Test
    public void testUpdateEloForWinningPlayerUsingMockDataForTicTacToe() throws IOException {
        // retrieve an existing player
        JSONObject user1 = leaderboard.findUserByName("player1");

        // verify that the existing player is not null
        assertNotNull("Player1 should exist in the leaderboard", user1);

        // extracting user 1's player UUID and elo
        UUID player1ID = UUID.fromString((String) user1.get("playerUUID"));
        Number initialElo = (Number) user1.get("tictactoeElo");
        Number initialWins = (Number) user1.get("winsInTicTacToe");
        Number initialGamesPlayed = (Number) user1.get("gamesPlayedTicTacToe");

        // create sessionID
        UUID sessionID = UUID.randomUUID();

        // update elo for the winnning player
        leaderboard.updateEloForWinningPlayer(player1ID, GameTypes.TICTACTOE, sessionID);

        // reload user1 to verify elo update
        JSONObject updatedUser1 = leaderboard.findUserByName("player1");

        // verify that elo has increased by 100
        Number updatedElo = (Number) updatedUser1.get("tictactoeElo");
        assertEquals("the elo score should increase by 100", initialElo.intValue() + 100, updatedElo.intValue());

        // verify that wins have increased by 1
        Number updatedWins = (Number) updatedUser1.get("winsInTicTacToe");
        assertEquals("Wins should increase by 1", initialWins.intValue() + 1, updatedWins.intValue());

        // verify that games played have increased by 1
        Number updatedGamesPlayed = (Number) updatedUser1.get("gamesPlayedTicTacToe");
        assertEquals("Games played should increase by 1", initialGamesPlayed.intValue() + 1, updatedGamesPlayed.intValue());

        // verify the recent matches
        JSONArray recentMatches = (JSONArray) updatedUser1.get("recentMatches");
        assertEquals("Recent matches should contain one game", 1, recentMatches.size());
        JSONObject recentMatch = (JSONObject) recentMatches.get(0);
        assertEquals("The match ID should match the session ID.", sessionID.toString(), recentMatch.get("matchID"));
        assertEquals("The game type should be TICTACTOE.", GameTypes.TICTACTOE.toString(), recentMatch.get("gameType"));
        assertEquals("The outcome should be 'W' for win.", "W", recentMatch.get("outcome"));
        assertEquals("The Elo score should match the updated Elo.", updatedElo, recentMatch.get("currentElo"));




    }


    @Test
    public void testUpdateEloForWinningPlayerUsingMockDataForConnect4() throws IOException {
        // retrieve an existing player
        JSONObject user1 = leaderboard.findUserByName("player1");

        // verify that the existing player is not null
        assertNotNull("Player1 should exist in the leaderboard", user1);

        // extracting user 1's player UUID and elo
        UUID player1ID = UUID.fromString((String) user1.get("playerUUID"));
        Number initialElo = (Number) user1.get("connect4Elo");
        Number initialWins = (Number) user1.get("winsInConnect4");
        Number initialGamesPlayed = (Number) user1.get("gamesPlayedConnect4");

        // create sessionID
        UUID sessionID = UUID.randomUUID();

        // update elo for the winnning player
        leaderboard.updateEloForWinningPlayer(player1ID, GameTypes.CONNECT4, sessionID);

        // reload user1 to verify elo update
        JSONObject updatedUser1 = leaderboard.findUserByName("player1");

        // verify that elo has increased by 100
        Number updatedElo = (Number) updatedUser1.get("connect4Elo");
        assertEquals("the elo score should increase by 100", initialElo.intValue() + 100, updatedElo.intValue());

        // verify that wins have increased by 1
        Number updatedWins = (Number) updatedUser1.get("winsInConnect4");
        assertEquals("Wins should increase by 1", initialWins.intValue() + 1, updatedWins.intValue());

        // verify that games played have increased by 1
        Number updatedGamesPlayed = (Number) updatedUser1.get("gamesPlayedConnect4");
        assertEquals("Games played should increase by 1", initialGamesPlayed.intValue() + 1, updatedGamesPlayed.intValue());

        // verify the recent matches
        JSONArray recentMatches = (JSONArray) updatedUser1.get("recentMatches");
        assertEquals("Recent matches should contain one game", 1, recentMatches.size());
        JSONObject recentMatch = (JSONObject) recentMatches.get(0);
        assertEquals("The match ID should match the session ID.", sessionID.toString(), recentMatch.get("matchID"));
        assertEquals("The game type should be CONNECT4", GameTypes.CONNECT4.toString(), recentMatch.get("gameType"));
        assertEquals("The outcome should be 'W' for win.", "W", recentMatch.get("outcome"));
        assertEquals("The Elo score should match the updated Elo.", updatedElo, recentMatch.get("currentElo"));




    }


    @Test
    public void testFetchTop5Players() throws IOException, IllegalAccessException, ParseException {
        JSONArray mockArray = new JSONArray();

        // adding mock data to the JSON array

        // first reset the leaderboard instance and load the mock data
        resetLeaderboardInstance();
        leaderboard = Leaderboard.getInstance();
        leaderboard.loadData();

        // get the top 5 players
        JSONArray top5Players = leaderboard.fetchTop5Players();

        // verifying

        assertNotNull("The top 5 players should not be null", top5Players);
        assertEquals("The top 5 players should contain 5 players", 5, top5Players.size());

        // testing to see if it puts the top 5 players in descending order based on elo
        String[] expectedOrder = {"player5", "player4", "player3", "player2", "player1"};
        for (int i = 0; i < top5Players.size(); i++) {
            JSONObject player = (JSONObject) top5Players.get(i);
            assertEquals("Players should be in correct order", expectedOrder[i], player.get("username"));
        }

    }

    @Test
    public void testUpdateEloForLosingPlayerUsingMockDataForCheckers() throws IOException {
        // retrieve an existing player
        JSONObject user1 = leaderboard.findUserByName("player1");

        // verify that the existing player is not null
        assertNotNull("Player1 should exist in the leaderboard", user1);

        // Extract player details
        UUID player1ID = UUID.fromString((String) user1.get("playerUUID"));
        Number initialElo = (Number) user1.get("checkersElo");
        Number initialLosses = (Number) user1.get("lossInCheckers");
        Number initialGamesPlayed = (Number) user1.get("gamesPlayedCheckers");

        // create sessionID
        UUID sessionID = UUID.randomUUID();

        // update Elo for losing a game
        leaderboard.updateEloForLosingPlayerCheckers(player1ID, sessionID);

        // reload the user data to verify updates
        JSONObject updatedUser1 = leaderboard.findUserByName("player1");

        // verify that Elo has decreased by 100
        Number updatedElo = (Number) updatedUser1.get("checkersElo");
        assertEquals("The Elo score should decrease by 100", initialElo.intValue() - 100, updatedElo.intValue());

        // verify that losses have increased by 1
        Number updatedLosses = (Number) updatedUser1.get("lossInCheckers");
        assertEquals("Losses should increase by 1", initialLosses.intValue() + 1, updatedLosses.intValue());

        // verify that games played have increased by 1
        Number updatedGamesPlayed = (Number) updatedUser1.get("gamesPlayedCheckers");
        assertEquals("Games played should increase by 1", initialGamesPlayed.intValue() + 1, updatedGamesPlayed.intValue());

        // verify the recent matches
        JSONArray recentMatches = (JSONArray) updatedUser1.get("recentMatches");
        assertEquals("Recent matches should contain one game", 1, recentMatches.size());
        JSONObject recentMatch = (JSONObject) recentMatches.get(0);
        assertEquals("The match ID should match the session ID.", sessionID.toString(), recentMatch.get("matchID"));
        assertEquals("The game type should be CHECKERS.", GameTypes.CHECKERS.toString(), recentMatch.get("gameType"));
        assertEquals("The outcome should be 'L' for loss.", "L", recentMatch.get("outcome"));
        assertEquals("The Elo score should match the updated Elo.", updatedElo, recentMatch.get("currentElo"));
    }


    @Test
    public void testUpdateEloForLosingPlayerUsingMockDataForTicTacToe() throws IOException {
        // retrieve an existing player
        JSONObject user1 = leaderboard.findUserByName("player1");

        // verify that the existing player is not null
        assertNotNull("Player1 should exist in the leaderboard", user1);

        // Extract player details
        UUID player1ID = UUID.fromString((String) user1.get("playerUUID"));
        Number initialElo = (Number) user1.get("tictactoeElo");
        Number initialLosses = (Number) user1.get("lossInTicTacToe");
        Number initialGamesPlayed = (Number) user1.get("gamesPlayedTicTacToe");

        // create sessionID
        UUID sessionID = UUID.randomUUID();

        // update Elo for losing a game
        leaderboard.updateEloForLosingPlayerTictactoe(player1ID, sessionID);

        // reload the user data to verify updates
        JSONObject updatedUser1 = leaderboard.findUserByName("player1");

        // verify that Elo has decreased by 100
        Number updatedElo = (Number) updatedUser1.get("tictactoeElo");
        assertEquals("The Elo score should decrease by 100", initialElo.intValue() - 100, updatedElo.intValue());

        // verify that losses have increased by 1
        Number updatedLosses = (Number) updatedUser1.get("lossInTicTacToe");
        assertEquals("Losses should increase by 1", initialLosses.intValue() + 1, updatedLosses.intValue());

        // verify that games played have increased by 1
        Number updatedGamesPlayed = (Number) updatedUser1.get("gamesPlayedTicTacToe");
        assertEquals("Games played should increase by 1", initialGamesPlayed.intValue() + 1, updatedGamesPlayed.intValue());

        // verify the recent matches
        JSONArray recentMatches = (JSONArray) updatedUser1.get("recentMatches");
        assertEquals("Recent matches should contain one game", 1, recentMatches.size());
        JSONObject recentMatch = (JSONObject) recentMatches.get(0);
        assertEquals("The match ID should match the session ID.", sessionID.toString(), recentMatch.get("matchID"));
        assertEquals("The game type should be TICTACTOE.", GameTypes.TICTACTOE.toString(), recentMatch.get("gameType"));
        assertEquals("The outcome should be 'L' for loss.", "L", recentMatch.get("outcome"));
        assertEquals("The Elo score should match the updated Elo.", updatedElo, recentMatch.get("currentElo"));
    }


    @Test
    public void testUpdateEloForLosingPlayerUsingMockDataForConnect4() throws IOException {
        // retrieve an existing player
        JSONObject user1 = leaderboard.findUserByName("player1");

        // verify that the existing player is not null
        assertNotNull("Player1 should exist in the leaderboard", user1);

        // Extract player details
        UUID player1ID = UUID.fromString((String) user1.get("playerUUID"));
        Number initialElo = (Number) user1.get("tictactoeElo");
        Number initialLosses = (Number) user1.get("lossInTicTacToe");
        Number initialGamesPlayed = (Number) user1.get("gamesPlayedTicTacToe");

        // create sessionID
        UUID sessionID = UUID.randomUUID();

        // update Elo for losing a game
        leaderboard.updateEloForLosingPlayerConnect4(player1ID, sessionID);

        // reload the user data to verify updates
        JSONObject updatedUser1 = leaderboard.findUserByName("player1");

        // verify that Elo has decreased by 100
        Number updatedElo = (Number) updatedUser1.get("connect4Elo");
        assertEquals("The Elo score should decrease by 100", initialElo.intValue() - 100, updatedElo.intValue());

        // verify that losses have increased by 1
        Number updatedLosses = (Number) updatedUser1.get("lossInConnect4");
        assertEquals("Losses should increase by 1", initialLosses.intValue() + 1, updatedLosses.intValue());

        // verify that games played have increased by 1
        Number updatedGamesPlayed = (Number) updatedUser1.get("gamesPlayedConnect4");
        assertEquals("Games played should increase by 1", initialGamesPlayed.intValue() + 1, updatedGamesPlayed.intValue());

        // verify the recent matches
        JSONArray recentMatches = (JSONArray) updatedUser1.get("recentMatches");
        assertEquals("Recent matches should contain one game", 1, recentMatches.size());
        JSONObject recentMatch = (JSONObject) recentMatches.get(0);
        assertEquals("The match ID should match the session ID.", sessionID.toString(), recentMatch.get("matchID"));
        assertEquals("The game type should be CONNECT4.", GameTypes.CONNECT4.toString(), recentMatch.get("gameType"));
        assertEquals("The outcome should be 'L' for loss.", "L", recentMatch.get("outcome"));
        assertEquals("The Elo score should match the updated Elo.", updatedElo, recentMatch.get("currentElo"));
    }





}