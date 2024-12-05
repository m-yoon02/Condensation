package ca.ucalgary.seng300.p4.condensation.Backend.Board;

import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.Backend.Pieces.GamePiece;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameBoardTest {

    private GameBoard<TestGamePiece> gb;

    private static class TestGamePiece extends GamePiece {
        public TestGamePiece(PieceColor color) {
            super(color);
        }
    }

    @Before
    public void setUp() throws Exception {
        int rowSize = 6;
        int columnSize = 7;
        gb = new GameBoard<>(rowSize, columnSize);
    }

    @Test
    public void testBoardInitialization() {
        for (int row = 0; row < gb.getRowSize(); row++) {
            for (int col = 0; col < gb.getColumnSize(); col++) {
                assertNull("Board should be empty at initialization", gb.getAt(col, row));
            }
        }
    }

    @Test
    public void testPositionWithinBounds_ValidPositions() {
        Vector2 position = new Vector2(0, 0);

        assertTrue("Position (0,0) should be within bounds", gb.positionWithinBounds(position));

        position = new Vector2(gb.getColumnSize() - 1, gb.getRowSize() - 1);
        assertTrue("Position at max indices should be within bounds", gb.positionWithinBounds(position));
    }

    @Test
    public void testPositionWithinBounds_InvalidPositions() {
        Vector2 position = new Vector2(-1, 0);

        assertFalse("Negative column index should be out of bounds", gb.positionWithinBounds(position));

        position = new Vector2(0, -1);
        assertFalse("Negative row index should be out of bounds", gb.positionWithinBounds(position));

        position = new Vector2(gb.getColumnSize(), 0);
        assertFalse("Column index equal to columnSize should be out of bounds", gb.positionWithinBounds(position));

        position = new Vector2(0, gb.getRowSize());
        assertFalse("Row index equal to rowSize should be out of bounds", gb.positionWithinBounds(position));
    }

    @Test
    public void testPlacePiece_EmptyPosition_Success() {
        Vector2 position = new Vector2(3, 2);
        TestGamePiece piece = new TestGamePiece(PieceColor.BLUE);

        boolean result = gb.placePiece(piece, position);

        assertTrue("Piece should be placed successfully", result);
        assertEquals("Piece at position should be the placed piece", piece, gb.getAt(position));
        assertEquals("Piece position should be updated", position, piece.getPosition());
    }

    @Test
    public void testPlacePiece_OccupiedPosition_Failure() {
        Vector2 position = new Vector2(3, 2);
        TestGamePiece piece1 = new TestGamePiece(PieceColor.BLUE);
        TestGamePiece piece2 = new TestGamePiece(PieceColor.ORANGE);
        gb.placePiece(piece1, position);

        boolean result = gb.placePiece(piece2, position);

        assertFalse("Piece should not be placed on occupied position", result);
        assertEquals("Original piece should remain at position", piece1, gb.getAt(position));
    }

    @Test
    public void testPlacePiece_PositionOutOfBounds_Failure() {
        Vector2 position = new Vector2(-1, 0);
        TestGamePiece piece = new TestGamePiece(PieceColor.BLUE);

        boolean result = gb.placePiece(piece, position);

        assertFalse("Piece should not be placed out of bounds", result);
    }

    @Test
    public void testMovePiece_ValidMove_Success() {

        Vector2 oldPosition = new Vector2(1, 1);
        Vector2 newPosition = new Vector2(2, 2);
        TestGamePiece piece = new TestGamePiece(PieceColor.BLUE);
        gb.placePiece(piece, oldPosition);

        boolean result = gb.movePiece(oldPosition, newPosition);

        assertTrue("Piece should be moved successfully", result);
        assertNull("Old position should now be empty", gb.getAt(oldPosition));
        assertEquals("Piece should be at new position", piece, gb.getAt(newPosition));
        assertEquals("Piece position should be updated", newPosition, piece.getPosition());
    }

    @Test
    public void testMovePiece_NoPieceAtOldPosition_Failure() {

        Vector2 oldPosition = new Vector2(1, 1);
        Vector2 newPosition = new Vector2(2, 2);

        boolean result = gb.movePiece(oldPosition, newPosition);

        assertFalse("Move should fail when there is no piece at old position", result);
    }

    @Test
    public void testMovePiece_ToOccupiedPosition_Failure() {
        Vector2 oldPosition = new Vector2(1, 1);
        Vector2 newPosition = new Vector2(2, 2);
        TestGamePiece piece1 = new TestGamePiece(PieceColor.BLUE);
        TestGamePiece piece2 = new TestGamePiece(PieceColor.ORANGE);
        gb.placePiece(piece1, oldPosition);
        gb.placePiece(piece2, newPosition);

        boolean result = gb.movePiece(oldPosition, newPosition);

        assertFalse("Move should fail when new position is occupied", result);
        assertEquals("Piece should remain at old position", piece1, gb.getAt(oldPosition));
        assertEquals("Other piece should remain at new position", piece2, gb.getAt(newPosition));
    }

    @Test
    public void testRemovePiece_PieceExists_Success() {
        Vector2 position = new Vector2(3, 3);
        TestGamePiece piece = new TestGamePiece(PieceColor.BLUE);
        gb.placePiece(piece, position);

        gb.removePiece(position);

        assertNull("Position should now be empty", gb.getAt(position));
    }

    @Test
    public void testRemovePiece_NoPieceAtPosition() {
        Vector2 position = new Vector2(3, 3);

        gb.removePiece(position);

        assertNull("Position should remain empty", gb.getAt(position));
    }

    @Test
    public void testIsMovePossible_EmptyPositionWithinBounds() {
        Vector2 position = new Vector2(2, 2);

        boolean result = gb.isMovePossible(position);

        assertTrue("Move should be possible on empty position within bounds", result);
    }

    @Test
    public void testIsMovePossible_OccupiedPosition() {
        Vector2 position = new Vector2(2, 2);
        TestGamePiece piece = new TestGamePiece(PieceColor.BLUE);
        gb.placePiece(piece, position);

        boolean result = gb.isMovePossible(position);

        assertFalse("Move should not be possible on occupied position", result);
    }

    @Test
    public void testIsMovePossible_PositionOutOfBounds() {
        Vector2 position = new Vector2(-1, 0);

        boolean result = gb.isMovePossible(position);

        assertFalse("Move should not be possible on position out of bounds", result);
    }

    @Test
    public void testIsBoardFull_EmptyBoard() {
        boolean result = gb.isBoardFull();

        assertFalse("Empty board should not be full", result);
    }

    @Test
    public void testIsBoardFull_PartiallyFilledBoard() {
        TestGamePiece piece = new TestGamePiece(PieceColor.BLUE);
        gb.placePiece(piece, new Vector2(0, 0));

        boolean result = gb.isBoardFull();

        assertFalse("Partially filled board should not be full", result);
    }

    @Test
    public void testIsBoardFull_CompletelyFilledBoard() {
        for (int row = 0; row < gb.getRowSize(); row++) {
            for (int col = 0; col < gb.getColumnSize(); col++) {
                gb.placePiece(new TestGamePiece(PieceColor.BLUE), new Vector2(col, row));
            }
        }

        boolean result = gb.isBoardFull();

        assertTrue("Completely filled board should be full", result);
    }

    @Test
    public void testIsPieceAt_PieceExists() {
        Vector2 position = new Vector2(4, 4);
        TestGamePiece piece = new TestGamePiece(PieceColor.BLUE);
        gb.placePiece(piece, position);

        boolean result = gb.isPieceAt(position);

        assertTrue("Should detect piece at position", result);
    }

    @Test
    public void testIsPieceAt_NoPieceExists() {
        Vector2 position = new Vector2(4, 4);

        boolean result = gb.isPieceAt(position);

        assertFalse("Should not detect any piece at empty position", result);
    }

    @Test
    public void testGetAt_PieceExists() {
        Vector2 position = new Vector2(2, 2);
        TestGamePiece piece = new TestGamePiece(PieceColor.BLUE);
        gb.placePiece(piece, position);

        TestGamePiece result = gb.getAt(position);

        assertEquals("Should return the piece at the position", piece, result);
    }

    @Test
    public void testGetAt_NoPieceExists() {
        Vector2 position = new Vector2(2, 2);

        TestGamePiece result = gb.getAt(position);

        assertNull("Should return null when no piece exists at position", result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetAt_IndicesOutOfBounds() {
        int x = -1;
        int y = 0;

        gb.getAt(x, y);
    }

    @Test
    public void testToJSONString() {
        gb.placePiece(new TestGamePiece(PieceColor.BLUE), new Vector2(0, 0));
        gb.placePiece(new TestGamePiece(PieceColor.ORANGE), new Vector2(1, 1));

        String jsonString = gb.toJSONString();

        assertNotNull("JSON string should not be null", jsonString);
    }

    @Test
    public void testIsBoardFull_MinimalBoard_Full() {
        GameBoard<TestGamePiece> minimalGb = new GameBoard<>(1, 1);
        TestGamePiece piece = new TestGamePiece(PieceColor.BLUE);
        minimalGb.placePiece(piece, new Vector2(0, 0));

        boolean result = minimalGb.isBoardFull();

        assertTrue("Minimal board should be full when the only position is occupied", result);
    }
}