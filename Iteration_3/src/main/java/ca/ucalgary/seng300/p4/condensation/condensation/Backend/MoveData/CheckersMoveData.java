package ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs.Vector2;

import java.util.UUID;

public class CheckersMoveData extends MoveData {
    private final Vector2 oldPosition;

    public CheckersMoveData(PieceColor pieceColor, Vector2 movePosition, Vector2 oldPosition, UUID playerID) {
        super(pieceColor, movePosition, playerID);
        this.oldPosition = oldPosition;
    }

    /**
     * Creates a checkers move data class representation from JSON
     * @param moveDataJSON the JSON string
     * @return the move data object
     */
    public static CheckersMoveData fromJSON(String moveDataJSON){
        return new CheckersMoveData(PieceColor.RED, new Vector2(0, 0), new Vector2(1, 1), UUID.randomUUID());
    }

    public Vector2 getOldPosition() {
        return oldPosition;
    }
}
