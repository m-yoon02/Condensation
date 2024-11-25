package ca.ucalgary.seng300.p4.condensation.condensation.Networking.DTO;

import org.json.simple.JSONObject;

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

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
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

    /**
     * Creates a Player object from a JSON representation which is solely designed for verifying
     * @param json JSONObject representing the player
     * @return Player object
     */
    public static Player fromJson(JSONObject json) {
        String username = (String) json.get("username");
        String playerUUID = (String) json.get("playerUUID");
        int leaderBoardPosition = ((Long) json.get("leaderBoardPosition")).intValue();
        int elo = ((Long) json.get("elo")).intValue();
        Player player = new Player(username, leaderBoardPosition, elo);
        player.setPlayerID(UUID.fromString(playerUUID));

        return new Player(username, leaderBoardPosition, elo);
    }

}
