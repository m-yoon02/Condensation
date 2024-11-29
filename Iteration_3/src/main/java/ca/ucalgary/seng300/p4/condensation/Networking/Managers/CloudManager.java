package ca.ucalgary.seng300.p4.condensation.condensation.Networking.Managers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.json.simple.JSONObject;

public class CloudManager {

    // we want to create a singleton instance of CloudManager
    private static CloudManager instance;

    // simulate the connection status to the cloud
    private boolean isConnected;

    // these variables will hel[ simulate cloud storage for game state and the player's data
    private JSONObject currentGameState;
    private List<UUID> syncedPlayerIDs;

    // constructor
    private CloudManager() {
        isConnected = false;
        syncedPlayerIDs = new ArrayList<>();
    }


    // want to get the singleton instance of CloudManager
    public static  synchronized CloudManager getInstance() {
        if (instance == null) {
            instance = new CloudManager();
        }
        return instance;

    }
    /**
     * Initialize connection to the Cloud Service
     * @return boolean indicating successful connection
     */
    public boolean initializeConncection(){
        if(!isConnected){
            System.out.println("Cloud connection established successfully.");
            return true;
        }
        else{
            System.err.println("Connection initialization interrupted.");
            isConnected = false;
            return false;
        }

    }
    /**
     * Checks if currently connected to the cloud service
     * @return boolean indicating connection status
     */

    public boolean isConnected(){
        return isConnected;
    }

    /**
     * Method to disconnect from the cloud service
     */
    public void disconnect(){
        if(isConnected) {
            isConnected = false;
            currentGameState = null;
            syncedPlayerIDs.clear();
            System.out.println("Disconnected from Cloud Service.");
        }
    }

    public boolean initializeConnection(){
        if(!isConnected){
            System.out.println("Cloud connection established successfully.");
            return true;
        }
        else{
            System.err.println("Connection initialization interrupted.");
            isConnected = false;
            return false;
        }
    }


    /**
     * we want to simulate the cloud saving the game state of a game session
     * @param gameState is a JSONObject that represents the current game
     * @return a boolean flag that will indicate whether the cloud has saved the game state of a game session
     */

    public boolean saveGameState(JSONObject gameState) {
        if (!isConnected) {
            System.err.println("Game could not be saved. Please check your connection again");
        }
        // if it is connected, then continue with saving game state
        this.currentGameState = new JSONObject(gameState);
        System.out.println("Game state has been saved successfully.");
        return true;

    }



    /**
     * This method retireves the last saved game 
     * @return JSONOject of the last saved game state
     */
    public JSONObject retrieveGameState() {
        if (!isConnected) {
            System.err.println("Can't retrieve game state. Please check your connection with the cloud.")
            return null; // returning null if there was no game state


        }

    }

    


    
    
}