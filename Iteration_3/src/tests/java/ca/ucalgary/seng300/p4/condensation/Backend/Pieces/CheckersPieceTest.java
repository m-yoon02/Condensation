package ca.ucalgary.seng300.p4.condensation.Backend.Pieces;

import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.PieceColor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CheckersPieceTest {

    CheckersPiece checkersPiece1;
    CheckersPiece checkersPiece2;

    @Before
    public void setUp() throws Exception {
        checkersPiece1 = new CheckersPiece(PieceColor.BLACK);
        checkersPiece2 = new CheckersPiece(new Vector2(1,2),PieceColor.WHITE);
    }

    @Test
    public void testTicTacToePiece1() {
        assertEquals("x value should be 0", 0,checkersPiece1.getX());
        assertEquals("y value should be 0", 0,checkersPiece1.getY());
        assertEquals("Colour should be black", PieceColor.BLACK,checkersPiece1.getColor());

        Vector2 vector2 = new Vector2(0,2);

        checkersPiece1.moveTo(vector2);
        assertEquals("x value should be 0", 0,checkersPiece1.getX());
        assertEquals("y value should be 2", 2,checkersPiece1.getY());
    }

    @Test
    public void testTicTacToePiece2() {
        assertEquals("x value should be 1", 1,checkersPiece2.getX());
        assertEquals("y value should be 2", 2,checkersPiece2.getY());
        assertEquals("Colour should be blue", PieceColor.WHITE,checkersPiece2.getColor());

    }

    @Test
    public void testIsKing() {
        assertFalse("Piece 1 should not be king", checkersPiece1.isKing());

        assertFalse("Piece 2 should not be king", checkersPiece2.isKing());
    }

    @Test
    public void testPromoteToKing() {

        checkersPiece1.promoteToKing();

        assertTrue("Piece 1 should be king", checkersPiece1.isKing());
        assertFalse("Piece 2 should not be king", checkersPiece2.isKing());
    }
}