package ca.ucalgary.seng300.p4.condensation.Networking.DTO;

import java.util.UUID;

public class Player {
    UUID playerID;

    String userName;
    int leaderBoardPosition;
    int elo;

    public Player(String userName, int leaderBoardPosition, int elo) {
        this.userName = userName;
        this.playerID = UUID.randomUUID();
        this.leaderBoardPosition = leaderBoardPosition;
        this.elo = elo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public int getLeaderBoardPosition() {
        return leaderBoardPosition;
    }

    public void setLeaderBoardPosition(int leaderBoardPosition) {
        this.leaderBoardPosition = leaderBoardPosition;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

}
