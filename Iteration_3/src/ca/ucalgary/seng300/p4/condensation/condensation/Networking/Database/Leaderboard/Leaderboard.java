package ca.ucalgary.seng300.p4.condensation.condensation.Networking.Database.Leaderboard;

import java.io.*;
import java.util.UUID;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;

/**
 * This class represents a Leaderboard that manages user data, including their ELO scores, and
 * provides functionality for retrieving and updating leaderboard information.
 */
public class Leaderboard {
    private String DATA_FILE = "/Users/mortezafaraji/Desktop/school/SENG300/seng300-f24-project/Iteration_3/src/ca/ucalgary/seng300/p4/condensation/condensation/Networking/Database/Leaderboard/Leaderboard.json";
    private JSONArray users;

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
     * Loads user data from the JSON file into the leaderboard.
     *
     * @throws IOException    if there is an error reading the file.
     * @throws ParseException if there is an error parsing the JSON data.
     */
    public void loadData() throws IOException, ParseException {
        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(DATA_FILE);

            users = (JSONArray) parser.parse(reader);
            printLeaderboard();
            reader.close();
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
            FileWriter fileWriter = new FileWriter(DATA_FILE);
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

    public void setDATA_FILE(String DATA_FILE) {
        this.DATA_FILE = DATA_FILE;
    }

    public void createUser(String name, String password) throws IOException {
        JSONObject user = new JSONObject(); //create new object

        user.put("id", UUID.randomUUID().toString()); //assign random uuid
        user.put("name", name);
        user.put("passowrd", password);
        user.put("elo", 0);

        this.saveData();
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

    /**
     * Finds a user by their password.
     *
     * @param pwd the username of the user.
     * @return a JSONObject representing the user, or null if no user is found.
     */
    public JSONObject findUserByPassword(String pwd) {
        for (Object object : users) {
            JSONObject user = (JSONObject) object;
            if (user.get("password").equals(pwd)) {
                return user;
            }
        }
        return null;
    }

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
     * Updates the ELO of a player who won a match by increasing it by 100.
     *
     * @param playerID the UUID of the winning player.
     */
    public void updateEloForWinningPlayer(UUID playerID) {
        if (findUserById(playerID) == null) {
            return;
        }

        JSONObject winningPlayer = findUserById(playerID);
        Number currentElo = (Number) winningPlayer.get("elo");
        currentElo = currentElo.intValue() + 100;

        winningPlayer.put("elo", currentElo);

        System.out.println("Elo has been updated for winning player with PlayerID: " + playerID);
    }

    /**
     * Retrieves the top 5 players based on their ELO scores.
     *
     * @return a JSONArray containing the top 5 players.
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
     * Updates the ELO of a player who lost a match by decreasing it by 3000.
     *
     * @param playerID the UUID of the losing player.
     */
    public void updateEloForLosingPlayer(UUID playerID) {
        if (findUserById(playerID) == null) {
            return;
        }

        JSONObject losingPlayer = findUserById(playerID);
        Number currentElo = (Number) losingPlayer.get("elo");
        currentElo = currentElo.intValue() - 3000;

        losingPlayer.put("elo", currentElo);

        System.out.println("Elo has been updated for losing player with PlayerID: " + playerID);
    }

    /**
     * Refreshes and sorts the leaderboard based on ELO scores.
     *
     * @return a JSONArray of sorted users.
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
}
