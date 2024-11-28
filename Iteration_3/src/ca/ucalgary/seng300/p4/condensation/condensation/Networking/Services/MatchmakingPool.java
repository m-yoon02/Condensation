package ca.ucalgary.seng300.p4.condensation.condensation.Networking.Services;

import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.Player;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.QueuedPlayerMemo;

import java.util.ArrayList;
import java.util.List;

public class MatchmakingPool {
    private static List<QueuedPlayerMemo> pool = new ArrayList<>();

    public MatchmakingPool() {



        // we need to hard code 3 random queuedPlayerMemos

    }

    /**
     * This method adds a player into the matchmaking pool
     * @param player the player being added
     */
    public void addPlayer(QueuedPlayerMemo player) {
        pool.add(player);
        System.out.println("Player Added: " + player.PlayerID());
    }

    /**
     * This method removes a player from the matchmaking pool
     * @param player the player being removed
     */
    public void removePlayer(QueuedPlayerMemo player) {
        pool.remove(player);
    }

    /**
     * This is a getter method for the matchmaking pool. Used in MatchmakingService.
     * @return the pool list
     */
    public List<QueuedPlayerMemo> getPool() {
        System.out.println(pool.size());
        return pool;

    }
}
