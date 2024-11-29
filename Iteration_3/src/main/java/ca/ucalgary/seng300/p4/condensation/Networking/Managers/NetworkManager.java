package ca.ucalgary.seng300.p4.condensation.Networking.Managers;
import ca.ucalgary.seng300.p4.condensation.Backend.Handlers.GameHandler;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.QueuedPlayerMemo;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.DatabaseBuilder;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameEventType;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Listeners.GameDataListener;
import ca.ucalgary.seng300.p4.condensation.Networking.Services.ChatService;
import ca.ucalgary.seng300.p4.condensation.Networking.Services.MatchmakingPool;
import ca.ucalgary.seng300.p4.condensation.Networking.Services.MatchmakingService;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.Client;
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

    private final String DATA_FILE = "/Users/samuelergando/seng300-f24-project/Iteration_3/src/main/java/ca/ucalgary/seng300/p4/condensation/Networking/Database/Leaderboard/Leaderboard.json";
    private Map<UUID, Client> loggedInClients;                                         // frontend will use this to access the client listeners for communication with backend
    private Map<UUID, List<GameDataListener>> sessionListeners = new HashMap<>();      // each session ID has a list of 2 client subscribers, that are notified on each phase of a game
    private final ChatService chatService;





    /**
     * constructor for network manager instance, when creating a new one
     */
    private NetworkManager() throws IOException, ParseException {
        matchmakingPool = new MatchmakingPool();
        matchmakingService = new MatchmakingService(matchmakingPool);   // matchmaking pool is already populated with queuedPlayerMemos.

        // loop through the pool list to create clients for all the playerIDs in the matchmaking pool.
        for( QueuedPlayerMemo queuedPlayer : matchmakingPool.getPool()){
            UUID currentPlayerID = queuedPlayer.PlayerID();
            Client currentClient = new Client(currentPlayerID);

            // now all the queued players have a corresponding client listener that's loggedIn mimmicking the behaviour of a network
            loggedInClients.put(currentPlayerID, currentClient);


        }
        gameSessionManager = new GameSessionManager();

        chatService = new ChatService();
        sessionListeners = new HashMap<>();
        loggedInClients = new HashMap<>();

    }

    /**
     * Singleton instance getter
     */
    public static NetworkManager getNetworkInstance() throws IOException, ParseException {

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
    public JSONObject playerLoginHandler(JSONObject loginPayload) throws IllegalArgumentException, IOException, ParseException {

        // Extract username and password
        String username = (String) loginPayload.get("username");
        String password = (String) loginPayload.get("password");

        // Check the database for username and password
        JSONObject validatePlayer = validateUserCredentials(username, password);
        UUID playerUUID = (UUID) validatePlayer.get("playerUUID");

        if (playerUUID == null) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        // Check if the user is already logged in
        if (loggedInClients.containsKey(playerUUID)) {
            throw new IllegalArgumentException("User already logged in");
        }

        // Create and store the Client object
        Client client = new Client(playerUUID);
        loggedInClients.put(playerUUID, client);

        // The response pay,load that frontEnd will recieve
        JSONObject loginResponse = new JSONObject();
        loginResponse.put("playerID", playerUUID.toString());
        loginResponse.put("username", username);

        return loginResponse;   // returns the json payload to client

    }

    public JSONObject validateUserCredentials(String username, String password) throws IOException, ParseException {

        JSONObject jsonUsername = Leaderboard.getInstance(DATA_FILE).findUserByName(username);

        JSONObject jsonPassword =Leaderboard.getInstance(DATA_FILE).findUserByPassword(password);

        if (jsonUsername.equals(jsonPassword)) {
            return jsonPassword;
        }
        // return null because user does not exist, probably a better way to handle but we need to iterate fas
        return null;

    }

    /**
     * createUser This will be used by frontend to add player to tne user database
     * @param signupPayload
     * PreCondition : The client sends the signupPayload
     * PostCondition : A player object is instantiated and a json representation of the client is saved to the user database
     * NOTE : This would interact with methods in the Database class so not sure how this would be Tested.
     * */


    public String signUpHandler (JSONObject signupPayload) throws IOException, ParseException {
        String username = (String) signupPayload.get("username");
        String password = (String) signupPayload.get("password");

        Leaderboard.getInstance(DATA_FILE).createUser(username, password);

        // we could also add a check to see if the username already exists in our database
        return String.format("Successfully created user with username %s", username);
    }

    public String editUsername(UUID Player){
        // retune some so
        return null;
    }

    public String editPassword(UUID Player){
        return null;
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
     * @param queuedPlayerPayload this has the playerID and the gameType enum
     * PreCondition: client instantiates a queuedMemo which has information on the playerID and the gameType they selected
     * PostCondition: able to verify the client is logged in, and them to pool and returns a matchedGameSession object, which eventually sends created
     *               gameSession information to the GameSessionManager and finally uuids to the backend via (sendCreatedGameSession)
     */
    public JSONObject handleQueuePlayer(JSONObject queuedPlayerPayload) throws IOException, ParseException {

        // Extracts all neccessary Player1 information from database and payload and instantiates the queuedPlayer
        String playerIDString = (String) queuedPlayerPayload.get("playerID");
        UUID player1UUID = UUID.fromString(playerIDString);
        GameTypes gameType = (GameTypes) queuedPlayerPayload.get("gameType");
        JSONObject player1 = Leaderboard.getInstance(DATA_FILE).findUserById(player1UUID);
        int elo = (int) player1.get("elo");
        QueuedPlayerMemo queuedPlayerMemo1 = new QueuedPlayerMemo(player1UUID, gameType, elo);           // now can make the queu

        // to do the matchmaking logic, remember to make sure that we add Player1 to the start of the matchmaking pool
        // we need to do the .random for the matchmaking pool
        matchmakingPool.addPlayer(queuedPlayerMemo1, 0);                                // how do we make sure we add this to index 0
        GameSession matchedGameSession = matchmakingService.findAndMatchPlayers();

        // do we need to gamehandler.startGame*() here not sure what this is lets finalize this sequence

        // client listener for the second player should already be instantiated in the constructor
        //
        network.sendCreatedGameSession(matchedGameSession);
        UUID sessionID = matchedGameSession.getSessionId();
        UUID player2UUID = matchedGameSession.getPlayer2ID();
        UUID getFirstPlayerID = matchedGameSession.getGameHandler().getFirstPlayerID();  // waiting on backend to implement this in gameHandler

        // preparing the json payload to be sent to the frontEnd
        JSONObject matchedSessionResponse = new JSONObject();
        matchedSessionResponse.put("sessionID", sessionID);
        matchedSessionResponse.put("player1ID", player1UUID);
        matchedSessionResponse.put("player2ID", player2UUID);
        matchedSessionResponse.put("currentPlayerTurn", getFirstPlayerID);
        matchedSessionResponse.put("gameType", gameType);

        // also everytime client sends network a payload they will need to give sessionID so we can find the gameSession object and update the game handler field in the GameSession's object.
        // This way this.gamehandler recieves a payload and should then notify the frontend using the client listeners.
        return matchedSessionResponse;
    }


    /**
     * handleChallengedQueuePlayer handles creating a gameSession following a profile lookup and challenging a plauer
     * @param challengeQueuedPlayer
     * @return
     */
    public JSONObject handleChallengedQueuePlayer(JSONObject challengeQueuedPlayer){
        return null;
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
    public void sendGameDataToClients(JSONObject clientPayload, UUID sessionID, GameEventType gameEventType){
        // The appropriate gameSession listener will have been notified
        notifyGameSessionClients(sessionID, gameEventType, clientPayload);          // Notify all subscribed clients

    }

    /**
     * recieveGameDataFromClient recieves the gameState json payload (includes sessionID) from client which they will directly call via network singleton.
     * - calls GameSessionManager instance partFrontEndPayload method -> returns gameSession object,  gameState json(only gameState data)
     */
    public void receiveGameDataFromClient(JSONObject receiveClientPayload){

        UUID SessionID = (UUID) receiveClientPayload.get("SessionID");
        String moveDataPayload  = receiveClientPayload.toJSONString();

        // this receive the current gameSession from the sessions hashmap which should still be active
        GameSession gameSession = gameSessionManager.getGameSessionBySessionID(SessionID);
        // this will retrieve the current state of the board
        GameHandler gameHandler = gameSession.getGameHandler();
        // pass frontend state to the gameHandler makeMove
        // this will notify the appropriate method listener in gameSession
        gameHandler.makeMove(moveDataPayload);

    }

    // ---------------------------------------------------------------------------------------------------
    // Network methods that handle end game session methods and logic.
    // ---------------------------------------------------------------------------------------------------


    // will delegate this to nathan and ani

    public JSONArray recievePlayerMessage(JSONObject recievePlayerMessage) {

        // PSEUDOCODE :
            // we also need the sessionID so we know which clients to notify
            // so chatService needs a reference to player 2 as well so that it has it's username in the message
            // P1 - UUID
            // we can find their username by UUID here which we can the pass into chat messages
            // Message
            // sendGameDataToClients
            // which means we will have to subscribe them to some sort of listeners lists




        //TODO: Need to get the reference of the user name, client should store UUID information on their following login and after creating a session.
        // JSONArray messages = chatService.sendChatMessage(msg, "username");
        // return messages;
        return null;
    }


}