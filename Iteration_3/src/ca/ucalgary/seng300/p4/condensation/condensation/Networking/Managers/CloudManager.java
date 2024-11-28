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

    public boolean isConnected(){
        return isConnected;
    }













}