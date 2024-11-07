package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.*;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.GameBoard;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData.CheckersMoveData;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Pieces.CheckersPiece;

public class CheckersHandler extends GameHandler<CheckersMoveData> {
    private static final int[] BOARD_SIZE = {8, 8};
    private final GameBoard<CheckersPiece> board = new GameBoard<>(BOARD_SIZE[0], BOARD_SIZE[1]);

    @Override
    public void startGame() {

    }

    @Override
    public void makeMove(CheckersMoveData moveData) {

    }

    private boolean captureIsPossible(PieceColor color) {
        return false;
    }

    private void checkForGameEnd(){

    }

    private void checkForPromotion(int[] pos){

    }

    private void processCaptureOnMove(int[] oldPos, int[] newPos) {

    }
}
