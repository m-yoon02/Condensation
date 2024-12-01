package ca.ucalgary.seng300.p4.condensation.Backend.Pieces;

import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.PieceColor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TicTacToePieceTest {
    TicTacToePiece ticTacToePiece1;
    TicTacToePiece ticTacToePiece2;
    @Before
    public void setUp() throws Exception {
        ticTacToePiece1 = new TicTacToePiece(PieceColor.BLUE);
        ticTacToePiece2 = new TicTacToePiece(new Vector2(1,1),PieceColor.RED);
    }

    @Test
    public void testTicTacToePiece1() {
        assertEquals("x value should be 0", 0,ticTacToePiece1.getX());
        assertEquals("y value should be 0", 0,ticTacToePiece1.getY());
        assertEquals("Colour should be blue", PieceColor.BLUE,ticTacToePiece1.getColor());

        Vector2 vector2 = new Vector2(0,2);

        ticTacToePiece1.moveTo(vector2);
        assertEquals("x value should be 0", 0,ticTacToePiece1.getX());
        assertEquals("y value should be 2", 2,ticTacToePiece1.getY());
    }

    @Test
    public void testTicTacToePiece2() {
        assertEquals("x value should be 1", 1,ticTacToePiece2.getX());
        assertEquals("y value should be 1", 1,ticTacToePiece2.getY());
        assertEquals("Colour should be blue", PieceColor.RED,ticTacToePiece2.getColor());

    }

    @Test
    public void testConstructorByColor() {
        PieceColor color = PieceColor.RED;
        TicTacToePiece piece = new TicTacToePiece(color);

        assertEquals(Vector2.ZERO, piece.getPosition());
        assertEquals(color, piece.getColor());
    }
}