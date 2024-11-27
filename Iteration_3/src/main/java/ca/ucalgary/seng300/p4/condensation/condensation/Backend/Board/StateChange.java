package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Board;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;

public record StateChange(TypeChange typeChange, PieceColor pieceColor, int[][] positions) {
}
