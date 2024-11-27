package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Board;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;

public class StateChange {
    private final PieceColor pieceColor;
    private final TypeChange typeChange;
    private final int[][] positions;

    public StateChange(TypeChange typeChange, int[][] positions){
        this(null, typeChange, positions);
    }

    public StateChange(PieceColor pieceColor, TypeChange typeChange, int[][] positions) {
        this.pieceColor = pieceColor;
        this.typeChange = typeChange;
        this.positions = positions;
    }

    public PieceColor getPieceColor() {
        return pieceColor;
    }

    public TypeChange getTypeChange() {
        return typeChange;
    }

    public int[][] getPositions() {
        return positions;
    }
}
