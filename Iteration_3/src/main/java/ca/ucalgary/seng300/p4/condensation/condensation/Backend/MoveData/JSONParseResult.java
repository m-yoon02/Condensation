package ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData;

import java.util.UUID;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public record JSONParseResult(UUID playerID, int[] newPosition, int[] oldPosition) {

    /**
     * Converts 2D JSON integer array into an actual int array
     * @param jsonArray the JSON array object
     * @return the parsed 2d int array
     */
    private static int[] convertJSONIntArray(JSONArray jsonArray){
        int[] intArray = new int[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++){
            intArray[i] = ((Long) jsonArray.get(i)).intValue();
        }
        return intArray;
    }

    /**
     * Creates a JSONParseResult from a JSON String
     * @param jsonString the JSON string
     * @return the JSONParseResult from the JSON string
     */
    public static JSONParseResult parseJSON(String jsonString) {
        try {
            Object object = new JSONParser().parse(jsonString);
            JSONObject jsonObject = (JSONObject) object;
            String playerID = (String) jsonObject.get("PlayerID");
            JSONArray oldPositionJSON = (JSONArray) jsonObject.get("OldPosition");
            return new JSONParseResult(
                    UUID.fromString(playerID),
                    convertJSONIntArray((JSONArray) jsonObject.get("NewPosition")),
                    oldPositionJSON != null ? convertJSONIntArray(oldPositionJSON) : null
            );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
