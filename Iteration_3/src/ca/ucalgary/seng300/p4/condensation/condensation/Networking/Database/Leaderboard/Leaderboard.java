package ca.ucalgary.seng300.p4.condensation.condensation.Networking.Database.Leaderboard;

import java.util.UUID;

public class Leaderboard {


    public void updateEloForWinningPlayer(UUID playerID) {
        System.out.println("Elo has been updated for winning player with PlayerID: " + playerID);
    }


    public void updateEloForLosingPlayer(UUID playerID) {
        System.out.println("Elo has been updated for losing player with PlayerID: " + playerID);
    }


    public void refreshLeaderboard() {
        System.out.println("Refreshed leaderboard");
    }
}
