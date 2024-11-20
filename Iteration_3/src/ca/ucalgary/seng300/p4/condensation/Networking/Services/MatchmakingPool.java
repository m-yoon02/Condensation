package ca.ucalgary.seng300.p4.condensation.Networking.Services;

import ca.ucalgary.seng300.p4.condensation.Networking.DTO.Player;

import java.util.List;

public class MatchmakingPool {
    private List<Player> pool;

    /**
     * Constructor for MatchmakingPool
     * @param pool takes the pool list
     */
    public MatchmakingPool(List<Player> pool) {
        this.pool = pool;
    }

    /**
     * This method adds a player into the matchmaking pool
     * @param player the player being added
     */
    public void addPlayer(Player player) {

    }

    /**
     * This method removes a player from the matchmaking pool
     * @param player the player being removed
     */
    public void removePlayer(Player player) {

    }

    /**
     * This is a getter method for the matchmaking pool. Used in MatchmakingService.
     * @return the pool list
     */
    public List<Player> getPool() {
        return pool;
    }
}
