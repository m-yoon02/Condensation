package ca.ucalgary.seng300.p4.condensation.Backend.Handlers;

public interface IGameHandler {
    void addGameEndListener(GameEndListener listener);
    void addTurnEndListener(TurnEndListener listener);
    //startGame will be used to initialize timers, makeMove will update timers
    void startGame();
    void makeMove(String moveDataJSON);
}
