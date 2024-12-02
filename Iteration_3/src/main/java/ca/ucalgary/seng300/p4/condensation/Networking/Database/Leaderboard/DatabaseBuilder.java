package ca.ucalgary.seng300.p4.condensation.Networking.Database.Leaderboard;

import org.json.simple.JSONObject;

import java.util.UUID;

public class DatabaseBuilder {
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
