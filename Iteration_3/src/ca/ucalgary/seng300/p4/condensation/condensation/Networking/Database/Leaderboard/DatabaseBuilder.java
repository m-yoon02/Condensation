package ca.ucalgary.seng300.p4.condensation.condensation.Networking.Database.Leaderboard;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.UUID;

public class DatabaseBuilder {
    public static void main(String[] args) {
        //put code in a try catch
        try {
            Leaderboard leaderboard = new Leaderboard();

            //make a bunch of users to test this
            leaderboard.getAllUsers().add(createUser("Hannah", 1500));
            leaderboard.getAllUsers().add(createUser("Nathan", 100));
            leaderboard.getAllUsers().add(createUser("Phil", 2000));
            leaderboard.getAllUsers().add(createUser("Sam", 2500));
            leaderboard.getAllUsers().add(createUser("Jack", 300));

            // This should print the top 5 players
            System.out.println(leaderboard.fetchTop5Players().toString());

            // Basically this class is so that we can test the leaderboard stuff

            //NOTE: it doesn't let me run this to test it because of javaFX not existing rn so i believe thats frontend stuff?

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * A method that creates a user to use for the builder
     * @param name the name
     * @param elo the elo
     * @return the JSONObject
     */
    private static JSONObject createUser(String name, int elo) {
        JSONObject user = new JSONObject(); //create new object

        user.put("id", UUID.randomUUID().toString()); //assign random uuid
        user.put("name", name);
        user.put("elo", elo);

        return user;
    }
}
