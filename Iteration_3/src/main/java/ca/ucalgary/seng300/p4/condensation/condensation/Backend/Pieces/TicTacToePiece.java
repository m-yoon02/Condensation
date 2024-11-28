package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Pieces;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;

public class TicTacToePiece extends GamePiece {
    public TicTacToePiece(Vector2 position, PieceColor color) {
        super(position, color);
    }

    public TicTacToePiece(PieceColor color) {
        super(color);
    }
}
