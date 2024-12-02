package ca.ucalgary.seng300.p4.condensation.Networking.Services;

import ca.ucalgary.seng300.p4.condensation.Networking.DTO.Player;
import ca.ucalgary.seng300.p4.condensation.Networking.DTO.QueuedPlayerMemo;
import ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard.Leaderboard;
import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MatchmakingPool {
    private static List<QueuedPlayerMemo> pool = new ArrayList<>();
    private final String DATA_FILE = "/Users/nateshoad/Desktop/University /Java_Files/SENG300F24P/seng300-f24-project/Iteration_3/src/main/java/ca/ucalgary/seng300/p4/condensation/Networking/Database/Leaderboard/Leaderboard.json";





    public MatchmakingPool() throws IOException, ParseException {
        this.fetch4RandomPlayers();
    }

    /**
     * This method adds a player into the matchmaking pool
     * @param player the player being added
     */
    public void addPlayer(QueuedPlayerMemo player, int index) {
        pool.add(index, player);
        System.out.println("Player Added: " + player.PlayerID());
    }

    public void addPlayer(QueuedPlayerMemo player) {
        pool.add(player);
        System.out.println("Player Added: " + player.PlayerID());
    }


    public String removePlayerFromMatchmakingPool(UUID playerID) {
        QueuedPlayerMemo playerToRemove = null;

        for (QueuedPlayerMemo queuedPlayer : pool) {
            if (queuedPlayer.PlayerID().equals(playerID)) {
                pool.remove(queuedPlayer);
                return String.format("You have successfully logged outm see you later ");                 // Exit loop after finding the player
            }
        }
        return String.format("Player not found in matchmaking pool: "+ playerID);


    }






    /**
     * This method removes a player from the matchmaking pool
     * @param player the player being removed
     */
    public void removePlayer(QueuedPlayerMemo player) {
        pool.remove(player);
    }

    /**
     * This is a getter method for the matchmaking pool. Used in MatchmakingService.
     * @return the pool list
     */
    public List<QueuedPlayerMemo> getPool() {
        System.out.println(pool.size());
        return pool;

    }

    /**
     * Fetches four random players from the leaderboard and adds them to a game queue,
     * associating each player with a specific game type.
     * @throws IOException    If an error occurs while accessing the leaderboard data.
     * @throws ParseException If the leaderboard data cannot be parsed correctly.
     */
    public void fetch4RandomPlayers() throws IOException, ParseException {
        ArrayList<JSONObject> UUIDarrayList = new ArrayList<>();
        for (Object object : Leaderboard.getInstance().getAllUsers()) {
            JSONObject jsonObject = (JSONObject) object;
            jsonObject.get("playerUUID");
            UUIDarrayList.add(jsonObject);
        }
        Random random = new Random();
        while (UUIDarrayList.size() < 4) {
            int randomIndex = random.nextInt(UUIDarrayList.size());
            JSONObject randomUUID = UUIDarrayList.get(randomIndex);

            // avoid duplicates
            if (!UUIDarrayList.contains(randomUUID)) {
                UUIDarrayList.add(randomUUID);
            }
        }

        GameTypes[] gameTypes = {GameTypes.CHECKERS, GameTypes.CONNECT4, GameTypes.TICTACTOE};


        int index = 0;
        for (JSONObject jsonObject : UUIDarrayList) {

            UUID playerID = UUID.fromString((String) jsonObject.get("playerUUID"));
            int elo = ((Long) jsonObject.get("checkersElo")).intValue();

            // Get the game type based on the current index
            GameTypes currentGameType = gameTypes[index];

            // Create the QueuedPlayerMemo with the selected game type
            QueuedPlayerMemo queuedPlayerMemo = new QueuedPlayerMemo(playerID, currentGameType, elo);
            pool.add(queuedPlayerMemo);

            index = (index + 1) % gameTypes.length; // Cycles back to 0 after the last game type
        }


    }
}
