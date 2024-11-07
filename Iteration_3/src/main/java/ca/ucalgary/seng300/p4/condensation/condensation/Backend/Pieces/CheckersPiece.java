package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Pieces;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;

public class CheckersPiece extends GamePiece {
    private boolean king = false;

    public CheckersPiece(int x, int y, PieceColor color) {
        super(x, y, color);
    }

    public boolean isKing() {
        return king;
    }

    public void promoteToKing() {
        this.king = true;
    }
}
