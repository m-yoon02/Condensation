package ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO;

import java.util.UUID;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.GameResult;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers.GameEndListener;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers.GameHandler;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers.IGameHandler;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers.TurnEndListener;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Enums.GameTypes;


public class GameSession implements GameEndListener, TurnEndListener {

    /**
     * We Created Turn END and Game END Listeners that subscribe to listener lists in GameHandler respectively
     * Passes Player 1 UUID and Player 2 into GameHandler
     * Passes GameSession objects GameEnd and TurnEnd listeners into GameHandler.
     */
    private final UUID sessionId;
    private final UUID player1;
    private final UUID player2;
    // field for instantited GameHandler
    private final GameHandler gameHandler;



    /**
     * When creating GameSession we initialize sessionID field, and player1 and player2 UUIDs which will be passed into some instance of GameHandler
     * @param player1
     * @param player2
     */

    public GameSession(Player player1, Player player2) {
        this.sessionId = UUID.randomUUID();
        this.player1 =  player1.getPlayerID();
        this.player2 =  player2.getPlayerID();
        // We might have to define something in GameHandler that takes a string gameType, and
        //
//        GameTypes gameType = player1.getType();
//        switch (gameType) {
//            case CHECKERS ->
//        }
        this.gameHandler //TODO: retriveve game type

    }

    @Override
    public void onGameEnd(GameResult gameResult, UUID winnerPlayer) {

    }

    @Override
    public void onTurnEnd(UUID playersTurnID) {

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

    //this.gameHandler.addGameEndListener(this); // Adds GameSesison to instance of GameHandler
    //this.gameHandler.addTurnEndListener(this); //

    // Or we can simply set this

    // For this do we do a this.addListener(this)

    //  void onGameEnd(GameResult gameResult, UUID winnerPlayer);
    // -- logic that goes to the sessionManager and removes game session
    // -- logic that sets player that lost, we know who won
    // -- leaderboard

    // if GameEnd






}