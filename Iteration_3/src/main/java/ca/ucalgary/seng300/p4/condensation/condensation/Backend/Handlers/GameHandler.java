package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.DTOs.PlayerMemo;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

//TODO: Need to somehow think of adding a GameTimer class that will be used here

public abstract class GameHandler implements IGameHandler{
    private final ArrayList<GameEndListener> gameEndListeners = new ArrayList<>();
    private final ArrayList<TurnEndListener> turnEndListeners = new ArrayList<>();
    private final HashMap<UUID, PlayerMemo> playerMemoHashMap = new HashMap<>();

    public GameHandler(UUID player1ID, UUID player2ID, PieceColor[] colors){
        Random rnd = new Random();
        int player1ColorIndex = rnd.nextInt(2);
        int player2ColorIndex = player1ColorIndex == 0 ? 1 : 0;
        playerMemoHashMap.put(player1ID, new PlayerMemo(player1ID, colors[player1ColorIndex]));
        playerMemoHashMap.put(player2ID, new PlayerMemo(player2ID, colors[player2ColorIndex]));
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
     */
    protected void notifyTurnEnd(UUID currentPlayerTurnID){
        for (TurnEndListener listener: turnEndListeners){
            listener.onTurnEnd(currentPlayerTurnID);
        }
    }

    /**
     * Notifies all game end listeners that the game has ended
     * @param gameResult the game result
     * @param winnerID the ID of the winner player if the game has ended on a win
     */
    protected void notifyGameEnd(GameResult gameResult, UUID winnerID){
        for (GameEndListener listener: gameEndListeners){
            listener.onGameEnd(gameResult, winnerID);
        }
    }
}
