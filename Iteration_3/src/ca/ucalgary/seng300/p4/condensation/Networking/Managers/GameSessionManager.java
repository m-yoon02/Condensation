package ca.ucalgary.seng300.p4.condensation.Networking.Managers;

import ca.ucalgary.seng300.p4.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.Player;
import ca.ucalgary.seng300.p4.condensation.Networking.Services.MatchmakingService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameSessionManager {


    // GameSession Manager will be a singleton like Network and will keep a hashmap called sessions

    private static GameSessionManager gameSessionManager;
    private final Map<String, GameSession> sessionsMap;
    private Player player2;


    public GameSessionManager() {               // Stores gamesSession by ID
        this.sessionsMap = new HashMap<>();
    }
    /**
     * getInstance() to retrieve the singleton gameSessionManager instance or create one if it doesn't already exist.
     * @return
     */
    public static GameSessionManager getInstance() {
        if (gameSessionManager == null) {
            gameSessionManager = new GameSessionManager();
        }
        return gameSessionManager;
    }

    // we need to parse the json payload we recieve from frontend
    // parse for the sessionID
    // method here that returns to gameSession obj the unparsed move data

//    /**
//     * When game instiated this creates a session : GameSession object which we will use for game related logic
//     * @param player1 the client playedID to be matched and have a game created for
//     */
//
//    public void createSession(Player player1) {
//        // tentative
//
//            Player Player2;
//
//            // We need to have Matchmaking Service to return Player 2 object to us. Or are we just returning either way I dont mind
//            player2 = MatchmakingService.class(player1); // this class should return a player 2 object.
//            GameSession gameSession = new GameSession(player1, player2);
//
//
//        // creates a session: GameSession( Player 1, Player 2)
//            // Generate a sessionID and put into the sessions Hashmap
//            System.out.println("Player:" + player1.getPlayerID() + " you have been matched with " + player2.getPlayerID());
//
//    }

}
