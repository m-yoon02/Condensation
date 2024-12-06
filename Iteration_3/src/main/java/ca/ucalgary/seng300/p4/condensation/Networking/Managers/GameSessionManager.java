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
        System.out.println("Active Game Sessions:");
        for (Map.Entry<UUID, GameSession> entry : sessionsMap.entrySet()) {
            UUID sessionId = entry.getKey();
            GameSession gameSession = entry.getValue();

            System.out.println("Session ID: " + sessionId);
            System.out.println("Player 1 ID: " + gameSession.getPlayer1ID());
            System.out.println("Player 2 ID: " + gameSession.getPlayer2ID());
            System.out.println("------------------------------------");
        }

        GameSession gameSession = sessionsMap.get(SessionID);
        System.out.println("Is gameSession null:" + gameSession);
        return gameSession;     // should return the gameSession object
    }

    /**
     * Adds a game session to the session manager.
     * @param gameSession The {@link GameSession} to be added.
     */
    public void addGameSession(GameSession gameSession){
        // add gameSession object to the hashmap
        UUID sessionID = gameSession.getSessionId();
        System.out.println(gameSessionManager);
        sessionsMap.put(sessionID, gameSession);

    }

    /**
     * Removes a game session from the session manager.
     * @param sessionID The unique identifier of the game session to be removed.
     */
    public void removeGameSession(UUID sessionID) {
        // remove gameSession object from hashmap
        sessionsMap.remove(sessionID);
    }



}
