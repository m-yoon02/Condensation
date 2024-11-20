package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.*;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.GameBoard;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData.CheckersMoveData;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Pieces.CheckersPiece;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs.Vector2;

public class CheckersHandler extends GameHandler {
    private static final int[] BOARD_SIZE = {8, 8};
    private final GameBoard<CheckersPiece> board = new GameBoard<>(BOARD_SIZE[0], BOARD_SIZE[1]);

    @Override
    public void startGame() {

    }

    @Override
    public void makeMove(String moveDataJSON) {

    }

    private boolean captureIsPossible(PieceColor color) {
        return false;
    }

    private void checkForGameEnd(){

    }

    private void checkForPromotion(Vector2 pos){

    }

    private void processCaptureOnMove(Vector2 oldPos, Vector2 newPos) {

    }
}
