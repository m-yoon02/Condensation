package ca.ucalgary.seng300.p4.condensation.Frontend.game;

import javafx.event.Event;

public abstract class Game {
    public abstract void showMove(Event event);

    public abstract void makeMove(Event event);

    public abstract void createBoard(Event event);

    public abstract void forfeit(Event event);

    public abstract void sendMessage(Event event);

    public abstract void showMessage(Event event);

    public abstract void notifyResult(Event event);
}
