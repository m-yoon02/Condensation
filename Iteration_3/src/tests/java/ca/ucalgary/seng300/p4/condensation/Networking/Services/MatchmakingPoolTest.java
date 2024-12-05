package ca.ucalgary.seng300.p4.condensation.Networking.Services;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.QueuedPlayerMemo;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;

import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
}