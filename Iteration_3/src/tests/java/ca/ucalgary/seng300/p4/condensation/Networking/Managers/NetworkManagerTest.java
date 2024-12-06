package ca.ucalgary.seng300.p4.condensation.Networking.Managers;

import ca.ucalgary.seng300.p4.condensation.Backend.Handlers.GameHandler;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.Client;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.Player;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.QueuedPlayerMemo;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameEventType;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Listeners.GameDataListener;
import ca.ucalgary.seng300.p4.condensation.Networking.Services.ChatService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.LeaderboardTest.MOCK_LEADERBOARD_FILE;
import static ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.LeaderboardTest.TEMP_PLAYER_FILE;
import static org.junit.Assert.*;

public class NetworkManagerTest {
    private GameSessionManager gameSessionManager;
    private ChatService chatService;
    private NetworkManager networkManager;
    private Leaderboard leaderboard;

    private Client player1;
    private Client player2;

    private QueuedPlayerMemo playerMemo1;
    private QueuedPlayerMemo playerMemo2;
    private GameSession gameSession;

    private GameDataListener gameDataListener; //mocking the GameDataListener interface


    @Before
    public void setUp() throws Exception {

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
        user2.put("gamesPlayedCheckers", 2);
        user2.put("winsInCheckers", 1);
        user2.put("gamesPlayedTicTacToe", 2);
        user2.put("winsInTicTacToe", 1);
        user2.put("tictactoeElo", 1300);
        user2.put("gamesPlayedConnect4", 2);
        user2.put("winsInConnect4", 1);
        user2.put("connect4Elo", 1300);
        user2.put("elo", 1300);
        user2.put("lossInCheckers", 1);
        user2.put("lossInTicTacToe", 1);
        user2.put("lossInConnect4", 1);
        mockData.add(user2);




        JSONObject user3 = new JSONObject();
        user3.put("playerUUID", UUID.randomUUID().toString());
        user3.put("username", "player3");
        user3.put("password", "password3");
        user3.put("checkersElo", 1400);
        user3.put("recentMatches", new JSONArray());
        user3.put("gamesPlayedCheckers", 3);
        user3.put("winsInCheckers", 2);
        user3.put("gamesPlayedTicTacToe", 3);
        user3.put("winsInTicTacToe", 2);
        user3.put("tictactoeElo", 1400);
        user3.put("gamesPlayedConnect4", 3);
        user3.put("winsInConnect4", 2);
        user3.put("connect4Elo", 1400);
        user3.put("elo", 1400);
        user3.put("lossInCheckers", 1);
        user3.put("lossInTicTacToe", 1);
        user3.put("lossInConnect4", 1);
        mockData.add(user3);

        // Create user4 with the same fields as user1 but different values
        JSONObject user4 = new JSONObject();
        user4.put("playerUUID", UUID.randomUUID().toString());
        user4.put("username", "player4");
        user4.put("password", "password4");
        user4.put("checkersElo", 1500);
        user4.put("recentMatches", new JSONArray());
        user4.put("gamesPlayedCheckers", 4);
        user4.put("winsInCheckers", 3);
        user4.put("gamesPlayedTicTacToe", 4);
        user4.put("winsInTicTacToe", 3);
        user4.put("tictactoeElo", 1500);
        user4.put("gamesPlayedConnect4", 4);
        user4.put("winsInConnect4", 3);
        user4.put("connect4Elo", 1500);
        user4.put("elo", 1500);
        user4.put("lossInCheckers", 1);
        user4.put("lossInTicTacToe", 1);
        user4.put("lossInConnect4", 1);
        mockData.add(user4);

// Create user5 with the same fields as user1 but different values
        JSONObject user5 = new JSONObject();
        user5.put("playerUUID", UUID.randomUUID().toString());
        user5.put("username", "player5");
        user5.put("password", "password5");
        user5.put("checkersElo", 1600);
        user5.put("recentMatches", new JSONArray());
        user5.put("gamesPlayedCheckers", 5);
        user5.put("winsInCheckers", 4);
        user5.put("gamesPlayedTicTacToe", 5);
        user5.put("winsInTicTacToe", 4);
        user5.put("tictactoeElo", 1600);
        user5.put("gamesPlayedConnect4", 5);
        user5.put("winsInConnect4", 4);
        user5.put("connect4Elo", 1600);
        user5.put("elo", 1600);
        user5.put("lossInCheckers", 1);
        user5.put("lossInTicTacToe", 1);
        user5.put("lossInConnect4", 1);
        mockData.add(user5);

// Create user6 with the same fields as user1 but different values
        JSONObject user6 = new JSONObject();
        user6.put("playerUUID", UUID.randomUUID().toString());
        user6.put("username", "player6");
        user6.put("password", "password6");
        user6.put("checkersElo", 1700);
        user6.put("recentMatches", new JSONArray());
        user6.put("gamesPlayedCheckers", 6);
        user6.put("winsInCheckers", 5);
        user6.put("gamesPlayedTicTacToe", 6);
        user6.put("winsInTicTacToe", 5);
        user6.put("tictactoeElo", 1700);
        user6.put("gamesPlayedConnect4", 6);
        user6.put("winsInConnect4", 5);
        user6.put("connect4Elo", 1700);
        user6.put("elo", 1700);
        user6.put("lossInCheckers", 1);
        user6.put("lossInTicTacToe", 1);
        user6.put("lossInConnect4", 1);
        mockData.add(user6);

// Create user7 with the same fields as user1 but different values
        JSONObject user7 = new JSONObject();
        user7.put("playerUUID", UUID.randomUUID().toString());
        user7.put("username", "player7");
        user7.put("password", "password7");
        user7.put("checkersElo", 1800);
        user7.put("recentMatches", new JSONArray());
        user7.put("gamesPlayedCheckers", 7);
        user7.put("winsInCheckers", 6);
        user7.put("gamesPlayedTicTacToe", 7);
        user7.put("winsInTicTacToe", 6);
        user7.put("tictactoeElo", 1800);
        user7.put("gamesPlayedConnect4", 7);
        user7.put("winsInConnect4", 6);
        user7.put("connect4Elo", 1800);
        user7.put("elo", 1800);
        user7.put("lossInCheckers", 1);
        user7.put("lossInTicTacToe", 1);
        user7.put("lossInConnect4", 1);
        mockData.add(user7);

// Create user8 with the same fields as user1 but different values
        JSONObject user8 = new JSONObject();
        user8.put("playerUUID", UUID.randomUUID().toString());
        user8.put("username", "player8");
        user8.put("password", "password8");
        user8.put("checkersElo", 1900);
        user8.put("recentMatches", new JSONArray());
        user8.put("gamesPlayedCheckers", 8);
        user8.put("winsInCheckers", 7);
        user8.put("gamesPlayedTicTacToe", 8);
        user8.put("winsInTicTacToe", 7);
        user8.put("tictactoeElo", 1900);
        user8.put("gamesPlayedConnect4", 8);
        user8.put("winsInConnect4", 7);
        user8.put("connect4Elo", 1900);
        user8.put("elo", 1900);
        user8.put("lossInCheckers", 1);
        user8.put("lossInTicTacToe", 1);
        user8.put("lossInConnect4", 1);
        mockData.add(user8);

        try (FileWriter writer = new FileWriter(MOCK_LEADERBOARD_FILE)) {
            writer.write(mockData.toJSONString());
        }

        // Initialize the leaderboard with the mock file
        networkManager.LEADERBOARD_FILE = MOCK_LEADERBOARD_FILE;
        leaderboard = Leaderboard.getInstance();

        // create a mock JSON file for fetchUserFromNewPLayerJSON test
        JSONObject playerData = new JSONObject();
        playerData.put("username", "testPlayer");

        try (FileWriter writer = new FileWriter(TEMP_PLAYER_FILE)) {
            writer.write(playerData.toJSONString());

        }
        networkManager.PLAYER_ID_FILE = TEMP_PLAYER_FILE;
        //create the players
        player1 = new Client(UUID.randomUUID());
        player2 = new Client(UUID.randomUUID());

        networkManager = new NetworkManager();

        //Manually add the clients to the NetworkManager
        networkManager.getLoggedInClients().put(player1.getPlayerId(), player1);
        networkManager.getLoggedInClients().put(player2.getPlayerId(), player2);

        playerMemo1 = new QueuedPlayerMemo(player1.getPlayerId(), GameTypes.CHECKERS, 100);
        playerMemo2 = new QueuedPlayerMemo(player2.getPlayerId(), GameTypes.CHECKERS, 110);

        //initialize all mock managers and objects
        gameSessionManager = new GameSessionManager();
        chatService = new ChatService();

        //create a game session for these players
        gameSession = new GameSession(playerMemo1, playerMemo2);

        gameSessionManager.addGameSession(gameSession);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetNetworkInstance() throws IOException, ParseException {
        // Get the first instance of NetworkManager
        NetworkManager instance1 = NetworkManager.getNetworkInstance();
        // Get the second instance of NetworkManager
        NetworkManager instance2 = NetworkManager.getNetworkInstance();
        // Validate that both instances are the same (Singleton)
        assertSame("getNetworkInstance should return the same instance", instance1, instance2);
        // Validate that the instance is not null
        assertNotNull("getNetworkInstance should not return null", instance1);
    }

    @Test
    public void testPlayerLoginHandler_ValidCredentials() throws IOException, ParseException {
        JSONObject loginPayload = new JSONObject();
        loginPayload.put("username", "player1");
        loginPayload.put("password", "password1");

        JSONObject loginResponse = networkManager.playerLoginHandler(loginPayload);

        assertEquals("player1", loginResponse.get("username"));
        assertNotNull("Player ID should not be null", loginResponse.get("playerID"));
        UUID playerUUID = UUID.fromString((String) loginResponse.get("playerID"));
        assertTrue("Player should be logged in", networkManager.getLoggedInClients().containsKey(playerUUID));
    }

    @Test
    public void testValidateUserCredentials_InvalidCredentials() throws IOException, ParseException {
        // Validate credentials with an invalid password
        JSONObject validatedUser = networkManager.validateUserCredentials("player1", "wrongpassword");

        // Verify that the user is null
        assertNull("Validated user should be null for invalid credentials", validatedUser);
    }

    @Test
    public void testIsPlayerOnline() {
        UUID playerID1 = UUID.randomUUID();
        UUID playerID2 = UUID.randomUUID();

        Client mockClient1 = new Client(playerID1);
        networkManager.getLoggedInClients().put(playerID1, mockClient1);

        boolean isOnlinePlayer1 = networkManager.isPlayerOnline(playerID1);
        assertTrue("Player1 should be recognized as online", isOnlinePlayer1);

        boolean isOnlinePlayer2 = networkManager.isPlayerOnline(playerID2);
        assertFalse("Player2 should be recognized as offline", isOnlinePlayer2);

        networkManager.getLoggedInClients().clear(); // Clear the map
        boolean isOnlinePlayer1AfterClear = networkManager.isPlayerOnline(playerID1);
        assertFalse("Player1 should be offline after clearing loggedInClients", isOnlinePlayer1AfterClear);
    }






    @Test
    public void testSendCreatedGameSession() {
        //call sendCreatedGameSession with the mock session
        networkManager.sendCreatedGameSession(gameSession);

        //verify that the session is added to the GameSessionManager
        assertNotNull(gameSessionManager.getGameSessionBySessionID(gameSession.getSessionId()));
    }

    @Test
    public void testPlayerSubscribed() {
        networkManager.sendCreatedGameSession(gameSession);

        assertTrue("Player 1 should be subscribed",
                networkManager.getSessionListeners().get(gameSession.getSessionId()).contains(player1));


        assertTrue("Player 2 should be subscribed",
                networkManager.getSessionListeners().get(gameSession.getSessionId()).contains(player2));
    }

//    @Test
//    public void testReceiveGameDataFromClient_ValidPayload() {
//        // Arrange
//        JSONObject payload = new JSONObject();
//        payload.put("SessionID", sessionID.toString());
//        payload.put("move", "e2e4");
//
//        // Act
//        networkManager.receiveGameDataFromClient(payload);
//
//        // Assert
//        assertEquals("e2e4", ((GameHandler) gameHandler).getLastMove());
//    }
//

    @Test
    public void sendGameDataToClients() throws IOException {
        //make a mock JSONObject for clientPayload
        JSONObject clientPayload = new JSONObject();
        clientPayload.put("move", "e2e4");
        clientPayload.put("SessionID", gameSession.getSessionId().toString());

        //check if the clients in the session were updated or the correct game handler was called by checking if the state has been modified
        GameSession session = gameSessionManager.getGameSessionBySessionID(gameSession.getSessionId());
        GameHandler gameHandler = session.getGameHandler();

        //stimulate a call in an actual game
        System.out.println("Before sending data, current turn: " + gameHandler.getFirstPlayerID());
        networkManager.sendGameDataToClients(clientPayload, gameSession.getSessionId(), GameEventType.TURN_END, GameTypes.CHECKERS);
        System.out.println("After sending data, current turn: " + gameHandler.getFirstPlayerID());

        UUID currentPlayerTurn = gameHandler.getFirstPlayerID();  //first player should have been changed after the move
        assertEquals(player1.getPlayerId(), currentPlayerTurn);  //player 1's turn should have ended
        assertNotEquals(player2.getPlayerId(), currentPlayerTurn);  //now it should be player 2's turn
    }

//    @T
//
//    @Test
//    public void testReceiveGameDataFromClient_ValidPayload() {
//        // Arrange
//        JSONObject payload = new JSONObject();
//        payload.put("SessionID", sessionID.toString());
//        payload.put("move", "e2e4");
//
//        // Act
//        networkManager.receiveGameDataFromClient(payload);
//
//        // Assert
//        assertEquals("e2e4", ((GameHandler) gameHandler).getLastMove());
//    }

    //helper method to mock sessionListeners map in NetworkManager
    private HashMap<UUID, List<GameDataListener>> mockSessionListenersMap() {
        HashMap<UUID, List<GameDataListener>> sessionListeners = new HashMap<>();
        sessionListeners.put(gameSession.getSessionId(), List.of(gameDataListener)); // Add a single mock listener to the session
        return sessionListeners;
    }



    @Test
    public void testSendChatToClient() {

        // NetworkManager.getNetworkInstance().handleQueuePlayer();
        //create a new JSON object
        JSONArray clientResponsePayload = new JSONArray();
        clientResponsePayload.add("Hello!");


        //call networkManager to send this chat data
        networkManager.sendChatDataToClients(clientResponsePayload, gameSession.getSessionId(), GameEventType.CHAT_MESSAGE_RECEIVED);

        //
        GameSession session = gameSessionManager.getGameSessionBySessionID(gameSession.getSessionId());

    }



    @Test
    public void editPassword () throws IOException, ParseException {
        JSONObject payload = new JSONObject();
        payload.put("playerID", "efbee4fb-36d3-4551-a111-6c19a0695416");
        payload.put("password", "strongPassword");

        String psw = networkManager.editPassword(payload);
        assertEquals("You have successfully changed your password to strongPassword", psw);
    }


}