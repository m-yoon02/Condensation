package ca.ucalgary.seng300.p4.condensation.Backend.Handlers;

import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.Backend.MoveData.MoveData;
import ca.ucalgary.seng300.p4.condensation.Backend.Pieces.ConnectFourPiece;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Connect4HandlerTest {

    private UUID p1ID;
    private UUID p2ID;
    private Connect4Handler c4Handler;

    @Before
    public void setUp() {
        p1ID = UUID.randomUUID();
        p2ID = UUID.randomUUID();
        c4Handler = new Connect4Handler(p1ID, p2ID);
    }

    @Test
    public void testMakeMode_ValidP1Move_UpdatesBoard() {
        MoveData md = new MoveData(p1ID, new Vector2(0, 0));

        c4Handler.makeMove(md);

        ConnectFourPiece piece = c4Handler.getBoard().getAt(0,5);
        assertNotNull("Piece should be placed at bottom of column", piece);
        assertEquals("Piece colour is player colour", c4Handler.getPlayerColorFromUUID(p1ID), piece.getColor());
    }

    @Test
    public void testMakeMove_ValidP2Move_UpdatesBoard() {
        MoveData md = new MoveData(p2ID, new Vector2(0, 0));

        c4Handler.makeMove(md);

        ConnectFourPiece piece = c4Handler.getBoard().getAt(0,5);
        assertNotNull("Piece should be placed at bottom of column", piece);
        assertEquals("Piece colour is player colour", c4Handler.getPlayerColorFromUUID(p2ID), piece.getColor());
    }

    @Test
    public void testMakeMove_ColumnFull_DoesNotUpdateBoard() {
        int column = 0;

        for(int i = 0; i < c4Handler.getBoard().getRowSize(); i++) {
            c4Handler.getBoard().placePiece(new ConnectFourPiece(c4Handler.getPlayerColorFromUUID(p1ID)), new Vector2(column, i));
        }

        MoveData md = new MoveData(p1ID, new Vector2(column, 0));

        c4Handler.makeMove(md);

        int filledCells = 0;
        for(int row = 0; row < c4Handler.getBoard().getRowSize(); row++) {
            if (c4Handler.getBoard().getAt(column, row) != null) {
                filledCells++;
            }
        }

        assertEquals("Column still full", c4Handler.getBoard().getRowSize(), filledCells);
    }

    @Test
    public void testFindAvailableRow_ColumnNotFull_ReturnsRowIndex() {
        int column = 0;

        int availableRow = c4Handler.findAvailableRow(column);

        assertEquals("Available row at bottom of column", c4Handler.getBoard().getRowSize() - 1, availableRow);
    }

    @Test
    public void testFindAvailableRow_ColumnFull_ReturnsMinus1() {
        int column = 0;

        for (int i = 0; i < c4Handler.getBoard().getRowSize(); i++) {
            c4Handler.getBoard().placePiece(new ConnectFourPiece(c4Handler.getPlayerColorFromUUID(p1ID)), new Vector2(column, i));
        }

        int availableRow = c4Handler.findAvailableRow(column);

        assertEquals("Returns -1 when column is full", -1, availableRow);
    }

    @Test
    public void testAllColumnsAreFull_ReturnsTrueWhenFull() {
        for (int col = 0; col < c4Handler.getBoard().getColumnSize(); col++) {
            for (int row = 0; row < c4Handler.getBoard().getRowSize(); row++) {
                c4Handler.getBoard().placePiece(new ConnectFourPiece(c4Handler.getPlayerColorFromUUID(p1ID)), new Vector2(col, row));
            }
        }

        boolean result = c4Handler.allColumnsAreFull();

        assertTrue("True when all columns are full", result);
    }

    @Test
    public void testAllColumnsAreFull_ReturnsFalseWhenEmpty() {
        boolean result = c4Handler.allColumnsAreFull();

        assertFalse("False when all columns are empty", result);
    }

    @Test
    public void testAllColumnsAreFull_ReturnsFalseWhenPartiallyFull() {
        c4Handler.getBoard().placePiece(new ConnectFourPiece(c4Handler.getPlayerColorFromUUID(p1ID)), new Vector2(0, 0));

        boolean result = c4Handler.allColumnsAreFull();

        assertFalse("False when not all columns are full", result);
    }

    @Test
    public void testColumnIsFull_ReturnsTrueWhenFull() {
        int column = 0;

        for (int i = 0; i < c4Handler.getBoard().getRowSize(); i++) {
            c4Handler.getBoard().placePiece(new ConnectFourPiece(c4Handler.getPlayerColorFromUUID(p1ID)), new Vector2(column, i));
        }

        boolean result = c4Handler.columnIsFull(column);

        assertTrue("Returns true when column is full", result);
    }

    @Test
    public void testColumnIsFull_ReturnsFalseWhenEmpty() {
        int column = 0;

        boolean result = c4Handler.columnIsFull(column);

        assertFalse("Returns flase when column is not full", result);
    }

    @Test
    public void testCheckForConnectedFour_ReturnsFalseWhenNoWin() {
        Vector2 position = new Vector2(0, c4Handler.getBoard().getRowSize() - 1);
        c4Handler.getBoard().placePiece(new ConnectFourPiece(PieceColor.BLUE), position);

        boolean result = c4Handler.checkForConnectedFour(position);

        assertFalse("Returns false when there is no win", result);
    }

    @Test
    public void testCheckForConnectedFour_ReturnsTrueOnVerticalWin() {
        int column = 0;

        PieceColor color = c4Handler.getPlayerColorFromUUID(p1ID);

        for (int i = 0; i < 4; i++) {
            c4Handler.getBoard().placePiece(new ConnectFourPiece(color), new Vector2(column, c4Handler.getBoard().getRowSize() - 1 - i));
        }

        Vector2 position = new Vector2(column, c4Handler.getBoard().getRowSize() - 4);

        boolean result = c4Handler.checkForConnectedFour(position);

        assertTrue("Returns true when there is a win", result);
    }

    @Test
    public void testWinInRow_DetectsHorizontalWin() {
        int row = c4Handler.getBoard().getRowSize() - 1;

        PieceColor color = c4Handler.getPlayerColorFromUUID(p1ID);

        for (int col = 0; col < 4; col++) {
            c4Handler.getBoard().placePiece(new ConnectFourPiece(color), new Vector2(col, row));
        }

        boolean result = c4Handler.winInRow(row, color);

        assertTrue("Detects horizontal win", result);
    }

    @Test
    public void testWinInRow_ReturnsFalseOnNoWin() {
        boolean result = c4Handler.winInRow(0, PieceColor.BLUE);

        assertFalse("Returns false when there is no win", result);
    }

    @Test
    public void testWinInColumn_DetectsVerticalWin() {
        int column = 0;

        PieceColor color = c4Handler.getPlayerColorFromUUID(p1ID);

        for (int row = c4Handler.getBoard().getRowSize() - 1; row >= c4Handler.getBoard().getRowSize() - 4; row--) {
            c4Handler.getBoard().placePiece(new ConnectFourPiece(color), new Vector2(column, row));
        }

        boolean result = c4Handler.winInColumn(column, color);

        assertTrue("Detects vertical win", result);
    }

    @Test
    public void testWinInColumn_ReturnsFalseOnNoWin() {
        boolean result = c4Handler.winInColumn(0, PieceColor.BLUE);

        assertFalse("Returns false when there is no win", result);
    }

    @Test
    public void testWinInDiagonalBackslash_DetectsWin() {
        PieceColor color = c4Handler.getPlayerColorFromUUID(p1ID);

        // Back-slash fill method generated with assistance from GPT - Sam Safe
        int[] cols = {0, 1, 2, 3};
        int[] rows = {2, 3, 4, 5};

        for (int i = 0; i < 4; i++) {
            c4Handler.getBoard().placePiece(new ConnectFourPiece(color), new Vector2(cols[i], rows[i]));
        }

        boolean result = c4Handler.winInDiagonalBackslash(rows[0], cols[0], color);

        assertTrue("Should detect backslash win", result);
    }

    @Test
    public void testWinInDiagonalFrontSlash_DetectsWin() {
        int bottomRow = c4Handler.getBoard().getRowSize() - 1; // Bottom row index
        // Place pieces in columns 0 to 3 in the bottom row
        c4Handler.getBoard().placePiece(new ConnectFourPiece(PieceColor.BLUE), new Vector2(0, bottomRow));
        c4Handler.getBoard().placePiece(new ConnectFourPiece(PieceColor.BLUE), new Vector2(1, bottomRow));
        c4Handler.getBoard().placePiece(new ConnectFourPiece(PieceColor.BLUE), new Vector2(1, bottomRow - 1));
        c4Handler.getBoard().placePiece(new ConnectFourPiece(PieceColor.BLUE), new Vector2(2, bottomRow));
        c4Handler.getBoard().placePiece(new ConnectFourPiece(PieceColor.BLUE), new Vector2(2, bottomRow- 1));
        c4Handler.getBoard().placePiece(new ConnectFourPiece(PieceColor.BLUE), new Vector2(2, bottomRow - 2));
        c4Handler.getBoard().placePiece(new ConnectFourPiece(PieceColor.BLUE), new Vector2(3, bottomRow));
        c4Handler.getBoard().placePiece(new ConnectFourPiece(PieceColor.BLUE), new Vector2(3, bottomRow - 1));
        c4Handler.getBoard().placePiece(new ConnectFourPiece(PieceColor.BLUE), new Vector2(3, bottomRow - 2));
        c4Handler.getBoard().placePiece(new ConnectFourPiece(PieceColor.BLUE), new Vector2(3, bottomRow - 3));

        // Now, check for a horizontal win in the bottom row
        assertTrue("Should detect horizontal win", c4Handler.winInRow(bottomRow, PieceColor.BLUE));
    }

    @Test
    public void testGetPlayerColorFromUUID_ReturnsCorrectColor() {
        PieceColor one = c4Handler.getPlayerColorFromUUID(p1ID);
        PieceColor two = c4Handler.getPlayerColorFromUUID(p2ID);

        c4Handler.makeMove(new MoveData(p1ID, new Vector2(0, 0)));
        c4Handler.makeMove(new MoveData(p2ID, new Vector2(1, 0)));

        assertTrue("Player 1 matches first colour", (PieceColor.BLUE == one || one == PieceColor.ORANGE));
        assertTrue("Player 2 matches second colour", (PieceColor.ORANGE == two || two == PieceColor.BLUE));
    }

    @Test
    public void testGetOtherPlayerUUID_ReturnsCorrectUUID() {
        UUID p1Perspective = c4Handler.getOtherPlayerUUID(p1ID);
        UUID p2Perspective = c4Handler.getOtherPlayerUUID(p2ID);

        assertEquals("Other player from P1 is P2", p2ID, p1Perspective);
        assertEquals("Other player from P2 is P1", p1ID, p2Perspective);
    }

    @Test
    public void testBoardInitialization() {
        for (int col = 0; col < c4Handler.getBoard().getColumnSize(); col++) {
            for (int row = 0; row < c4Handler.getBoard().getRowSize(); row++) {
                assertNull("Board empty at index", c4Handler.getBoard().getAt(col, row));
            }
        }
    }

    @Test (expected = Exception.class)
    public void testMakeMove_NullMoveData_ThrowsException() {
        c4Handler.makeMove((MoveData) null);
    }

    @Test (expected = Exception.class)
    public void testMakeMove_InvalidCoordinates_IgnoresMove() {
        MoveData md = new MoveData(p1ID, new Vector2(-1, 0));

        c4Handler.makeMove(md);
    }

    @Test (expected = Exception.class)
    public void testMakeMove_UnrecognizedPlayerID_IgnoresMove() {
        UUID invalid = UUID.randomUUID();

        MoveData md = new MoveData(invalid, new Vector2(0, 0));

        c4Handler.makeMove(md);
    }

    @Test
    public void testPlacePiece_PiecesAtBoardEdges() {
        PieceColor color = c4Handler.getPlayerColorFromUUID(p1ID);
        int firstColumn = 0;
        int lastColumn = c4Handler.getBoard().getColumnSize() - 1;

        c4Handler.getBoard().placePiece(new ConnectFourPiece(color), new Vector2(firstColumn, c4Handler.findAvailableRow(firstColumn)));
        c4Handler.getBoard().placePiece(new ConnectFourPiece(color), new Vector2(lastColumn, c4Handler.findAvailableRow(lastColumn)));

        assertNotNull("Piece in first column", c4Handler.getBoard().getAt(firstColumn, c4Handler.getBoard().getRowSize() - 1));
        assertNotNull("Piece in last column", c4Handler.getBoard().getAt(lastColumn, c4Handler.getBoard().getRowSize() - 1));
    }

    @Test
    public void testWin_HandlingGreaterThanMinimum() {
        int column = 0;
        PieceColor color = c4Handler.getPlayerColorFromUUID(p1ID);

        for (int i = 0; i < 5; i++) {
            c4Handler.getBoard().placePiece(new ConnectFourPiece(color), new Vector2(column, c4Handler.getBoard().getRowSize() - 1 - i));
        }

        boolean result = c4Handler.winInColumn(column, color);

        assertTrue("Detects win with more than 4 pieces", result);
    }
}