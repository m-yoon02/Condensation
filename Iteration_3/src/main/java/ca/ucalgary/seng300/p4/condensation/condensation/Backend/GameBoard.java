package ca.ucalgary.seng300.p4.condensation.condensation.Backend;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Pieces.GamePiece;

import java.util.ArrayList;

//May or may not switch from int[]{x, y} to just int x, int y

public class GameBoard<T extends GamePiece> {
    private int rowSize;
    private int columnSize;
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

    public boolean positionWithinBounds(int[] position){
        return position[0] >= 0 && position[0] < columnSize
                && position[1] >= 0 && position[0] < rowSize;
    }

    public boolean placePiece(T piece, int[] position){
        return false;
    }

    public boolean movePiece(int[] oldPosition, int[] newPosition){
        return false;
    }

    public boolean removePiece(int[] position){
        return false;
    }

    public boolean isMovePossible(int[] position){
        return false;
    }

    public boolean isBoardFull(){
        return false;
    }

    public T getPiece(int[] position){
        return null;
    }
}