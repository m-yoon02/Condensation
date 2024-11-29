package ca.ucalgary.seng300.p4.condensation.Backend.Board;

import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.Backend.Pieces.GamePiece;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

public class GameBoard<T extends GamePiece> {
    private final int rowSize;
    private final int columnSize;
    private final ArrayList<ArrayList<T>> board;

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
     * Internal method to make removing easier
     * @param position the position of the piece to be removed
     */
    private void deletePieceAt(Vector2 position) {
        board.get(position.y()).set(position.x(), null);
    }

    /**
     * Internal place method to make adding easier
     * @param piece the piece to be added
     * @param position the position to add it in
     */
    private void addPieceAt(T piece, Vector2 position){
        board.get(position.y()).set(position.x(), piece);
        piece.moveTo(position);
    }

    /**
     * Checks if (x, y) is within the board's bounds
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if within bounds, else false
     */
    public boolean positionWithinBounds(int x, int y){
        return x >= 0 && x < columnSize
                && y >= 0 && y < rowSize;
    }

    /**
     * Checks if the given Vector2 position is within bounds
     * @param position the Vector2 position
     * @return true if within bounds, else false
     */
    public boolean positionWithinBounds(Vector2 position){
        return positionWithinBounds(position.x(), position.y());
    }

    /**
     * Places a newly created piece at the position
     * @param piece the newly created piece
     * @param position the position to place it in
     * @return true if it is successfully placed, else false
     */
    public boolean placePiece(T piece, Vector2 position){
        if (isMovePossible(position)) {
            addPieceAt(piece, position);
            return true;
        }
        return false;
    }

    /**
     * Moves a piece from oldPosition to newPosition
     * @param oldPosition the old position of the piece
     * @param newPosition the new position of the piece
     * @return true if the move was successful, else false
     */
    public boolean movePiece(Vector2 oldPosition, Vector2 newPosition){
        T piece = getAt(oldPosition);
        if (isMovePossible(newPosition) && piece != null){
            addPieceAt(piece, newPosition);
            deletePieceAt(oldPosition);
            return true;
        }
        return false;
    }

    /**
     * Removes a piece at the position
     * @param position the position of the piece
     */
    public void removePiece(Vector2 position){
        if (positionWithinBounds(position) && isPieceAt(position)) {
            deletePieceAt(position);
        }
    }

    /**
     * Checks if a move is possible (if there is no other piece there and if it is in bounds)
     * @param position the position to check for
     * @return true if it is possible, else false
     */
    public boolean isMovePossible(Vector2 position){
        return positionWithinBounds(position) && !isPieceAt(position);
    }

    /**
     * Checks if the board is full
     * @return true if full, else false
     */
    public boolean isBoardFull(){
        for (ArrayList<T> arrayList: board){
            for (T piece: arrayList){
                if (piece == null){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if there is currently a piece at the position
     * @param position the position to check for
     * @return true if there is a piece, else false
     */
    public boolean isPieceAt(Vector2 position){
        return isPieceAt(position.x(), position.y());
    }

    /**
     * Checks if a piece exists at (x, y)
     * @param x the x position
     * @param y the y position
     * @return the piece at (x, y) if it exists, else null
     */
    public boolean isPieceAt(int x, int y){
        return getAt(x, y) != null;
    }

    /**
     * Gets the piece at (x, y)
     * @param x the x position
     * @param y the y position
     * @return the piece if it exists, else null
     */
    public T getAt(int x, int y){
        return board.get(y).get(x);
    }

    /**
     * Gets the piece at the given position, if one exists
     * @param position the position of the piece
     * @return the piece if it exists, else null
     */
    public T getAt(Vector2 position){
        return getAt(position.x(), position.y());
    }

    public int getRowSize() {
        return rowSize;
    }

    public int getColumnSize() {
        return columnSize;
    }

    /**
     * Converts the current Board state into a JSON String
     * @return the JSON String representation of the Board
     */
    public String toJSONString() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < getRowSize(); i++){
            JSONArray inner = new JSONArray();
            for (int j = 0; j < getColumnSize(); j++) {
                T piece = getAt(j, i);
                if (piece != null){
                    inner.add(piece.getColor().toString());
                    continue;
                }
                inner.add("EMPTY");
            }
            jsonArray.add(inner);
        }
        return jsonArray.toJSONString();
    }
}