package ca.ucalgary.seng300.p4.condensation.condensation.Networking.Managers;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.Client;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.Player;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.QueuedPlayerMemo;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Services.ChatManager;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Services.MatchmakingPool;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Services.MatchmakingService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NetworkManager {


    /**
     * Network Manager acts as facade which abstracts a way syncing between game, chat and other elements.
     * Network manager implements singleton design pattern for network
     */

    private static NetworkManager network;
    private final MatchmakingService matchmakingService;
    private final GameSessionManager gameSessionManager;
    private final ChatManager chatManager;
    private final MatchmakingPool matchmakingPool;

    // client listener lists mapped to specific to their gameSessions
   // private final Map<UUID, List<GameDataListener>> sessionListeners;
    private final JSONArray userDatabase;
    private final String userDatabaseFilePath = "ca/ucalgary/seng300/p4/condensation/condensation/Networking/Database/PlayerInformation/UserDatabase.json";

    private final Map<UUID, Client> loggedInClients;



    /**
     * constructor for network manager instance, when creating a new one
     */
    private NetworkManager(){
        this.loggedInClients = new HashMap<>();
        matchmakingPool = new MatchmakingPool();
        matchmakingService = new MatchmakingService(matchmakingPool);
        gameSessionManager = new GameSessionManager();
        chatManager = new ChatManager();
        this.userDatabase = loadUserDatabase();

//        sessionListeners = new HashMap<>();

    }

    /**
     * Singleton instance getter
     */
    public static NetworkManager getNetworkInstance() {
        // creates network instance if one doesn't exist
        if (network == null) {
            network = new NetworkManager();
        }
        return network;
    }


    /**
     * Loads the user database from the file into jsonArray that will be parsed for login and written to during signup
     * @return JSONArray containing user data
     */
    private JSONArray loadUserDatabase() {
        try (FileReader reader = new FileReader(userDatabaseFilePath)) {
            JSONParser parser = new JSONParser();
            return (JSONArray) parser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }


    // ---------------------------------------------------------------------------------------------------
    // Network methods that handles userLogin and userSignup
    // ---------------------------------------------------------------------------------------------------

    /**
     * Handles a login request from the frontend.
     * @param loginPayload JSON string containing username and password
     * @return A response with the PlayerUUID is successfull,otherwise responds with error message
     * @throws IllegalArgumentException If the login payload is invalid
     */
    public String playerLoginHandler(String loginPayload) throws IllegalArgumentException {
        try {
            // Parse the JSON payload
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(loginPayload);

            // Extract username and password
            String username = (String) json.get("username");
            String password = (String) json.get("password");

            // Check the database for username and password
            // will return PlayerUUID if user found in database
            UUID playerUUID = validateUserCredentials(username, password);

            if (playerUUID == null) {
                throw new IllegalArgumentException("Invalid username or password");
            }

            // Check if the user is already logged in
            if (loggedInClients.containsKey(playerUUID)) {
                throw new IllegalArgumentException("User already logged in");
            }

            // Create and store the Client object
            Client client = new Client(playerUUID, username);
            loggedInClients.put(playerUUID, client);

            JSONObject response = new JSONObject();
            response.put("playerID", playerUUID.toString());
            String responseString = response.toJSONString();

            // Return a success response
            System.out.println("User logged in: " + username + " (" + playerUUID + ")");
            return responseString;
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid login payload", e);
        }
    }

    public UUID validateUserCredentials(String username, String password) {
    }
}