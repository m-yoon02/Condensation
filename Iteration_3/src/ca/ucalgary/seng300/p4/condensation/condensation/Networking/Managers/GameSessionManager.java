package ca.ucalgary.seng300.p4.condensation.condensation.Networking.Managers;

import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameSessionManager {


    // GameSession Manager will be a singleton like Network and will keep a hashmap called sessions

    private static GameSessionManager gameSessionManager;
    private final Map<UUID, GameSession> sessionsMap;
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

    // a method that parses the payload and extracts the sessionID
    // sessionID -> gameSession from hashmap
    // return a gameSession Object to networking method
    /**
     * addGameSession
     * @param gameSession
     */
    public void addGameSession(GameSession gameSession){
        // add gameSession object to the hashmap
        UUID sessionID = gameSession.getSessionId();
        gameSessionManager.sessionsMap.put(sessionID, gameSession);

    }

    /**
     * removeGameSession removes gameSession following on gameEnd
     * @param sessionID
     */
    public void removeGameSession(UUID sessionID) {
        // remove gameSession object from hashmap
        gameSessionManager.sessionsMap.remove(sessionID);
    }



}
