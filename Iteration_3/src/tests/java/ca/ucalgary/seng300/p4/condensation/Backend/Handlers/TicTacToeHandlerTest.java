package ca.ucalgary.seng300.p4.condensation.Backend.Handlers;

import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.Vector2;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.Backend.MoveData.MoveData;
import ca.ucalgary.seng300.p4.condensation.Backend.Pieces.TicTacToePiece;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.*;
public class TicTacToeHandlerTest {

    private UUID p1ID;
    private UUID p2ID;
    private TicTacToeHandler ticTacToeHandler;

    @Before
    public void setUp() throws Exception {
        p1ID = UUID.randomUUID();
        p2ID = UUID.randomUUID();
        ticTacToeHandler = new TicTacToeHandler(p1ID, p2ID);
    }

    @After
    public void tearDown() throws Exception {
        p1ID = UUID.randomUUID();
        p2ID = UUID.randomUUID();
        ticTacToeHandler = new TicTacToeHandler(p1ID, p2ID);
    }

    @Test
    public void testBoardInitialization() {
        for (int col = 0; col < ticTacToeHandler.getBoard().getColumnSize(); col++) {
            for (int row = 0; row < ticTacToeHandler.getBoard().getRowSize(); row++) {
                assertNull("Board empty at index", ticTacToeHandler.getBoard().getAt(col, row));
            }
        }
    }

