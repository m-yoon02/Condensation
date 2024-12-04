package ca.ucalgary.seng300.p4.condensation.Backend.Pieces;

import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.PieceColor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConnectFourPieceTest {

    @Test
    public void testConstructorByVectorAndColor() {
        Vector2 position = new Vector2(3, 2);
        PieceColor color = PieceColor.BLUE;
        ConnectFourPiece piece = new ConnectFourPiece(position, color);

        assertEquals(position, piece.getPosition());
        assertEquals(color, piece.getColor());
    }
}