package ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;

public class MoveData {
    private final PieceColor pieceColor;
    //int[] represents x and y {x, y}
    private final int[] movePosition;

    public MoveData(PieceColor pieceColor, int[] movePosition) {
        this.pieceColor = pieceColor;
        this.movePosition = movePosition;
    }

    public PieceColor getPieceColor() {
        return pieceColor;
    }

    public int[] getMovePosition() {
        return movePosition;
    }
}
