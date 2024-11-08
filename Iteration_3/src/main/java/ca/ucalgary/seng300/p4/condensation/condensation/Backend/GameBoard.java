package ca.ucalgary.seng300.p4.condensation.condensation.Backend;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Pieces.GamePiece;

import java.util.ArrayList;

public class GameBoard<T extends GamePiece> {
    private final int rowSize;
    private final int columnSize;
    private ArrayList<ArrayList<T>> board;

    public GameBoard(int rowSize, int columnSize) {
        this.rowSize = rowSize;
        this.columnSize = columnSize;
        this.board = new ArrayList<>(rowSize);
        for (int i = 0; i < rowSize; i++){
            ArrayList<T> inner = new ArrayList<>(columnSize);
            for (int j = 0; j < columnSize; j++){
                inner.add(null);
            }
            board.add(inner);
        }
    }

    public boolean positionWithinBounds(Vector2 position){
        return position.getX() >= 0 && position.getX() < columnSize
                && position.getY() >= 0 && position.getY() < rowSize;
    }

    public boolean placePiece(T piece, Vector2 position){
        return false;
    }

    public boolean movePiece(Vector2 oldPosition, Vector2 newPosition){
        return false;
    }

    public boolean removePiece(Vector2 position){
        return false;
    }

    public boolean isMovePossible(Vector2 position){
        return false;
    }

    public boolean isBoardFull(){
        return false;
    }

    public T getPiece(Vector2 position){
        return null;
    }
}