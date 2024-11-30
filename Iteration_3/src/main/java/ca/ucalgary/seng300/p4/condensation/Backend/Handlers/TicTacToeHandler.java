package ca.ucalgary.seng300.p4.condensation.Backend.Handlers;

import ca.ucalgary.seng300.p4.condensation.Backend.Enums.GameResult;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.Backend.Board.GameBoard;
import ca.ucalgary.seng300.p4.condensation.Backend.MoveData.MoveData;
import ca.ucalgary.seng300.p4.condensation.Backend.Pieces.TicTacToePiece;
import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;

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
        makeMove(MoveData.fromJSON(moveDataJSON));
    }

    // This is the main driver of your game logic.
    // Assume that this method is called whenever a player makes a move
    // You are to use the move data and translate it to the GameBoard
    // You are also required to check for game winning scenarios here
    // This will also notify when the game has ended, and which player's turn it is after the move
    /**
     * This is the main driver of your game logic. Allows player to make move
     * @param moveData the Move data of the move made
     */
    public void makeMove(MoveData moveData) {
        //USE THIS MOVE DATA WHEN MAKING MOVES IT CONTAINS DATA
        UUID playerID = moveData.getPlayerID();
        PieceColor color = getPlayerMemoFromUUID(moveData.getPlayerID()).pieceColor();
        Vector2 movePosition = moveData.getMovePosition();
        TicTacToePiece piece = new TicTacToePiece(movePosition, color);
        if (!this.board.placePiece(piece, movePosition)){
            this.notifyTurnEnd(playerID, board.toJSONString());
            return;
        }
        if (checkForThreeInARow(color)){
            this.notifyGameEnd(GameResult.WIN, playerID);
        } else if (board.isBoardFull()){
            this.notifyGameEnd(GameResult.TIE,null);
        } else {
            this.notifyTurnEnd(getOtherPlayerUUID(playerID), board.toJSONString());
        }
    }

    public boolean checkForThreeInARow(PieceColor color){
        return checkDiagonalWin(color)||checkHorizontalWin(color)||checkVerticalWin(color);

    }
    private boolean checkVerticalWin(PieceColor color){
        boolean win =false;
        if (board.getAt(0,0)!=null && board.getAt(1,0)!=null && board.getAt(2,0)!=null){
            //colours of in first diagonal
            PieceColor color1 = board.getAt(0, 0).getColor();
            PieceColor color2 = board.getAt(1, 0).getColor();
            PieceColor color3 = board.getAt(2, 0).getColor();
            if (color1==color && color2==color && color3==color){
                win=true;
            } else if (board.getAt(0,1)!=null && board.getAt(1,1)!=null && board.getAt(2,1)!=null){
                //colours of in first diagonal
                color1 = board.getAt(0, 1).getColor();
                color2 = board.getAt(1, 1).getColor();
                color3 = board.getAt(2, 1).getColor();
                if (color1==color && color2==color && color3==color){
                    win=true;
                }} else if (board.getAt(0,2)!=null && board.getAt(1,2)!=null && board.getAt(2,2)!=null){
                //colours of in first diagonal
                color1 = board.getAt(0, 1).getColor();
                color2 = board.getAt(1, 1).getColor();
                color3 = board.getAt(2, 1).getColor();
                if (color1==color && color2==color && color3==color){
                    win=true;
                }
            }
        }
        return win;
    }

    private boolean checkHorizontalWin(PieceColor color){
        boolean win =false;
        if (board.getAt(0,0)!=null && board.getAt(0,1)!=null && board.getAt(0,2)!=null){
            //colours of in first diagonal
            PieceColor color1 = board.getAt(0, 0).getColor();
            PieceColor color2 = board.getAt(0, 1).getColor();
            PieceColor color3 = board.getAt(0, 2).getColor();
            if (color1==color && color2==color && color3==color){
                win=true;
            } else if (board.getAt(1,0)!=null && board.getAt(1,1)!=null && board.getAt(1,2)!=null){
                //colours of in first diagonal
                color1 = board.getAt(1, 0).getColor();
                color2 = board.getAt(1, 1).getColor();
                color3 = board.getAt(1, 2).getColor();
                if (color1==color && color2==color && color3==color){
                    win=true;
                }} else if (board.getAt(2,0)!=null && board.getAt(2,1)!=null && board.getAt(2,2)!=null){
                //colours of in first diagonal
                color1 = board.getAt(2, 0).getColor();
                color2 = board.getAt(2, 1).getColor();
                color3 = board.getAt(2, 2).getColor();
                if (color1==color && color2==color && color3==color){
                    win=true;
                }
            }
        }
        return win;
    }
    private boolean checkDiagonalWin(PieceColor color) {
        boolean win = false;
        if (board.getAt(0, 0) != null && board.getAt(1, 1) != null && board.getAt(2, 2) != null) {
            //colours of in first diagonal
            PieceColor color1 = board.getAt(0, 0).getColor();
            PieceColor color2 = board.getAt(1, 1).getColor();
            PieceColor color3 = board.getAt(2, 2).getColor();
            if (color1 == color && color2 == color && color3 == color) {
                win = true;
            } else if (board.getAt(0, 2) != null && board.getAt(1, 1) != null && board.getAt(2, 0) != null) {
                //colours of in first diagonal
                color1 = board.getAt(0, 2).getColor();
                color2 = board.getAt(1, 1).getColor();
                color3 = board.getAt(2, 0).getColor();
                if (color1 == color && color2 == color && color3 == color) {
                    win = true;
                }
            }
        }
        return win;
    }
}
