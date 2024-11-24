package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.GameBoard;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData.MoveData;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Pieces.TicTacToePiece;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs.Vector2;

import java.util.UUID;

public class TicTacToeHandler extends GameHandler {
    private static final int[] BOARD_SIZE = {3, 3};
    // X can be RED, O can be BLUE
    private static final PieceColor[] COLORS = {PieceColor.RED, PieceColor.BLUE};
    private final GameBoard<TicTacToePiece> board = new GameBoard<>(BOARD_SIZE[0], BOARD_SIZE[1]);

    public TicTacToeHandler(UUID player1ID, UUID player2ID) {
        super(player1ID, player2ID, COLORS);
    }

    @Override
    public void makeMove(String moveDataJSON) {
        //USE THIS MOVE DATA WHEN MAKING MOVES IT CONTAINS DATA
        MoveData moveData = MoveData.fromJSON(moveDataJSON);
        UUID playerID = moveData.getPlayerID();
        PieceColor color = getPlayerMemoFromUUID(moveData.getPlayerID()).pieceColor();
        Vector2 movePosition = moveData.getMovePosition();

        boolean isMovePossible = board.isMovePossible();
    }

    private void checkForThreeInARow(Vector2 pos){

    }
}
