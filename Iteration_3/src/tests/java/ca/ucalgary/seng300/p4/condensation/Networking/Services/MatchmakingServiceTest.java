package ca.ucalgary.seng300.p4.condensation.Networking.Services;

import ca.ucalgary.seng300.p4.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.QueuedPlayerMemo;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import org.junit.After;
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

    @After
    public void tearDown() {
        pool.clearPool();
    }


    @Test
    public void testFindAndMatchPlayers_Success() throws IOException, org.json.simple.parser.ParseException {
        // Add players to the pool
        QueuedPlayerMemo player1 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.TICTACTOE, 1500);
        QueuedPlayerMemo player2 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.TICTACTOE, 1600);
        QueuedPlayerMemo player3 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.TICTACTOE, 1700);

        // add 3 players because there is 13 already in the pool
        pool.addPlayer(player1);
        pool.addPlayer(player2);
        pool.addPlayer(player3);

        // Find and match players
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        GameSession session = matchmakingService.findAndMatchPlayers();

        // Verify the match
        assertNotNull("Game session should not be null", session);
        assertEquals("Game session should contain player1", player1.PlayerID(), session.getPlayer1ID());
        assertEquals("Game session should contain player2", player2.PlayerID(), session.getPlayer2ID());
    }

    @Test(expected = RuntimeException.class)
    public void testFindAndMatchPlayers_NotEnoughPlayers() throws IOException, org.json.simple.parser.ParseException {

        // Find and match players final session should be null
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        // match will be null because there is not enough players
        GameSession session = matchmakingService.findAndMatchPlayers();

        // Verify the match
        assertNull("Game session should be null", session);
    }

    @Test(expected = RuntimeException.class)
    public void testFindAndMatchPlayers_NoMatch() throws IOException, org.json.simple.parser.ParseException {
        // Add players to the pool
        QueuedPlayerMemo player1 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.TICTACTOE, 1500);
        QueuedPlayerMemo player2 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.CHECKERS, 1600);
        QueuedPlayerMemo player3 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.CONNECT4, 1700);

        // add 3 players because there is 13 already in the pool
        pool.addPlayer(player1);
        pool.addPlayer(player2);
        pool.addPlayer(player3);

        // Find and match players
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        // match will be null because GameTypes are different
        GameSession session = matchmakingService.findAndMatchPlayers();

        // Verify the match
        assertNull("Game session should not null", session);
    }

    @Test(expected = RuntimeException.class)
    public void testFindAndMatchPlayers_EloGap() throws IOException, org.json.simple.parser.ParseException {
        // Add players to the pool
        QueuedPlayerMemo player1 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.CHECKERS, 1000);
        QueuedPlayerMemo player2 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.CHECKERS, 6500);
        QueuedPlayerMemo player3 = new QueuedPlayerMemo(UUID.randomUUID(), GameTypes.CHECKERS, 12000);

        // add 3 players because there is 13 already in the pool
        pool.addPlayer(player1);
        pool.addPlayer(player2);
        pool.addPlayer(player3);

        // Find and match players
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        matchmakingService.findAndMatchPlayers();
        // match will be null because of the elo gap
        GameSession session = matchmakingService.findAndMatchPlayers();

        // Verify the match
        assertNull("Game session should not null", session);
    }
}