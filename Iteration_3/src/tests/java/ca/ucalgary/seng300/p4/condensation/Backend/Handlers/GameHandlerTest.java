package ca.ucalgary.seng300.p4.condensation.Backend.Handlers;

import ca.ucalgary.seng300.p4.condensation.Backend.DTOs.PlayerMemo;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.GameResult;
import ca.ucalgary.seng300.p4.condensation.Backend.Enums.PieceColor;
import ca.ucalgary.seng300.p4.condensation.Backend.GameTimer.GameTimer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class GameHandlerTest {

    private static class TestGameHandler extends GameHandler {
        private TestGameTimer testGameTimer;

        public TestGameHandler(UUID player1ID, UUID player2ID, PieceColor[] colors, TestGameTimer gameTimer) {
            super(player1ID, player2ID, colors);
            this.testGameTimer = gameTimer;
        }

        @Override
        public void makeMove(String moveDataJSON) {

        }

        // Override to use our test game timer
        protected GameTimer getGameTimer() {
            return testGameTimer;
        }
    }

    // Custom GameTimer for testing that allows simulating behavior
    private static class TestGameTimer extends GameTimer {
        private boolean startCalled = false;
        private boolean shouldThrowInterrupted = false;

        public TestGameTimer(UUID player1ID, UUID player2ID, int defaultTime) {
            super(player1ID, player2ID, defaultTime);
        }

        public void setShouldThrowInterrupted(boolean value) {
            this.shouldThrowInterrupted = value;
        }

        public boolean wasStartCalled() {
            return startCalled;
        }

        @Override
        public void startTimer() throws InterruptedException {
            startCalled = true;
            if (shouldThrowInterrupted) {
                throw new InterruptedException("Simulated interruption");
            }
        }
    }

    private UUID player1ID;
    private UUID player2ID;
    private TestGameHandler gameHandler;
    private TestGameTimer testGameTimer;
    private PieceColor[] colors;

    @Before
    public void setUp() {
        // Setup test data before each test
        player1ID = UUID.randomUUID();
        player2ID = UUID.randomUUID();
        testGameTimer = new TestGameTimer(player1ID, player2ID, 90);

        colors = new PieceColor[]{PieceColor.WHITE, PieceColor.BLACK};
        gameHandler = new TestGameHandler(player1ID, player2ID, colors, testGameTimer);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testConstructor() {
        // Verify player memos are created correctly
        PlayerMemo player1Memo = gameHandler.getPlayerMemoFromUUID(player1ID);
        PlayerMemo player2Memo = gameHandler.getPlayerMemoFromUUID(player2ID);

        assertNotNull(player1Memo);
        assertNotNull(player2Memo);

        // Ensure players have different colors
        assertThat("Players should have different colours",player1Memo.pieceColor(), not(equals(player2Memo.pieceColor())));

        // Verify first player is set
        assertNotNull("First Player should not be null",gameHandler.getFirstPlayerID());
    }

    @Test
    public void testGetPlayerColorFromUUID() {
        PlayerMemo player1Memo = gameHandler.getPlayerMemoFromUUID(player1ID);
        PieceColor expectedColor = player1Memo.pieceColor();

        assertEquals("Colour expected and returned dont match",expectedColor, gameHandler.getPlayerColorFromUUID(player1ID));
    }

    @Test
    public void testGetOtherPlayerUUID_1() {
        UUID otherPlayerID = gameHandler.getOtherPlayerUUID(player1ID);

        assertEquals("Player 2 should be returned when we pass in player 1",player2ID, otherPlayerID);
    }

    @Test
    public void testGetOtherPlayerUUID_2() {
        UUID otherPlayerID = gameHandler.getOtherPlayerUUID(player2ID);

        assertEquals("Player 1 should be returned when we pass in player 2",player1ID, otherPlayerID);
    }


    @Test
    public void testGetPlayerMemoFromPieceColor() {
        PieceColor player1Color = gameHandler.getPlayerColorFromUUID(player1ID);
        PlayerMemo playerMemo = gameHandler.getPlayerMemoFromPieceColor(player1Color);

        assertEquals("Colour should uniquely identify the players",player1ID, playerMemo.playerID());
    }

    @Test
    public void testAddGameEndListener() {
        // Create a mock listener
        final boolean[] listenerCalled = {false};
        GameEndListener mockListener = (gameResult, winnerID) -> listenerCalled[0] = true;

        gameHandler.addGameEndListener(mockListener);

        // Simulate game end (you might need to expose a method in TestGameHandler)
        gameHandler.notifyGameEnd(GameResult.WIN, player1ID);

        assertTrue(listenerCalled[0]);
    }

    @Test
    public void testAddTurnEndListener() {
        // Create a mock listener
        final boolean[] listenerCalled = {false};
        TurnEndListener mockListener = (currentPlayerTurnID, boardStateJSON) -> listenerCalled[0] = true;

        gameHandler.addTurnEndListener(mockListener);

        // Simulate turn end (you might need to expose a method in TestGameHandler)
        gameHandler.notifyTurnEnd(player1ID, "{}");

        assertTrue(listenerCalled[0]);
    }

//    @Test
//    public void testStartGame_SuccessfulStart() {
//        try {
//            // Act
//            gameHandler.startGame();
//
//            // Assert
//            assertTrue("startTimer should have been called", testGameTimer.wasStartCalled());
//        } catch (Exception e) {
//            fail("No exception should be thrown during successful game start");
//        }
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void testStartGame_InterruptedExceptionHandling() {
//        // Arrange: Configure timer to throw InterruptedException
//        testGameTimer.setShouldThrowInterrupted(true);
//
//        // Act & Assert: Should throw RuntimeException
//        gameHandler.startGame();
//    }
//
//    @Test
//    public void testStartGame_RuntimeExceptionContainsOriginalCause() {
//        // Arrange: Configure timer to throw InterruptedException
//        testGameTimer.setShouldThrowInterrupted(true);
//
//        try {
//            // Act
//            gameHandler.startGame();
//            fail("Expected RuntimeException was not thrown");
//        } catch (RuntimeException e) {
//            // Assert
//            assertTrue("Cause should be InterruptedException",
//                    e.getCause() instanceof InterruptedException);
//            assertEquals("Simulated interruption", e.getCause().getMessage());
//        }
//    }


//
//    @Test
//    public void addGameEndListener() {
//    }
//
//    @Test
//    public void addTurnEndListener() {
//    }
//
//    @Test
//    public void notifyTurnEnd() {
//    }
//
//    @Test
//    public void notifyGameEnd() {
//    }

    @Test
    public void testGetPlayerMemoFromUUID() {
        assertNotNull("A player should exist with UUID defined",gameHandler.getPlayerMemoFromUUID(player1ID));
    }

    @Test
    public void testColorNotNull_PlayerColorFromUUID() {
        assertNotNull("Color has to be returned", gameHandler.getPlayerColorFromUUID(player1ID));
    }
}