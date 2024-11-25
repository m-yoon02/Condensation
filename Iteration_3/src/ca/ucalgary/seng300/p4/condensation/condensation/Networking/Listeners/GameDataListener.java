package ca.ucalgary.seng300.p4.condensation.condensation.Networking.Listeners;

import org.json.simple.JSONObject;

public interface GameDataListener {
    void onGameDataReceived(JSONObject gameData);

}

