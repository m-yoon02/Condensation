package Networking.Managers;

import Networking.DTO.Connection;
import Networking.Services.ChatManager;
import Networking.Services.MatchmakingService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NetworkManager {

    private static NetworkManager network;
    private final Map<String, Connection> connections;  // map of userID to Connection
    private final MatchmakingService matchmakingService;
    private final GameSessionManager gameSessionManager;
    private final ChatManager chatManager;


    /**
     * constructor for network manager instance, when creating a new one
     *
     */
    private NetworkManager(){
        connections = new HashMap<>();
        matchmakingService = new MatchmakingService();
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

    /**
     * Simulates a user connection by logging their connection status.
     */
    public void connect(UUID playerID){
        Connection connection = new Connection(playerID);
        connections.put(playerID.toString(), connection);
        System.out.println("User " + playerID.toString() + " is connected.");

    }








}
