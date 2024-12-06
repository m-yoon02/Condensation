package ca.ucalgary.seng300.p4.condensation.Networking.Managers;

import ca.ucalgary.seng300.p4.condensation.Backend.Handlers.GameHandler;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.Client;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.Player;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.QueuedPlayerMemo;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.LeaderboardTest;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameEventType;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Listeners.GameDataListener;
import ca.ucalgary.seng300.p4.condensation.Networking.Services.ChatService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
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


    public static final String MOCK_LEADERBOARD_FILE = "mockLeaderboard.json";
    public static final String TEMP_PLAYER_FILE = "tempPlayers.json";

    private GameDataListener gameDataListener; //mocking the GameDataListener interface


    @Before
    public void setUp() throws Exception {
        // Initialize the leaderboard with the mock file
        NetworkManager.LEADERBOARD_FILE = MOCK_LEADERBOARD_FILE;

        NetworkManager.PLAYER_ID_FILE = TEMP_PLAYER_FILE;

        JSONArray mockData = new JSONArray();

        // Add sample user data
        JSONObject user1 = new JSONObject();
        user1.put("playerUUID", "efbee4fb-36d3-4551-a111-6c19a0695416");
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
        user2.put("playerUUID", "71491e49-f970-4a78-b8dd-0901df523fe6");
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
        user3.put("playerUUID", "2f137003-8885-4a71-bf8c-34f69225cabc");
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

        JSONObject user4 = new JSONObject();
        user4.put("playerUUID", "fb67ee61-2f75-415e-9bd0-046d7f654cf7");
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

        JSONObject user5 = new JSONObject();
        user5.put("playerUUID", "c0d6679a-4ad7-4346-8f16-191ec6627f3a");
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

        JSONObject user6 = new JSONObject();
        user6.put("playerUUID", "e080d46b-d51b-402c-9505-cdb509220144");
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

        JSONObject user7 = new JSONObject();
        user7.put("playerUUID", "f4f3a399-8b1a-462e-8501-6847c5595554");
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

        JSONObject user8 = new JSONObject();
        user8.put("playerUUID", "db0d2e68-8a10-4fc7-82d6-7440261d4f02");
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


        networkManager = NetworkManager.getNetworkInstance();
        leaderboard = Leaderboard.getInstance();





        // create a mock JSON file for fetchUserFromNewPLayerJSON test
        JSONObject playerData = new JSONObject();
        playerData.put("username", "testPlayer");

        try (FileWriter writer = new FileWriter(TEMP_PLAYER_FILE)) {
            writer.write(playerData.toJSONString());

        }

        //create the players
        player1 = new Client(UUID.randomUUID());
        player2 = new Client(UUID.randomUUID());

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


    @Test
    public void testPlayerLoginHandler_ValidCredentials() throws IOException, ParseException {
        JSONObject loginPayload = new JSONObject();
        loginPayload.put("username", "player1");
        loginPayload.put("password", "strongPassword");

        JSONObject loginResponse = networkManager.playerLoginHandler(loginPayload);

        assertEquals("player1", loginResponse.get("username"));
        assertNotNull("Player ID should not be null", loginResponse.get("playerID"));
        UUID playerUUID = UUID.fromString((String) loginResponse.get("playerID"));
        assertTrue("Player should be logged in", networkManager.getLoggedInClients().containsKey(playerUUID));
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
    public void testValidateUserCredentials_InvalidCredentials() throws IOException, ParseException {
        // Validate credentials with an invalid password
        JSONObject validatedUser = networkManager.validateUserCredentials("player2", "wrongpassword");

        // Verify that the user is null
        assertNull("Validated user should be null for invalid credentials", validatedUser);
    }

    @Test
    public void testSignUpHandler() throws IOException, ParseException {
        JSONObject signUpPayload = new JSONObject();
        signUpPayload.put("username", "Samuel");
        signUpPayload.put("password", "2005");

        String response = networkManager.signUpHandler(signUpPayload);

        assertEquals("Successfully created user with username Samuel", response);

    }

    @Test
    public void editUsernameTest() throws  IOException, ParseException{
        JSONObject editUsernamePayload = new JSONObject();
        String Sam1 = "player1";
        editUsernamePayload.put("playerID", "efbee4fb-36d3-4551-a111-6c19a0695416");
        editUsernamePayload.put("NewUsername", "player1");

        String response = networkManager.editUsername(editUsernamePayload);
        assertEquals("You have successfully changed your username to player1", response);

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
    public void testHandleQueuePlayer() throws IOException, ParseException {

        JSONObject queuedPlayerPayload = new JSONObject();
        String playerID = "efbee4fb-36d3-4551-a111-6c19a0695416";
        queuedPlayerPayload.put("playerID", playerID);
        queuedPlayerPayload.put("gameType", GameTypes.CHECKERS);

        String response = networkManager.handleQueuePlayer(queuedPlayerPayload);

        assertNotNull("Response should not be null", response);
        JSONObject responseJson = (JSONObject) new org.json.simple.parser.JSONParser().parse(response);

        // Verify the returned session details
        assertNotNull("Session ID should not be null", responseJson.get("sessionID"));
        assertEquals("efbee4fb-36d3-4551-a111-6c19a0695416", playerID);
        assertNotNull("Player2 UUID should not be null (matched player)", responseJson.get("player2ID"));
        assertNotNull("Current player turn should not be null", responseJson.get("currentPlayerTurn"));
        assertEquals("Game type should match", GameTypes.CHECKERS.toString(), responseJson.get("gameType"));

        GameTypes gameType = GameTypes.valueOf((String) responseJson.get("gameType"));
        // Verify the matchmaking pool and game session manager
        GameSession createdSession = gameSessionManager.getGameSessionBySessionID(
                UUID.fromString((String) responseJson.get("sessionID"))
        );
        assertNull("Game session should not be created", createdSession);
        assertEquals("Game type should match in session", GameTypes.CHECKERS, gameType);
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

    @After
    public void poo() throws IOException {
        FileWriter writer = new FileWriter(LeaderboardTest.MOCK_LEADERBOARD_FILE, false);
        writer.write("""
                [
                  {
                    "checkersElo": 1200,
                    "lossInTicTacToe": 0,
                    "elo": 1200,
                    "lossInConnect4": 0,
                    "tictactoeElo": 1200,
                    "winsInCheckers": 1,
                    "winsInTicTacToe": 1,
                    "connect4Elo": 1200,
                    "playerUUID": "efbee4fb-36d3-4551-a111-6c19a0695416",
                    "password": "password1",
                    "gamesPlayedConnect4": 1,
                    "winsInConnect4": 1,
                    "recentMatches": [],
                    "gamesPlayedTicTacToe": 1,
                    "lossInCheckers": 0,
                    "username": "player1",
                    "gamesPlayedCheckers": 1
                  },
                  {
                    "checkersElo": 1300,
                    "lossInTicTacToe": 1,
                    "elo": 1300,
                    "lossInConnect4": 1,
                    "tictactoeElo": 1300,
                    "winsInCheckers": 1,
                    "winsInTicTacToe": 1,
                    "connect4Elo": 1300,
                    "playerUUID": "71491e49-f970-4a78-b8dd-0901df523fe6",
                    "password": "password2",
                    "gamesPlayedConnect4": 2,
                    "winsInConnect4": 1,
                    "recentMatches": [],
                    "gamesPlayedTicTacToe": 2,
                    "lossInCheckers": 1,
                    "username": "player2",
                    "gamesPlayedCheckers": 2
                  },
                  {
                    "checkersElo": 1400,
                    "lossInTicTacToe": 1,
                    "elo": 1400,
                    "lossInConnect4": 1,
                    "tictactoeElo": 1400,
                    "winsInCheckers": 2,
                    "winsInTicTacToe": 2,
                    "connect4Elo": 1400,
                    "playerUUID": "2f137003-8885-4a71-bf8c-34f69225cabc",
                    "password": "password3",
                    "gamesPlayedConnect4": 3,
                    "winsInConnect4": 2,
                    "recentMatches": [],
                    "gamesPlayedTicTacToe": 3,
                    "lossInCheckers": 1,
                    "username": "player3",
                    "gamesPlayedCheckers": 3
                  },
                  {
                    "checkersElo": 1500,
                    "lossInTicTacToe": 1,
                    "elo": 1500,
                    "lossInConnect4": 1,
                    "tictactoeElo": 1500,
                    "winsInCheckers": 3,
                    "winsInTicTacToe": 3,
                    "connect4Elo": 1500,
                    "playerUUID": "fb67ee61-2f75-415e-9bd0-046d7f654cf7",
                    "password": "password4",
                    "gamesPlayedConnect4": 4,
                    "winsInConnect4": 3,
                    "recentMatches": [],
                    "gamesPlayedTicTacToe": 4,
                    "lossInCheckers": 1,
                    "username": "player4",
                    "gamesPlayedCheckers": 4
                  },
                  {
                    "checkersElo": 1600,
                    "lossInTicTacToe": 1,
                    "elo": 1600,
                    "lossInConnect4": 1,
                    "tictactoeElo": 1600,
                    "winsInCheckers": 4,
                    "winsInTicTacToe": 4,
                    "connect4Elo": 1600,
                    "playerUUID": "c0d6679a-4ad7-4346-8f16-191ec6627f3a",
                    "password": "password5",
                    "gamesPlayedConnect4": 5,
                    "winsInConnect4": 4,
                    "recentMatches": [],
                    "gamesPlayedTicTacToe": 5,
                    "lossInCheckers": 1,
                    "username": "player5",
                    "gamesPlayedCheckers": 5
                  },
                  {
                    "checkersElo": 1700,
                    "lossInTicTacToe": 1,
                    "elo": 1700,
                    "lossInConnect4": 1,
                    "tictactoeElo": 1700,
                    "winsInCheckers": 5,
                    "winsInTicTacToe": 5,
                    "connect4Elo": 1700,
                    "playerUUID": "e080d46b-d51b-402c-9505-cdb509220144",
                    "password": "password6",
                    "gamesPlayedConnect4": 6,
                    "winsInConnect4": 5,
                    "recentMatches": [],
                    "gamesPlayedTicTacToe": 6,
                    "lossInCheckers": 1,
                    "username": "player6",
                    "gamesPlayedCheckers": 6
                  },
                  {
                    "checkersElo": 1800,
                    "lossInTicTacToe": 1,
                    "elo": 1800,
                    "lossInConnect4": 1,
                    "tictactoeElo": 1800,
                    "winsInCheckers": 6,
                    "winsInTicTacToe": 6,
                    "connect4Elo": 1800,
                    "playerUUID": "f4f3a399-8b1a-462e-8501-6847c5595554",
                    "password": "password7",
                    "gamesPlayedConnect4": 7,
                    "winsInConnect4": 6,
                    "recentMatches": [],
                    "gamesPlayedTicTacToe": 7,
                    "lossInCheckers": 1,
                    "username": "player7",
                    "gamesPlayedCheckers": 7
                  },
                  {
                    "checkersElo": 1900,
                    "lossInTicTacToe": 1,
                    "elo": 1900,
                    "lossInConnect4": 1,
                    "tictactoeElo": 1900,
                    "winsInCheckers": 7,
                    "winsInTicTacToe": 7,
                    "connect4Elo": 1900,
                    "playerUUID": "db0d2e68-8a10-4fc7-82d6-7440261d4f02",
                    "password": "password8",
                    "gamesPlayedConnect4": 8,
                    "winsInConnect4": 7,
                    "recentMatches": [],
                    "gamesPlayedTicTacToe": 8,
                    "lossInCheckers": 1,
                    "username": "player8",
                    "gamesPlayedCheckers": 8
                  }
                ]
                """);
        writer.flush();
        writer.close();

        System.out.println("poo is being called lol");
    }


}