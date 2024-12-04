package ca.ucalgary.seng300.p4.condensation.Networking.Services;

import ca.ucalgary.seng300.p4.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.QueuedPlayerMemo;

import java.util.List;

public class MatchmakingService {
    private final MatchmakingPool pool;
    private final int rankThreshold = 5000;

    /**
     * The constructor for MatchmakingService
     * @param pool the matchmaking pool
     */
    public MatchmakingService(MatchmakingPool pool) {
        this.pool = pool;
    }

    /**
     * Matches players from the queue based on their game type and Elo rating.
     * Creates a new game session when a suitable match is found.
     * @return A {@link GameSession} object representing the matched players.
     * @throws RuntimeException If no suitable match is found.
     */
    public GameSession findAndMatchPlayers() {
        //get the pool of players
        List<QueuedPlayerMemo> players = pool.getPool();
        GameSession ourGamesession;
        System.out.println(pool.getPool().toString());


        //first for loop will go through all the players starting with the first player in queue
        //if the first player does not get a match (from the second for loop), it will move on to looking for a match
        //for the second player in queue, and so on
        for (int j = 0; j < players.size(); j++) {

            //get the player we are setting as "player1" for now in this iteration
            QueuedPlayerMemo player1 = players.get(j);

            //the minimum rank that a possible match for player 1 should have
            int minRank = player1.Elo() - rankThreshold;
            //the maximum rank that a possible match for player 1 should have
            int maxRank = player1.Elo() + rankThreshold;

            //use a for loop to go through all the other players and match them
            for (int i = 1; i < players.size(); i++) { //to do less, we can skip the first player in queue because we don't need to check them anymore after 1st iteration
                //update player 2
                QueuedPlayerMemo player2 = players.get(i);
                if (player1.gameType() != player2.gameType()) {
                    continue;
                }

                //if player1 and player2 are not the same player, check if it could be a possible match
                if (!player1.equals(player2)) {
                    //check if that player can be a possible match
                    if (player2.Elo() >= minRank && player2.Elo() <= maxRank) {
                        //match found!

                        //remove both of those players from queue
                        pool.removePlayer(player1);
                        pool.removePlayer(player2);

                        System.out.println("Player1IDM:" + player1.PlayerID());
                        System.out.println("Player2IDM:" + player2.PlayerID());


                        ourGamesession = new GameSession(player1, player2);
                         return ourGamesession;

                    }
                }
            }
        }
        throw new RuntimeException("No game found");
    }
}
