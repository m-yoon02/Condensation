package ca.ucalgary.seng300.p4.condensation.Networking.Services;

import ca.ucalgary.seng300.p4.condensation.Networking.DTO.Player;

import java.util.ArrayList;
import java.util.List;

public class MatchmakingPool {
    private List<Player> pool = new ArrayList<>();

    /**
     * This method adds a player into the matchmaking pool
     * @param player the player being added
     */
    public void addPlayer(Player player) {
        pool.add(player);
        System.out.println("Player Added: " + player.getPlayerID());
    }

    /**
     * This method removes a player from the matchmaking pool
     * @param player the player being removed
     */
    public void removePlayer(Player player) {
        pool.remove(player);
    }

    /**
     * This is a getter method for the matchmaking pool. Used in MatchmakingService.
     * @return the pool list
     */
    public List<Player> getPool() {
        System.out.println(pool.size());
        return pool;

    }
}
