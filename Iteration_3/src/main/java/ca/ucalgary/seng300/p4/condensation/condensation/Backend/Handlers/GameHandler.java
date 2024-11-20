package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.*;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData.MoveData;

import java.util.ArrayList;
import java.util.UUID;

//TODO: Need to somehow think of adding a GameTimer class that will be used here

public abstract class GameHandler<T extends MoveData> implements IGameHandler<T>{
    private final ArrayList<GameEndListener> gameEndListeners = new ArrayList<>();
    private final ArrayList<TurnEndListener> turnEndListeners = new ArrayList<>();

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
