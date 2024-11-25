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

import java.util.*;

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
    private Map<UUID, Client> loggedInClients;
    private Map<UUID, List<GameDataListener>> sessionListeners = new HashMap<>();


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
        loggedInClients = new HashMap<>();

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
     * @param loginPayload JSONObject containing username and password
     * @return If a success the string on the player UUID and if not some kind of fail message ( exception)
     * @throws IllegalArgumentException If the login payload is invalid
     * PreCondition : the client calls this network method with the parameter : loginPayload
     * PostCondition : the method verfies login information, adds client to logged in list, and returns a playerID json string back to client
     *
     */
    public String playerLoginHandler(JSONObject loginPayload) throws IllegalArgumentException {
        try {

            // Extract username and password
            String username = (String) loginPayload.get("username");
            String password = (String) loginPayload.get("password");

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
     * createUser This will be used by frontend to add player to tne user database
     * @param signupPayload
     * PreCondition : The client sends the signupPayload
     * PostCondition : A player object is instantiated and a json representation of the client is saved to the user database
     * NOTE : This would interact with methods in the Database class so not sure how this would be Tested.
     * */

    public void  createUser(JSONObject signupPayload){

    }



    /**
     * Retrieves a logged-in client by a playerID. This can be used during game or other actions that require client logged
     * in precondition.
     * @param playerId The playerId
     * @return The Client object, or null if not logged in
     * PreCondition : whoever needs to access a specific logged in client would pass in a player UUID
     * PostCondition : The method should return a client object if logged in, or throw an exception
     */
    public Client getClient(UUID playerId) {
        Client client = loggedInClients.get(playerId);
        if (client == null) {
            throw new IllegalArgumentException("No client found for player ID: " + playerId);
        }
        return client;
    }


    /**
     * subscribeToGameSession this method is used to subscribe a client to a list of listeners that map to particular sessionID
     * @param sessionID
     * @param client
     *
     */
    public void subscribeToGameSession(UUID sessionID, Client client) {
        // Check if the sessionID already exists in the map
        List<GameDataListener> listeners = sessionListeners.get(sessionID);

        // If no listeners exist for this sessionID, create a new list
        if (listeners == null) {
            listeners = new ArrayList<>();
            sessionListeners.put(sessionID, listeners);
        }
        // Add the listener to the list
        listeners.add(client);

        // Print a confirmation message
        System.out.println("Client subscribed to session: " + sessionID);
    }






    // ---------------------------------------------------------------------------------------------------
    // Network methods that handles queueing, creating, and starting game session sequence.
    // ---------------------------------------------------------------------------------------------------

    /**
     * clientQueuePlayer called by the controller to recieve a queuedPlayer
     * @param queuedMemoObject This has playerUUID, gameType, and the elo of the player
     * PreCondition: client instantiates a queuedMemo which has information on the playerID and the gameType they selected
     * PostCondition: able to verify the client is logged in, and them to pool and returns a matchedGameSession object, which eventually sends created
     *               gameSession information to the GameSessionManager and finally uuids to the backend via (sendCreatedGameSession)
     */
    public void clientQueuePlayer(QueuedPlayerMemo queuedMemoObject){
        UUID playerUUID = queuedMemoObject.PlayerID();
        Client client = getClient(playerUUID);                                    // reference to logged in client
        matchmakingPool.addPlayer(queuedMemoObject);                                // adds the playerMemo to a matchmakingPool
        GameSession matchedGameSession = matchmakingService.findAndMatchPlayers();
        network.sendCreatedGameSession(matchedGameSession);   // This will match first available player in the list to eligible player2
    }

    /**
     * sendCreatedGameSession is helper to clientQueuePlayer, this will send the gameSession object to the GameSession Manager
     * @param gameSession object that has all the info. about the ongoing game it corresponds to.
     * PreCondition: recieves a gameSession returned by matchmaking service
     * PostCondition: adds gameSession to the sessions hashmap in GameSessionManager, and sends the PlayerReferences to the backend
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
        // This remains a stub for now, however functionality would require a backend listener
        // this should send to something to a subscribed Backend Listener
    }


    // ---------------------------------------------------------------------------------------------------
    // Network methods that handle intra game session methods and logic.
    // ---------------------------------------------------------------------------------------------------

    /**
     * sendGameDataToClients sends the clientPayload = sessionID + gameState payload to the clients
     * @param clientPayload
     */
    public void sendGameDataToClients(JSONObject clientPayload){
        // This remains a stub, but implementation would require having client listeners you implement GamaData Listener
    }

    /**
     * recieveGameDataFromClient recieves the gameState json payload (includes sessionID) from client
     * - calls GameSessionManager instance partFrontEndPayload method -> returns gameSession object,  gameState json(only gameState data)
     */
    public void recieveGameDataFromClient(JSONObject recieveClientPayload){
        // recieve a payload json formatted String
        // GameSession gameSession = retured by GSM;
    }

    // ---------------------------------------------------------------------------------------------------
    // Network methods that handle end game session methods and logic.
    // ---------------------------------------------------------------------------------------------------


    // we need to





}