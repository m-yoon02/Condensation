package ca.ucalgary.seng300.p4.condensation.Networking.Services;

import ca.ucalgary.seng300.p4.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.QueuedPlayerMemo;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.*;

public class MatchmakingServiceTest {

    private MatchmakingService matchmakingService;
    private MatchmakingPool pool;

    @Before
    public void setUp() throws IOException, org.json.simple.parser.ParseException {
        pool = new MatchmakingPool();
        matchmakingService = new MatchmakingService(pool);
    }

    @Test
    public void testFindAndMatchPlayers_Success() throws IOException, org.json.simple.parser.ParseException {
        // Add players to the pool
        QueuedPlayerMemo player1 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.TICTACTOE, 1500);
        QueuedPlayerMemo player2 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.TICTACTOE, 1600);
        pool.addPlayer(player1);
        pool.addPlayer(player2);

        // Find and match players
        GameSession session = matchmakingService.findAndMatchPlayers();

        // Verify the match
        assertNotNull("Game session should not be null", session);
        assertEquals("Game session should contain player1", session.getPlayer1ID(), player1.PlayerID());
        assertEquals("Game session should contain player2", session.getPlayer2ID(), player2.PlayerID());
    }

    @Test(expected = RuntimeException.class)
    public void testFindAndMatchPlayers_NoMatch() throws IOException, org.json.simple.parser.ParseException {
        // Add players to the pool
        QueuedPlayerMemo player1 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.CHECKERS, 1500);
        QueuedPlayerMemo player2 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.CHECKERS, 1600); // Different game type
        pool.addPlayer(player1);
        pool.addPlayer(player2);

        // Attempt to find and match players
        matchmakingService.findAndMatchPlayers();
    }

    @Test(expected = RuntimeException.class)
    public void testFindAndMatchPlayers_EmptyPool() throws IOException, org.json.simple.parser.ParseException {
        // Attempt to find and match players with an empty pool
        matchmakingService.findAndMatchPlayers();
    }
}