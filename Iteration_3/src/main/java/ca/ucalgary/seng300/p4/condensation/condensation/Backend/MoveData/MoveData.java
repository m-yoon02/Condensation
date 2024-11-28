package ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs.Vector2;

import java.util.UUID;

public class MoveData {
    private final Vector2 movePosition;
    private final UUID playerID;

    public MoveData(UUID playerID, Vector2 movePosition) {
        this.movePosition = movePosition;
        this.playerID = playerID;
    }

    /**
     * Creates a move data class representation from JSON
     * @param moveDataJSON the JSON string
     * @return the move data object
     */
    public static MoveData fromJSON(String moveDataJSON){
        JSONParseResult jsonParseResult = JSONParseResult.parseJSON(moveDataJSON);
        return new MoveData(jsonParseResult.playerID()
                , Vector2.fromArray(jsonParseResult.newPosition()));
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public Vector2 getMovePosition() {
        return movePosition;
    }
}
