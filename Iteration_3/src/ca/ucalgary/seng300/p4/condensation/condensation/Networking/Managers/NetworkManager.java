package ca.ucalgary.seng300.p4.condensation.condensation.Networking.Managers;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.QueuedPlayerMemo;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Listeners.GameDataListener;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Services.ChatManager;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Services.MatchmakingPool;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Services.MatchmakingService;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.Client;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.List;
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
    private final Map<UUID, List<GameDataListener>> sessionListeners;
    private final Map<UUID, Client> loggedInClients;

    // we need some kind of database instance here
    private final String userDatabaseFilePath = "ca/ucalgary/seng300/p4/condensation/condensation/Networking/Database/UserDatabase.json";




    /**
     * constructor for network manager instance, when creating a new one
     */
    private NetworkManager(){
        matchmakingPool = new MatchmakingPool();
        matchmakingService = new MatchmakingService(matchmakingPool);
        gameSessionManager = new GameSessionManager();
        chatManager = new ChatManager();
        sessionListeners = new HashMap<>();
        this.loggedInClients = new HashMap<>();

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

    // ---------------------------------------------------------------------------------------------------
    // Network methods that handles userLogin and userSignup
    // ---------------------------------------------------------------------------------------------------


    // do we load a database here when ever we instatiate rthe singletom

    /**
     * Handles a login request from the frontend.
     * @param loginPayload JSON string containing username and password
     * @return If a success the string on the player UUID and if not some kind of fail message
     * @throws IllegalArgumentException If the login payload is invalid
     */
    public String playerLoginHandler(String loginPayload) throws IllegalArgumentException {
        try {
            // parses the JSON payload
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(loginPayload);

            // Extract username and password
            String username = (String) json.get("username");
            String password = (String) json.get("password");

            // Check the database for username and password
            UUID playerUUID = validateUserCredentials(username, password);     // do we define this in network manager or something else
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

            // The response pay,load that frontEnd will recieve
            JSONObject response = new JSONObject();
            response.put("playerID", playerUUID.toString());

            System.out.println("User logged in: " + username + " (" + playerUUID + ")");
            return response.toJSONString(); // Return the response as a JSON string

        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid login payload", e);
            throw new RuntimeException(e);
        }

    }


    /**
     * createUser This will be used by frontend to add player to tne user databse
     * @param signupPayload
     * */

    public void  createUser(String signupPayload) {
        // write to the database or call some database function that does this
    }



    /**
     * Retrieves a logged-in client by a playerID. This can be used during game or other actions that require client logged
     * in precondition.
     * @param playerId The playerId
     * @return The Client object, or null if not logged in
     */
    public Client getClient(UUID playerId) {
        Client client = loggedInClients.get(playerId);
        if (client == null) {
            throw new IllegalArgumentException("No client found for player ID: " + playerId);
        }
        return client;
    }





    // ---------------------------------------------------------------------------------------------------
    // Network methods that handles queueing, creating, and starting game session sequence.
    // ---------------------------------------------------------------------------------------------------

    /**
     * clientQueuePlayer called by the controller to recieve a queuedPlayer
     * @param queuedMemoObject This has playerUUID, gameType, and the elo of the player
     */
    public void clientQueuePlayer(QueuedPlayerMemo queuedMemoObject){
        UUID playerUUID = queuedMemoObject.PlayerID();
        Client client = getClient(playerUUID);                                    // reference to logged in client 
        matchmakingPool.addPlayer(queuedMemoObject);                                // adds the playerMemo to a matchmakingPool
        GameSession matchedGameSession = matchmakingService.findAndMatchPlayers();
        network.sendCreatedGameSession(matchedGameSession);   // This will match first available player in the list to eligible player2
    }

    /**
     * sendCreatedGameSession will send the gameSession object to the GameSession Manager
     * @param gameSession object that has all the info. about the ongoing game it corresponds to.
     * @return
     */
    public void sendCreatedGameSession(GameSession gameSession){
        gameSessionManager.addGameSession(gameSession);
        network.sendPlayerReferences(gameSession.getPlayer1ID(), gameSession.getPlayer2ID());
    }

    /**
     * sendPlayerReferences sends the players UUIDSs to backend for reference, this concludes the starting game session sequence.
     * Following backend will start game and the inter-game session sequence will begin
     * @param Player1 this represents player1's uuid
     * @param Player2 this represents player2's uuid
     */
    public void sendPlayerReferences(UUID Player1, UUID Player2){
        // this should send to some Backend recieve method the UUIDs of Player1 and Player2
    }


    // ---------------------------------------------------------------------------------------------------
    // Network methods that handle intra game session methods and logic.
    // ---------------------------------------------------------------------------------------------------

    /**
     * sendGameDataToClients the readyPayload = sessionID + gameState payload to the clients
     * @param clientPayload t
     */
    public void sendGameDataToClients(String clientPayload){
        // send the clientPayload to the clients
        // there needs to be somekind of recieving method from the frontEnd
    }

    /**
     *recieveGameDataFromClient
     */
    public void recieveGameDataFromClient(){
        // recieve a payload json formatted String
        // GameSession gameSession = retured by GSM;
    }

    // ---------------------------------------------------------------------------------------------------
    // Network methods that handle end game session methods and logic.
    // ---------------------------------------------------------------------------------------------------


    // we need to





}