    @Test (expected = Exception.class)
    public void testMakeMove_NullMoveData_ThrowsException() {
        try {
            ticTacToeHandler.makeMove((MoveData) null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testMakeMove_InvalidCoordinates_IgnoresMove() {
        MoveData md = new MoveData(p1ID, new Vector2(-1, 0));

        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test (expected = Exception.class)
    public void testMakeMove_UnrecognizedPlayerID_IgnoresMove() {
        UUID invalid = UUID.randomUUID();

        MoveData md = new MoveData(invalid, new Vector2(0, 0));

        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }



    @Test
    public void testMakeMove_ValidP1Move_UpdatesBoard() {
        MoveData md = new MoveData(p1ID, new Vector2(0, 0));

        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        TicTacToePiece piece = ticTacToeHandler.getBoard().getAt(0,0);
        assertNotNull("Piece should be played at the move coordinates", piece);
        assertEquals("Piece colour is player colour", ticTacToeHandler.getPlayerColorFromUUID(p1ID), piece.getColor());
    }


    @Test
    public void testMakeMove_ValidP2Move_UpdatesBoard() {
        MoveData md = new MoveData(p2ID, new Vector2(0, 1));

        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        TicTacToePiece piece = ticTacToeHandler.getBoard().getAt(0,1);
        assertNotNull("Piece should be played at the move coordinates", piece);
        assertEquals("Piece colour is player colour", ticTacToeHandler.getPlayerColorFromUUID(p2ID), piece.getColor());
    }


    @Test
    public void testMakeMove_InvalidP1Move_DoesNotUpdateBoard() {
        MoveData md = new MoveData(p1ID, new Vector2(0, 0));
        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        md = new MoveData(p2ID, new Vector2(0, 1));
        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        md = new MoveData(p1ID, new Vector2(0, 1));
        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        TicTacToePiece piece = ticTacToeHandler.getBoard().getAt(0,1);

        assertEquals("Player 1 cannot move in location that P2 occupies", ticTacToeHandler.getPlayerColorFromUUID(p2ID), piece.getColor());
    }

    @Test
    public void testMakeMove_InvalidP2Move_DoesNotUpdateBoard() {
        MoveData md = new MoveData(p1ID, new Vector2(0, 0));
        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        md = new MoveData(p2ID, new Vector2(0, 1));
        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        md = new MoveData(p1ID, new Vector2(0, 2));
        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        md = new MoveData(p2ID, new Vector2(0, 2));
        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        TicTacToePiece piece = ticTacToeHandler.getBoard().getAt(0,2);

        assertEquals("Player 2 cannot move in location that P1 occupies",ticTacToeHandler.getPlayerColorFromUUID(p1ID), piece.getColor());
    }



    @Test
    public void testVerticalWins_VerticalWinFirstColumn_DoesUpdateBoard() throws IOException, ParseException {
        MoveData md = new MoveData(p1ID, new Vector2(0, 0));
        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        md = new MoveData(p1ID, new Vector2(1, 0));
        ticTacToeHandler.makeMove(md);


        md = new MoveData(p1ID, new Vector2(2, 0));
        ticTacToeHandler.makeMove(md);

        //Player 1 should win in first column
        assertTrue("Player 1 should win as all values in column 0 are the same", ticTacToeHandler.checkVerticalWin(ticTacToeHandler.getPlayerColorFromUUID(p1ID)));
    }


    @Test
    public void testVerticalWins_VerticalWinSecondColumn_DoesUpdateBoard() throws IOException, ParseException {
        MoveData md = new MoveData(p1ID, new Vector2(0, 1));
        ticTacToeHandler.makeMove(md);

        md = new MoveData(p1ID, new Vector2(1, 1));
        ticTacToeHandler.makeMove(md);


        md = new MoveData(p1ID, new Vector2(2, 1));
        ticTacToeHandler.makeMove(md);

        PieceColor color = ticTacToeHandler.getPlayerColorFromUUID(p1ID);

        //Player 1 should win in second column
        assertTrue("Player 1 should win as all values in column 1 are the same", ticTacToeHandler.checkVerticalWin(color));
    }

    @Test
    public void testVerticalWins_VerticalWinThirdColumn_DoesUpdateBoard() throws IOException, ParseException {
        MoveData md = new MoveData(p1ID, new Vector2(0, 2));
        ticTacToeHandler.makeMove(md);

        md = new MoveData(p1ID, new Vector2(1, 2));
        ticTacToeHandler.makeMove(md);


        md = new MoveData(p1ID, new Vector2(2, 2));
        ticTacToeHandler.makeMove(md);

//        System.out.printf("%s %s %s\n", ticTacToeHandler.getBoard().getAt(0,2).getColor(), ticTacToeHandler.getBoard().getAt(1,2).getColor(), ticTacToeHandler.getBoard().getAt(2,2).getColor());

        PieceColor color = ticTacToeHandler.getPlayerColorFromUUID(p1ID);

        //Player 1 should win in third column
        assertTrue("Player 1 should win as all values in column 2 are the same", ticTacToeHandler.checkVerticalWin(color));
    }


    @Test
    public void testVerticalWins_NoVerticalWin_DoesUpdateBoard() throws IOException, ParseException {
        MoveData md = new MoveData(p1ID, new Vector2(0, 0));
        ticTacToeHandler.makeMove(md);

        md = new MoveData(p2ID, new Vector2(1, 0));
        ticTacToeHandler.makeMove(md);


        md = new MoveData(p1ID, new Vector2(2, 0));
        ticTacToeHandler.makeMove(md);

        PieceColor color = ticTacToeHandler.getPlayerColorFromUUID(p1ID);

        assertFalse("Player 1 should not win as all values in column are not the same", ticTacToeHandler.checkHorizontalWin(color));
    }


    @Test
    public void testHorizontalWins_HorizontalWinFirstRow_DoesUpdateBoard() throws IOException, ParseException {
        MoveData md = new MoveData(p1ID, new Vector2(0, 0));
        ticTacToeHandler.makeMove(md);

        md = new MoveData(p1ID, new Vector2(0,1));
        ticTacToeHandler.makeMove(md);


        md = new MoveData(p1ID, new Vector2(0, 2));
        ticTacToeHandler.makeMove(md);

        //Player 1 should win in first row
        assertTrue("Player 1 should win as all values in row 0 are the same", ticTacToeHandler.checkHorizontalWin(ticTacToeHandler.getPlayerColorFromUUID(p1ID)));
    }


    @Test
    public void testHorizontalWins_HorizontalWinSecondRow_DoesUpdateBoard() throws IOException, ParseException {
        MoveData md = new MoveData(p1ID, new Vector2(1, 0));
        ticTacToeHandler.makeMove(md);

        md = new MoveData(p1ID, new Vector2(1, 1));
        ticTacToeHandler.makeMove(md);


        md = new MoveData(p1ID, new Vector2(1, 2));
        ticTacToeHandler.makeMove(md);

        PieceColor color = ticTacToeHandler.getPlayerColorFromUUID(p1ID);

        //Player 1 should win in second row
        assertTrue("Player 1 should win as all values in row 1 are the same", ticTacToeHandler.checkHorizontalWin(color));
    }

    @Test
    public void testHorizontalWins_HorizontalWinThirdRow_DoesUpdateBoard() throws IOException, ParseException {
        MoveData md = new MoveData(p1ID, new Vector2(2, 0));
        ticTacToeHandler.makeMove(md);

        md = new MoveData(p1ID, new Vector2(2, 1));
        ticTacToeHandler.makeMove(md);


        md = new MoveData(p1ID, new Vector2(2, 2));
        ticTacToeHandler.makeMove(md);

        PieceColor color = ticTacToeHandler.getPlayerColorFromUUID(p1ID);

        //Player 1 should win in third row
        assertTrue("Player 1 should win as all values in row 2 are the same", ticTacToeHandler.checkHorizontalWin(color));
    }

    @Test
    public void testHorizontalWins_NoHorizontalWin_DoesUpdateBoard() throws IOException, ParseException {
        MoveData md = new MoveData(p1ID, new Vector2(2, 0));
        ticTacToeHandler.makeMove(md);

        md = new MoveData(p2ID, new Vector2(2, 1));
        ticTacToeHandler.makeMove(md);


        md = new MoveData(p1ID, new Vector2(2, 2));
        ticTacToeHandler.makeMove(md);

        PieceColor color = ticTacToeHandler.getPlayerColorFromUUID(p1ID);

        assertFalse("Player 1 should not win as all values in row 2 are not the same", ticTacToeHandler.checkHorizontalWin(color));
    }

    @Test
    public void testDiagonalWins_DiagonalOneWin_DoesUpdateBoard() throws IOException, ParseException {
        MoveData md = new MoveData(p1ID, new Vector2(0, 0));
        ticTacToeHandler.makeMove(md);

        md = new MoveData(p1ID, new Vector2(1, 1));
        ticTacToeHandler.makeMove(md);


        md = new MoveData(p1ID, new Vector2(2, 2));
        ticTacToeHandler.makeMove(md);

        PieceColor color = ticTacToeHandler.getPlayerColorFromUUID(p1ID);

        assertTrue("Player 1 should win as all values in diagonal 1 are the same", ticTacToeHandler.checkDiagonalWin(color));
    }

    @Test
    public void testDiagonalWins_DiagonalTwoWin_DoesUpdateBoard() throws IOException, ParseException {
        MoveData md = new MoveData(p1ID, new Vector2(0, 2));
        ticTacToeHandler.makeMove(md);

        md = new MoveData(p1ID, new Vector2(1, 1));
        ticTacToeHandler.makeMove(md);


        md = new MoveData(p1ID, new Vector2(2, 0));
        ticTacToeHandler.makeMove(md);

        PieceColor color = ticTacToeHandler.getPlayerColorFromUUID(p1ID);

        assertTrue("Player 1 should win as all values in diagonal 2 are the same", ticTacToeHandler.checkDiagonalWin(color));
    }


    @Test
    public void testDiagonalWins_NoWinDiagonalOne_1_DoesUpdateBoard() throws IOException, ParseException {
        MoveData md = new MoveData(p1ID, new Vector2(0, 0));
        ticTacToeHandler.makeMove(md);

        md = new MoveData(p2ID, new Vector2(1, 1));
        ticTacToeHandler.makeMove(md);


        md = new MoveData(p1ID, new Vector2(2, 2));
        ticTacToeHandler.makeMove(md);

        PieceColor color = ticTacToeHandler.getPlayerColorFromUUID(p1ID);

        assertFalse("Player 1 should not win as not all values in diagonal 1 are the same", ticTacToeHandler.checkDiagonalWin(color));
    }

    @Test
    public void testDiagonalWins_NoWinDiagonalOne_2_DoesUpdateBoard() throws IOException, ParseException {
        MoveData md = new MoveData(p2ID, new Vector2(0, 0));
        ticTacToeHandler.makeMove(md);

        md = new MoveData(p1ID, new Vector2(1, 1));
        ticTacToeHandler.makeMove(md);


        md = new MoveData(p1ID, new Vector2(2, 2));
        ticTacToeHandler.makeMove(md);

        PieceColor color = ticTacToeHandler.getPlayerColorFromUUID(p1ID);

        assertFalse("Player 1 should not win as not all values in diagonal 1 are the same", ticTacToeHandler.checkDiagonalWin(color));
    }

    @Test
    public void testDiagonalWins_NoWinDiagonalOne_3_DoesUpdateBoard() throws IOException, ParseException {
        MoveData md = new MoveData(p1ID, new Vector2(0, 0));
        ticTacToeHandler.makeMove(md);

        md = new MoveData(p1ID, new Vector2(1, 1));
        ticTacToeHandler.makeMove(md);


        md = new MoveData(p2ID, new Vector2(2, 2));
        ticTacToeHandler.makeMove(md);

        PieceColor color = ticTacToeHandler.getPlayerColorFromUUID(p1ID);

        assertFalse("Player 1 should not win as not all values in diagonal 1 are the same", ticTacToeHandler.checkDiagonalWin(color));
    }

    @Test
    public void testDiagonalWins_NoWinDiagonalTwo_1_DoesUpdateBoard() {
        MoveData md = new MoveData(p1ID, new Vector2(0, 2));
        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        md = new MoveData(p2ID, new Vector2(1, 1));
        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        md = new MoveData(p1ID, new Vector2(2, 0));
        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        PieceColor color = ticTacToeHandler.getPlayerColorFromUUID(p1ID);

        assertFalse("Player 1 should not win as not all values in diagonal 2 are the same", ticTacToeHandler.checkDiagonalWin(color));
    }

    @Test
    public void testDiagonalWins_NoWinDiagonalTwo_2_DoesUpdateBoard() {
        MoveData md = new MoveData(p2ID, new Vector2(0, 2));
        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        md = new MoveData(p1ID, new Vector2(1, 1));
        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        md = new MoveData(p1ID, new Vector2(2, 0));
        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        PieceColor color = ticTacToeHandler.getPlayerColorFromUUID(p1ID);

        assertFalse("Player 1 should not win as not all values in diagonal 2 are the same", ticTacToeHandler.checkDiagonalWin(color));
    }

    @Test
    public void testDiagonalWins_NoWinDiagonalTwo_3_DoesUpdateBoard() {
        MoveData md = new MoveData(p1ID, new Vector2(0, 2));
        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        md = new MoveData(p1ID, new Vector2(1, 1));
        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        md = new MoveData(p2ID, new Vector2(2, 0));
        try {
            ticTacToeHandler.makeMove(md);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        PieceColor color = ticTacToeHandler.getPlayerColorFromUUID(p1ID);

        assertFalse("Player 1 should not win as not all values in diagonal 2 are the same", ticTacToeHandler.checkDiagonalWin(color));
    }

}