package ca.ucalgary.seng300.p4.condensation.condensation.Networking.Managers;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.GameSession;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.QueuedPlayerMemo;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Services.ChatManager;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Services.MatchmakingPool;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Services.MatchmakingService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NetworkManager {


    /**
     * Network Manager acts as facade which abstracts a way syncing between game, chat and other elements.
     * Network manager implements singleton design pattern for network
     */

    private static NetworkManager network;
    private final Map<String, Connection> connections;  // map of userID to Connection
    private final MatchmakingService matchmakingService;
    private final GameSessionManager gameSessionManager;
    private final ChatManager chatManager;
    private final MatchmakingPool matchmakingPool;


    /**
     * constructor for network manager instance, when creating a new one
     *
     */
    private NetworkManager(){
        connections = new HashMap<>();
        matchmakingPool = new MatchmakingPool();
        matchmakingService = new MatchmakingService(matchmakingPool);
        gameSessionManager = new GameSessionManager();
        chatManager = new ChatManager();

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
    // Network methods that handles queueing, creating, and starting game session sequence.
    // ---------------------------------------------------------------------------------------------------

    /**
     * queuePlayer method recieves a queuedMemoObject from the client and pools it. Then
     * @param queuedMemoObject This has playerUUID, gameType, and the elo of the player
     */
    public void queuePlayer(QueuedPlayerMemo queuedMemoObject){

        matchmakingPool.addPlayer(queuedMemoObject);                                // adds the playerMemo to a matchmakingPool
        network.sendCreatedGameSession(matchmakingService.findAndMatchPlayers());   // This will match first available player in the list to eligible player2

    }

    /**
     * sendCreatedGameSession will send the gameSession object to the GameSession Manager
     * @param gameSession
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

        // send this information to the backend they need to define some kind of recieve class
    }


    // ---------------------------------------------------------------------------------------------------
    // Network methods that handle intra game session methods and logic.
    // ---------------------------------------------------------------------------------------------------

    /**
     * sendGameDataToClients the readyPayload = sessionID + gameState payload to the clients
     * @param readyPayload
     */
    public void sendGameDataToClients(String readyPayload){
        // send the readyPayload = sessionID + PlayerTurn + gameState payload to the clients
        // there needs to be somekind of recieving method from the frontEnd
    }

    public void recieveGameDataFromClient(){

        // recieve a payload json formatted String
        // GameSession gameSession = retured by GSM;
        //


    }

    // ---------------------------------------------------------------------------------------------------
    // Network methods that handle end game session methods and logic.
    // ---------------------------------------------------------------------------------------------------


    // we need to





}