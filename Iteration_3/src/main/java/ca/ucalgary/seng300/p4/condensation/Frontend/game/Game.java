package ca.ucalgary.seng300.p4.condensation.Frontend.game;

public abstract class Game {
    public abstract void showMove();

    public abstract void makeMove();

    public abstract void createBoard();

    public abstract void forfeit();

    public abstract void sendMessage();

    public abstract void showMessage();

    public abstract void notifyResult();
}
