package ca.ucalgary.seng300.p4.condensation.Backend.Handlers;

import ca.ucalgary.seng300.p4.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.*;
import ca.ucalgary.seng300.p4.condensation.Backend.Board.GameBoard;
import ca.ucalgary.seng300.p4.condensation.Backend.MoveData.CheckersMoveData;
import ca.ucalgary.seng300.p4.condensation.Backend.Pieces.CheckersPiece;
import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;

import java.util.UUID;

public class CheckersHandler extends GameHandler {
    private static final int[] BOARD_SIZE = {8, 8};
    private static final PieceColor[] COLORS = {PieceColor.RED, PieceColor.BLACK};
    private final GameBoard<CheckersPiece> board = new GameBoard<>(BOARD_SIZE[0], BOARD_SIZE[1]);

    public CheckersHandler(UUID player1ID, UUID player2ID) {
        super(player1ID, player2ID, COLORS);
    }

    // This is the main driver of your game logic.
    // Assume that this method is called whenever a player makes a move
    // You are to use the move data and translate it to the GameBoard
    // You are also required to check for game winning scenarios here
    // This will also notify when the game has ended, and which player's turn it is after the move
    @Override
    public void makeMove(String moveDataJSON) {
        //USE THIS MOVE DATA WHEN MAKING MOVES IT CONTAINS DATA
        CheckersMoveData checkersMoveData = CheckersMoveData.fromJSON(moveDataJSON);
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
