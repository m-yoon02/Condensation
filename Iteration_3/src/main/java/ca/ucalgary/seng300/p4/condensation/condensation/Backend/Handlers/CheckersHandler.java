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
        for (int x = 0; x < BOARD_SIZE[0]; x++) {
            for (int y = 0; y < BOARD_SIZE[1]; y ++) {
                CheckersPiece piece = board.getAt(x, y);
                if(piece != null && piece.getColor() == color && canCapture(piece)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canCapture(CheckersPiece piece) {
        int[][] directions = getDirections(piece);
        for (int[] dir: directions){
            Vector2 adjacentPos = new Vector2(piece.getX() + dir[0], piece.getY() + dir[1]);
            Vector2 capturePos = new Vector2(piece.getX() + dir[0] * 2, piece.getY() + dir[1] * 2);
            if (board.positionWithinBounds(capturePos) && board.positionWithinBounds(adjacentPos)){
                CheckersPiece adjacentPiece = board.getAt(adjacentPos);
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


    private boolean checkForGameEnd(PieceColor color){
        for(int i = 0; i < BOARD_SIZE[0]; i++){
            for(int j = 0; j < BOARD_SIZE[1]; j++){
                Vector2 pos = new Vector2(i, j);
                if(board.getAt(pos).getColor().equals(color)){
                    if(canMove(pos) || canJump(pos)){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    /**
     *A helper method to check if a piece at a position can move 1 tile ahead. Doesn't check jumps
     *
     * @param pos of the piece that is being checked
     * @return true if piece is able to move
     */
    private boolean canMove(Vector2 pos){
        if(pos.x() == 0){
            if(pos.y() == 7){
                if(board.getAt(pos.x()+1, pos.y()-1) == null){
                    return true;
                }
            } else {
                if(board.getAt(pos.x()+1, pos.y()+1) == null
                        || board.getAt(pos.x()+1, pos.y()-1) == null){
                    return true;
                }
            }
        } else if(pos.x() == 7){
            if(pos.y() == 0){
                if(board.getAt(pos.x()-1, pos.y()+1) == null){
                    return true;
                }
            } else {
                if(board.getAt(pos.x()-1, pos.y()+1) == null
                        || board.getAt(pos.x()-1, pos.y()-1) == null){
                    return true;
                }
            }
        } else{
            if(pos.y() == 0){
                if(board.getAt(pos.x()-1, pos.y()+1) == null
                        || board.getAt(pos.x()+1, pos.y()+1) == null){
                    return true;
                }
            } else if(pos.y() == 7){
                if(board.getAt(pos.x()-1, pos.y()-1) == null
                        || board.getAt(pos.x()+1, pos.y()-1) == null){
                    return true;
                }
            } else {
                if(board.getAt(pos.x()-1, pos.y()+1) == null || board.getAt(pos.x()+1, pos.y()-1) == null
                || board.getAt(pos.x()+1, pos.y()+1) == null || board.getAt(pos.x()-1, pos.y()-1) == null){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * A helper method to check if a piece can jump over another piece from a given position
     *
     * @param pos
     * @return boolean
     */
    private boolean canJump(Vector2 pos){
        if(pos.x() == 0){
            if(pos.y() == 1){
                if (board.getAt(pos.x()+1, pos.y()+1) != null
                        && board.getAt(pos.x()+2, pos.y()+2) == null) {
                    return true;
                }
            } else if(pos.y() == 7){
                if (board.getAt(pos.x()+1, pos.y()-1) != null
                        && board.getAt(pos.x()+2, pos.y()-2) == null) {
                    return true;
                }
            } else{
                if((board.getAt(pos.x()+1, pos.y()+1) != null && board.getAt(pos.x()+2, pos.y()+2) == null)
                        || (board.getAt(pos.x()+1, pos.y()-1) != null && board.getAt(pos.x()+2, pos.y()-2) == null)){
                    return true;
                }
            }
        } else if(pos.x() == 1){
            if(pos.y() == 0){
                if(board.getAt(pos.x()+1, pos.y()+1) != null
                        && board.getAt(pos.x()+2, pos.y()+2) == null){
                    return true;
                }
            } else if(pos.y() == 6){
                if(board.getAt(pos.x()+1, pos.y()-1) != null
                        && board.getAt(pos.x()+2, pos.y()-2) == null){
                    return true;
                }
            } else{
                if((board.getAt(pos.x()+1, pos.y()+1) != null && board.getAt(pos.x()+2, pos.y()+2) == null)
                        || (board.getAt(pos.x()+1, pos.y()-1) != null && board.getAt(pos.x()+2, pos.y()-2) == null)){
                    return true;
                }
            }
        } else if(pos.x() == 6){
            if(pos.y() == 1){
                if (board.getAt(pos.x()-1, pos.y()+1) != null
                        && board.getAt(pos.x()-2, pos.y()+2) == null) {
                    return true;
                }
            } else if(pos.y() == 7){
                if (board.getAt(pos.x()-1, pos.y()-1) != null
                        && board.getAt(pos.x()-2, pos.y()-2) == null) {
                    return true;
                }
            } else{
                if((board.getAt(pos.x()-1, pos.y()+1) != null && board.getAt(pos.x()-2, pos.y()+2) == null)
                        || (board.getAt(pos.x()-1, pos.y()-1) != null && board.getAt(pos.x()-2, pos.y()-2) == null)){
                    return true;
                }
            }
        } else if(pos.x() == 7){
            if(pos.y() == 0){
                if (board.getAt(pos.x()-1, pos.y()+1) != null
                        && board.getAt(pos.x()-2, pos.y()+2) == null) {
                    return true;
                }
            } else if(pos.y() == 6){
                if (board.getAt(pos.x()-1, pos.y()-1) != null
                        && board.getAt(pos.x()-2, pos.y()-2) == null) {
                    return true;
                }
            } else{
                if((board.getAt(pos.x()-1, pos.y()+1) != null && board.getAt(pos.x()-2, pos.y()+2) == null)
                        || (board.getAt(pos.x()-1, pos.y()-1) != null && board.getAt(pos.x()-2, pos.y()-2) == null)){
                    return true;
                }
            }
        } else {
            if(pos.y() == 0 || pos.y() == 1){
                if((board.getAt(pos.x()+1, pos.y()+1) != null && board.getAt(pos.x()+2, pos.y()+2) == null)
                        || (board.getAt(pos.x()-1, pos.y()+1) != null && board.getAt(pos.x()-2, pos.y()+2) == null)){
                    return true;
                }
            }  else if(pos.y() == 6 || pos.y() == 7){
                if((board.getAt(pos.x()+1, pos.y()-1) != null && board.getAt(pos.x()+2, pos.y()-2) == null)
                        || (board.getAt(pos.x()-1, pos.y()-1) != null && board.getAt(pos.x()-2, pos.y()-2) == null)){
                    return true;
                }
            } else{
                if((board.getAt(pos.x()+1, pos.y()-1) != null && board.getAt(pos.x()+2, pos.y()-2) == null)
                        || (board.getAt(pos.x()-1, pos.y()-1) != null && board.getAt(pos.x()-2, pos.y()-2) == null)
                        || (board.getAt(pos.x()+1, pos.y()+1) != null && board.getAt(pos.x()+2, pos.y()+2) == null)
                        || (board.getAt(pos.x()-1, pos.y()+1) != null && board.getAt(pos.x()-2, pos.y()+2) == null)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if a piece at the specified position qualifies for promotion
     * and promotes it to a king if it does.
     *
     * @param pos the position of the piece to check for promotion.
     */
    private void checkForPromotion(Vector2 pos){
        CheckersPiece piece = board.getAt(pos);
        if (piece == null){
            return;
        }

        int finalRow = -1;
        if (piece.getColor() == PieceColor.RED){
            finalRow = 0;
        } else if (piece.getColor() == PieceColor.BLACK){
            finalRow = 7;
        }

        if (pos.x() == finalRow && !piece.isKing()){
            piece.promoteToKing();
        }
    }

    private void processCaptureOnMove(Vector2 oldPos, Vector2 newPos) {

    }
}
