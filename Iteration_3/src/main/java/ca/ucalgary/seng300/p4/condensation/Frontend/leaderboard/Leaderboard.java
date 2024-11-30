package ca.ucalgary.seng300.p4.condensation.Frontend.leaderboard;
import javafx.event.Event;

public abstract class Leaderboard {
    public int numInLeaderboard;
    public abstract String getLeaderboard();
    public abstract void showLeaderboard(Event event);
}
