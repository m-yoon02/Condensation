package ca.ucalgary.seng300.p4.condensation.Networking.DTO;

import java.io.IOException;
import java.util.UUID;

import ca.ucalgary.seng300.p4.condensation.Backend.Enums.GameResult;
import ca.ucalgary.seng300.p4.condensation.Backend.Handlers.*;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameEventType;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.GameSessionManager;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


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
    private final GameTypes gameType;
    private final String DATA_FILE = "/Users/nateshoad/Desktop/University /Java_Files/SENG300F24P/seng300-f24-project/Iteration_3/src/main/java/ca/ucalgary/seng300/p4/condensation/Networking/Database/Leaderboard/Leaderboard.json";


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
        this.gameType = playerMemo1.gameType();

        GameTypes gameType = playerMemo1.gameType();
            switch (gameType) {
                // since we basically handle the state of backend we would pass into gameHandler the PlayerIDs, and we simply send client payload to this.gamehandler

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

        // since we basically handle the state of backend we would pass into gameHandler the PlayerIDs


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

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    /**
     * onGameEnd will update leaderboards, remove gameSession object from the sessions hashmap, and will send this information to the frontend.
     * @param gameResult
     * @param winnerPlayer
     * @PreCondition Game just ended and backend notifies game session listener that this event has occured
     * @PostCondiiton update leaderboards, remove gameSession object from the sessions hashmap, and will send this information to the frontend as JSON Payload
     */
    @Override
    public void onGameEnd(GameResult gameResult, UUID winnerPlayer) throws IOException, ParseException {

        GameEventType gameEventType = GameEventType.GAME_END;
        // we need to make the response payload here as well
        JSONObject gameEndPayload = new JSONObject();
        gameEndPayload.put("UpdateType", gameEventType);

        // This handles the payload information of if some player wins
        // I can use ternary to make this cleaner lol.
        if(gameResult == GameResult.WIN){
            gameEndPayload.put("GameState", GameResult.WIN);
            if (winnerPlayer.equals(this.player1ID)) {
                Leaderboard.getInstance().updateEloForWinningPlayer(this.player1ID, this.gameType);
                Leaderboard.getInstance().updateEloForLosingPlayer(this.player2ID, this.gameType);
                gameEndPayload.put("PlayerWon", this.player1ID);
                gameEndPayload.put("PlayerLost", this.player2ID);


                // as winner and player 2 as loser
            } else {
                // update leaderboard for player 2 winner, and player 1 as loser
                Leaderboard.getInstance().updateEloForWinningPlayer(this.player2ID, this.gameType);
                Leaderboard.getInstance().updateEloForLosingPlayer(this.player1ID, this.gameType);
                gameEndPayload.put("PlayerWon", this.player2ID);
                gameEndPayload.put("PlayerLost", this.player1ID);
            }
        }else{
            Leaderboard.getInstance(DATA_FILE).updateForTieGame(this.player1ID, this.player2ID, this.sessionId, this.gameType);
            gameEndPayload.put("GameState", GameResult.TIE);
            // Player fields will not exist and elo's will not change.

        }

        // we will also need to store some kind of game history
        // sends the payload to frontend client listeners.
        NetworkManager.getNetworkInstance().sendGameDataToClients(gameEndPayload, this.sessionId, gameEventType, this.gameType);
        
        // remove game from the GameSessionsManager hashmap
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
    public void onTurnEnd(UUID playersTurnID, String gameStateJson) {


        UUID sessionId = this.sessionId;
        // this will be passed in to client listener so it knows what kind of gameEvent the gameSession listener has been notified about
        GameEventType gameEventType = GameEventType.TURN_END;

        // verify if this is how frontend will recieve information from backend
        JSONObject responseClientPayload = new JSONObject();
        responseClientPayload.put("UpdateType", "TURN_END");
        responseClientPayload.put("SessionID", sessionId);
        responseClientPayload.put("PlayerTurnID", playersTurnID);
        responseClientPayload.put("Changes", gameStateJson);

        try {
            NetworkManager.getNetworkInstance().sendGameDataToClients(responseClientPayload, sessionId, gameEventType, this.gameType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    // This is what the backend will send back to the client

    // ```json
    //{
    //    "UpdateType": "GAME_END", //(OR StateChange)
    //    "GameState": "WIN", // (or TIE)
    //    // IF GameState == WIN:
    //    "PlayerWon": "UUID of the player who won",
    //    "PlayerLost": "UUID of the player who lost"
    //    // if GameState == TIE, above fields will NOT exist
    //}
    //```
    //
    //```json
    //{
    //    "UpdateType": "TURN_END",
    //    "SessionID": "UUID of the Session",
    //    "PlayerTurn": "UUID of the next player's turn",
    //    "Changes": [
    //        ["EMPTY", "RED", "EMPTY"],
    //        ["BLUE", "RED", "EMPTY"],
    //        ["EMPTY", "BLUE", "EMPTY"],
    //    ] // A 2D array representing the current state of the board on the Back End
    //    // RED, BLUE are not the only colours, colours differ per game, SEE BELOW
    //}


    // This is what frontend recieves from the backend via recieveGameDataFromClient
    // we recieve this as a json object from the frontend not as a string

    // {
    //    "SessionID": "UUID Representing Session",
    //    "PlayerID": "UUID of the player making the move",
    //    "NewPosition": [0, 5],
    //    // If the game is Checkers:
    //    "OldPosition": [1, 6]
    //    //NewPosition represents the new position that the player wants to move the piece to
    //    //IF THE GAME IS CHECKERS, OldPosition will represent the old position of the piece
    //}










}