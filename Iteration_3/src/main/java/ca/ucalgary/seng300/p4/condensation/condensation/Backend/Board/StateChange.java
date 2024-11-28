package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Board;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StateChange {

    /**
     * Converts a change in board state into a JSON string
     * @param typeChange the type of board state change
     * @param positions positional data, positions[0] = new position, positions[1] = old position
     * @return the converted JSON object
     */
    public static JSONObject stateToJSONObject(TypeChange typeChange, int[][] positions){
        return stateToJSONObject(null, typeChange, positions);
    }

    /**
     * Converts a change in board state into a JSON string
     * @param pieceColor the color of the piece modified
     * @param typeChange the type of board state change
     * @param positions positional data, positions[0] = new position, positions[1] = old position
     * @return the converted JSON object
     */
    public static JSONObject stateToJSONObject(PieceColor pieceColor, TypeChange typeChange, int[][] positions){
        Map<String, String> map = new HashMap<>();
        map.put("Type", typeChange.toString());
        if (pieceColor != null){
            map.put("Color", pieceColor.toString());
        }
        map.put("NewPosition", Arrays.toString(positions[0]));
        if (positions.length > 1){
            map.put("OldPosition", Arrays.toString(positions[1]));
        }
        return new JSONObject(map);
    }
}
