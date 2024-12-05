package ca.ucalgary.seng300.p4.condensation.Networking.Services;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.QueuedPlayerMemo;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;

import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;
import java.util.List;

import static org.junit.Assert.*;

public class MatchmakingPoolTest {

    private MatchmakingPool matchmakingPool;

    private Leaderboard leaderboardMock;

    @Before
    public void setUp() throws Exception {
        leaderboardMock = new Leaderboard();
        Leaderboard.setInstance(leaderboardMock);

        matchmakingPool = new MatchmakingPool();
    }

    @After
    public void tearDown() throws Exception {
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