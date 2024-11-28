package ca.ucalgary.seng300.p4.condensation.Networking.Managers;

import ca.ucalgary.seng300.p4.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.Player;

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


    /**
     * Returns a gameSession obbject to
     * @param SessionID
     * @return
     */
    public GameSession getGameSessionBySessionID(UUID SessionID){

        GameSession gameSession = sessionsMap.get(SessionID);
        return gameSession;     // should return the gameSession object
    }

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
