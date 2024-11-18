package ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs.Vector2;

public class MoveData {
    private final PieceColor pieceColor;
    private final Vector2 movePosition;

    public MoveData(PieceColor pieceColor, int[] movePosition) {
        this.pieceColor = pieceColor;
        this.movePosition = new Vector2(movePosition[0], movePosition[1]);
    }

    public PieceColor getPieceColor() {
        return pieceColor;
    }

    public Vector2 getMovePosition() {
        return movePosition;
    }
}
