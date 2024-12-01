package ca.ucalgary.seng300.p4.condensation.Backend.Handlers;

import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.Backend.Pieces.CheckersPiece;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class CheckersHandlerTest {

    private CheckersHandler checkersHandler;
    private UUID player1ID;
    private UUID player2ID;

    @Before
    public void setUp() {
        player1ID = UUID.randomUUID();
        player2ID = UUID.randomUUID();
        checkersHandler = new CheckersHandler(player1ID, player2ID);
    }

    @Test
    public void testGetDirections_KingPiece() {
        CheckersPiece kingPiece = new CheckersPiece(new Vector2(2, 5), PieceColor.RED);
        kingPiece.promoteToKing();

        int[][] directions = checkersHandler.getDirections(kingPiece);

        assertEquals(4, directions.length);
        assertArrayEquals(new int[]{1, 1}, directions[0]);
        assertArrayEquals(new int[]{1, -1}, directions[1]);
        assertArrayEquals(new int[]{-1, 1}, directions[2]);
        assertArrayEquals(new int[]{-1, -1}, directions[3]);
    }

    @Test
    public void testGetDirections_RedPiece() {
        CheckersPiece redPiece = new CheckersPiece(new Vector2(2, 5), PieceColor.RED);

        int[][] directions = checkersHandler.getDirections(redPiece);

        assertEquals(2, directions.length);
        assertArrayEquals(new int[]{-1, -1}, directions[0]);
        assertArrayEquals(new int[]{-1, 1}, directions[1]);
    }

    @Test
    public void testGetDirections_BlackPiece() {
        CheckersPiece blackPiece = new CheckersPiece(new Vector2(5, 2), PieceColor.BLACK);

        int[][] directions = checkersHandler.getDirections(blackPiece);

        assertEquals(2, directions.length);
        assertArrayEquals(new int[]{1, 1}, directions[0]);
        assertArrayEquals(new int[]{1, -1}, directions[1]);
    }

    @Test
    public void testCanCapture_PieceCanCapture() {
        CheckersPiece redPiece = new CheckersPiece(new Vector2(5, 2), PieceColor.RED);
        CheckersPiece blackPiece = new CheckersPiece(new Vector2(4, 3), PieceColor.BLACK);
        checkersHandler.getBoard().placePiece(redPiece, redPiece.getPosition());
        checkersHandler.getBoard().placePiece(blackPiece, blackPiece.getPosition());

        boolean canCapture = checkersHandler.canCapture(redPiece);

        assertTrue(canCapture);
    }

    @Test
    public void testCanCapture_PieceCannotCapture() {
        CheckersPiece redPiece = new CheckersPiece(new Vector2(5, 2), PieceColor.RED);
        checkersHandler.getBoard().placePiece(redPiece, redPiece.getPosition());

        boolean canCapture = checkersHandler.canCapture(redPiece);

        assertFalse(canCapture);
    }

    @Test
    public void testCaptureIsPossible_PlayerCanCapture() {
        CheckersPiece redPiece = new CheckersPiece(new Vector2(5, 2), PieceColor.RED);
        CheckersPiece blackPiece = new CheckersPiece(new Vector2(4, 3), PieceColor.BLACK);
        checkersHandler.getBoard().placePiece(redPiece, redPiece.getPosition());
        checkersHandler.getBoard().placePiece(blackPiece, blackPiece.getPosition());

        boolean capturePossible = checkersHandler.captureIsPossible(PieceColor.RED);

        assertTrue(capturePossible);
    }

    @Test
    public void testCaptureIsPossible_PlayerCannotCapture() {
        CheckersPiece redPiece = new CheckersPiece(new Vector2(5, 2), PieceColor.RED);
        checkersHandler.getBoard().placePiece(redPiece, redPiece.getPosition());

        boolean capturePossible = checkersHandler.captureIsPossible(PieceColor.RED);

        assertFalse(capturePossible);
    }

    @Test
    public void testIsValidMove_ValidNonCaptureMove() {
        CheckersPiece redPiece = new CheckersPiece(new Vector2(5, 2), PieceColor.RED);
        checkersHandler.getBoard().placePiece(redPiece, redPiece.getPosition());
        Vector2 newPos = new Vector2(4, 1);

        boolean isValid = checkersHandler.isValidMove(redPiece.getPosition(), newPos);

        assertTrue(isValid);
    }

    @Test
    public void testIsValidMove_ValidCaptureMove() {
        CheckersPiece redPiece = new CheckersPiece(new Vector2(5, 2), PieceColor.RED);
        CheckersPiece blackPiece = new CheckersPiece(new Vector2(4, 3), PieceColor.BLACK);
        checkersHandler.getBoard().placePiece(redPiece, redPiece.getPosition());
        checkersHandler.getBoard().placePiece(blackPiece, blackPiece.getPosition());
        Vector2 newPos = new Vector2(3, 4);

        boolean isValid = checkersHandler.isValidMove(redPiece.getPosition(), newPos);

        assertTrue(isValid);
    }

    @Test
    public void testIsValidMove_InvalidMove_NotDiagonal() {
        CheckersPiece redPiece = new CheckersPiece(new Vector2(5, 2), PieceColor.RED);
        checkersHandler.getBoard().placePiece(redPiece, redPiece.getPosition());
        Vector2 newPos = new Vector2(5, 3);

        boolean isValid = checkersHandler.isValidMove(redPiece.getPosition(), newPos);

        assertFalse(isValid);
    }

    @Test
    public void testIsValidMove_InvalidMove_OccupiedDestination() {
        CheckersPiece redPiece = new CheckersPiece(new Vector2(5, 2), PieceColor.RED);
        CheckersPiece blockingPiece = new CheckersPiece(new Vector2(4, 1), PieceColor.RED);
        checkersHandler.getBoard().placePiece(redPiece, redPiece.getPosition());
        checkersHandler.getBoard().placePiece(blockingPiece, blockingPiece.getPosition());
        Vector2 newPos = new Vector2(4, 1);

        boolean isValid = checkersHandler.isValidMove(redPiece.getPosition(), newPos);

        assertFalse(isValid);
    }

    @Test
    public void testCanMove_PieceCanMove() {
        CheckersPiece redPiece = new CheckersPiece(new Vector2(5, 2), PieceColor.RED);
        checkersHandler.getBoard().placePiece(redPiece, redPiece.getPosition());

        boolean canMove = checkersHandler.canMove(redPiece.getPosition());

        assertTrue(canMove);
    }

    @Test
    public void testCanJump_PieceCanJump() {
        CheckersPiece redPiece = new CheckersPiece(new Vector2(5, 2), PieceColor.RED);
        CheckersPiece blackPiece = new CheckersPiece(new Vector2(4, 3), PieceColor.BLACK);
        checkersHandler.getBoard().placePiece(redPiece, redPiece.getPosition());
        checkersHandler.getBoard().placePiece(blackPiece, blackPiece.getPosition());

        boolean canJump = checkersHandler.canJump(redPiece.getPosition());

        assertTrue(canJump);
    }

    @Test
    public void testCanJump_PieceCannotJump() {
        CheckersPiece redPiece = new CheckersPiece(new Vector2(0, 0), PieceColor.RED);
        checkersHandler.getBoard().placePiece(redPiece, redPiece.getPosition());

        boolean canJump = checkersHandler.canJump(redPiece.getPosition());

        assertFalse(canJump);
    }

    @Test
    public void testCheckForPromotion_PieceShouldPromote() {
        CheckersPiece redPiece = new CheckersPiece(new Vector2(0, 1), PieceColor.RED);
        checkersHandler.getBoard().placePiece(redPiece, redPiece.getPosition());

        checkersHandler.checkForPromotion(redPiece.getPosition());

        assertTrue(redPiece.isKing());
    }

    @Test
    public void testCheckForPromotion_PieceShouldNotPromote() {
        CheckersPiece redPiece = new CheckersPiece(new Vector2(1, 2), PieceColor.RED);
        checkersHandler.getBoard().placePiece(redPiece, redPiece.getPosition());

        checkersHandler.checkForPromotion(redPiece.getPosition());

        assertFalse(redPiece.isKing());
    }

    @Test
    public void testProcessCaptureOnMove_CapturesPiece() {
        CheckersPiece redPiece = new CheckersPiece(new Vector2(5, 2), PieceColor.RED);
        CheckersPiece blackPiece = new CheckersPiece(new Vector2(4, 3), PieceColor.BLACK);
        checkersHandler.getBoard().placePiece(redPiece, redPiece.getPosition());
        checkersHandler.getBoard().placePiece(blackPiece, blackPiece.getPosition());

        Vector2 newPos = new Vector2(3, 4);
        checkersHandler.processCaptureOnMove(redPiece.getPosition(), newPos);

        assertNull(checkersHandler.getBoard().getAt(4, 3));
    }

    @Test
    public void testProcessCaptureOnMove_NoPieceToCapture() {
        CheckersPiece redPiece = new CheckersPiece(new Vector2(5, 2), PieceColor.RED);
        checkersHandler.getBoard().placePiece(redPiece, redPiece.getPosition());

        Vector2 newPos = new Vector2(4, 1);
        checkersHandler.processCaptureOnMove(redPiece.getPosition(), newPos);

        // No exception should occur, board state remains unchanged
        assertNotNull(checkersHandler.getBoard().getAt(5, 2));
        assertNull(checkersHandler.getBoard().getAt(4, 1));
    }

    @Test
    public void testMakeMove_ValidMove() {
        // Since makeMove is not implemented, this test cannot be performed fully.
        // Once makeMove is implemented, add appropriate assertions here.
    }

    @Test
    public void testMakeMove_InvalidMove() {
        // Since makeMove is not implemented, this test cannot be performed fully.
        // Once makeMove is implemented, add appropriate assertions here.
    }
}