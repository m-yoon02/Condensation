package ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;

public class CheckersMoveData extends MoveData {
    private final int[] oldPosition;

    public CheckersMoveData(PieceColor pieceColor, int[] movePosition, int[] oldPosition) {
        super(pieceColor, movePosition);
        this.oldPosition = oldPosition;
    }

    public int[] getOldPosition() {
        return oldPosition;
    }
}
