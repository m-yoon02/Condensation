package ca.ucalgary.seng300.p4.condensation.Networking.Managers;

import ca.ucalgary.seng300.p4.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameSessionManager {


    // GameSession Manager will be a singleton like Network and will keep a hashmap called sessions

    private static final GameSessionManager gameSessionManager = new GameSessionManager();
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
        return gameSessionManager;
    }


    /**
     * Retrieves a game session by its unique session ID.
     * @param SessionID The unique identifier of the game session.
     * @return The {@link GameSession} object associated with the given session ID, or {@code null} if not found.
     */
    public GameSession getGameSessionBySessionID(UUID SessionID){
        System.out.println("GAMESESSION IS IN HERE" + SessionID);
        GameSession gameSession = sessionsMap.get(SessionID);
        return gameSession;     // should return the gameSession object
    }

    /**
     * Adds a game session to the session manager.
     * @param gameSession The {@link GameSession} to be added.
     */
    public void addGameSession(GameSession gameSession){
        // add gameSession object to the hashmap
        UUID sessionID = gameSession.getSessionId();
        System.out.println("add Session ID: " +  sessionID);
        System.out.println(gameSessionManager);
        sessionsMap.put(sessionID, gameSession);

    }

    /**
     * Removes a game session from the session manager.
     * @param sessionID The unique identifier of the game session to be removed.
     */
    public void removeGameSession(UUID sessionID) {
        // remove gameSession object from hashmap
        gameSessionManager.sessionsMap.remove(sessionID);
    }



}
