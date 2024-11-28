package ca.ucalgary.seng300.p4.condensation.Frontend.queue;

import javafx.event.Event;

public abstract class Queue {
    public abstract void showPlayerAdded(Event event);

    public abstract void showPlayerRemoved(Event event);

    public abstract void showPlayerFound(Event event);

    public abstract void redirectToGame(Event event);

    public abstract void showNoPlayerFound(Event event);

    public abstract void getQueue();

    public abstract void showQueue(Event event);

    public abstract void addYourself(Event event);

    public abstract void returnToMenu(Event event);
}
