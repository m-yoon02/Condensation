package ca.ucalgary.seng300.p4.condensation.Backend.Handlers;

import ca.ucalgary.seng300.p4.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.Backend.Board.GameBoard;
import ca.ucalgary.seng300.p4.condensation.Backend.MoveData.CheckersMoveData;
import ca.ucalgary.seng300.p4.condensation.Backend.Pieces.CheckersPiece;
import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;

import java.util.UUID;

import static java.lang.Math.abs;

public class CheckersHandler extends GameHandler {
    private static final int[] BOARD_SIZE = {8, 8};
    private static final PieceColor[] COLORS = {PieceColor.RED, PieceColor.BLACK};
    private final GameBoard<CheckersPiece> board = new GameBoard<>(BOARD_SIZE[0], BOARD_SIZE[1]);

    public CheckersHandler(UUID player1ID, UUID player2ID) {
        super(player1ID, player2ID, COLORS);
    }

    /**
     * For testing purposes
     * @return the board
     */
    public GameBoard<CheckersPiece> getBoard() {
        return this.board;
    }

    @Override
    public void makeMove(String moveDataJSON) {
        makeMove(CheckersMoveData.fromJSON(moveDataJSON));
    }

    // This is the main driver of your game logic.
    // Assume that this method is called whenever a player makes a move
    // You are to use the move data and translate it to the GameBoard
    // You are also required to check for game winning scenarios here
    // This will also notify when the game has ended, and which player's turn it is after the move
    public void makeMove(CheckersMoveData checkersMoveData) {
        //USE THIS MOVE DATA WHEN MAKING MOVES IT CONTAINS DATA
    }

    public boolean captureIsPossible(PieceColor color) {
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

    public boolean canCapture(CheckersPiece piece) {
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

    public int[][] getDirections(CheckersPiece piece){
        if (piece.isKing()){
            return new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        }
        // Assuming red is on the bottom and black is on the top
        else if (piece.getColor() == PieceColor.RED){
            return new int[][]{{-1, -1}, {-1, 1}};
        }
        else{
            return new int[][]{{1, 1}, {1, -1}};
        }
    }

    /**
     * checks if a piece at a given position can move to another position in the board's current state
     *
     * @param oldPos, the piece's original position
     * @param newPos, the target destination position
     * @return true if the move is valid, false otherwise
     */
    public boolean isValidMove(Vector2 oldPos, Vector2 newPos){
        int distanceX = abs(newPos.x()) - abs(oldPos.x());
        int distanceY = abs(newPos.y()) - abs(oldPos.y());
        if(abs(distanceX) > 2 || abs(distanceY) > 2 || abs(distanceX) < 1 || abs(distanceY) < 1
                || abs(distanceY) != abs(distanceX) || board.getAt(newPos) != null){
            return false;
        }
        if(abs(distanceX) == 2 && board.getAt(oldPos.x()+(distanceX/2), oldPos.y()+(distanceY/2)) == null){
            return false;
        }
        return true;
    }
    /**
     * Checks if a player is able to make further moves
     *
     * @param color of pieces of the player to check
     * @return true if game cannot continue, false otherwise
     */
    public boolean checkForGameEnd(PieceColor color){
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
    public boolean canMove(Vector2 pos){
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
    public boolean canJump(Vector2 pos){
        if(pos.x() == 0 || pos.x() == 1){
            if(pos.y() == 0 || pos.y() == 1){
                if (board.getAt(pos.x()+1, pos.y()+1) != null
                        && board.getAt(pos.x()+2, pos.y()+2) == null) {
                    return true;
                }
            } else if(pos.y() == 6 || pos.y() == 7){
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
        } else if(pos.x() == 6 || pos.x() == 7){
            if(pos.y() == 0 || pos.y() == 1){
                if (board.getAt(pos.x()-1, pos.y()+1) != null
                        && board.getAt(pos.x()-2, pos.y()+2) == null) {
                    return true;
                }
            } else if(pos.y() == 6 || pos.y() == 7){
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
    public void checkForPromotion(Vector2 pos){
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

    /**
     * Processes the capture of a piece, to be used after a capture move is performed
     * @param oldPos the old position of the capturing piece
     * @param newPos the new position of the capturing piece
     */
    public void processCaptureOnMove(Vector2 oldPos, Vector2 newPos) {
        // Calculated by using midpoint, and assuming kings capture with finite range
        int capturedPieceRow = (oldPos.x() + newPos.x()) / 2;
        int capturedPieceCol = (oldPos.y() + newPos.y()) / 2;
        Vector2 capturedPiecePos = new Vector2(capturedPieceRow, capturedPieceCol);

        board.removePiece(capturedPiecePos);
    }
}
