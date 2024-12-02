package ca.ucalgary.seng300.p4.condensation.Networking.DTO;

import java.util.UUID;


/**
 * Represents a player in the system, including their unique ID, username,
 * leaderboard position, and Elo rating.
 */
public class Player {
    UUID playerID;
    String userName;
    int leaderBoardPosition;
    int elo;

    /**
     * Constructs a new Player with the specified username, leaderboard position, and Elo rating.
     * A unique player ID is automatically generated.
     * @param userName            The player's username.
     * @param leaderBoardPosition The player's position on the leaderboard.
     * @param elo                 The player's Elo rating.
     */
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
