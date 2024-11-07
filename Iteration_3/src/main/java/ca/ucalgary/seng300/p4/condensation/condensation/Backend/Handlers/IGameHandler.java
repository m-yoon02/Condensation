package ca.ucalgary.seng300.p4.condensation.condensation.Backend.Handlers;

import ca.ucalgary.seng300.p4.condensation.condensation.Backend.MoveData.MoveData;

public interface IGameHandler <T extends MoveData> {
    void addGameEndListener(GameEndListener listener);
    void addTurnEndListener(TurnEndListener listener);
    //startGame will be used to initialize timers, makeMove will update timers
    void startGame();
    void makeMove(T moveData);
}
