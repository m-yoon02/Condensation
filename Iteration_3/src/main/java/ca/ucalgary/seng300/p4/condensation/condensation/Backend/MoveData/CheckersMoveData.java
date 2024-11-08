package ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Vector2;

public class CheckersMoveData extends MoveData {
    private final Vector2 oldPosition;

    public CheckersMoveData(PieceColor pieceColor, int[] movePosition, int[] oldPosition) {
        super(pieceColor, movePosition);
        this.oldPosition = new Vector2(oldPosition[0], oldPosition[1]);
    }

    public Vector2 getOldPosition() {
        return oldPosition;
    }
}
