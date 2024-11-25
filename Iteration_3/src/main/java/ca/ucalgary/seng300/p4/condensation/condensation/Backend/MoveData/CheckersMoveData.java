package ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs.Vector2;

import java.util.UUID;

public class CheckersMoveData extends MoveData {
    private final Vector2 oldPosition;

    public CheckersMoveData(UUID playerID, Vector2 movePosition, Vector2 oldPosition) {
        super(playerID, movePosition);
        this.oldPosition = oldPosition;
    }

    /**
     * Creates a checkers move data class representation from JSON
     * @param moveDataJSON the JSON string
     * @return the move data object
     */
    public static CheckersMoveData fromJSON(String moveDataJSON){
        JSONParseResult jsonParseResult = JSONParseResult.parseJSON(moveDataJSON);
        return new CheckersMoveData(jsonParseResult.playerID()
                , Vector2.fromArray(jsonParseResult.positions()[0])
                , Vector2.fromArray(jsonParseResult.positions()[1]));
    }

    public Vector2 getOldPosition() {
        return oldPosition;
    }
}
