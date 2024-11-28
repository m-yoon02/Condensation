package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs.PlayerMemo;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.*;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.GameTimer.GameTimer;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.GameTimer.TimerEndListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public abstract class GameHandler implements IGameHandler{
    //NOTE: Might change this later depending on if we want time to change depending on games
    private static final int DEFAULT_TIME = 60 * 10;

    private final ArrayList<GameEndListener> gameEndListeners = new ArrayList<>();
    private final ArrayList<TurnEndListener> turnEndListeners = new ArrayList<>();
    private final HashMap<UUID, PlayerMemo> playerMemoHashMap = new HashMap<>();
    private final UUID firstPlayerID;
    protected final GameTimer gameTimer;

    private class EndGameOnTimerEndListener implements TimerEndListener {
        @Override
        public void onTimerEnd(UUID winnerPlayerID) {
            notifyGameEnd(GameResult.WIN, winnerPlayerID);
        }
    }

    public GameHandler(UUID player1ID, UUID player2ID, PieceColor[] colors){
        Random rnd = new Random();
        int player1ColorIndex = rnd.nextInt(2);
        int player2ColorIndex = player1ColorIndex == 0 ? 1 : 0;
        playerMemoHashMap.put(player1ID, new PlayerMemo(player1ID, colors[player1ColorIndex]));
        playerMemoHashMap.put(player2ID, new PlayerMemo(player2ID, colors[player2ColorIndex]));
        firstPlayerID = getPlayerMemoFromPieceColor(colors[0]).playerID();
        gameTimer = new GameTimer(player1ID, player2ID, DEFAULT_TIME);
        gameTimer.setTimerTo(player1ColorIndex == 0 ? player1ID : player2ID);
        gameTimer.addTimerEndListener(new EndGameOnTimerEndListener());
    }

    @Override
    public void startGame() {
        gameTimer.startTimer();
    }

    @Override
    public void addGameEndListener(GameEndListener listener) {
        gameEndListeners.add(listener);
    }

    @Override
    public void addTurnEndListener(TurnEndListener listener) {
        turnEndListeners.add(listener);
    }

    /**
     * Notifies all turn end listeners that a turn has ended and which player's turn it is currently
     * @param currentPlayerTurnID the ID of the next player's turn
     * @param boardStateJSON a JSON string representing changes in
     */
    protected void notifyTurnEnd(UUID currentPlayerTurnID, String boardStateJSON){
        gameTimer.setTimerTo(currentPlayerTurnID);
        for (TurnEndListener listener: turnEndListeners){
            listener.onTurnEnd(currentPlayerTurnID, boardStateJSON);
        }
    }

    /**
     * Notifies all game end listeners that the game has ended
     * @param gameResult the game result
     * @param winnerID the ID of the winner player if the game has ended on a win
     */
    protected void notifyGameEnd(GameResult gameResult, UUID winnerID){
        //Stop the timer when the game ends obviously
        gameTimer.stopTimer();
        for (GameEndListener listener: gameEndListeners){
            listener.onGameEnd(gameResult, winnerID);
        }
    }

    /**
     * Gets the player memo from a given player ID
     * @param playerID the player ID
     * @return the player memo representing the player from the ID
     */
    public PlayerMemo getPlayerMemoFromUUID(UUID playerID){
        return playerMemoHashMap.get(playerID);
    }

    /**
     * Gets the player color from a given player ID
     * @param playerID the player ID to retrieve the color from
     * @return the color of the player
     */
    public PieceColor getPlayerColorFromUUID(UUID playerID) {
        return getPlayerMemoFromUUID(playerID).pieceColor();
    }

    /**
     * Gets the other player's player memo whose PlayerID differs from the given player id
     * @param playerID the player id of which player NOT to get
     * @return the other player, if one exists
     */
    public PlayerMemo getOtherPlayerMemoFromUUID(UUID playerID){
        for (PlayerMemo playerMemo: playerMemoHashMap.values()){
            if (!playerMemo.playerID().equals(playerID)) {
                return playerMemo;
            }
        }
        return null;
    }

    /**
     * Gets the other player's player ID whose PlayerID differs from the given player id
     * @param playerID the player id of which player NOT to get
     * @return the other player's ID, if one exists
     */
    public UUID getOtherPlayerUUID(UUID playerID){
        return getOtherPlayerMemoFromUUID(playerID).playerID();
    }

    /**
     * Gets the player memo from a given PieceColor
     * @param pieceColor the PieceColor that the player chose
     * @return the player memo if the player exists, otherwise null
     */
    public PlayerMemo getPlayerMemoFromPieceColor(PieceColor pieceColor){
        for (PlayerMemo playerMemo: playerMemoHashMap.values()){
            if (playerMemo.pieceColor() == pieceColor) {
                return playerMemo;
            }
        }
        return null;
    }

    /**
     * Gets the ID of the player who goes first
     * @return UUID of the first player's ID
     */
    public UUID getFirstPlayerID() {
        return firstPlayerID;
    }
}
