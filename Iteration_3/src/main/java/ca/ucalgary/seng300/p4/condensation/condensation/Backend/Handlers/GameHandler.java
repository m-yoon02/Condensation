package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.Enums.*;
import ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData.MoveData;

import java.util.ArrayList;

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

    protected void notifyTurnEnd(PieceColor currentPlayerTurnColor){
        for (TurnEndListener listener: turnEndListeners){
            listener.onTurnEnd(currentPlayerTurnColor);
        }
    }

    protected void notifyGameEnd(GameResult gameResult, PieceColor winnerColor){
        for (GameEndListener listener: gameEndListeners){
            listener.onGameEnd(gameResult, winnerColor);
        }
    }
}
