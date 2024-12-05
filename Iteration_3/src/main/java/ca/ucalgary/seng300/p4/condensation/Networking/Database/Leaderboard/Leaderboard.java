package ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard;

import java.io.*;
import java.util.UUID;

import ca.ucalgary.seng300.p4.condensation.Networking.Enums.GameTypes;
import ca.ucalgary.seng300.p4.condensation.Networking.Managers.NetworkManager;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;

/**
 * This class represents a Leaderboard that manages user data, including their ELO scores, and
 * provides functionality for retrieving and updating leaderboard information.
 */
public class Leaderboard {
    private static Leaderboard instance; // Singleton instance
    private JSONArray users;
    private JSONObject playerInGame;

    /**
     * Constructs a new Leaderboard instance and loads data from the specified file.
     *
     * @throws IOException    if there is an error reading the file.
     * @throws ParseException if there is an error parsing the JSON data.
     */
    public Leaderboard() throws IOException, ParseException {
        loadData();
    }

    /**
     * Provides global access to the single instance of the Leaderboard class.
     *
     * @param
     * @return the singleton instance of the Leaderboard.
     * @throws IOException    if there is an error reading the file.
     * @throws ParseException if there is an error parsing the JSON data.
     */
    public static synchronized Leaderboard getInstance() throws IOException, ParseException {
        if (instance == null) {
            instance = new Leaderboard();
        }
        return instance;
    }



