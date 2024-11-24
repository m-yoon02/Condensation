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
     * pre conditions:
     * 1.valid column index,
     * 2.non-negative column size
     *
     * post conditions:
     * 1. If an empty (null) row exists in the specified column:
     *      The method returns the index of the lowest empty row in the column.
     * 2. If no empty row exists in the specified column:
     *      The method returns -1
     * @param column
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

    private boolean allColumnsAreFull() {
        return false;
    }

    private boolean columnIsFull(int column) {
        return false;
    }

    private void checkForConnectedFour(Vector2 piecePos){

    }
}
