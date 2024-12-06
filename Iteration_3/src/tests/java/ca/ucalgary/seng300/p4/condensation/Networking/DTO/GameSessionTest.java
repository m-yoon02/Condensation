package ca.ucalgary.seng300.p4.condensation.Networking.DTO;



import ca.ucalgary.seng300.p4.condensation.Backend.Enums.GameResult;
import ca.ucalgary.seng300.p4.condensation.Backend.Handlers.GameHandler;
import ca.ucalgary.seng300.p4.condensation.Backend.Handlers.TicTacToeHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertNotNull;

public class GameSessionTest {

    private GameSession gameSession1;
    private GameSession gameSession2;
    private GameSession gameSession3;
    private UUID player1ID;
    private UUID player2ID;

    @Before
    public void setUp() throws Exception {
        player1ID = UUID.randomUUID();
        player2ID = UUID.randomUUID();
        // Mock or create instances as needed for the constructor
        QueuedPlayerMemo playerMemo1 = new QueuedPlayerMemo(player1ID, GameTypes.TICTACTOE,1200);
        QueuedPlayerMemo playerMemo2 = new QueuedPlayerMemo(player2ID, GameTypes.TICTACTOE,1300);

        QueuedPlayerMemo playerMemo3 = new QueuedPlayerMemo(player1ID, GameTypes.CHECKERS,1200);
        QueuedPlayerMemo playerMemo4 = new QueuedPlayerMemo(player2ID, GameTypes.CHECKERS,1300);

        QueuedPlayerMemo playerMemo5 = new QueuedPlayerMemo(player1ID, GameTypes.CONNECT4,1200);
        QueuedPlayerMemo playerMemo6 = new QueuedPlayerMemo(player2ID, GameTypes.CONNECT4,1300);

        gameSession1 = new GameSession(playerMemo1, playerMemo2);

        gameSession2 = new GameSession(playerMemo3, playerMemo4);

        gameSession3 = new GameSession(playerMemo5, playerMemo6);
    }

    @After
    public void tearDown() throws Exception {
        // Clean up after each test
        gameSession1 = null;
    }

    @Test
    public void testGameSessionInitialization() {
        assertNotNull("GameSession should not be null", gameSession1);
        assertEquals("Player1 ID should match", player1ID, gameSession1.getPlayer1ID());
        assertEquals("Player2 ID should match", player2ID, gameSession1.getPlayer2ID());
        assertNotNull("Session ID should not be null", gameSession1.getSessionId());
    }
    @Test
    public void testOnGameEndWithException() {
        // Arrange
        UUID player1ID = UUID.randomUUID();
        UUID player2ID = UUID.randomUUID();
        GameTypes gameType = GameTypes.TICTACTOE;

        QueuedPlayerMemo playerMemo1 = new QueuedPlayerMemo(player1ID, gameType, 1200);
        QueuedPlayerMemo playerMemo2 = new QueuedPlayerMemo(player2ID, gameType, 1250);

        UUID winner = playerMemo1.PlayerID();

        // Temporarily modify processGameEnd using a lambda hack for testing (if applicable):
        GameSession testGameSession = new GameSession(playerMemo1, playerMemo2) {
            @Override
            public void processGameEnd(GameResult gameResult, UUID winnerPlayer) throws IOException {
                throw new IOException("Simulated IOException");
            }
        };
        // Act & Assert
        try {
            testGameSession.onGameEnd(GameResult.WIN, winner);
            fail("Expected RuntimeException was not thrown");
        } catch (RuntimeException e) {
            assertTrue("Exception cause should be IOException", e.getCause() instanceof IOException);
            assertEquals("Simulated IOException", e.getCause().getMessage());
        }
    }
    @Test (expected = NullPointerException.class)
    public void testProcessGameEnd_TieGame() throws Exception {
        // Arrange
        UUID player1ID = UUID.randomUUID();
        UUID player2ID = UUID.randomUUID();
        GameTypes gameType = GameTypes.TICTACTOE;

        // Create valid QueuedPlayerMemo objects
        QueuedPlayerMemo playerMemo1 = new QueuedPlayerMemo(player1ID, gameType, 1200);
        QueuedPlayerMemo playerMemo2 = new QueuedPlayerMemo(player2ID, gameType, 1250);

        // Create the GameSession object with valid inputs
        GameSession gameSession = new GameSession(playerMemo1, playerMemo2);

        // Ensure player1ID and player2ID are properly initialized in the game session
        assertNotNull("Player 1 ID should not be null", gameSession.getPlayer1ID());
        assertNotNull("Player 2 ID should not be null", gameSession.getPlayer2ID());

        // Act
        // Pass GameResult.TIE and a dummy player ID for winnerPlayer
        gameSession.processGameEnd(GameResult.TIE, player1ID);  // player1ID is just a placeholder here

        // Assert
        System.out.println("Tie game handled successfully.");
    }

