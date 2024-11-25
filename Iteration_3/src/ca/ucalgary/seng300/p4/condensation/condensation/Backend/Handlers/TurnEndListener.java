package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers;

import org.json.simple.JSONObject;

import java.util.UUID;

public interface TurnEndListener {
    void onTurnEnd(UUID playersTurnID, JSONObject gameStateJson);
}
