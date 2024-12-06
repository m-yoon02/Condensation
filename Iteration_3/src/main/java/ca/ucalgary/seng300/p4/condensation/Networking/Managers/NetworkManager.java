package ca.ucalgary.seng300.p4.condensation.Networking.Managers;
import ca.ucalgary.seng300.p4.condensation.Backend.Handlers.GameHandler;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.QueuedPlayerMemo;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameEventType;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Listeners.GameDataListener;
import ca.ucalgary.seng300.p4.condensation.Networking.Services.ChatService;
import ca.ucalgary.seng300.p4.condensation.Networking.Services.MatchmakingPool;
import ca.ucalgary.seng300.p4.condensation.Networking.Services.MatchmakingService;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.Client;
import org.controlsfx.control.tableview2.filter.parser.Parser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final ChatService chatService;

    private static final String ABSOLUTE_PATH;
    private static final String LEADERBOARD_DIR;
    public static String LEADERBOARD_FILE;
    public static String PLAYER_ID_FILE;

    static {
        Path basePath = Paths.get("").toAbsolutePath();
        if (!basePath.endsWith("Iteration_3")) {
            basePath = basePath.resolve("Iteration_3");
        }
        ABSOLUTE_PATH = basePath.normalize().toString();
        LEADERBOARD_DIR = Paths.get(ABSOLUTE_PATH, "src", "main", "java", "ca", "ucalgary", "seng300", "p4", "condensation", "Networking", "Database", "Leaderboard").toString();
        LEADERBOARD_FILE = Paths.get(LEADERBOARD_DIR, "Leaderboard.json").toString();
        PLAYER_ID_FILE = Paths.get(LEADERBOARD_DIR, "playerID.json").toString();
    }

    /**
     * constructor for network manager instance, when creating a new one
     */
    public NetworkManager() throws IOException, ParseException {
        matchmakingPool = new MatchmakingPool();
        matchmakingService = new MatchmakingService(matchmakingPool);   // matchmaking pool is already populated with queuedPlayerMemos.
        loggedInClients = new HashMap<>();

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
     * Handles the player login process by validating credentials, checking login status,
     * and registering the player as logged in.
     * @param loginPayload The player's login credentials.
     * @return A {@link JSONObject} containing the login response with the username and player ID.
     * @throws IllegalArgumentException If the credentials are invalid, the user is already logged in,
     *                                  or if the user cannot be found.
     * @throws IOException              If an error occurs during processing.
     * @throws ParseException           If the data cannot be parsed correctly.
     */
    public JSONObject playerLoginHandler(JSONObject loginPayload) throws IllegalArgumentException, IOException, ParseException {

        // Extract username and password
        String username = (String) loginPayload.get("username");
        String password = (String) loginPayload.get("password");


        // Check the database for username and password
        JSONObject validatePlayer = validateUserCredentials(username, password);

        if(validatePlayer == null){
            throw new IllegalArgumentException("Invalid username or password (valid player is null)");
        }
        UUID playerUUID = UUID.fromString((String) validatePlayer.get("playerUUID"));

        if (playerUUID == null) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        // Check if the user is already logged in
        if (loggedInClients.containsKey(playerUUID)) {
            this.logoutHandler(playerUUID);
        }

        // Create and store the Client object
        Client client = new Client(playerUUID);
        loggedInClients.put(playerUUID, client);

        // The response pay,load that frontEnd will recieve
        JSONObject loginResponse = new JSONObject();
        loginResponse.put("username", username);
        loginResponse.put("playerID", playerUUID.toString());

        return loginResponse;   // returns the json payload to client

    }

    /**
     * Validates user credentials by checking the provided username and password against the leaderboard records.
     * @param username The username to validate.
     * @param password The password to validate.
     * @return A {@link JSONObject} representing the user's data if the credentials are valid; {@code null} otherwise.
     * @throws IOException    If an error occurs during processing.
     * @throws ParseException If the data cannot be parsed correctly.
     */
    public JSONObject validateUserCredentials(String username, String password) throws IOException, ParseException {

        JSONObject jsonUsername = Leaderboard.getInstance().findUserByName(username);
        System.out.println(LEADERBOARD_FILE);
        String databasePassword = (String) jsonUsername.get("password");

        // return null because user does not exist, probably a better way to handle but we need to iterate fas
        if(databasePassword.equals(password)){
            return jsonUsername;
        }

        return null;

    }

    /**
     * Handles the user signup process by creating a new user in the leaderboard.
     * @param signupPayload A {@link JSONObject} containing the user's signup details.
     * @return A message confirming the successful creation of the user.
     * @throws IOException    If an error occurs during processing.
     * @throws ParseException If the payload cannot be parsed correctly.
     */
    public String signUpHandler (JSONObject signupPayload) throws IOException, ParseException {
        String username = (String) signupPayload.get("username");
        String password = (String) signupPayload.get("password");

        Leaderboard.getInstance().createUser(username, password);
        System.out.println(Leaderboard.getInstance().getAllUsers());

        // we could also add a check to see if the username already exists in our database
        return String.format("Successfully created user with username %s", username);
    }


    /**
     * Edits a player's username in the leaderboard.
     * @param editUsernamePayload A {@link JSONObject} containing the player's ID and the new username.
     * @return A message confirming the successful username change.
     * @throws IOException    If an error occurs during processing.
     * @throws ParseException If the payload cannot be parsed correctly.
     */
    public String editUsername(JSONObject editUsernamePayload) throws IOException, ParseException {
        UUID playedID = UUID.fromString((String) editUsernamePayload.get("playerID"));
        String newUsername = (String) editUsernamePayload.get("NewUsername");

        Leaderboard.getInstance().changeName(playedID, newUsername);

        return String.format("You have successfully changed your username to %s", newUsername);
    }

    /**
     * Edits a player's password in the leaderboard.
     * @param editPasswordPayload A {@link JSONObject} containing the player's ID and the new password.
     * @return A message confirming the successful password change.
     * @throws IOException    If an error occurs during processing.
     * @throws ParseException If the payload cannot be parsed correctly.
     */
    public String editPassword(JSONObject editPasswordPayload) throws IOException, ParseException {
        UUID playedID = UUID.fromString((String) editPasswordPayload.get("playerID"));
        String newPassword = (String) editPasswordPayload.get("password");

        Leaderboard.getInstance().changePassword(playedID, newPassword);

        return String.format("You have successfully changed your password to %s", newPassword);
    }


    public String logoutHandler(UUID playerID){

        loggedInClients.remove(playerID);  // remove the client from the logged in clients list
        String response = matchmakingPool.removePlayerFromMatchmakingPool(playerID);
        return response;
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
     * Subscribes a client to a specific game session, allowing the client to receive updates
     * and events related to that session.
     * @param sessionID The ID of the game session to subscribe to.
     * @param client    The client to subscribe to the session.
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
     * Notifies all clients subscribed to a specific game session about a game event.
     * @param sessionID             The ID of the game session.
     * @param eventType             The type of game event to notify clients about.
     * @param clientResponsePayload The data payload associated with the game event.
     * @param gameType              The type of game being played in the session.
     * @throws IOException If an error occurs while notifying clients.
     */
    public void notifyGameSessionClients(UUID sessionID, GameEventType eventType, JSONObject clientResponsePayload, GameTypes gameType) throws IOException {

        // here we are getting the specific list of clients that are subscribed to the session
        List<GameDataListener> clientListeners = sessionListeners.get(sessionID);

        if (clientListeners != null) {
            for (GameDataListener listener : clientListeners) {
                listener.onGameEvent(eventType, clientResponsePayload, gameType);
            }
        } else {
            System.out.println("No clients found in the gameSession: " + sessionID);
        }
    }

    /**
     * Notifies all clients subscribed to a specific game session about a chat message event.
     * @param clientResponsePayload The chat message data to send to the clients.
     * @param sessionID             The ID of the game session.
     * @param gameEventType         The type of game event associated with the chat message.
     */
    public void notifyGameSessionClientsOfMessage(JSONArray clientResponsePayload, UUID sessionID, GameEventType gameEventType) {

        // here we are getting the specific list of clients that are subscribed to the session
        List<GameDataListener> clientListeners = sessionListeners.get(sessionID);

        if (clientListeners != null) {
            for (GameDataListener listener : clientListeners) {
                listener.onChatEvent(gameEventType, clientResponsePayload);    // we need to implement a client listener method for sending chat messages
            }
        } else {
            System.out.println("No clients found in the gameSession: " + sessionID);
        }
    }

    /**
     * Checks if a player is currently online.
     * @param playerID The unique ID of the player to check.
     * @return {@code true} if the player is online; {@code false} otherwise.
     */

    public boolean isPlayerOnline(UUID playerID) {
        // Print out the loggedInClients map
        System.out.println("Logged-in Clients: ");
        loggedInClients.forEach((id, client) ->
                System.out.println("ID: " + id + ", Client: " + client)
        );

        // Check if the playerID exists in the map
        return loggedInClients.containsKey(playerID);
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
    public String handleQueuePlayer(JSONObject queuedPlayerPayload) throws IOException, ParseException {

        System.out.println(queuedPlayerPayload);
        // Extracts all neccessary Player1 information from database and payload and instantiates the queuedPlayer
        String playerIDString = (String) queuedPlayerPayload.get("playerID");
        System.out.println("Poo" + playerIDString);
        UUID player1UUID = UUID.fromString(playerIDString);
        System.out.println("Poo" + player1UUID);

        GameTypes gameType = (GameTypes) queuedPlayerPayload.get("gameType");
        JSONObject player1 = Leaderboard.getInstance().findUserById(player1UUID);
        System.out.println("This is player1" + player1);

        Number elo = 0;
        switch(gameType){
            case CHECKERS -> {
                elo = (Number) player1.get("checkersElo");
            }
            case TICTACTOE -> {
                elo = (Number) player1.get("tictactoeElo");
            }
            case CONNECT4 -> {
                elo = (Number) player1.get("connect4Elo");
            }
        }
        QueuedPlayerMemo queuedPlayerMemo1 = new QueuedPlayerMemo(player1UUID, gameType, elo.intValue());           // now can make the queu

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
        matchedSessionResponse.put("sessionID", sessionID.toString());
        System.out.println("sessionID: " + sessionID.toString());
        matchedSessionResponse.put("player1ID", player1UUID.toString());
        matchedSessionResponse.put("player2ID", player2UUID.toString());
        matchedSessionResponse.put("currentPlayerTurn", getFirstPlayerID.toString());
        matchedSessionResponse.put("gameType", gameType.toString());

        // also everytime client sends network a payload they will need to give sessionID so we can find the gameSession object and update the game handler field in the GameSession's object.
        // This way this.gamehandler recieves a payload and should then notify the frontend using the client listeners.
        return matchedSessionResponse.toJSONString();
    }


    /**
     * handleChallengedQueuePlayer handles creating a gameSession following a profile lookup and challenging a plauer
     * @param challengeQueuedPlayer
     * @return
     */
    public String handleChallengedQueuePlayer(JSONObject challengeQueuedPlayer) throws IOException, ParseException {

        // I need player1ID this is the logged in player from the playerId.json
        // we need a player2ID
        UUID player1ID = UUID.fromString(String.valueOf(challengeQueuedPlayer.get("Player1ID")));
        JSONObject player1User = Leaderboard.getInstance().findUserById(player1ID);
        GameTypes gameType = (GameTypes) challengeQueuedPlayer.get("gameType");

        String usernamePlayer2 = (String) challengeQueuedPlayer.get("Player2Username");
        JSONObject player2User = Leaderboard.getInstance().findUserByName(usernamePlayer2);
        UUID player2ID = UUID.fromString((String) player2User.get("playerUUID"));



        Number player1Elo = 0;
        Number player2Elo = 0;
        switch(gameType){
            case CHECKERS -> {
                player1Elo = (Number) player1User.get("checkersElo");
                player2Elo = (Number) player2User.get("checkersElo");

            }
            case TICTACTOE -> {
                player1Elo = (Number) player1User.get("tictactoeElo");
                player2Elo = (Number) player2User.get("tictactoeElo");

            }
            case CONNECT4 -> {
                player1Elo = (Number) player1User.get("connect4Elo");
                player2Elo = (Number) player2User.get("tictactoeElo");

            }
        }



        // create QueuedPlayerMemos for player1 and player 2
        QueuedPlayerMemo queuedPlayerMemo1 = new QueuedPlayerMemo(player1ID, gameType,  player1Elo.intValue());
        QueuedPlayerMemo queuedPlayerMemo2 = new QueuedPlayerMemo(player2ID, gameType,  player2Elo.intValue());


        //  now we need to bypass matchmaking and go directly create the game
        GameSession matchedGameSession = new GameSession(queuedPlayerMemo1, queuedPlayerMemo2);
        network.sendCreatedGameSession(matchedGameSession);
        UUID sessionID = matchedGameSession.getSessionId();
        UUID getFirstPlayerID = matchedGameSession.getGameHandler().getFirstPlayerID();

        JSONObject matchedSessionResponse = new JSONObject();

        // send session data to frontend

        matchedSessionResponse.put("sessionID", sessionID.toString());
        matchedSessionResponse.put("player1ID", player1ID.toString());
        matchedSessionResponse.put("player2ID", player2ID.toString());
        matchedSessionResponse.put("currentPlayerTurn", getFirstPlayerID.toString());
        matchedSessionResponse.put("gameType", gameType.toString());

        // also everytime client sends network a payload they will need to give sessionID so we can find the gameSession object and update the game handler field in the GameSession's object.
        // This way this.gamehandler recieves a payload and should then notify the frontend using the client listeners.
        return matchedSessionResponse.toJSONString();

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
     * Sends game data to all clients associated with a specific game session.
     * @param clientPayload The game data to send.
     * @param sessionID     The ID of the game session.
     * @param gameEventType The type of game event triggering the update.
     * @param gameType      The type of game being played.
     * @throws IOException If an error occurs while sending data.
     */
    public void sendGameDataToClients(JSONObject clientPayload, UUID sessionID, GameEventType gameEventType, GameTypes gameType) throws IOException {
        // The appropriate gameSession listener will have been notified
        notifyGameSessionClients(sessionID, gameEventType, clientPayload, gameType);          // Notify all subscribed clients

    }


    /**
     * Sends chat data to all clients associated with a specific game session.
     * @param clientResponsePayload The chat data to send.
     * @param sessionID             The ID of the game session.
     * @param type                  The type of event triggering the chat update.
     */
    public void sendChatDataToClients(JSONArray clientResponsePayload,UUID sessionID, GameEventType type){
        notifyGameSessionClientsOfMessage(clientResponsePayload, sessionID, type);          // Notify all subscribed clients
    }

    /**
     * Processes game data received from a client and updates the game session's state.
     * @param receiveClientPayload The data received from the client.
     */
    public void receiveGameDataFromClient(JSONObject receiveClientPayload){

        //Treat this as a String, or it will become a malformed JSON string
        UUID SessionID = UUID.fromString((String)receiveClientPayload.get("SessionID"));
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


    /**
     * Handles a player's chat message by forwarding it to the recipient
     * and broadcasting the chat data to clients in the game session. This should work in theory but we implementated chat
     * in each of the game frontend controllers.
     * @param recievePlayerMessage the playerID and their message
     * @return Always returns {@code null}.
     * @throws RuntimeException if an error occurs during processing.
     */
    public JSONArray recievePlayerMessage(JSONObject recievePlayerMessage) {

        try {
            // parse the JASONObject
            UUID sessionID = (UUID) recievePlayerMessage.get("SessionID");
            UUID player1ID = (UUID) recievePlayerMessage.get("Player1ID");
            String player1Message = (String) recievePlayerMessage.get("Message");

            // get the game session along with player 2's UUID associated w that game session
            GameSession gameSession = gameSessionManager.getGameSessionBySessionID(sessionID);
            UUID player2ID = gameSession.getPlayer2ID();
            JSONObject player1 = Leaderboard.getInstance().findUserById(player1ID);
            JSONObject player2 = Leaderboard.getInstance().findUserById(player2ID);

            String player1Username = (String) player1.get("username");
            String player2Username = (String) player2.get("username");

            JSONArray chatResponsePayload = chatService.sendChatMessage(player1Username,player1Message,player2Username);
            this.sendChatDataToClients(chatResponsePayload, sessionID, GameEventType.CHAT_MESSAGE_RECEIVED);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void forfeitGame(UUID sessionID, UUID forfeitedPlayerID) {
        GameSession gameSession = gameSessionManager.getGameSessionBySessionID(sessionID);
        if (gameSession != null) {
            gameSession.processForfeit(forfeitedPlayerID);
        } else {
            throw new NullPointerException("GameSession instance is null!");
        }
    }
    //getter methods for testing
    public Map<UUID, Client> getLoggedInClients() {
        return loggedInClients;
    }

    public Map<UUID, List<GameDataListener>> getSessionListeners() {
        return sessionListeners;
    }
}