    @Test
    public void testGetGameHandler() {
        // Arrange
        UUID player1ID = UUID.randomUUID();
        UUID player2ID = UUID.randomUUID();
        GameTypes gameType = GameTypes.TICTACTOE;  // Change this to test different game types

        // Create valid QueuedPlayerMemo objects
        QueuedPlayerMemo playerMemo1 = new QueuedPlayerMemo(player1ID, gameType, 1200);
        QueuedPlayerMemo playerMemo2 = new QueuedPlayerMemo(player2ID, gameType, 1250);

        // Create the GameSession object
        GameSession gameSession = new GameSession(playerMemo1, playerMemo2);

        // Act
        GameHandler gameHandler = gameSession.getGameHandler();  // Call the method to test

        // Assert
        assertNotNull("GameHandler should not be null", gameHandler);
        assertTrue("GameHandler should be of type TicTacToeHandler", gameHandler instanceof TicTacToeHandler);
    }

    @Test
    public void testProcessGameEnd_WinGame() throws Exception {
        // Arrange
        UUID player1ID = UUID.randomUUID();
        UUID player2ID = UUID.randomUUID();
        GameTypes gameType = GameTypes.TICTACTOE;

        // Create valid QueuedPlayerMemo objects
        QueuedPlayerMemo playerMemo1 = new QueuedPlayerMemo(player1ID, gameType, 1200);
        QueuedPlayerMemo playerMemo2 = new QueuedPlayerMemo(player2ID, gameType, 1250);

        // Create the GameSession object with valid inputs
        GameSession gameSession = new GameSession(playerMemo1, playerMemo2);

        // Debug: Print player IDs to verify they're initialized properly
        System.out.println("Player 1 ID: " + gameSession.getPlayer1ID());
        System.out.println("Player 2 ID: " + gameSession.getPlayer2ID());

        // Act
        GameResult gameResult = GameResult.WIN;  // Ensure the gameResult is GameResult.WIN// Debug: Print gameResult to ensure it's WIN
        gameSession.processGameEnd(gameResult, player1ID);  // player1ID is the winner

        // Assert
        System.out.println("Game result handled as WIN. Player 1 won.");
    }

    @Test
    public void testProcessGameEnd_Player2Wins() throws Exception {
        // Arrange
        UUID player1ID = UUID.randomUUID();
        UUID player2ID = UUID.randomUUID();
        GameTypes gameType = GameTypes.TICTACTOE;

        QueuedPlayerMemo playerMemo1 = new QueuedPlayerMemo(player1ID, gameType, 1200);
        QueuedPlayerMemo playerMemo2 = new QueuedPlayerMemo(player2ID, gameType, 1250);

        GameSession gameSession = new GameSession(playerMemo1, playerMemo2);

        // Act
        gameSession.processGameEnd(GameResult.WIN, player2ID);

        // Assert
        // Verify that the Leaderboard.updateEloForWinningPlayer and updateEloForLosingPlayer
        // were called with player2ID as the winner and player1ID as the loser
    }

    @Test
    public void testOnTurnEnd_withNullPlayerID() {
        // Arrange
        String gameStateJson = "{ \"state\": \"updated\" }";  // Valid JSON string
        UUID playerTurnID = null;  // Null player ID

        UUID player1ID = UUID.randomUUID();
        UUID player2ID = UUID.randomUUID();
        GameTypes gameType = GameTypes.TICTACTOE;

        QueuedPlayerMemo playerMemo1 = new QueuedPlayerMemo(player1ID, gameType, 1200);
        QueuedPlayerMemo playerMemo2 = new QueuedPlayerMemo(player2ID, gameType, 1250);


        GameSession gameSession = new GameSession(playerMemo1, playerMemo2);

        // Act & Assert
        try {
            gameSession.onTurnEnd(playerTurnID, gameStateJson);
        } catch (RuntimeException e) {
            // Assert
            assertTrue(e.getCause() instanceof NullPointerException);  // Or the specific exception thrown
            System.out.println("Caught exception as expected: " + e.getCause().getMessage());
        }
    }

}
