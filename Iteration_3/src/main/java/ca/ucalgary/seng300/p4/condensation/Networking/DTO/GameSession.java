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
    private final String DATA_FILE = "\\C:\\Users\\Richa\\IdeaProjects\\demo6\\seng300-f24-project\\Iteration_3\\src\\main\\java\\ca\\ucalgary\\seng300\\p4\\condensation\\Networking\\Database\\Leaderboard\\Leaderboard.json\\";


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
     * Handles the end of a game by determining the result, updating player Elo ratings,
     * notifying clients, and cleaning up the game session.
     * @param gameResult   The result of the game (WIN, TIE, etc.).
     * @param winnerPlayer The UUID of the player who won the game.
     * @throws IOException    If an error occurs while sending game data to clients.
     * @throws ParseException If there is an error parsing or processing game-related data.
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
                Leaderboard.getInstance().updateEloForWinningPlayer(this.player1ID, this.gameType, this.sessionId);
                Leaderboard.getInstance().updateEloForLosingPlayer(this.player2ID, this.gameType, this.sessionId);
                gameEndPayload.put("PlayerWon", this.player1ID);
                gameEndPayload.put("PlayerLost", this.player2ID);


                // as winner and player 2 as loser
            } else {
                // update leaderboard for player 2 winner, and player 1 as loser
                Leaderboard.getInstance().updateEloForWinningPlayer(this.player2ID, this.gameType, this.sessionId);
                Leaderboard.getInstance().updateEloForLosingPlayer(this.player1ID, this.gameType, this.sessionId);
                gameEndPayload.put("PlayerWon", this.player2ID);
                gameEndPayload.put("PlayerLost", this.player1ID);
            }
        }else{
            Leaderboard.getInstance().updateForTieGame(this.player1ID, this.player2ID, this.sessionId, this.gameType);
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
     * Handles the end of a player's turn by creating a payload with the updated game state
     * and notifying the clients in the game session.
     * @param playersTurnID  The UUID of the player whose turn has ended.
     * @param gameStateJson  A JSON string representing the updated game state.
     * @throws RuntimeException If an error occurs while sending game data to clients.
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











}