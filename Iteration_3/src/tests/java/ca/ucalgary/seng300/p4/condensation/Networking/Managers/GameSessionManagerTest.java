package ca.ucalgary.seng300.p4.condensation.Networking.Managers;

import ca.ucalgary.seng300.p4.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.QueuedPlayerMemo;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

import static org.junit.Assert.*;

public class GameSessionManagerTest {

    private GameSessionManager manager;
    private GameSession testGameSession1;
    private GameSession testGameSession2;
    private UUID testSessionID1;
    private UUID testSessionID2;

    @Before
    public void setUp() throws Exception {
        // want to set up the singleton reference
        manager = GameSessionManager.getInstance();


        // kinda creating some sort of mock data for GameSession object
        QueuedPlayerMemo playerMemo1 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.CHECKERS, 1000);
        QueuedPlayerMemo playerMemo2 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.CHECKERS, 1100);
        testGameSession1 = new GameSession(playerMemo1, playerMemo2);
        testSessionID1 = testGameSession1.getSessionId();

        // doing the same but with a different game
        QueuedPlayerMemo playerMemo3 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.TICTACTOE, 1200);
        QueuedPlayerMemo playerMemo4 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.TICTACTOE, 1300);
        testGameSession2 = new GameSession(playerMemo3, playerMemo4);
        testSessionID2 = testGameSession2.getSessionId();



    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getInstance() {
        GameSessionManager anotherManager = GameSessionManager.getInstance();
        // want to check that both instances are the same
        assertNotNull(manager);
        assertNotNull(anotherManager);
        assertSame("GameSessionManager should return the same instance", manager, anotherManager);
    }

    @Test
    public void getGameSessionBySessionID() {
    }

    @Test
    public void addGameSession() {
    }

    @Test
    public void removeGameSession() {
    }
}