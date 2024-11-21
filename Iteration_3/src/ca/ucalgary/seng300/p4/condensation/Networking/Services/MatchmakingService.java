package ca.ucalgary.seng300.p4.condensation.Networking.Services;

import ca.ucalgary.seng300.p4.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.Player;

import java.util.List;

public class MatchmakingService {
    private final MatchmakingPool pool;
    private final int rankThreshold = 100;

    /**
     * The constructor for MatchmakingService
     * @param pool the matchmaking pool
     */
    public MatchmakingService(MatchmakingPool pool) {
        this.pool = pool;
    }

    /**
     * This method basically loops through the pool of players and looks to match players with each other
     * if "player2" is within the rankThreshold of "player1", a match is found and a game session is created.
     * @return a game session for player 1 and player 2
     */
    public GameSession findAndMatchPlayers() {
        //get the pool of players
        List<Player> players = pool.getPool();

        //create a flag to know if a match was found or not, if not, throw an exception
        boolean flag = false;

        //initialize player 1 and player 2
        Player player1 = null;
        Player player2 = null;

        //first for loop will go through all the players starting with the first player in queue
        //if the first player does not get a match (from the second for loop), it will move on to looking for a match
        //for the second player in queue, and so on
        for (int j = 0; j < players.size(); j++) {

            //get the player we are setting as "player1" for now in this iteration
            player1 = players.get(j);

            //the minimum rank that a possible match for player 1 should have
            int minRank = player1.getElo() - rankThreshold;
            //the maximum rank that a possible match for player 1 should have
            int maxRank = player1.getElo() + rankThreshold;

            //use a for loop to go through all the other players and match them
            for (int i = 1; i < players.size(); i++) { //start at i = 1 because we are not checking the first player in queue
                //update player 2
                player2 = players.get(i);

                //check if that player can be a possible match
                if (player2.getElo() >= minRank || player2.getElo() <= maxRank) {
                    //match found!
                    flag = true; //set the flag to true

                    //remove both of those players from queue
                    pool.removePlayer(player1);
                    pool.removePlayer(player2);

                    break; //exit the loop
                }
            }
        }

        //if the flag is still false, no matches were found and throw an exception
        if (!flag) {
            throw new RuntimeException("No possible matches found.");
        }

        GameSession gameSession = new GameSession(player1, player2);

        return gameSession;
    }
}
