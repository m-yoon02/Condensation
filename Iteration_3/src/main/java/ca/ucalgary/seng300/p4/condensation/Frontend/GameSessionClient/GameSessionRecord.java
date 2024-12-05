package ca.ucalgary.seng300.p4.condensation.Frontend.GameSessionClient;

import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Arrays;
import java.util.UUID;

public class GameSessionRecord {
    private boolean initialized = false;
    private UUID sessionID;
    private UUID currentPlayerID;
    private UUID player1ID;
    private UUID player2ID;
    private GameTypes gameType;

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

    public GameTypes getGameType() {
        return gameType;
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

    /**
     * Randomizes the GameSessionRecord for testing
     * @param chosenGame the GameType to test for
     */
    public void randomize(GameTypes chosenGame) {
        initialized = true;
        sessionID = UUID.randomUUID();
        player1ID = UUID.randomUUID();
        player2ID = UUID.randomUUID();
        currentPlayerID = player1ID;
        gameType = chosenGame;
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
        gameType = GameTypes.valueOf((String) jsonObject.get("gameType"));
    }

    public static JSONObject parseMoveData(UUID sessionID, UUID playerID, int[] newPosition){
        return parseMoveData(sessionID, playerID, newPosition, null);
    }

    public static JSONObject parseMoveData(UUID sessionID, UUID playerID, int[] newPosition, int[] oldPosition) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("SessionID", sessionID.toString());
        jsonObject.put("PlayerID", playerID.toString());
        jsonObject.put("NewPosition", convertIntArrayToJSONArray(newPosition));
        if (oldPosition != null) {
            jsonObject.put("OldPosition", convertIntArrayToJSONArray(oldPosition));
        }
        return jsonObject;
    }

    public static void updateBoardRepresentationFromJSON(String[][] boardRepresentation, JSONArray jsonArray) {
        for (String[] strings : boardRepresentation)
            Arrays.fill(strings, "EMPTY");
        for (int i = 0; i < boardRepresentation.length; i++) {
            JSONArray outer = (JSONArray) jsonArray.get(i);
            for (int j = 0; j < boardRepresentation[i].length; j++) {
                String color = (String) outer.get(j);
                if (!color.equals("EMPTY")) {
                    boardRepresentation[i][j] = color;
                }
            }
        }
    }

    private static JSONArray convertIntArrayToJSONArray(int[] position){
        JSONArray jsonArray = new JSONArray();
        for (int i : position){
            jsonArray.add(i);
        }
        return jsonArray;
    }
}
