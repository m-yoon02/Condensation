package ca.ucalgary.seng300.p4.condensation.Frontend.queue;

import javafx.event.Event;

public abstract class Queue {
    public abstract void showPlayerAdded();

    public abstract void showPlayerRemoved();

    public abstract void showPlayerFound();

    public abstract void redirectToGame(Event event);

    public abstract void showNoPlayerFound();

    public abstract String getQueue();

    public abstract void addYourself();

    public abstract void returnToMenu(Event event);
}
