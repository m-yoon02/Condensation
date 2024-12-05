package ca.ucalgary.seng300.p4.condensation.Networking.Managers;

import ca.ucalgary.seng300.p4.condensation.Networking.DTO.Client;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.Networking.Services.ChatService;
import org.junit.After;
import org.junit.Before;

public class NetworkManagerTest {
    private GameSessionManager gameSessionManager;
    private ChatService chatService;
    private NetworkManager networkManager;

    private Client player1;
    private Client player2;
    private GameSession gameSession;


    @Before
    public void setUp() throws Exception {
        //initialize all mock managers and objects
        gameSessionManager = new GameSessionManager();
        chatService = new ChatService();


    }

    @After
    public void tearDown() throws Exception {
    }


}