package ca.ucalgary.seng300.p4.condensation.Backend.Pieces;

import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.PieceColor;
import org.junit.Test;

import static org.junit.Assert.*;

public class CheckersPieceTest {


    @Test
    public void testConstructorByColor() {
        PieceColor color = PieceColor.BLACK;
        CheckersPiece piece = new CheckersPiece(color);

        assertEquals(Vector2.ZERO, piece.getPosition());
        assertEquals(color, piece.getColor());
        assertFalse(piece.isKing());
    }

    @Test
    public void testPromoteToKing() {
        CheckersPiece piece = new CheckersPiece(PieceColor.RED);

        assertFalse(piece.isKing());

        piece.promoteToKing();

        assertTrue(piece.isKing());
    }
}