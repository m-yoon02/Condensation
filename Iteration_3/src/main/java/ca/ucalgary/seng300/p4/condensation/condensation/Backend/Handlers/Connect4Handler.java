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

    // This is the main driver of your game logic.
    // Assume that this method is called whenever a player makes a move
    // You are to use the move data and translate it to the GameBoard
    // You are also required to check for game winning scenarios here
    // This will also notify when the game has ended, and which player's turn it is after the move
    @Override
    public void makeMove(String moveDataJSON) {
        //USE THIS MOVE DATA WHEN MAKING MOVES IT CONTAINS DATA
        MoveData moveData = MoveData.fromJSON(moveDataJSON);
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
