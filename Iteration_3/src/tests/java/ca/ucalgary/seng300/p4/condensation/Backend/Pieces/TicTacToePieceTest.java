package ca.ucalgary.seng300.p4.condensation.Backend.Pieces;

import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.PieceColor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TicTacToePieceTest {

    @Test
    public void testConstructorByColor() {
        PieceColor color = PieceColor.RED;
        TicTacToePiece piece = new TicTacToePiece(color);

        assertEquals(Vector2.ZERO, piece.getPosition());
        assertEquals(color, piece.getColor());
    }
}