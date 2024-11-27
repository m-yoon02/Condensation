package ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO;

import java.net.http.WebSocket;
import java.util.UUID;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.GameResult;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers.*;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Managers.GameSessionManager;
import ca.ucalgary.seng300.p4.condensation.condensation.Networking.Managers.NetworkManager;
import org.json.simple.JSONObject;


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

        GameTypes gameType = playerMemo1.gameType();
            switch (gameType) {
                case CHECKERS :
                    gameHandler1 = new CheckersHandler(this.player1ID, this.player2ID);
                case TICTACTOE:
                    gameHandler1 = new TicTacToeHandler(this.player1ID, this.player2ID);
                case CONNECT4:
                    gameHandler1 = new TicTacToeHandler(this.player1ID, this.player2ID);
          }

        // adding gameSession to listen to TurnEnd and GameEnd
        this.gameHandler = gameHandler1;
        this.gameHandler.addTurnEndListener(this);
        this.gameHandler.addGameEndListener(this);


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


    /**
     * onGameEnd will update leaderboards, remove gameSession object from the sessions hashmap, and will send this information to the frontend.
     * @param gameResult
     * @param winnerPlayer
     * @PreCondition Game just ended and backend notifies game session listener that this event has occured
     * @PostCondiiton update leaderboards, remove gameSession object from the sessions hashmap, and will send this information to the frontend as JSON Payload
     */
    @Override
    public void onGameEnd(GameResult gameResult, UUID winnerPlayer) {
        System.out.println("Game has ended");
        if (winnerPlayer.equals(this.player1ID)) {
            // update leaderboard for player 1 as winner and player 2 as loser
        } else {
            // update leaderboard for player 2 winner, and player 1 as loser
        }

        // we will need to send something to the FrontEnd about the Game being over


        GameSessionManager.getInstance().removeGameSession(this.sessionId);

    }

    /**
     * onTurnEnd This is the implementation for the method that's invoked when a Turn has ended on the backend.
     * @param playersTurnID
     * @param gameStateJson
     * PreCondition : recieves the PlayerTurnID and gameStateJson on listener notification.
     * PostCondition : Calls network singelton method that sends formatted JSON Object data to client
     */

    @Override
    public void onTurnEnd(UUID playersTurnID, JSONObject gameStateJson) {
        System.out.println("Its now " + playersTurnID.toString() + "'s Turn");
        String currentPlayerTurn = null;

        if(playersTurnID == this.player1ID){
            currentPlayerTurn = "Player1";
        }else{
            currentPlayerTurn = "Player2";
        }

        // add sessionId to the gameState payload and send it over the network
        JSONObject clientPayload = new JSONObject();
        clientPayload.put("sessionId", this.sessionId.toString());
        clientPayload.put("currentPlayerTurnID", playersTurnID);              // display name for frontend
        clientPayload.put("currentPlayerTurnDisplayName", currentPlayerTurn); // uuid for checks frontEND may need on their end
        clientPayload.put("gameState",gameStateJson);                        // represents the gameState following backend applying move data

        // sends the game data to clients via the network singleton
        NetworkManager.getNetworkInstance().sendGameDataToClients(clientPayload);

    }


    //************
    // we will sends to backend "sendGameDataToBackEnd()"
    // frontEnd data,
    // im backend recieve method









}