package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.GameBoard;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData.MoveData;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Pieces.TicTacToePiece;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs.Vector2;

public class TicTacToeHandler extends GameHandler {
    private static final int[] BOARD_SIZE = {3, 3};
    private final GameBoard<TicTacToePiece> board = new GameBoard<>(BOARD_SIZE[0], BOARD_SIZE[1]);

    @Override
    public void startGame() {

    }

    @Override
    public void makeMove(String moveDataJSON) {

    }

    private void checkForThreeInARow(Vector2 pos){

    }
}
