package ca.ucalgary.seng300.p4.condensation.Backend.Pieces;

import ca.ucalgary.seng300.p4.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;

public abstract class GamePiece {
    private Vector2 position;
    private final PieceColor color;

    public GamePiece(Vector2 position, PieceColor color) {
        this.position = position;
        this.color = color;
    }

    public int getX() {
        return position.x();
    }

    public int getY() {
        return position.y();
    }

    public Vector2 getPosition(){
        return position;
    }

    public PieceColor getColor() {
        return color;
    }

    //Note: This is a local only change, this will not update on the game board
    public void moveTo(Vector2 newPosition){
        this.position = newPosition;
    }
}
