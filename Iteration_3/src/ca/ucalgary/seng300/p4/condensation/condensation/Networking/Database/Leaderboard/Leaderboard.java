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

    public String JSONObjectToString(JSONObject object) {

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
        //Instructions: Basically just try and parse and print the leaderboard if you
        //can make it pretty that's awesome, if not deadass
        //just print the JSON in JSON format

        //sort the users by elo in descending order
        users.sort((user1, user2) -> {
            Number elo1 = (Number) ((JSONObject) user1).get("elo");
            Number elo2 = (Number) ((JSONObject) user2).get("elo");
            return Integer.compare(elo2.intValue(), elo1.intValue()); // compare them, if elo 1 is greater, put at front, if not push back
        });

        //we can create a new array that will hold the top 5 players
        JSONArray top5Players = new JSONArray();

        //to make sure there is ONLY 5 players that go into the array, we have a count that keeps track of how many objects it extracts from users
        int count = 0;

        //loop through the array and add the players into top5Players
        for (Object object : users) {
            if (count <= 5) {
                top5Players.add(object);
                count++;
            } else {
                break; //exit loop if 5 objects are in the top5Players
            }
        }

        //print the top 5 players, don't know if its needed, wrote it anyways just in case. Can delete if we dont want it.
        //(understood the instructions a lil wrong SRRY)
        System.out.println("The Top 5 Players:"); //print a header
        for (Object object : top5Players) {
            JSONObject user = (JSONObject) object;
            System.out.println("Name: " + user.get("name") + ", Elo: " + user.get("elo"));
        }

        return top5Players;
    }


    /// THIS SHOULD BE THE SAME AS THE TOP FUNCTION JUST INCREAMENT THE VALUE BY 30
    public void updateEloForLosingPlayer(UUID playerID) {
        if (findUserById(playerID) == null) {
            return;
        }

        JSONObject losingPlayer = findUserById(playerID);
        Number currentElo = (Number) losingPlayer.get("elo");
        currentElo = currentElo.intValue() + 30; //idk if im understanding this but is this right??? or do u mean -30? change it when u review it ;-;

        losingPlayer.put("elo", currentElo);

        System.out.println("Elo has been updated for losing player with PlayerID: " + playerID);
    }


    public void refreshLeaderboard() {
        System.out.println("Refreshed leaderboard");
    }


    public void printLeaderboard() {
        System.out.println("PRINTED LEADER BOARD:");

        //loop through the users and print them!
        for (Object object : users) {
            JSONObject user = (JSONObject) object;
            System.out.println("Name: " + user.get("name") + ", Elo: " + user.get("elo"));
        }
    }
}
