package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.GameBoard;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData.MoveData;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Pieces.ConnectFourPiece;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs.Vector2;

public class Connect4Handler extends GameHandler<MoveData> {
    private static final int[] BOARD_SIZE = {6, 7};
    private final GameBoard<ConnectFourPiece> board = new GameBoard<>(BOARD_SIZE[0], BOARD_SIZE[1]);

    @Override
    public void startGame() {

    }

    @Override
    public void makeMove(MoveData moveData) {

    }

    private int findAvailableRow(int column) {
        return 0;
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
