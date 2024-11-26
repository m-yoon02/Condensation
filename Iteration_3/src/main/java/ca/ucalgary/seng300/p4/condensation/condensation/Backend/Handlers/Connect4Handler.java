package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.GameBoard;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData.MoveData;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Pieces.ConnectFourPiece;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs.Vector2;

import java.util.UUID;

public class Connect4Handler extends GameHandler {
    private static final int[] BOARD_SIZE = {6, 7};
    private static final PieceColor[] COLORS = {PieceColor.BLUE, PieceColor.ORANGE};
    private final GameBoard<ConnectFourPiece> board = new GameBoard<>(BOARD_SIZE[0], BOARD_SIZE[1]);

    public Connect4Handler(UUID player1ID, UUID player2ID) {
        super(player1ID, player2ID, COLORS);
    }

    @Override
    public void makeMove(String moveDataJSON) {
        //USE THIS MOVE DATA WHEN MAKING MOVES IT CONTAINS DATA
        MoveData moveData = MoveData.fromJSON(moveDataJSON);
    }

    /**
     * pre-conditions:
     * 1. Board is initialized
     * 2. valid column index,
     * 3. non-negative column size
     *
     * post-conditions:
     * 1. If an empty (null) row exists in the specified column:
     *      The method returns the index of the lowest empty row in the column.
     * 2. If no empty row exists in the specified column:
     *      The method returns -1
     * @param column, index of target column
     * @return the index of the lowest available row for specified column
     */
    private int findAvailableRow(int column) {
        // Iterates through the rows of the specified column from bottom to top.
        for(int row = board.getColumnSize() - 1; row >= 0 ; row--){
            if (board.getAt(column, row) == null){
                return row;
            }
        }
        // No available row was found.
        return -1;
    }

    /**
     * pre-conditions:
     * 1. Board is initialized
     *
     * post-conditions:
     * 1. Returns false:
     *      If any position in the top row (row TOP_ROW) is empty (null).
     * 2. Returns true:
     *      If all positions in the top row (row TOP_ROW) are occupied (non-null).
     *
     * @return true if all cells in the top row are occupied (non-null);
     *         false if at least one cell in the top row is empty (null)
     */
    private boolean allColumnsAreFull() {
        int TOP_ROW  = 0;
        // Loop through each column in the top row.
        for(int i = 0; i < board.getRowSize(); i++ ){
            // If any spot in the top row is empty, the board is not full.
            if (board.getAt(i, TOP_ROW) == null){
                return false;
            }
        }
        // All spots in the top row are filled, so the board is full.
        return true;
    }

    /**
     * pre-conditions:
     * 1. Board is initialized
     * 2. Valid column index
     *
     * post-conditions:
     * 1. Returns false:
     *      If the cell at the top row (TOP_ROW) of the specified column is empty (null).
     * 2. Returns true:
     *      If the cell at the top row (TOP_ROW) of the specified column is occupied (non-null).
     * @param column, index of target column
     * @return true if the cell at the top row of the specified column is occupied (non-null);
     *         false if the cell is empty (null).
     */
    private boolean columnIsFull(int column) {
        int TOP_ROW  = 0;
        // Check if the top row cell at the given column is empty.
        if (board.getAt(column, TOP_ROW) == null){
            // If the cell is empty, return false.
            return false;
        }
        // If the cell is not empty, return true.
        return true;
    }

