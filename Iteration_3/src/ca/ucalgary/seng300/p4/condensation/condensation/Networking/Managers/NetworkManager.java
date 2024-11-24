package ca.ucalgary.seng300.p4.condensation.condensation.Networking.Managers;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO.Connection;
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
        network.sendPlayerReferences(gameSession);
    }

    /**
     * sendPlayerReferences sends the players UUIDSs to backend for reference, this concludes the starting game session sequence.
     * -- We need to discuss sending information back to the backend here either just sessionID, uuid,
     * @param
     * @param
     */
    public void sendPlayerReferences(GameSession gameSession){
        // send this information to the backend
    }


    // define send message
    public void sendMessage(UUID receivingPlayedId, String message){

        // checks to see if the receiving player is connected
        Connection recieverConnection = connections.get(receivingPlayedId.toString());

        if(recieverConnection != null && recieverConnection.isConnected()){
            System.out.println("Sending message to " + receivingPlayedId.toString() + ": " + message);
            // send message to backend

            // do checks for different types of message like for Chat what else ???
        }
        else {
            System.out.println("Cannot send message: " + receivingPlayedId.toString() + " is not connected.");
        }

    }




    // define recieve message
    // -- will require some kind of logic to dilnieate how to handle the message.


}