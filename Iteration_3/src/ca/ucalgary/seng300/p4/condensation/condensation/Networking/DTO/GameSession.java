package ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO;

import java.net.http.WebSocket;
import java.util.UUID;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.GameResult;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers.*;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Managers.GameSessionManager;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Managers.NetworkManager;


public class GameSession implements GameEndListener, TurnEndListener {

    /**
     * We Created Turn END and Game END Listeners that subscribe to listener lists in GameHandler respectively
     * Passes Player 1 UUID and Player 2 into GameHandler
     * Passes GameSession objects GameEnd and TurnEnd listeners into GameHandler.
     */
    private final UUID sessionId;
    private final UUID player1ID;
    private final UUID player2ID;
    // field for instantited GameHandler
    private final GameHandler gameHandler;
    private UUID currentPlayerTurn;

    /**
     * When creating GameSession we initialize sessionID field, and player1 and player2 UUIDs which will be passed into some instance of GameHandler
     * @param playerMemo1
     * @param playerMemo2
     */

    public GameSession(QueuedPlayerMemo playerMemo1, QueuedPlayerMemo playerMemo2) {
        GameHandler gameHandler1 = null;
        this.sessionId = UUID.randomUUID();
        this.player1ID =  playerMemo1.PlayerID();
        this.player2ID =  playerMemo2.PlayerID();
        // We might have to define something in GameHandler that takes a string gameType, and
        //
        GameTypes gameType = playerMemo1.gameType();
            switch (gameType) {
                case CHECKERS :
                    gameHandler1 = new CheckersHandler(this.player1ID, this.player2ID);
                case TICTACTOE:
                    gameHandler1 = new TicTacToeHandler(this.player1ID, this.player2ID);
                case CONNECT4:
                    gameHandler1 = new TicTacToeHandler(this.player1ID, this.player2ID);
          }


        this.gameHandler = gameHandler1;
        this.gameHandler.addTurnEndListener(this);
        this.gameHandler.addGameEndListener(this);
        this.currentPlayerTurn = null;
    }

    public UUID getPlayer1ID() {
        return player1ID;
    }

    public UUID getPlayer2ID() {
        return player2ID;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    @Override
    public void onGameEnd(GameResult gameResult, UUID winnerPlayer) {
        System.out.println("Game has ended");
        if (winnerPlayer.equals(this.player1ID)) {
            // update leaderboard for player 1
        } else {
            // update leaderboard for player 2
        }
        GameSessionManager.getInstance().removeGameSession(this.sessionId);

    }

    @Override
    public void onTurnEnd(UUID playersTurnID, String gameStateJson) {
        System.out.println("Its now " + playersTurnID.toString() + "'s Turn");
        this.currentPlayerTurn = playersTurnID;

        String readyPayload = this.sessionId.toString() + gameStateJson;
        NetworkManager.getNetworkInstance().sendGameDataToClients(readyPayload);    // sessionID + gameState

    }

    // do we instantiate gameHandler or just set it since who is supposed to handle exactly what kind of game a customer picks ??
    // Create a new GameHandler with the provided player IDs
    // -- this.gameHandler = new GameHandler(this.player1, this.player2, new PieceColor[]{PieceColor.RED, PieceColor.BLUE});


    //************
    // we will send MoveData to the backEnd


    /**
     * Listener pattern implementation
     * Instantiate listeners for TurnEnd and GameEnd Listeners
     * pass this information to backend so that  they can subscribe them
     */



    //  void onGameEnd(GameResult gameResult, UUID winnerPlayer);
    // -- logic that goes to the sessionManager and removes game session
    // -- logic that sets player that lost, we know who won
    // -- leaderboard

    // if GameEnd






}