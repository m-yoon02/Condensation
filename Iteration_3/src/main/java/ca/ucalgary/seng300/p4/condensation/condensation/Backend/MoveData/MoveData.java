package ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs.Vector2;

import java.util.UUID;

public class MoveData {
    private final PieceColor pieceColor;
    private final Vector2 movePosition;
    private final UUID playerID;

    public MoveData(PieceColor pieceColor, Vector2 movePosition, UUID playerID) {
        this.pieceColor = pieceColor;
        this.movePosition = movePosition;
        this.playerID = playerID;
    }

    /**
     * Creates a move data class representation from JSON
     * @param moveDataJSON the JSON string
     * @return the move data object
     */
    public static MoveData fromJSON(String moveDataJSON){
        return new MoveData(PieceColor.RED, new Vector2(0, 0), UUID.randomUUID());
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public PieceColor getPieceColor() {
        return pieceColor;
    }

    public Vector2 getMovePosition() {
        return movePosition;
    }
}
