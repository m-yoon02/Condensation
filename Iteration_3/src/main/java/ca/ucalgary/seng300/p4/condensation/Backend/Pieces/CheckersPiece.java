package ca.ucalgary.seng300.p4.condensation.Backend.Pieces;

import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.PieceColor;

public class CheckersPiece extends GamePiece {
    private boolean king = false;

    public CheckersPiece(Vector2 position, PieceColor color) {
        super(position, color);
    }

    public CheckersPiece(PieceColor color) {
        super(color);
    }

    public boolean isKing() {
        return king;
    }

    public void promoteToKing() {
        this.king = true;
    }
}
