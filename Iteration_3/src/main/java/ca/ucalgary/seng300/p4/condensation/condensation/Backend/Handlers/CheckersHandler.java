package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers;

import java.util.UUID;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.GameBoard;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData.CheckersMoveData;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Pieces.CheckersPiece;

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

    private boolean canCapture(CheckersPiece piece) {
        int[][] directions = getDirections(piece);
        for (int[] dir: directions){
            Vector2 adjacentPos = new Vector2(piece.getX() + dir[0], piece.getY() + dir[1]);
            Vector2 capturePos = new Vector2(piece.getX() + dir[0] * 2, piece.getY() + dir[1] * 2);
            if (board.positionWithinBounds(capturePos) && board.positionWithinBounds(adjacentPos)){
                CheckersPiece adjacentPiece = board.getPiece(adjacentPos);
                if (adjacentPiece != null && board.isMovePossible(capturePos) && adjacentPiece.getColor() != piece.getColor()){
                    return true;
                }

            }
        }
        return false;
    }

    private int[][] getDirections(CheckersPiece piece){
        if (piece.isKing()){
            return new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        }
        // Assuming red is on the bottom and black is on the top
        else if (piece.getColor() == PieceColor.RED){
            return new int[][]{{1, -1}, {-1, -1}};
        }
        else{
            return new int[][]{{1, 1}, {-1, 1}};
        }
    }


    private void checkForGameEnd(){

    }

    private void checkForPromotion(Vector2 pos){

    }

    private void processCaptureOnMove(Vector2 oldPos, Vector2 newPos) {

    }
}
