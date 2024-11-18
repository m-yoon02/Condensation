package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Pieces;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs.Vector2;

public abstract class GamePiece {
    private int x;
    private int y;
    private final PieceColor color;

    public GamePiece(int x, int y, PieceColor color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector2 getPosition(){
        return new Vector2(x, y);
    }

    public PieceColor getColor() {
        return color;
    }

    //Note: This is a local only change, this will not update on the game board
    public void moveTo(int x, int y){
        this.x = x;
        this.y = y;
    }
}