    /**
     * Loads user data from the JSON file into the leaderboard.
     *
     * @throws IOException    if there is an error reading the file.
     * @throws ParseException if there is an error parsing the JSON data.
     */
    public void loadData() throws IOException, ParseException {
        File file = new File(NetworkManager.LEADERBOARD_FILE);
        if (!file.exists()) {
            throw new FileNotFoundException("Leaderboard.json not found in the working directory: " + file.getAbsolutePath());
        }

        try (FileReader reader = new FileReader(file)) {
            JSONParser parser = new JSONParser();
            users = (JSONArray) parser.parse(reader);
            printLeaderboard();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    /**
     * Saves the current leaderboard data to the JSON file.
     *
     * @throws IOException if there is an error writing to the file.
     */
    public void saveData() throws IOException {
        try {
            FileWriter fileWriter = new FileWriter(NetworkManager.LEADERBOARD_FILE);
            fileWriter.write(users.toJSONString());
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Data could not be saved");
        }
    }

    /**
     * Retrieves all user data as a JSONArray.
     *
     * @return a JSONArray of all users.
     */
    public JSONArray getAllUsers() {
        return users;
    }

    public void createUser(String name, String password) throws IOException {
        JSONObject user = new JSONObject(); // Create a new user object


        // Assign random UUID and user credentials
        user.put("playerUUID", UUID.randomUUID().toString());
        user.put("username", name);
        user.put("password", password);

        // Set default ELO scores
        user.put("connect4Elo", 1200);
        user.put("checkersElo", 1200);
        user.put("tictactoeElo", 1200);

        // Set default stats for games
        user.put("leaderBoardPosition", 0); // Default leaderboard position
        user.put("gamesPlayedCheckers", 0);
        user.put("winsInCheckers", 0);
        user.put("lossInCheckers", 0);
        user.put("gamesPlayedConnect4", 0);
        user.put("winsInConnect4", 0);
        user.put("lossInConnect4", 0);
        user.put("gamesPlayedTicTacToe", 0);
        user.put("winsInTicTacToe", 0);
        user.put("lossInTicTacToe", 0);

        // Initialize an empty list of recent matches
        JSONArray recentMatches = new JSONArray();
        user.put("recentMatches", recentMatches);
        users.add(user);

    }
    /**
     * Finds a user by their username.
     *
     * @param name the username of the user.
     * @return a JSONObject representing the user, or null if no user is found.
     */
    public JSONObject findUserByName(String name) {
        for (Object object : users) {
            JSONObject user = (JSONObject) object;
            if (user.get("username").equals(name)) {
                return user;
            }
        }
        return null;
    }




//
//    /**
//     * Finds a user by their password.
//     *
//     * @param pwd the username of the user.
//     * @return a JSONObject representing the user, or null if no user is found.
//     */
//    public JSONObject findUserByPassword(String pwd) {
//        for (Object object : users) {
//            JSONObject user = (JSONObject) object;
//            System.out.println(user);
//            System.out.println(user.get("password"));
//            if (user.get("password").equals(pwd)) {
//                return user;
//            }
//        }
//        return null;
//    }

    /**
     * Finds a user by their username and returns their details as a JSON string.
     *
     * @param name the username of the user.
     * @return a JSON string of the user details, or null if no user is found.
     */
    public String findUserByNameToString(String name) {
        for (Object object : users) {
            JSONObject user = (JSONObject) object;
            if (user.get("username").equals(name)) {
                return user.toJSONString();
            }
        }
        return null;
    }

    public void changeName(UUID playerID, String newName) {
        for (Object object : users) {
            JSONObject user = (JSONObject) object;
            if (user.get("playerUUID").equals(playerID.toString())) {
                user.put("username", newName);
            }
        }
    }
    public void changePassword(UUID playerID, String newPass) {
        for (Object object : users) {
            JSONObject user = (JSONObject) object;
            if (user.get("playerUUID").equals(playerID.toString())) {
                user.put("username", newPass);
            }
        }
    }

    /**
     * Finds a user by their unique UUID.
     *
     * @param uuid the UUID of the user.
     * @return a JSONObject representing the user, or null if no user is found.
     */
    public JSONObject findUserById(UUID uuid) {
        String uuidToString = uuid.toString();

        for (Object object : users) {
            JSONObject user = (JSONObject) object;
            if (user.get("playerUUID").equals(uuidToString)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Updates the leaderboard and game data to reflect a tied game result for the specified players and game type.
     * @param player1ID  The UUID of the first player in the tie.
     * @param player2ID  The UUID of the second player in the tie.
     * @param sessionID  The UUID of the game session where the tie occurred.
     * @param gameType   The type of game that ended in a tie.
     */

    public void updateForTieGame(UUID player1ID, UUID player2ID, UUID sessionID, GameTypes gameType ){

        switch (gameType) {
            case CHECKERS -> {
                updateForTieGameCheckers(player1ID, player2ID, sessionID);
            }
            case TICTACTOE -> {
                updateForTieGameTicTacToe(player1ID, player2ID, sessionID);
            }
            case CONNECT4 -> {
                updateForTieGameConnect4(player1ID, player2ID, sessionID);
            }
        }

    }

    /**
     * Updates the game data for a tied game of Checkers, modifying the recent matches
     * and Elo ratings of both players involved.
     * @param player1ID  The UUID of the first player in the tie.
     * @param player2ID  The UUID of the second player in the tie.
     * @param sessionID  The UUID of the game session where the tie occurred.
     */
    public void updateForTieGameCheckers(UUID player1ID, UUID player2ID, UUID sessionID){

        // I need to the player 1 json object then I need to simply add a tied update\
        JSONObject player1 = findUserById(player1ID);
        JSONArray player1RecentMatches = (JSONArray) player1.get("recentMatches");
        String player1Elo = (String) player1.get("checkersElo");
        JSONObject player1CurrentMatch = new JSONObject();
        player1CurrentMatch.put("matchID", String.valueOf(sessionID));
        player1CurrentMatch.put("gameType", String.valueOf(GameTypes.CHECKERS));
        player1CurrentMatch.put("outcome", "T");
        player1CurrentMatch.put("currentElo", player1Elo);
        player1RecentMatches.add(player1CurrentMatch);
        player1.put("recentMatches", player1RecentMatches);


        // add user to the database

        JSONObject player2 = findUserById(player1ID);
        JSONArray player2RecentMatches = (JSONArray) player1.get("recentMatches");
        String player2Elo = (String) player1.get("checkersElo");
        JSONObject player2CurrentMatch = new JSONObject();
        player2CurrentMatch.put("matchID", sessionID);
        player2CurrentMatch.put("gameType", GameTypes.CHECKERS);
        player2CurrentMatch.put("outcome", "T");
        player2CurrentMatch.put("currentElo", player1Elo);
        player2RecentMatches.add(player2CurrentMatch);
        player2.put("recentMatches", player2RecentMatches);

    }


    /**
     * Updates the game data for a tied game of Tic-Tac-Toe, modifying the recent matches
     * and Elo ratings of both players involved.
     * @param player1ID  The UUID of the first player in the tie.
     * @param player2ID  The UUID of the second player in the tie.
     * @param sessionID  The UUID of the game session where the tie occurred.
     */
    public void updateForTieGameTicTacToe(UUID player1ID, UUID player2ID, UUID sessionID){

        // I need to the player 1 json object then I need to simply add a tied update\
        JSONObject player1 = findUserById(player1ID);
        JSONArray player1RecentMatches = (JSONArray) player1.get("recentMatches");
        String player1Elo = (String) player1.get("tictactoeElo");
        JSONObject player1CurrentMatch = new JSONObject();
        player1CurrentMatch.put("matchID", sessionID);
        player1CurrentMatch.put("gameType", GameTypes.TICTACTOE);
        player1CurrentMatch.put("outcome", "T");
        player1CurrentMatch.put("currentElo", player1Elo);
        player1RecentMatches.add(player1CurrentMatch);
        player1.put("recentMatches", player1RecentMatches);


        // add user to the database

        JSONObject player2 = findUserById(player1ID);
        JSONArray player2RecentMatches = (JSONArray) player1.get("recentMatches");
        String player2Elo = (String) player1.get("tictactoeElo");
        JSONObject player2CurrentMatch = new JSONObject();
        player2CurrentMatch.put("matchID", sessionID);
        player2CurrentMatch.put("gameType", GameTypes.TICTACTOE);
        player2CurrentMatch.put("outcome", "T");
        player2CurrentMatch.put("currentElo", player1Elo);
        player2RecentMatches.add(player2CurrentMatch);
        player2.put("recentMatches", player2RecentMatches);




    }



    /**
     * Updates the game data for a tied game of Connect 4, modifying the recent matches
     * and Elo ratings of both players involved.
     * @param player1ID  The UUID of the first player in the tie.
     * @param player2ID  The UUID of the second player in the tie.
     * @param sessionID  The UUID of the game session where the tie occurred.
     */
    public void updateForTieGameConnect4(UUID player1ID, UUID player2ID, UUID sessionID){

        // I need to the player 1 json object then I need to simply add a tied update\
        JSONObject player1 = findUserById(player1ID);
        JSONArray player1RecentMatches = (JSONArray) player1.get("recentMatches");
        String player1Elo = (String) player1.get("connect4Elo");
        JSONObject player1CurrentMatch = new JSONObject();
        player1CurrentMatch.put("matchID", sessionID);
        player1CurrentMatch.put("gameType", GameTypes.CONNECT4);
        player1CurrentMatch.put("outcome", "T");
        player1CurrentMatch.put("currentElo", player1Elo);
        player1RecentMatches.add(player1CurrentMatch);
        player1.put("recentMatches", player1RecentMatches);

        // add user to the database

        JSONObject player2 = findUserById(player1ID);
        JSONArray player2RecentMatches = (JSONArray) player1.get("recentMatches");
        String player2Elo = (String) player1.get("connect4Elo");
        JSONObject player2CurrentMatch = new JSONObject();
        player2CurrentMatch.put("matchID", sessionID);
        player2CurrentMatch.put("gameType", GameTypes.CONNECT4);
        player2CurrentMatch.put("outcome", "T");
        player2CurrentMatch.put("currentElo", player1Elo);
        player2RecentMatches.add(player2CurrentMatch);
        player2.put("recentMatches", player2RecentMatches);



    }




    /**
     * Updates the Elo rating for a winning player based on the game type and session.
     * @param playerID  The UUID of the player who won the game.
     * @param gameType  The type of game played (e.g., CHECKERS, TICTACTOE, CONNECT4).
     * @param sessionID The UUID of the game session where the win occurred.
     */
    public void updateEloForWinningPlayer(UUID playerID, GameTypes gameType, UUID sessionID) throws IOException {
        switch (gameType) {
            case CHECKERS -> {
                updateEloForWinningPlayerCheckers(playerID, sessionID);
            }
            case TICTACTOE -> {
                updateEloForWinningPlayerTictactoe(playerID, sessionID);
            }
            case CONNECT4 -> {
                updateEloForWinningPlayerConnect4(playerID, sessionID);
            }
        }
    }

    /**
     * Updates the Elo rating, win count, and game history for a player who won a game of Checkers.
     * @param playerID  The UUID of the player who won the game.
     * @param sessionID The UUID of the game session where the win occurred.
     */
    private void updateEloForWinningPlayerCheckers(UUID playerID, UUID sessionID) throws IOException {
        String StringID = sessionID.toString();
        System.out.println("TYPE OF SESSION ID: " + StringID.getClass().getSimpleName());

        for (Object user : users) {
            JSONObject JsonUser = (JSONObject) user;
            if (JsonUser.get("playerUUID").equals(playerID.toString())) {

                Number winsInCheckers = (Number) JsonUser.get("winsInCheckers") ; // update wins in gamet
                Number totalCheckerGames = (Number) JsonUser.get("gamesPlayedCheckers");
                totalCheckerGames = totalCheckerGames.intValue() + 1;
                winsInCheckers = winsInCheckers.intValue() + 1;
                Number currentElo = (Number) JsonUser.get("checkersElo");
                currentElo = currentElo.intValue() + 100;
                JSONArray recentMatches = (JSONArray) JsonUser.get("recentMatches");
                JSONObject currentMatch = new JSONObject();
                currentMatch.put("matchID",StringID);
                currentMatch.put("gameType", GameTypes.CHECKERS.toString());
                currentMatch.put("outcome", "W");
                currentMatch.put("currentElo", currentElo);
                recentMatches.add(currentMatch);

                JsonUser.put("checkersElo", currentElo);
                JsonUser.put("winsInCheckers", winsInCheckers);
                JsonUser.put("gamesPlayedCheckers", totalCheckerGames);
                JsonUser.put("recentMatches", recentMatches);

                // update the game history
                try {
                    saveData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            System.out.println("Cant find plater lmao");
        }








        System.out.println("Elo has been updated for winning player with PlayerID: " + playerID);
    }


    /**
     * Updates the Elo rating, win count, and game history for a player who won a game of Tic-Tac-Toe.
     * @param playerID  The UUID of the player who won the game.
     * @param sessionID The UUID of the game session where the win occurred.
     */
    private void updateEloForWinningPlayerTictactoe(UUID playerID, UUID sessionID) {
        String StringID = sessionID.toString();
        System.out.println("TYPE OF SESSION ID: " + StringID.getClass().getSimpleName());

        for (Object user : users) {
            JSONObject JsonUser = (JSONObject) user;
            if (JsonUser.get("playerUUID").equals(playerID.toString())) {

                Number winsInCheckers = (Number) JsonUser.get("winsInTicTacToe"); // update wins in gamet
                Number totalCheckerGames = (Number) JsonUser.get("gamesPlayedTicTacToe");
                totalCheckerGames = totalCheckerGames.intValue() + 1;
                winsInCheckers = winsInCheckers.intValue() + 1;
                Number currentElo = (Number) JsonUser.get("tictactoeElo");
                currentElo = currentElo.intValue() + 100;
                JSONArray recentMatches = (JSONArray) JsonUser.get("recentMatches");
                JSONObject currentMatch = new JSONObject();
                currentMatch.put("matchID", StringID);
                currentMatch.put("gameType", GameTypes.TICTACTOE.toString());
                currentMatch.put("outcome", "W");
                currentMatch.put("currentElo", currentElo);
                recentMatches.add(currentMatch);

                JsonUser.put("tictactoeElo", currentElo);
                JsonUser.put("winsInTicTacToe", winsInCheckers);
                JsonUser.put("gamesPlayedTicTacToe", totalCheckerGames);
                JsonUser.put("recentMatches", recentMatches);

                // update the game history
                try {
                    saveData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            System.out.println("Cant find plater lmao");
        }
    }


    /**
     * Updates the Elo rating, win count, and game history for a player who won a game of Connect 4.
     * @param playerID  The UUID of the player who won the game.
     * @param sessionID The UUID of the game session where the win occurred.
     */
    private void updateEloForWinningPlayerConnect4(UUID playerID, UUID sessionID) throws IOException {
        String StringID = sessionID.toString();
        System.out.println("TYPE OF SESSION ID: " + StringID.getClass().getSimpleName());

        for (Object user : users) {
            JSONObject JsonUser = (JSONObject) user;
            if (JsonUser.get("playerUUID").equals(playerID.toString())) {

                Number winsInCheckers = (Number) JsonUser.get("winsInConnect4"); // update wins in gamet
                Number totalCheckerGames = (Number) JsonUser.get("gamesPlayedConnect4");
                totalCheckerGames = totalCheckerGames.intValue() + 1;
                winsInCheckers = winsInCheckers.intValue() + 1;
                Number currentElo = (Number) JsonUser.get("connect4Elo");
                currentElo = currentElo.intValue() + 100;
                JSONArray recentMatches = (JSONArray) JsonUser.get("recentMatches");
                JSONObject currentMatch = new JSONObject();
                currentMatch.put("matchID", StringID);
                currentMatch.put("gameType", GameTypes.CONNECT4.toString());
                currentMatch.put("outcome", "W");
                currentMatch.put("currentElo", currentElo);
                recentMatches.add(currentMatch);

                JsonUser.put("connect4Elo", currentElo);
                JsonUser.put("winsInConnect4", winsInCheckers);
                JsonUser.put("gamesPlayedConnect4", totalCheckerGames);
                JsonUser.put("recentMatches", recentMatches);

                // update the game history
                try {
                    saveData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            System.out.println("Cant find plater lmao");
        }
    }

    /**
     * Fetches the top 5 players based on their Elo ratings.
     * @return A {@link JSONArray} containing the top 5 players, each represented as a {@link JSONObject}.
     * @throws IllegalArgumentException If any user is missing the "elo" field.
     */
    public JSONArray fetchTop5Players() {
        users.sort((user1, user2) -> {
            Number elo1 = (Number) ((JSONObject) user1).get("elo");
            Number elo2 = (Number) ((JSONObject) user2).get("elo");
            if (elo1 == null || elo2 == null) {
                throw new IllegalArgumentException("User is missing 'elo' field.");
            }
            return Integer.compare(elo2.intValue(), elo1.intValue());
        });

        JSONArray top5Players = new JSONArray();
        int count = 0;

        for (Object object : users) {
            if (count < 5) {
                top5Players.add(object);
                count++;
            } else {
                break;
            }
        }

        System.out.println("The Top 5 Players:");
        for (Object object : top5Players) {
            JSONObject user = (JSONObject) object;
            System.out.println("Name: " + user.get("username") + ", Elo: " + user.get("elo"));
        }

        return top5Players;
    }

    /**
     * Updates the Elo rating for a player who lost a game, based on the game type and session ID.
     * @param playerID  The UUID of the player who lost the game.
     * @param gameType  The type of game played (e.g., CHECKERS, TICTACTOE, CONNECT4).
     * @param sessionId The UUID of the game session where the loss occurred.
     */
    public void updateEloForLosingPlayer(UUID playerID, GameTypes gameType, UUID sessionId) {
        switch (gameType) {
            case CHECKERS -> {
                updateEloForLosingPlayerCheckers(playerID, sessionId);
            }
            case TICTACTOE -> {
                updateEloForLosingPlayerTictactoe(playerID, sessionId);
            }
            case CONNECT4 -> {
                updateEloForLosingPlayerConnect4(playerID, sessionId);
            }
        }

    }


    /**
     * Updates the Elo rating, loss count, and game history for a player who lost a game of Checkers.
     * @param playerID  The UUID of the player who lost the game.
     * @param sessionID The UUID of the game session where the loss occurred.
     */
    public void updateEloForLosingPlayerCheckers(UUID playerID, UUID sessionID) {
        String StringID = sessionID.toString();
        System.out.println("TYPE OF SESSION ID: " + StringID.getClass().getSimpleName());

        for (Object user : users) {
            JSONObject JsonUser = (JSONObject) user;
            if (JsonUser.get("playerUUID").equals(playerID.toString())) {

                Number lossChecker = (Number) JsonUser.get("lossInCheckers"); // update wins in gamet
                Number totalCheckerGames = (Number) JsonUser.get("gamesPlayedCheckers");
                totalCheckerGames = totalCheckerGames.intValue() + 1;
                lossChecker = lossChecker.intValue() + 1;
                Number currentElo = (Number) JsonUser.get("checkersElo");
                currentElo = currentElo.intValue() - 100;
                JSONArray recentMatches = (JSONArray) JsonUser.get("recentMatches");
                JSONObject currentMatch = new JSONObject();
                currentMatch.put("matchID", StringID);
                currentMatch.put("gameType", GameTypes.CHECKERS.toString());
                currentMatch.put("outcome", "L");
                currentMatch.put("currentElo", currentElo);
                recentMatches.add(currentMatch);

                JsonUser.put("checkersElo", currentElo);
                JsonUser.put("lossInCheckers", lossChecker);
                JsonUser.put("gamesPlayedCheckers", totalCheckerGames);
                JsonUser.put("recentMatches", recentMatches);

                // update the game history
                try {
                    saveData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            System.out.println("Cant find plater lmao");
        }
    }


    /**
     * Updates the Elo rating, loss count, and game history for a player who lost a game of Checkers.
     * @param playerID  The UUID of the player who lost the game.
     * @param sessionID The UUID of the game session where the loss occurred.
     */
    public void updateEloForLosingPlayerTictactoe(UUID playerID, UUID sessionID) {
        String StringID = sessionID.toString();
        System.out.println("TYPE OF SESSION ID: " + StringID.getClass().getSimpleName());

        for (Object user : users) {
            JSONObject JsonUser = (JSONObject) user;
            if (JsonUser.get("playerUUID").equals(playerID.toString())) {

                Number lossChecker = (Number) JsonUser.get("lossInTicTacToe"); // update wins in gamet
                Number totalCheckerGames = (Number) JsonUser.get("gamesPlayedTicTacToe");
                totalCheckerGames = totalCheckerGames.intValue() + 1;
                lossChecker = lossChecker.intValue() + 1;
                Number currentElo = (Number) JsonUser.get("tictactoeElo");
                currentElo = currentElo.intValue() - 100;
                JSONArray recentMatches = (JSONArray) JsonUser.get("recentMatches");
                JSONObject currentMatch = new JSONObject();
                currentMatch.put("matchID", StringID);
                currentMatch.put("gameType", GameTypes.TICTACTOE.toString());
                currentMatch.put("outcome", "L");
                currentMatch.put("currentElo", currentElo);
                recentMatches.add(currentMatch);

                JsonUser.put("tictactoeElo", currentElo);
                JsonUser.put("lossInTicTacToe", lossChecker);
                JsonUser.put("gamesPlayedTicTacToe", totalCheckerGames);
                JsonUser.put("recentMatches", recentMatches);

                // update the game history
                try {
                    saveData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            System.out.println("Cant find plater lmao");
        }
    }

    /**
     * Updates the Elo rating, loss count, and game history for a player who lost a game of Connect 4.
     * @param playerID  The UUID of the player who lost the game.
     * @param sessionID The UUID of the game session where the loss occurred.
     */
    public void updateEloForLosingPlayerConnect4(UUID playerID, UUID sessionID) {
        String StringID = sessionID.toString();
        System.out.println("TYPE OF SESSION ID: " + StringID.getClass().getSimpleName());

        for (Object user : users) {
            JSONObject JsonUser = (JSONObject) user;
            if (JsonUser.get("playerUUID").equals(playerID.toString())) {

                Number lossChecker = (Number) JsonUser.get("lossInConnect4"); // update wins in gamet
                Number totalCheckerGames = (Number) JsonUser.get("gamesPlayedConnect4");
                totalCheckerGames = totalCheckerGames.intValue() + 1;
                lossChecker = lossChecker.intValue() + 1;
                Number currentElo = (Number) JsonUser.get("connect4Elo");
                currentElo = currentElo.intValue() - 100;
                JSONArray recentMatches = (JSONArray) JsonUser.get("recentMatches");
                JSONObject currentMatch = new JSONObject();
                currentMatch.put("matchID", StringID);
                currentMatch.put("gameType", GameTypes.CONNECT4.toString());
                currentMatch.put("outcome", "L");
                currentMatch.put("currentElo", currentElo);
                recentMatches.add(currentMatch);

                JsonUser.put("connect4Elo", currentElo);
                JsonUser.put("lossInConnect4", lossChecker);
                JsonUser.put("gamesPlayedConnect4", totalCheckerGames);
                JsonUser.put("recentMatches", recentMatches);

                // update the game history
                try {
                    saveData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            System.out.println("Cant find plater lmao");
        }
    }



    /**
     * Refreshes the leaderboard by sorting users based on their Elo ratings in descending order.
     * @return A {@link JSONArray} containing the sorted leaderboard.
     * @throws IllegalArgumentException If any user is missing the "elo" field.
     */
    public JSONArray refreshLeaderboard() {
        users.sort((user1, user2) -> {
            Number elo1 = (Number) ((JSONObject) user1).get("elo");
            Number elo2 = (Number) ((JSONObject) user2).get("elo");
            if (elo1 == null || elo2 == null) {
                throw new IllegalArgumentException("User is missing 'elo' field.");
            }
            return Integer.compare(elo2.intValue(), elo1.intValue());
        });

        JSONArray sortedLeaderboard = new JSONArray();

        System.out.println("Sorted LeaderBoard:");
        for (Object object : users) {
            JSONObject user = (JSONObject) object;
            System.out.println("Name: " + user.get("username") + ", Elo: " + user.get("elo"));
        }
        return sortedLeaderboard;
    }

    /**
     * Prints the current leaderboard to the console.
     */
    public void printLeaderboard() {
        System.out.println("PRINTED LEADER BOARD:");

        for (Object object : users) {
            JSONObject user = (JSONObject) object;
            System.out.println("Name: " + user.get("username") + ", Elo: " + user.get("elo"));
        }
    }



   public String fetchUserFromNewPlayerJson() throws FileNotFoundException {
       File file = new File(NetworkManager.PLAYER_ID_FILE);
       if (!file.exists()) {
           throw new FileNotFoundException("Leaderboard.json not found in the working directory: " + file.getAbsolutePath());
       }

       try (FileReader reader = new FileReader(file)) {
           JSONParser parser = new JSONParser();
           JSONObject player = (JSONObject) parser.parse(reader);
           String username = (String) player.get("username");
           return username;
       } catch (Exception e) {
           e.printStackTrace();
       }
      return null;
   }

   //setter for testing
    public static void setInstance(Leaderboard instance) {
        Leaderboard.instance = instance;
    }



}
