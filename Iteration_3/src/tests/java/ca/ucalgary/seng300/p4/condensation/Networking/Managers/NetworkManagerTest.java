package ca.ucalgary.seng300.p4.condensation.Networking.Managers;

import ca.ucalgary.seng300.p4.condensation.Networking.DTO.Client;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.Player;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.QueuedPlayerMemo;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Services.ChatService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;

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

}