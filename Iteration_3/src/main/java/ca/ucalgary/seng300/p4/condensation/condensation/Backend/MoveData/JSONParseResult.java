package ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData;

import java.util.Arrays;
import java.util.UUID;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public record JSONParseResult(UUID playerID, int[][] positions) {

    /**
     * Converts 2D JSON integer array into an actual int array
     * @param jsonArray the JSON array object
     * @return the parsed 2d int array
     */
    private static int[][] convertJSON2DIntArray(JSONArray jsonArray){
        int[][] intArray = new int[jsonArray.size()][2];
        for (int i = 0; i < jsonArray.size(); i++){
            JSONArray positionJSON = (JSONArray) jsonArray.get(i);
            for (int j = 0; j < positionJSON.size(); j++){
                intArray[i][j] = ((Long) positionJSON.get(j)).intValue();
            }
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
            return new JSONParseResult(UUID.fromString(playerID), convertJSON2DIntArray((JSONArray) ((JSONObject) object).get("Positions")));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
