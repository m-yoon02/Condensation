package ca.ucalgary.seng300.p4.condensation.Networking.Services;

import ca.ucalgary.seng300.p4.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.Player;

import java.util.List;

public class MatchmakingService {
    private final MatchmakingPool pool;
    private final int rankThreshold;

    /**
     * The constructor for MatchmakingService
     * @param pool
     * @param rankThreshold
     */
    public MatchmakingService(MatchmakingPool pool, int rankThreshold) {
        this.pool = pool;
        this.rankThreshold = rankThreshold;
    }

    /**
     *
     * @return
     */
    public GameSession findAndMatchPlayers() {
        List<Player> players = pool.getPool();

        for (int i = 0; i < players.size(); i++) {

        }

        GameSession gameSession = new GameSession()
    }
}
