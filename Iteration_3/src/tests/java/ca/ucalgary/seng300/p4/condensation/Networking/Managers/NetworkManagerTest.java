package ca.ucalgary.seng300.p4.condensation.Networking.Managers;

import ca.ucalgary.seng300.p4.condensation.Backend.Handlers.GameHandler;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.Client;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.Player;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.QueuedPlayerMemo;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameEventType;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Services.ChatService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.*;

public class NetworkManagerTest {
    private GameSessionManager gameSessionManager;
    private ChatService chatService;
    private NetworkManager networkManager;

    private Client player1;
    private Client player2;

    private QueuedPlayerMemo playerMemo1;
    private QueuedPlayerMemo playerMemo2;
    private GameSession gameSession;


    @Before
    public void setUp() throws Exception {
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

    @Test
    public void testSendChatToClient() {
        //create a new JSON object
        JSONArray chatMessage = new JSONArray();
        chatMessage.add("Player 1: Hello!");


    }
}