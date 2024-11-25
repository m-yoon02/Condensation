package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.*;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.GameBoard;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData.CheckersMoveData;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Pieces.CheckersPiece;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs.Vector2;

import java.util.UUID;

public class CheckersHandler extends GameHandler {
    private static final int[] BOARD_SIZE = {8, 8};
    private static final PieceColor[] COLORS = {PieceColor.RED, PieceColor.BLACK};
    private final GameBoard<CheckersPiece> board = new GameBoard<>(BOARD_SIZE[0], BOARD_SIZE[1]);

    public CheckersHandler(UUID player1ID, UUID player2ID) {
        super(player1ID, player2ID, COLORS);
    }

    @Override
    public void makeMove(String moveDataJSON) {
        //USE THIS MOVE DATA WHEN MAKING MOVES IT CONTAINS DATA
        CheckersMoveData checkersMoveData = CheckersMoveData.fromJSON(moveDataJSON);
    }

    private boolean captureIsPossible(PieceColor color) {
        return false;
    }

    private boolean checkForGameEnd(PieceColor color){
        boolean gameEnd = true;
        for(int i = 0; i < BOARD_SIZE[0]; i++){
            for(int j = 0; j < BOARD_SIZE[1]; j++){
                Vector2 pos = new Vector2(i, j);
                if(board.getPiece(pos).getColor().equals(color)){
                    gameEnd = false;
                }
            }
        }
        return gameEnd;
    }

    private void checkForPromotion(Vector2 pos){

    }

    private void processCaptureOnMove(Vector2 oldPos, Vector2 newPos) {

    }
}
