package Networking.Managers;

import Networking.DTO.Connection;
import Networking.Services.ChatManager;
import Networking.Services.MatchmakingService;

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

    /**
     *  Simulates a user disconnection by logging their connection status.
     */
    public void disconnect(UUID playerID) {
        connections.remove(playerID.toString());
        System.out.println("User " + playerID.toString() + " disconnected.");
    }


    // define send message
    public void sendMessage(UUID receivingPlayedId, String message){

        // checks to see if the receiving player is connected
        Connection recieverConnection = connections.get(receivingPlayedId.toString());

        if(recieverConnection != null && recieverConnection.isConnected()){
            System.out.println("Sending message to " + receivingPlayedId.toString() + ": " + message);

            // do checks for different types of message like for Chat what else ???
        }
        else {
            System.out.println("Cannot send message: " + receivingPlayedId.toString() + " is not connected.");
        }

    }





    // define recieve message
    // -- will require some kind of logic to dilnieate how to handle the message.









}
