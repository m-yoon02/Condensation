package ca.ucalgary.seng300.p4.condensation.Networking.Listeners;

import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameEventType;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public interface GameDataListener {
    void onGameEvent(GameEventType eventType, JSONObject payload, GameTypes gameTypes);

    void onChatEvent(GameEventType gameEventType, JSONArray clientResponsePayload);



}

