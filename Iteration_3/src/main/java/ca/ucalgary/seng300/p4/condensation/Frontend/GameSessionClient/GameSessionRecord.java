package ca.ucalgary.seng300.p4.condensation.Frontend.GameSessionClient;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.UUID;

public class GameSessionRecord {
    private boolean initialized = false;
    private UUID sessionID;
    private UUID currentPlayerID;
    private UUID player1ID;
    private UUID player2ID;

    private static final GameSessionRecord gameSessionRecord = new GameSessionRecord();

    private GameSessionRecord() {};

    public static GameSessionRecord getGameSessionRecord() {
        return gameSessionRecord;
    }

    public UUID getCurrentPlayerID() {
        return currentPlayerID;
    }

    public UUID getPlayer1ID() {
        return player1ID;
    }

    public UUID getPlayer2ID() {
        return player2ID;
    }

    public UUID getSessionID() {
        return sessionID;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void reset() {
        player1ID = null;
        player2ID = null;
        currentPlayerID = null;
        initialized = false;
    }

    public void fromJSON(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) new JSONParser().parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        initialized = true;
        sessionID = UUID.fromString((String) jsonObject.get("sessionID"));
        player1ID = UUID.fromString((String) jsonObject.get("player1ID"));
        player2ID = UUID.fromString((String) jsonObject.get("player2ID"));
        currentPlayerID = UUID.fromString((String) jsonObject.get("currentPlayerTurn"));
    }
}
