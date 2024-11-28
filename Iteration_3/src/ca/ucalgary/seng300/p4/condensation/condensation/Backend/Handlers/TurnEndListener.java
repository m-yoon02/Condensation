package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.UUID;

public interface TurnEndListener {
    void onTurnEnd(UUID playersTurnID, String gameStateJson) throws IOException, ParseException;

}
