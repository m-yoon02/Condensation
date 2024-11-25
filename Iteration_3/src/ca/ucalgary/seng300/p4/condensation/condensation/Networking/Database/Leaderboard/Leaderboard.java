package ca.ucalgary.seng300.p4.condensation.condensation.Networking.Database.Leaderboard;

import java.io.*;
import java.util.UUID;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Leaderboard {
    private static final String DATA_FILE = "Leaderboard.json";
    private JSONArray users;


    public Leaderboard() throws IOException, ParseException {
        loadData();
    }

    public void loadData() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        FileReader reader = new FileReader(DATA_FILE);

        users = (JSONArray) parser.parse(reader);
        reader.close();
    }

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

    public JSONArray getAllUsers() {
        return users;
    }

    public JSONObject findUserByName(String name) {

        for (Object object : users) {
            JSONObject user = (JSONObject) object;
            if (user.get("name") == name) {
                return user;
            }
        }

        return null;
    }


    public JSONObject findUserById(UUID uuid) {
        String uuidToString = uuid.toString();

        for (Object object : users) {
            JSONObject user = (JSONObject) object;
            if (user.get("id") == uuidToString) {
                return user;
            }
        }
        return null;
    }

    public String JSONObjectToString() {

        // TODO: do this
    }

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

    public JSONArray fetchTop5Players() {

    }


    /// THIS SHOULD BE THE SAME AS THE TOP FUNCTION JUST INCREAMENT THE VALUE BY 30
    public void updateEloForLosingPlayer(UUID playerID) {
        System.out.println("Elo has been updated for losing player with PlayerID: " + playerID);
    }


    public void refreshLeaderboard() {
        System.out.println("Refreshed leaderboard");
    }


    public void printLeaderboard() {
        System.out.println("PRINTED LEADER BOARD:");
    }
}
