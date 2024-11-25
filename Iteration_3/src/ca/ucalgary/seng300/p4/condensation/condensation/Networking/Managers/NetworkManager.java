package ca.ucalgary.seng300.p4.condensation.condensation.Networking.Managers;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.Client;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.Player;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.QueuedPlayerMemo;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Listeners.GameDataListener;
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
   // private final Map<UUID, List<GameDataListener>> sessionListeners;
    private final JSONArray userDatabase;
    private final String userDatabaseFilePath = "ca/ucalgary/seng300/p4/condensation/condensation/Networking/Database/PlayerInformation/Database.json";

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
     * @return A success or failure response as JSON
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
            Player player = validateUserCredentials(username, password);
            UUID plauerUUID = player.getPlayerID();
            int playerELO = player.getElo();

            if (plauerUUID == null) {
                throw new IllegalArgumentException("Invalid username or password");
            }

            // Check if the user is already logged in
            if (loggedInClients.containsKey(plauerUUID)) {
                throw new IllegalArgumentException("User already logged in");
            }

            // Create and store the Client object
            Client client = new Client(plauerUUID, username, null, playerELO);          // we set gameType to null for now this and elo
            loggedInClients.put(plauerUUID, client);

            // Return a success response
            System.out.println("User logged in: " + username + " (" + plauerUUID + ")");
            return "Login Successful";
        } catch (ParseException | IOException e) {
            throw new IllegalArgumentException("Invalid login payload", e);
        }
    }

    /**
     * Validates the user's credentials by checking the simulated database. Helper to playerLoginHandler
     * @param username The provided username
     * @param password The provided password
     * @return The Player object if credentials are valid, or null if invalid
     * */
    public Player validateUserCredentials(String username, String password) throws IOException, ParseException {

        for (Object obj : userDatabase) {           // parses the json
            JSONObject playerJson = (JSONObject) obj;

            // Check if username and password match
            if (playerJson.get("username").equals(username) && playerJson.get("password").equals(password)) {
                // Deserialize the matching player into a Player object this is neccessary because we need to values about the player to returned
                // so we make a new player object return it, and now login get all the fields it needs from here to make a client.
                return Player.fromJson(playerJson);
            }
        }
        return null; // No match found
    }

    /**
     * Retrieves a logged-in client by their playerID. Can be used in application whenever certain actions require a logged in client
     * @param playerId The playerId
     * @return The Client object, or null if not logged in
     */
    public Client getClient(UUID playerId) {
        return loggedInClients.get(playerId);
    }



    // ---------------------------------------------------------------------------------------------------
    // Network methods that handles queueing, creating, and starting game session sequence.
    // ---------------------------------------------------------------------------------------------------
    /**
     * clientQueuePlayer called by the controller to recieve a queuedPlayer
     * @param queuedMemoObject This has playerUUID, gameType, and the elo of the player
     */
    public void clientQueuePlayer(QueuedPlayerMemo queuedMemoObject){
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
        network.sendPlayerReferencesToBackend(gameSession.getPlayer1ID(), gameSession.getPlayer2ID());
    }

    /**
     * sendPlayerReferences sends the players UUIDSs to backend for reference, this concludes the starting game session sequence.
     * Following backend will start game and the inter-game session sequence will begin
     * @param Player1 this represents player1's uuid
     * @param Player2 this represents player2's uuid
     */
    public void sendPlayerReferencesToBackend(UUID Player1, UUID Player2){
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