    private boolean checkForConnectedFour(Vector2 piecePos){
        int row = piecePos.y();
        int column = piecePos.x();

        PieceColor pieceColor = board.getAt(column, row).getColor();

        if (winInColumn(column, pieceColor) || winInRow(column, pieceColor) || winInDiagonalBackslash(row, column, pieceColor) || winInDiagonalFrontslash(row, column, pieceColor)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Checks if a player has won by having 4 consecutive pieces in a given row.
     *
     * Preconditions:
     *  - The `rowIndex` must be a valid row within the bounds of the board.
     *  - The `pieceColor` must not be `null`.
     *
     * Postconditions:
     *  - Returns `true` if exactly 4 consecutive pieces of the specified color exist in the row.
     *  - Returns `false` if no such sequence of 4 pieces exists in the row.
     *
     * @param rowIndex The index of the row to check for a win.
     * @param pieceColor The color of the piece to check for consecutive alignment.
     * @return `true` if there are 4 consecutive pieces of the given color in the row,
     *         `false` otherwise.
     */

    private boolean winInRow(int rowIndex, PieceColor pieceColor) {
        // Variable to track consecutive pieces of the same color in a row
        int chain = 0;

        // Loop through each column in the specified row
        for (int columnIndex = 0; columnIndex < board.getColumnSize(); col++) {
            // Get the piece at the current position in the row.
            T currentPiece = board.getAt(columnIndex, rowIndex);
            // If the piece exists and its color matches the provided pieceColor.
            if (currentPiece != null && currentPiece.getColor() == pieceColor) {
                // Increment the chain of matching pieces
                chain++;
                // If there are 4 consecutive pieces of the same color in a row, return true indicating a win.
                if (chain == 4) {
                    return true;
                }
            }
            else {
                // If the piece is null or the color doesn't match, reset the chain count.
                chain = 0;
            }
        }
        // Return false if no 4 consecutive pieces of the same color were found.
        return false;
    }

    /**
     * Checks if a player has won by having 4 consecutive pieces of a specified color in a given column.
     *
     * Preconditions:
     * - The `columnIndex` must be a valid column within the bounds of the board.
     * - The `pieceColor` must not be `null`.
     *
     * Postconditions:
     * - Returns `true` if exactly 4 consecutive pieces of the specified color exist in the column.
     * - Returns `false` if no such sequence of 4 pieces exists in the column.
     *
     * @param columnIndex The index of the column to check for a win. Must be within board boundaries.
     * @param pieceColor The color of the piece to check for consecutive alignment. Must not be null.
     * @return `true` if there are 4 consecutive pieces of the given color in the column,
     *         `false` otherwise.
     */

    private boolean winInColumn(int columnIndex, PieceColor pieceColor) {
        // Variable to track consecutive pieces of the same color in a column.
        int chain = 0;

        // Loop through each row in the specified column
        for (int rowIndex = 0; rowIndex < board.getRowSize(); col++) {
            // Get the piece at the current position in the column.
            T currentPiece = board.getAt(columnIndex, rowIndex);
            // If the piece exists and its color matches the provided pieceColor.
            if (currentPiece != null && currentPiece.getColor() == pieceColor) {
                // Increment the chain of matching pieces.
                chain++;
                // If there are 4 consecutive pieces of the same color in the column, return true indicating a win.
                if (chain == 4) {
                    return true;
                }
            }
            else {
                // If the piece is null or the color doesn't match, reset the chain count.
                chain = 0;
            }
        }
        // Return false if no 4 consecutive pieces of the same color were found.
        return false;
    }

    /**
     * Checks if a player has won by having 4 consecutive pieces of a specified color along a
     * backslash (`\`) diagonal starting from the specified position.
     *
     * Preconditions:
     * - The `rowIndex` and `columnIndex` must be valid indices within the bounds of the board.
     * - The `pieceColor` must not be `null`.
     *
     * Postconditions:
     * - Returns `true` if exactly 4 consecutive pieces of the specified color exist along the diagonal.
     * - Returns `false` if no such sequence of 4 pieces exists along the diagonal.
     *
     * @param rowIndex The row index of the starting position to check for a win.
     * @param columnIndex The column index of the starting position to check for a win.
     * @param pieceColor The color of the piece to check for consecutive alignment. Must not be null.
     * @return `true` if there are 4 consecutive pieces of the given color along the diagonal,
     *         `false` otherwise.
     */
    private boolean winInDiagonalBackslash(int rowIndex, int columnIndex, PieceColor pieceColor) {
        int topRow;
        int leftCol;

        // Trace back to the top-leftmost position of the diagonal.
        while (rowIndex > 0 && columnIndex > 0) {
            rowIndex -= 1;
            columnIndex -= 1;
        }

        // Store the top-leftmost position of the diagonal.
        topRow = rowIndex;
        leftCol = columnIndex;

        // Variable to track consecutive pieces of the same color along the diagonal.
        int chain = 0;

        // Iterate diagonally down-right to check for consecutive pieces of the specified color.
        for (; topRow < board.getRowSize() && leftCol < board.getColumnSize(); topRow++, leftCol++) {
            // Get the piece at the current position in the diagonal.
            T currentPiece = board.getAt(leftCol, topRow);
            // If the piece exists and its color matches the provided pieceColor.
            if (currentPiece != null && currentPiece.getColor() == pieceColor) {
                // Increment the chain of matching pieces.
                chain++;
                // If there are 4 consecutive pieces of the same color, return true indicating a win.
                if (chain == 4) {
                    return true;
                }
            }
            else {
                // If the piece is null or the color doesn't match, reset the chain count.
                chain = 0;
            }
        }
        // Return false if no 4 consecutive pieces of the same color were found along the diagonal.
        return false;
    }

    /**
     * Checks if a player has won by having 4 consecutive pieces of a specified color along a
     * frontslash (`/`) diagonal starting from the specified position.
     *
     * Preconditions:
     * - The `rowIndex` and `columnIndex` must be valid indices within the bounds of the board.
     * - The `pieceColor` must not be `null`.
     *
     * Postconditions:
     * - Returns `true` if exactly 4 consecutive pieces of the specified color exist along the diagonal.
     * - Returns `false` if no such sequence of 4 pieces exists along the diagonal.
     *
     * @param rowIndex The row index of the starting position to check for a win.
     * @param columnIndex The column index of the starting position to check for a win.
     * @param pieceColor The color of the piece to check for consecutive alignment. Must not be null.
     * @return `true` if there are 4 consecutive pieces of the given color along the diagonal,
     *         `false` otherwise.
     */
    private boolean winInDiagonalFrontslash(int rowIndex, int columnIndex, PieceColor pieceColor) {
        // Variables to track the starting point of the diagonal traversal.
        int bottomRow;
        int leftCol;

        // Trace back to the bottom-leftmost position of the diagonal.
        while ((rowIndex < board.getRowSize() - 1) && (columnIndex > 0)) {
            rowIndex += 1;
            columnIndex -= 1;
        }

        // Store the bottom-leftmost position of the diagonal.
        bottomRow = rowIndex;
        leftCol = columnIndex;

        // Variable to track consecutive pieces of the same color along the diagonal.
        int chain = 0;

        // Iterate diagonally up-right to check for consecutive pieces of the specified color.
        for (; bottomRow > 0 && leftCol < board.getColumnSize(); bottomRow--, leftCol++) {
            // Get the piece at the current position in the diagonal.
            T currentPiece = board.getAt(leftCol, bottomRow);
            // If the piece exists and its color matches the provided pieceColor.
            if (currentPiece != null && currentPiece.getColor() == pieceColor) {
                // Increment the chain of matching pieces.
                chain++;
                // If there are 4 consecutive pieces of the same color, return true indicating a win.
                if (chain == 4) {
                    return true;
                }
            }
            else {
                // If the piece is null or the color doesn't match, reset the chain count.
                chain = 0;
            }
        }
        // Return false if no 4 consecutive pieces of the same color were found along the diagonal.
        return false;
    }

}
