package ca.ucalgary.seng300.p4.condensation.condensation.Networking.Managers;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.QueuedPlayerMemo;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Database.Leaderboard.Leaderboard;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Enums.GameEventType;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Listeners.GameDataListener;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Services.ChatService;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Services.MatchmakingPool;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Services.MatchmakingService;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.Client;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

public class NetworkManager {


    /**
     * Network Manager acts as facade which abstracts a way syncing between game, chat and other elements.
     * Network manager implements singleton design pattern for network
     */

    private static NetworkManager network;
    private final MatchmakingService matchmakingService;
    private final GameSessionManager gameSessionManager;
    private final MatchmakingPool matchmakingPool;

    // client listener lists mapped to specific to their gameSessions
    private Map<UUID, Client> loggedInClients;                                         // frontend will use this to access the client listeners for communication with backend
    private Map<UUID, List<GameDataListener>> sessionListeners = new HashMap<>();      // each session ID has a list of 2 client subscribers, that are notified on each phase of a game
    private final Leaderboard leaderboard = new Leaderboard();
    private final ChatService chatService;






    /**
     * constructor for network manager instance, when creating a new one
     */
    private NetworkManager() throws IOException, ParseException {
        matchmakingPool = new MatchmakingPool();
        matchmakingService = new MatchmakingService(matchmakingPool);
        gameSessionManager = new GameSessionManager();
        chatService = new ChatService();
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

    /**
     * Handles a login request from the frontend.
     * @param loginPayload JSONObject containing username and password
     * @return If a success the string on the player UUID and if not some kind of fail message ( exception)
     * @throws IllegalArgumentException If the login payload is invalid
     * PreCondition : the client calls this network method with the parameter : loginPayload
     * PostCondition : the method verfies login information, adds client to logged in list, and returns a playerID json string back to client
     *
     */
    public JSONObject playerLoginHandler(JSONObject loginPayload) throws IllegalArgumentException {

        // Extract username and password
        String username = (String) loginPayload.get("username");
        String password = (String) loginPayload.get("password");

        // Check the database for username and password
        JSONObject validatePlayer = validateUserCredentials(username, password);     // do we define this in network manager or something else
        UUID playerUUID = (UUID) validatePlayer.get("playerUUID");

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
        response.put("username", username);

        return response;   // returns the json payload to client

    }

    public JSONObject validateUserCredentials(String username, String password) {
        JSONObject JSONusername = leaderboard.findUserByName(username);
        JSONObject JSONpassword =leaderboard.findUserByPassword(password);

        if (JSONusername.equals(JSONpassword)) {
            return JSONpassword;
        }
        return null;

    }

    /**
     * createUser This will be used by frontend to add player to tne user database
     * @param signupPayload
     * PreCondition : The client sends the signupPayload
     * PostCondition : A player object is instantiated and a json representation of the client is saved to the user database
     * NOTE : This would interact with methods in the Database class so not sure how this would be Tested.
     * */


    public String signUpHandler (JSONObject signupPayload) throws IOException {
        String username = (String) signupPayload.get("username");
        String password = (String) signupPayload.get("password");

        leaderboard.createUser(username, password);
        String responseString = String.format("Successfully created user with username %s and password %s", username, password);
        return responseString;
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

    // ---------------------------------------------------------------------------------------------------
    // Network methods that handles client listener implementation to gameSession for real-time updates
    // ---------------------------------------------------------------------------------------------------

    /**
     * subscribeToGameSession this method is used to subscribe a client to a list of listeners ( 2 client players) that map to particular sessionID
     * @param sessionID The game session's UUID
     * @param client
     */
    public void subscribeToGameSession(UUID sessionID, Client client) {
        // Check if the sessionID already exists in the map
        List<GameDataListener> clientListeners = sessionListeners.get(sessionID);

        // if no listeners exist for this sessionID, create a new list
        if (clientListeners == null) {
            clientListeners = new ArrayList<>();
            sessionListeners.put(sessionID, clientListeners);
        }
        // add the listener to the list
        clientListeners.add(client);

        // Print a confirmation message
        System.out.println("Client subscribed to session: " + sessionID);
    }

    /**
     *Notifies all clients subscribed to a game session about one of 4 different gameEvents
     * @param sessionID
     * @param eventType
     * @param clientResponsePayload
     */
    public void notifyGameSessionClients(UUID sessionID, GameEventType eventType, JSONObject clientResponsePayload) {

        // here we are getting the specific list of clients that are subscribed to the session

        List<GameDataListener> clientListeners = sessionListeners.get(sessionID);

        if (clientListeners != null) {
            for (GameDataListener listener : clientListeners) {

                listener.onGameEvent(eventType, clientResponsePayload);
            }
        } else {
            System.out.println("No clients found in the gameSession: " + sessionID);
        }
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
     * ** Bypasses matchmaking logic in clientQueuePlayer
     * handleQueuePlayer called by the controller to recieve a queuedPlayer
     * @param queuedPlauerPayload This has playerUUID, gameType, and the elo of the player
     */
    public JSONObject handleQueuePlayer(JSONObject queuedPlauerPayload){

        // get the id
        // get the gameType
        // we can get the elo from the database by playerID
        // instantiate a queuedPlayerMemo1
//        UUID player1UUID = queuedPlayerMemo1.PlayerID();
//        GameTypes gameType = queuedPlayerMemo1.gameType();
//        int elo = queuedPlayerMemo1.Elo();

        QueuedPlayerMemo queuedPlayerMemo1 = new QueuedPlayerMemo();

        // we are going to mimic the process of a second client signup and matchmaking process.
        UUID player2UUID = UUID.fromString("345e6789-e89d-45d5-a678-62783628511");
        QueuedPlayerMemo queuedPlayerMemo2 = new QueuedPlayerMemo(player2UUID, gameType, elo + 20);

        Client client1 = getClient(player1UUID);  // this client1
        Client client2 = new Client(player2UUID, "Alan");   // a client2
        loggedInClients.put(player2UUID, client2);                  // so now the second client is logged in and a match has been found


        // this instantiates a new gameSession which also holds the state of the gameHandler, so communication should really occur between
        GameSession matchedGameSession = new GameSession(queuedPlayerMemo1, queuedPlayerMemo2);
        UUID sessionID = matchedGameSession.getSessionId();
        network.sendCreatedGameSession(matchedGameSession);   // This will match first available player in the list to eligible player2



        JSONObject matchedSessionResponse = new JSONObject();
//        {
//            "sessionId": "abcdef12-3456-7890-abcd-ef1234567890",
//                "player1": "player1UUID",
//                "player2": "player2UUID",
//                "currentPlayerTurn" : "player1UUID",  // default turn to player1 so frontend can now start intra gamelogic
//                "gameType": "TICTACTOE"
//        }

        matchedSessionResponse.put("sessionID", sessionID);
        matchedSessionResponse.put("player1", player1UUID);
        matchedSessionResponse.put("player2", player2UUID);
        matchedSessionResponse.put("gameType", gameType);

        // frontend should save these references to player1 and player2 as part of the client side state,  this is how they will access the client listener which is notified by backend via the network.
        // also everytime client sends network a payload they will need to give sessionID so we can find the gameSession object and update the game handler field in the GameSession's object.
        // This way this.gamehandler recieves a payload and should then notify the frontend using the client listeners.
        return matchedSessionResponse;
    }

    /**
     * sendCreatedGameSession is a helper to clientQueuePlayer. This method manages the process of associating a newly created game session with the necessary components and listeners.
     * @param gameSession The GameSession object containing all the details about the ongoing game it corresponds to.
     * PreCondition:
     * - Receives a valid gameSession object returned by the matchmaking service.
     * - Assumes that the players involved in the session are already logged in and exist in the loggedInClients map.
     * PostCondition:
     * - Adds the gameSession to the sessions map in GameSessionManager for tracking active sessions.
     * - Subscribes both player clients to the game session's updates by associating them with the session's listener list.
     * */
    public void sendCreatedGameSession(GameSession gameSession){


        UUID sessionID = gameSession.getSessionId();
        Client player1ID = getClient(gameSession.getPlayer1ID());
        Client player2ID = getClient(gameSession.getPlayer2ID());

        // Subscribe both players to the game session
        // Each client is subscribed as a listener to receive real-time updates for the specified game session
        subscribeToGameSession(sessionID, player1ID);
        subscribeToGameSession(sessionID, player2ID);

        // Add the session to the GameSessionManager
        // this is just a hashmap that contains all active gameSession objects, which are mostly useful when we recieve data from the frontend
        gameSessionManager.addGameSession(gameSession);

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

        // Parse sessionID from the payload
        String sessionIdString = (String) clientPayload.get("sessionId");
        UUID sessionId = UUID.fromString(sessionIdString);
        notifyGameSessionClients(sessionId, eventType, clientPayload);          // Notify all subscribed clients

    }

    /**
     * recieveGameDataFromClient recieves the gameState json payload (includes sessionID) from client
     * - calls GameSessionManager instance partFrontEndPayload method -> returns gameSession object,  gameState json(only gameState data)
     */
    public void recieveGameDataFromClient(String recieveClientPayload){

        JSONObject payload = new JSONObject();

        // we get the sessionID, to get the gameSession object
        // gameSession has a field this.gamehandler
        // -------------------------------------------------
        // recieve a payload json formatted String
        // GameSession gameSession = retured by GSM;
        // gamehandler = gameSession.getGameHandler()
        // gamehandler.makeMove(String moveDataJSON)
        // -- this will notifyTurnEnd or notifyGameEnd
        // -- which will
    }

    // ---------------------------------------------------------------------------------------------------
    // Network methods that handle end game session methods and logic.
    // ---------------------------------------------------------------------------------------------------


    // we need to


    public JSONArray recievePlayerMessage(String msg) {

        //TODO: Need to get the reference of the user name, client should store UUID information on their following login and after creating a session.
        JSONArray messages = chatService.sendChatMessage(msg, "username");
        return messages;
    }


}