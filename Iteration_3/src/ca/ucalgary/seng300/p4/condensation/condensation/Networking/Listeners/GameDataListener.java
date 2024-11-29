package ca.ucalgary.seng300.p4.condensation.condensation.Networking.Listeners;

import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Enums.GameEventType;
import org.json.simple.JSONObject;

public interface GameDataListener {
    void onGameEvent(GameEventType eventType, JSONObject payload);

    void onGameDataReceived(JSONObject gameData);
}

