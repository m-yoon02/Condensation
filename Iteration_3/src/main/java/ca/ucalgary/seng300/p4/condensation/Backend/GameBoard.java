package ca.ucalgary.seng300.p4.condensation.Backend;

import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.Backend.Pieces.GamePiece;

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

    /**
     * Checks if a position is within the bounds of a board
     * @param position the position to check for
     * @return true if within bounds, else false
     */
    public boolean positionWithinBounds(Vector2 position){
        return position.x() >= 0 && position.x() < columnSize
                && position.y() >= 0 && position.y() < rowSize;
    }

    /**
     * Places a newly created piece at the position
     * @param piece the newly created piece
     * @param position the position to place it in
     * @return true if it is successfully placed, else false
     */
    public boolean placePiece(T piece, Vector2 position){
        return false;
    }

    /**
     * Moves a piece from oldPosition to newPosition
     * @param oldPosition the old position of the piece
     * @param newPosition the new position of the piece
     * @return true if the move was successful, else false
     */
    public boolean movePiece(Vector2 oldPosition, Vector2 newPosition){
        return false;
    }

    /**
     * Removes a piece at the position
     * @param position the position of the piece
     */
    public void removePiece(Vector2 position){
    }

    /**
     * Checks if a move is possible (if there is no other piece there and if it is in bounds)
     * @param position the position to check for
     * @return true if it is possible, else false
     */
    public boolean isMovePossible(Vector2 position){
        return false;
    }

    /**
     * Checks if the board is full
     * @return true if full, else false
     */
    public boolean isBoardFull(){
        return false;
    }

    /**
     * Gets the piece at the given position, if one exists
     * @param position the position of the piece
     * @return the piece if it exists, else null
     */
    public T getPiece(Vector2 position){
        return null;
    }
}